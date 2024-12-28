package com.worktree.hrms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.constants.CommonConstants;
import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.service.TestConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
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
                    break;
                case "gcs":
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

