package com.test.awsdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(CognitoClient.class);

	@Value("${cloud.aws.region.static}")
	String region;

	// AWSCognitoIdentityProviderClient implements AWSCognitoIdentityProvider

	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		LOGGER.info("--- Cognito Configuration Completed ---");

		return AWSCognitoIdentityProviderClientBuilder.standard()
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.withRegion(Regions.fromName(region))
				.build();

	}
}
