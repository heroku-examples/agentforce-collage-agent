package com.herokudevrel.agentforce.collageagent.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "com.herokudevrel.agentforce.collageagent")
public class AwsS3BucketProperties {

    @NotBlank(message = "S3 bucket name must be configured")
    private String bucketName;

}