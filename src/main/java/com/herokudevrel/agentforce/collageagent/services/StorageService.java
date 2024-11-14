package com.herokudevrel.agentforce.collageagent.services;

import com.herokudevrel.agentforce.collageagent.config.ConfigProperties;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ConfigProperties.class)
public class StorageService {

    private final S3Template s3Template;
    private final ConfigProperties configProperties;

    public void save(BufferedImage image, String format, String objectKey) throws IOException {
        var bucketName = configProperties.getBucketName();
        // Convert BufferedImage to InputStream
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            baos.flush();
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray())) {
                s3Template.upload(bucketName, objectKey, inputStream);
            }
        }
    }

    public S3Resource retrieve(String objectKey) {
        var bucketName = configProperties.getBucketName();
        return s3Template.download(bucketName, objectKey);
    }
}