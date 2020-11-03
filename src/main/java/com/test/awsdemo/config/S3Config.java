package com.test.awsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Config.class);
	
	@Value("${cloud.aws.region.static}")
	String region;


    @Bean
    public AmazonS3 s3client() {
    	LOGGER.info("--- S3 Configuration Completed---");
        // AWSCredentials credentials = new BasicAWSCredentials(accessKey,accessSecret);
        AmazonS3 s3Client  = AmazonS3ClientBuilder
        		  .standard()
        		  //.withCredentials(new AWSStaticCredentialsProvider(credentials))
                  .withCredentials(new DefaultAWSCredentialsProviderChain())
        		  .withRegion(Regions.fromName(region))
        		  .build();
        return s3Client ;
    }

	

    @Bean
    public AmazonS3Client amazonS3Client() {
        return  (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
      		  	.withRegion(Regions.fromName(region))
                .build();
    }

}

