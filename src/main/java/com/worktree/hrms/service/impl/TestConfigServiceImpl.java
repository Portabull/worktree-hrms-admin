package com.worktree.hrms.service.impl;

import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.core.http.policy.*;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.service.TestConfigService;
import com.worktree.hrms.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class TestConfigServiceImpl implements TestConfigService {

    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(TestConfigServiceImpl.class);

    public void validateStorageConfiguration(Map<String, Object> payload) {
        try {
            String provider = payload.get("provider").toString().toLowerCase();
            switch (provider) {
                case "aws3":
                    validateAWSS3(payload);
                    break;
                case "azureadls":
                    validateADLS(payload);
                    break;
                case "gcs":
                    validateGCPStorage(payload);
                    break;
                case "serverlocalstorage":
                    validateFileStorageConfig(payload);
                    break;
            }

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateADLS(Map<String, Object> payload) {
        String adlsAccountName = payload.get("adlsAccountName").toString();
        String adlsContainerName = payload.get("adlsContainerName").toString();
        String adlsAccountKey = payload.get("adlsAccountKey").toString();


        String host = adlsAccountName + ".blob.core.windows.net";
        // Endpoint for the Azure Blob Storage account
        String endpoint = "https://" + host;

        // Step 1: Validate the account name using DNS
        if (!isHostReachable(host, 2000)) {
            throw new BadRequestException("Invalid account name please check and try after sometime.");
        }

        try {
            // Step 2: Configure retry options with minimal retries
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

            // Step 3: Check if the container exists
            boolean containerExists = blobServiceClient.getBlobContainerClient(adlsContainerName).exists();

            if (!containerExists) {
                throw new BadRequestException("Container does not exist. Please create this container or try another.");
            }

        } catch (BadRequestException e) {
            throw e;
        } catch (BlobStorageException e) {
            logger.error("Exception Occurred :: ", e);
            int statusCode = e.getStatusCode();
            String errorMessage = e.getMessage();
            switch (statusCode) {
                case 401:
                    throw new BadRequestException("Unauthorized: Please check your access key or SAS token.");
                case 403:
                    throw new BadRequestException("Forbidden: You do not have permission to access this resource.");
                case 404:
                    throw new BadRequestException("Not Found: The specified endpoint or container name is incorrect.");
                default:
                    throw new BadRequestException(errorMessage);
            }
        } catch (Exception e) {
            logger.error("Exception Occurred :: ", e);
            if (e.getCause() instanceof UnknownHostException) {
                throw new BadRequestException("Invalid account name. Please check and try again later.");
            }
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateGCPStorage(Map<String, Object> payload) {
        File file = null;
        try {
            String gcpProjectId = payload.get("gcpProjectId").toString();
            String gcpBucketName = payload.get("gcpBucketName").toString();
            String gcpCredFile = payload.get("gcpCredFile").toString();
            file = FileUtils.convertToFile(gcpCredFile, payload.get("gcpCredFileName").toString());

            try (InputStream inputStream = new FileInputStream(file)) {
                // Step 1: Initialize GCP Storage client using provided credentials
                StorageOptions storageOptions = StorageOptions.newBuilder()
                        .setProjectId(gcpProjectId)
                        .setCredentials(ServiceAccountCredentials.fromStream(inputStream))
                        .build();

                Storage storage = storageOptions.getService();

                // Step 2: Check if the bucket exists
                Bucket bucket = storage.get(gcpBucketName);
                if (bucket == null) {
                    throw new BadRequestException("Bucket does not exist. Please create this bucket or try another.");
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Credentials file not found: ", e);
            throw new BadRequestException("Invalid credentials file path. Please check the provided path and try again.");
        } catch (StorageException e) {
            logger.error("GCP Storage exception occurred: ", e);
            int statusCode = e.getCode();
            String errorMessage = e.getMessage();
            switch (statusCode) {
                case 401:
                    throw new BadRequestException("Unauthorized: Please check your credentials file or permissions.");
                case 403:
                    throw new BadRequestException("Forbidden: You do not have permission to access this bucket.");
                case 404:
                    throw new BadRequestException("Not Found: The specified bucket name is incorrect.");
                default:
                    throw new BadRequestException(errorMessage);
            }
        } catch (BadRequestException e) {
            throw e;
        } catch (IOException e) {
            logger.error("IOException occurred: ", e);
            throw new BadRequestException("Error reading the credentials file. Please check and try again.");
        } catch (Exception e) {
            logger.error("Exception occurred: ", e);
            throw new BadRequestException(CommonConstants.INTERNAL_SERVER_ERROR);
        } finally {
            logger.info("File Deleted : {}", file != null ? file.delete() : null);
        }
    }


    private boolean isHostReachable(String host, int timeoutMs) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, 443), timeoutMs); // Check HTTPS port
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validateFileStorageConfig(Map<String, Object> payload) {
        try {

            String localFileLocation = payload.get("localFileLocation").toString();

            File file = new File(localFileLocation);

            List list = ((List<Object>) payload.get("additionalProperties"));

            boolean createDir = list.stream().anyMatch(val -> ((Map) val).get("name").toString().equalsIgnoreCase("create-dir")
                    && ((Map) val).get("value").toString().equalsIgnoreCase("y"));

            if (createDir && !file.exists()) {
                Files.createDirectories(Paths.get(localFileLocation));
            }

            if (!file.exists()) {
                throw new BadRequestException("Directory does not exist; please create the directory manually or add the additional property create-dir: y to enable automatic creation by the system."
                );
            }

            if (!file.isDirectory()) {
                throw new BadRequestException("Please give directory path not a file path");
            }

        } catch (AccessDeniedException e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Failed to create the directory for the specified path. Please ensure that the necessary permissions are granted and try again."
            );
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Invalid details entered. Please provide the correct secret access key and region.");
        }
    }

    private void validateAWSS3(Map<String, Object> payload) {
        try {
            String region = payload.get("awsRegion").toString();
            String bucketName = payload.get("bucketName").toString();
            String accessKey = payload.get("accessKey").toString();
            String secretKey = payload.get("secretKey").toString();

            S3Client s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .build();


            // Use headBucket to validate the bucket and credentials
            s3.headBucket(builder -> builder.bucket(bucketName));
        } catch (S3Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Invalid bucket or credentials: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Invalid details entered. Please provide the correct secret access key and region.");
        }
    }
}

