package com.worktree.hrms.service.impl;

import com.worktree.hrms.exceptions.BadRequestException;
import com.worktree.hrms.service.TestConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.util.Map;

@Service
public class TestConfigServiceImpl implements TestConfigService {

    private final Logger logger = LoggerFactory.getLogger(TestConfigServiceImpl.class);

    public void validateStorageConfiguration(Map<String, Object> payload) {
        try {
            String provider = payload.get("provider").toString();
            if (provider.equalsIgnoreCase("aws3")) {
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
            }
        } catch (S3Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Invalid bucket or credentials: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred", e);
            throw new BadRequestException("Invalid details entered. Please provide the correct secret access key and region.");
        }
    }
}

