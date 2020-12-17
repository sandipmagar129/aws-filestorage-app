package com.test.awsdemo.controller;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.model.Bucket;
import com.test.awsdemo.service.S3Bucket;
import com.test.awsdemo.service.S3FileOperation;

@RestController
@CrossOrigin(maxAge = 3600)
public class ApiController {

	@Autowired
	S3Bucket bucket;

	@Autowired
	S3FileOperation fileOperation;

	@PostMapping(path = "/api/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Object> uploadFile(RedirectAttributes model,
			@RequestPart(value = "file", required = false) MultipartFile[] files,
			@RequestParam("description") String description) throws IOException {
		
		JSONObject response = new JSONObject();
		if (files.length == 0) {
			response.put("msg", "Please select files to upload");
		}
		boolean status = fileOperation.uploadFile(files, description);		
		response.put("files", files);
		response.put("status", status);	    
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(path = "/api/download")
	public ResponseEntity<byte[]> downloadFile(@RequestParam(value = "file") String file) throws IOException {
		byte[] data = fileOperation.downloadFile(file);
		if (data == null) {
			return ResponseEntity.noContent().build();
		}
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok()
				// .contentLength(data.length)
				// .header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + file + "\"").body(resource.getByteArray());
	}

	@GetMapping(path = "/api/downloadUrl")
	public ResponseEntity<String> fileUrl(@RequestParam(value = "file") String file) throws IOException {
		String url = fileOperation.downloadFileURL(file);
		if (url == null) {
			return ResponseEntity.notFound().build();
		}
		JSONObject response = new JSONObject();
		response.put("url", url);
		return ResponseEntity.ok().body(response.toString());
	}
	
	@GetMapping(value = "/api/bucket", produces = { "application/json" })
	List<Bucket> buckets() {
		return bucket.getAllBuckets();
	}

}
