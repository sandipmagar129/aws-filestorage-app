package com.test.awsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;

@Configuration
public class EC2Config {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CognitoClient.class);

	@Bean
	public AmazonEC2 ec2Client() {
    	LOGGER.info("--- EC2 Configuration Completed---");
		return AmazonEC2ClientBuilder.defaultClient();
	}

}
