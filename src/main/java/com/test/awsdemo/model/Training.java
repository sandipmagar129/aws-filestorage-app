package com.test.awsdemo.model;

import java.util.List;

public class Training {

	private Integer id;
	private String name;
	private List<Material> material;
	private List<User> trainers;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Material> getMaterial() {
		return material;
	}
	public void setMaterial(List<Material> material) {
		this.material = material;
	}
	public List<User> getTrainers() {
		return trainers;
	}
	public void setTrainers(List<User> trainers) {
		this.trainers = trainers;
	}
	
	
}
