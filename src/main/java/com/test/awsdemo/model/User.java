package com.test.awsdemo.model;

//https://www.javachinna.com/secure-spring-boot-rest-api-with-basic-authentication-role-based-authorization-database/
//https://www.codejava.net/frameworks/spring-boot/spring-boot-security-role-based-authorization-tutorial
//https://www.baeldung.com/rest-api-spring-oauth2-angular
//https://blog.softtek.com/en/token-based-api-authentication-with-spring-and-jwt
//https://www.toptal.com/spring/spring-boot-oauth2-jwt-rest-protection
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

	private Long id;
	private String username;
	@JsonIgnore
	private String password;
	private String email;
	private Set<Role> roles = new HashSet<>();
	private Set<Training> training = new HashSet<>();

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public Set<Training> getTraining() {
		return training;
	}


	public void setTraining(Set<Training> training) {
		this.training = training;
	}
	
}
