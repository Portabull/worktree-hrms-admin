package com.worktree.hrms.utils;

import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.core.http.policy.*;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigurationUtils {


    public Map<String, Object> getStorageStatistics(Map<String, Object> storageConfiguration) {


        String provider = storageConfiguration.get("provider").toString().toLowerCase();
        BucketStats bucketStats = null;
        switch (provider) {
            case "aws3":
                bucketStats = getS3BucketStats(storageConfiguration);
                break;
            case "azureadls":
                bucketStats = getADLSBucketStats(storageConfiguration);
                break;
            case "gcs":
                bucketStats = getGCSBucketStats(storageConfiguration);
                break;
            case "serverlocalstorage":
                bucketStats = getLocalStorageBucketStats(storageConfiguration);
                break;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("fileTypeCounts", bucketStats.fileTypeCounts);
        response.put("folderFiles", bucketStats.folderFiles);
        response.put("totalFiles", bucketStats.totalFiles);
        response.put("totalSizeBytes", bucketStats.totalSizeBytes);
        return response;
    }

    private BucketStats getLocalStorageBucketStats(Map<String, Object> payload) {
        BucketStats stats = new BucketStats();
        String rootPath = payload.get("localFileLocation").toString();
        File root = new File(rootPath);

        if (!root.exists() || !root.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory: " + rootPath);
        }

        scanDirectory(root, rootPath, stats);

        // Convert sizeBytes to readable size
        stats.folderFiles.forEach((folder, info) -> {
            long sizeBytes = (Long) info.get("sizeBytes");
            info.put("size", formatSize(sizeBytes));
            info.remove("sizeBytes");
        });

        return stats;
    }

    private void scanDirectory(File dir, String basePath, BucketStats stats) {
        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("Warning: Cannot access or read directory: " + dir.getAbsolutePath());
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, basePath, stats);
            } else {
                long size = file.length();
                stats.totalFiles++;
                stats.totalSizeBytes += size;

                String fileType = getFileType(file.getName());
                stats.fileTypeCounts.merge(fileType, 1L, Long::sum);

                String relativeFolder = getRelativeFolder(file.getParentFile(), basePath);

                stats.folderFiles.computeIfAbsent(relativeFolder, k -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("count", 0L);
                    map.put("sizeBytes", 0L);
                    return map;
                });

                Map<String, Object> folderInfo = stats.folderFiles.get(relativeFolder);
                long count = (Long) folderInfo.get("count");
                long folderSize = (Long) folderInfo.get("sizeBytes");

                folderInfo.put("count", count + 1);
                folderInfo.put("sizeBytes", folderSize + size);
            }
        }
    }

    private String getRelativeFolder(File folder, String basePath) {
        String absolute = folder.getAbsolutePath();
        String base = new File(basePath).getAbsolutePath();
        if (absolute.equals(base)) return "Root";
        return "Root" + absolute.substring(base.length()).replace(File.separator, "/");
    }


    private BucketStats getGCSBucketStats(Map<String, Object> payload) {
        return null;
    }

    private BucketStats getADLSBucketStats(Map<String, Object> payload) {
        String adlsAccountName = payload.get("adlsAccountName").toString();
        String adlsContainerName = payload.get("adlsContainerName").toString();
        String adlsAccountKey = payload.get("adlsAccountKey").toString();

        String host = adlsAccountName + ".blob.core.windows.net";
        // Endpoint for the Azure Blob Storage account
        String endpoint = "https://" + host;

        RetryOptions retryOptions = new RetryOptions(new FixedDelayOptions(0, java.time.Duration.ofSeconds(5)));

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .sasToken(adlsAccountKey)
                .addPolicy(new RetryPolicy(retryOptions)) // Apply custom retry policy
                .httpClient(new NettyAsyncHttpClientBuilder()
                        .readTimeout(java.time.Duration.ofSeconds(5)) // Read timeout
                        .connectTimeout(java.time.Duration.ofSeconds(5)) // Connect timeout
                        .responseTimeout(java.time.Duration.ofSeconds(5)) // Response timeout
                        .build())
                .httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS))
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(adlsContainerName);


        BucketStats stats = new BucketStats();

        PagedIterable<BlobItem> blobs = containerClient.listBlobs();
        for (BlobItem blobItem : blobs) {
            String name = blobItem.getName();

            if (name.endsWith("/")) continue; // skip virtual folders

            BlobClient blobClient = containerClient.getBlobClient(name);
            BlobProperties properties = blobClient.getProperties();

            long size = properties.getBlobSize();

            // Update total stats
            stats.totalFiles++;
            stats.totalSizeBytes += size;

            // File type
            String fileType = getFileType(name);
            stats.fileTypeCounts.merge(fileType, 1L, Long::sum);

            // Folder stats
            String folder = getFolder(name);
            stats.folderFiles.computeIfAbsent(folder, k -> {
                Map<String, Object> map = new HashMap<>();
                map.put("count", 0L);
                map.put("sizeBytes", 0L);
                return map;
            });

            Map<String, Object> folderInfo = stats.folderFiles.get(folder);
            long count = (Long) folderInfo.get("count");
            long folderSize = (Long) folderInfo.get("sizeBytes");

            folderInfo.put("count", count + 1);
            folderInfo.put("sizeBytes", folderSize + size);
        }

        // Convert sizeBytes to readable size and clean up
        stats.folderFiles.forEach((folder, info) -> {
            long sizeBytes = (Long) info.get("sizeBytes");
            info.put("size", formatSize(sizeBytes));
            info.remove("sizeBytes");
        });

        return stats;
    }

    public BucketStats getS3BucketStats(Map<String, Object> storageConfiguration) {
        String accessKey = storageConfiguration.get("accessKey").toString();
        String region = storageConfiguration.get("awsRegion").toString();
        String bucketName = storageConfiguration.get("bucketName").toString();
        String secretKey = storageConfiguration.get("secretKey").toString();
        int maxKeysPerRequest = 1000;

        try (S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build()) {

            BucketStats stats = new BucketStats();
            String continuationToken = null;

            do {
                ListObjectsV2Request request = ListObjectsV2Request.builder()
                        .bucket(bucketName)
                        .maxKeys(maxKeysPerRequest)
                        .continuationToken(continuationToken)
                        .build();

                ListObjectsV2Response response = s3.listObjectsV2(request);

                for (S3Object s3Object : response.contents()) {
                    String key = s3Object.key();

                    if (key.endsWith("/") || s3Object.size() == 0) {
                        continue; // Skip folder-like objects
                    }

                    long size = s3Object.size();

                    // Update totals
                    stats.totalSizeBytes += size;
                    stats.totalFiles++;

                    // File type stats
                    String fileType = getFileType(key);
                    stats.fileTypeCounts.merge(fileType, 1L, Long::sum);

                    // Folder stats
                    String folder = getFolder(key);
                    stats.folderFiles.computeIfAbsent(folder, k -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("count", 0L);
                        map.put("sizeBytes", 0L);
                        return map;
                    });

                    Map<String, Object> folderInfo = stats.folderFiles.get(folder);
                    long count = (Long) folderInfo.get("count");
                    long folderSize = (Long) folderInfo.get("sizeBytes");

                    folderInfo.put("count", count + 1);
                    folderInfo.put("sizeBytes", folderSize + size);
                }

                continuationToken = response.nextContinuationToken();

            } while (continuationToken != null);

            // Format size for each folder and clean up sizeBytes
            for (Map.Entry<String, Map<String, Object>> entry : stats.folderFiles.entrySet()) {
                Map<String, Object> folderInfo = entry.getValue();
                long sizeBytes = (Long) folderInfo.get("sizeBytes");
                folderInfo.put("size", formatSize(sizeBytes));
                folderInfo.remove("sizeBytes"); // remove raw size if not needed
            }

            return stats;

        } catch (Exception e) {
            System.err.println("Error calculating bucket stats: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static class BucketStats {
        long totalSizeBytes = 0;
        long totalFiles = 0;
        Map<String, Long> fileTypeCounts = new HashMap<>();
        Map<String, Map<String, Object>> folderFiles = new HashMap<>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Bucket Statistics:\n");
            sb.append("Total Size: ").append(formatSize(totalSizeBytes)).append("\n");
            sb.append("Total Files: ").append(totalFiles).append("\n");

            sb.append("\nFile Type Counts:\n");
            fileTypeCounts.forEach((type, count) ->
                    sb.append(String.format("- %s: %d\n", type, count)));

            sb.append("\nFolder-wise File Details:\n");
            folderFiles.forEach((folder, info) ->
                    sb.append(String.format("- %s: count=%s, size=%s\n",
                            folder,
                            info.get("count"),
                            info.get("size"))));

            return sb.toString();
        }
    }

    private static String getFileType(String key) {
        String extension = key.contains(".") ? key.substring(key.lastIndexOf('.') + 1).toLowerCase() : "";
        switch (extension) {
            case "pdf":
                return "PDF";
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return "Image";
            case "txt":
                return "Text";
            case "doc":
            case "docx":
                return "Document";
            default:
                return "Other";
        }
    }

    private static String getFolder(String key) {
        int lastSlash = key.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash == 0) {
            return "Root";
        }
        return key.substring(0, lastSlash);
    }

    private static String formatSize(long bytes) {
        double sizeInGB = bytes / (1024.0 * 1024.0 * 1024.0);
        double sizeInMB = bytes / (1024.0 * 1024.0);
        if (sizeInGB >= 1) {
            return String.format("%.2f GB", sizeInGB);
        } else {
            return String.format("%.2f MB", sizeInMB);
        }
    }


}
