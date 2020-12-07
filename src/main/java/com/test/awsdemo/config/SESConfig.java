package com.test.awsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class SESConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SESConfig.class);

	@Value("${cloud.aws.region.static}")
	String region;

	@Bean
	public AmazonSimpleEmailService sesClient() {
    	LOGGER.info("--- SES Configuration Completed---");
		return AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.withRegion(Regions.fromName(region))        		
				.build();
	}
}
