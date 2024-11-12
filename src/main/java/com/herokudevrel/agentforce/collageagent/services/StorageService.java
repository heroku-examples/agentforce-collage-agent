package com.herokudevrel.agentforce.collageagent.services;

import com.herokudevrel.agentforce.collageagent.config.AwsS3BucketProperties;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3BucketProperties.class)
public class StorageService {

    private final S3Template s3Template;
    private final AwsS3BucketProperties awsS3BucketProperties;

    public void save(MultipartFile file) throws IOException {
        var objectKey = file.getOriginalFilename();
        var bucketName = awsS3BucketProperties.getBucketName();
        s3Template.upload(bucketName, objectKey, file.getInputStream());
    }

    public S3Resource retrieve(String objectKey) {
        var bucketName = awsS3BucketProperties.getBucketName();
        return s3Template.download(bucketName, objectKey);
    }

    public void delete(String objectKey) {
        var bucketName = awsS3BucketProperties.getBucketName();
        s3Template.deleteObject(bucketName, objectKey);
    }

}