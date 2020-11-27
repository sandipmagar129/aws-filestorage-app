package com.test.awsdemo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amazonaws.services.s3.model.Bucket;
import com.test.awsdemo.service.S3Bucket;
import com.test.awsdemo.service.S3FileOperation;

@Controller
@CrossOrigin(maxAge = 3600)
public class TestController {

	@Value("${bucket.name}")
	String bucketName;

	@Autowired
	S3Bucket bucket;

	@Autowired
	S3FileOperation fileOperation;

	@RequestMapping("/")
	public String homePage() {
		return "index";
	}

	@RequestMapping("/upload")
	public String uploadPage(Model model) {
		model.addAttribute("mymsg", "");
		return "upload";
	}

	@PostMapping(path = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String uploadFile(RedirectAttributes model,
			@RequestPart(value = "file", required = false) MultipartFile[] files,
			@RequestParam("description") String description) throws IOException {
		if (files.length == 0) {
			model.addAttribute("mymsg", "Please select files to upload");
			return "redirect:/";
		}
		fileOperation.uploadFile(bucketName, files);
		String fileName = "";
		for (MultipartFile file : files) {
			fileName += file.getOriginalFilename() + " ";
		}
		model.addFlashAttribute("mymsg", "Succefully Uploaded Files : " + fileName + '!' + description);
		return "redirect:/";
	}

	@GetMapping(path = "/download")
	public ResponseEntity<byte[]> uploadFile(@RequestParam(value = "file") String file,
			@RequestParam(value = "bucket") String bName) throws IOException {
		byte[] data = fileOperation.getFile(file, bName);

		if (data == null) {
			return ResponseEntity.noContent().build();
		}

		ByteArrayResource resource = new ByteArrayResource(data);

		return ResponseEntity.ok()
				// .contentLength(data.length)
				// .header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + file + "\"").body(resource.getByteArray());
	}

	@GetMapping(value = "/bucket", produces = { "application/json" })
	List<Bucket> buckets() {
		return bucket.getAllBuckets();
	}

}
