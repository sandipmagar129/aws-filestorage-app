package com.test.awsdemo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(maxAge = 3600)
public class ApiController {

	@GetMapping("/ping")
	public String ping() {
		return "OK";
	}
}
