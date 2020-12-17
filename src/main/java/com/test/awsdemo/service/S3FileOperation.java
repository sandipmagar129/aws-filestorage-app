package com.test.awsdemo.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileOperation {
	
//	Using multipart upload provides the following advantages:
//
//	Improved throughput - You can upload parts in parallel to improve throughput.
//
//	Quick recovery from any network issues - Smaller part size minimizes the impact of restarting a failed upload due to a network error.
//
//	Pause and resume object uploads - You can upload object parts over time. Once you initiate a multipart upload there is no expiry; you must explicitly complete or stop the multipart upload.
//
//	Begin an upload before you know the final object size - You can upload an object as you are creating it.
//		
	
	public boolean uploadFile(String fileName, MultipartFile file);
	public void deleteFile(String fileName);
	public byte[] downloadFile(String key);
	public String downloadFileURL(String key);
	public void copyFile(String srcBucket, String srcFileName,String destBucket,String destFileName);
	boolean uploadFile(MultipartFile[] files, String docDescription);

}
