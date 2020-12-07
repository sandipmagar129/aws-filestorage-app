package com.test.awsdemo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

@Service
public class S3BucketImpl implements S3Bucket {

	private static final Logger LOGGER = LoggerFactory.getLogger(S3BucketImpl.class);

	@Autowired
	AmazonS3 s3client;

	@Override
	public List<Bucket> getAllBuckets() {
		LOGGER.info(" --- Listing Buckets Request --- ");

		return s3client.listBuckets();
	}

	@Override
	public Bucket createBucket(String bucketName) {
		LOGGER.info(" --- Creating bucket " + bucketName + " --- ");
		return s3client.createBucket(bucketName);
	}

	@Override
	public void deleteBucket(String bucketName) {
		LOGGER.info(" --- Bucket Delete Request " + bucketName + " --- ");
		s3client.deleteBucket(bucketName);
	}

	@Override
	public ObjectListing getObjects(String bucketName) {
		LOGGER.info(" --- Bucket Object Information " + bucketName + " --- ");
		if (s3client.doesBucketExistV2(bucketName)) {
			ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().withBucketName(bucketName));

			// Mutiple Filter for search
			// .withPrefix("thumbnail"));

			return objectListing;
		}
		return null;
	}

	@Override
	public Boolean checkBucket(String bucketName) {
		LOGGER.info(" --- Checking Bucket Existence " + bucketName + " --- ");
		if (s3client.doesBucketExistV2(bucketName))
			return true;
		return false;
	}

}
