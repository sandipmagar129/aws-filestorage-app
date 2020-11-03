package com.test.awsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

@Configuration
//@EnableDynamoDBRepositories(basePackages = "com.example.aws.db")
public class DynamoDBConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoDBConfig.class);

	@Value("${cloud.aws.region.static}")
	String region;
	
	@Bean
	public DynamoDBMapper dynamoDBMapper() {
		LOGGER.info("--- DynamoDB Configuration Completed ---");
		return new DynamoDBMapper(amazonDynamoDB(), DynamoDBMapperConfig.DEFAULT);
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
       return AmazonDynamoDBClientBuilder.standard()
                //.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("XXX", "XXX")))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
        		.withRegion(Regions.fromName(region))
                .build();
	}
}
