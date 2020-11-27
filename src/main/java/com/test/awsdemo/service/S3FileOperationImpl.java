package com.test.awsdemo.service;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;

@Service
public class S3FileOperationImpl implements S3FileOperation {

	// NOTES:
	// If you're uploading large objects over a stable high-bandwidth network, use
	// multipart upload to maximize the use of your available bandwidth by uploading
	// object parts in parallel for multi-threaded performance.
	// If you're uploading over a spotty network, use multipart upload to increase
	// resiliency to network errors by avoiding upload restarts. When using
	// multipart upload, you need to retry uploading only parts that are interrupted
	// during the upload. You don't need to restart uploading your object from the
	// beginning.

	private Logger LOGGER = LoggerFactory.getLogger(S3FileOperationImpl.class);

	@Autowired
	AmazonS3 s3client;

	@Autowired
	S3Bucket bucket;

	@Override
	public void uploadFile(String bucketName, String fileName, MultipartFile file) {
		LOGGER.info(" --- Uploading a new file to S3 Bucket --- ");

		if (!s3client.doesBucketExistV2(bucketName))
			bucket.createBucket(bucketName);

		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(file.getSize());

			// Normal
			// s3client.putObject(new PutObjectRequest(bucketName, fileName,
			// file.getInputStream(),metadata));

			// Track Upload Progress

			TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3client).build();
			PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata);

			// To receive notifications when bytes are transferred, add a
			// ProgressListener to your request.
			request.setGeneralProgressListener(new ProgressListener() {
				public void progressChanged(ProgressEvent progressEvent) {
					System.out.println("Transferred bytes: " + progressEvent.getBytesTransferred());
				}
			});

			// TransferManager processes all transfers asynchronously,
			// so this call returns immediately.
			Upload upload = tm.upload(request);

			// Optionally, you can wait for the upload to finish before continuing.
			upload.waitForCompletion();

		} catch (IOException e) {
			LOGGER.error("IOException: " + e.getMessage());
		} catch (AmazonServiceException a) {
			LOGGER.info("Error Message:    " + a.getMessage());
			LOGGER.info("HTTP Status Code: " + a.getStatusCode());
			LOGGER.info("AWS Error Code:   " + a.getErrorCode());
			LOGGER.info("Error Type:       " + a.getErrorType());
			LOGGER.info("Request ID:       " + a.getRequestId());
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteFile(String bucketName, String key) {
		LOGGER.info(" --- Deleting file from bucket --- ");
		s3client.deleteObject(bucketName, key);
	}

	@Override
	public byte[] getFile(String key, String bucketName) {
		try {
			if (s3client.doesBucketExistV2(bucketName)) {
				LOGGER.info(" --- Downloading an file from bucket --- ");
				S3Object obj = s3client.getObject(new GetObjectRequest(bucketName, key));
				S3ObjectInputStream stream = obj.getObjectContent();

				LOGGER.info("Content-Type: " + obj.getObjectMetadata().getContentType());

				try {
					byte[] content = IOUtils.toByteArray(stream);
					obj.close();
					return content;
				} catch (IOException e) {
					LOGGER.error("IOException: " + e.getMessage());
				} catch (AmazonServiceException a) {
					LOGGER.info("Error Message:    " + a.getMessage());
					LOGGER.info("HTTP Status Code: " + a.getStatusCode());
					LOGGER.info("AWS Error Code:   " + a.getErrorCode());
					LOGGER.info("Error Type:       " + a.getErrorType());
					LOGGER.info("Request ID:       " + a.getRequestId());
				}
			}
		} catch (AmazonServiceException a) {
			LOGGER.info("Error Message:    " + a.getMessage());
			LOGGER.info("HTTP Status Code: " + a.getStatusCode());
			LOGGER.info("AWS Error Code:   " + a.getErrorCode());
			LOGGER.info("Error Type:       " + a.getErrorType());
			LOGGER.info("Request ID:       " + a.getRequestId());
		}
		return null;
	}

	public void copyFile(String srcBucket, String srcFileName, String destBucket, String destFileName) {
		LOGGER.info(" --- Copying a File to Another S3 Bucket --- ");

		if (s3client.doesBucketExistV2(srcBucket)) {
			if (s3client.doesBucketExistV2(destBucket))
				bucket.createBucket(destBucket);
			s3client.copyObject(srcBucket, srcFileName, destBucket, destFileName);
		}
	}

	@Override
	public void uploadFile(String bucketName, MultipartFile[] files) {
		LOGGER.info(" --- Uploading a new files to S3 Bucket --- ");

		try {
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(file.getSize());
				s3client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
			}
		} catch (IOException e) {
			LOGGER.error("IOException: " + e.getMessage());
		} catch (AmazonServiceException a) {
			LOGGER.info("Error Message:    " + a.getMessage());
			LOGGER.info("HTTP Status Code: " + a.getStatusCode());
			LOGGER.info("AWS Error Code:   " + a.getErrorCode());
			LOGGER.info("Error Type:       " + a.getErrorType());
			LOGGER.info("Request ID:       " + a.getRequestId());
		}

	}

	@Override
	public String downloadFile(String key, String bucketName) {

		try {
			// Set the presigned URL to expire after one hour.
			java.util.Date expiration = new java.util.Date();
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 60;
			expiration.setTime(expTimeMillis);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
					.withMethod(HttpMethod.GET).withExpiration(expiration);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

			return url.toString();
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
		return null;
	}

}
