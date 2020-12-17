package com.test.awsdemo.controller;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.simpleemail.model.GetTemplateResult;
import com.test.awsdemo.ops.EmailNotificationService;

@RestController
@CrossOrigin(maxAge = 3600)
public class ServiceController {

	@Value("${cloud.aws.region.static}")
	String region;
	
	@Value("${bucket.name}")
	String bucketName;

	@Value("${app.version}")
	String appVersion;

	@Value("${spring.application.name}")
	String appName;
	
	@Autowired
	EmailNotificationService ens;
	
	@GetMapping("/ping")
	public String ping() {
		return "OK";
	}
	
	@GetMapping("/service")
	public String service() {
		JSONObject servicejson = new JSONObject();
		servicejson.put("appname", appName);
		servicejson.put("version", appVersion);
		servicejson.put("region", region);
		servicejson.put("bucket", bucketName);
		return servicejson.toString();
	}
	
	@GetMapping("/emailtemplate")
	public String emailtemplate() {
		GetTemplateResult result = ens.createTemplate();
		JSONObject servicejson = new JSONObject();
		servicejson.put("name", result.getTemplate().getTemplateName());
		servicejson.put("subject", result.getTemplate().getSubjectPart());
		return servicejson.toString();
	}
	
	@GetMapping("/sendemail")
	public String sendEmail() {
		String result = ens.sendEmail(new ArrayList<String>(), new String(), new String());
		JSONObject servicejson = new JSONObject();
		servicejson.put("stats", result);
		return servicejson.toString();
	}
}
