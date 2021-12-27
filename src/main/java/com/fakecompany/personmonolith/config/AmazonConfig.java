package com.fakecompany.personmonolith.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    @Value("${amazon.s3.access-key.one}")
    private String s3AccessKey;

    @Value("${amazon.s3.secret-key.one}")
    private String s3SecretKey;

    @Value("${amazon.s3.region.one}")
    private String s3Region;

    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(s3Region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }
}
