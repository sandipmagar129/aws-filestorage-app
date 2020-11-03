package com.test.awsdemo.service;

import java.util.List;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;

public interface S3Bucket {

	public List<Bucket> getAllBuckets();
	public Bucket createBucket(String bucketName);
	public void deleteBucket(String bucketName);
	public ObjectListing getObjects(String bucketName);
	public Boolean checkBucket(String bucketName);
}
