package com.upiicsa.stormbook.models;

import java.util.Date;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class Student {
	private String enrollment;
	private String name;
	private char grade;
	private char group;
	private Date createdAt;
	private Date updatedAt;
	
	public String getEnrollment() {
		return enrollment;
	}
	
	public void setEnrollment(String enrollment) {
		this.enrollment = enrollment;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public char getGrade() {
		return grade;
	}
	
	public void setGrade(char grade) {
		this.grade = grade;
	}
	
	public char getGroup() {
		return group;
	}
	
	public void setGroup(char group) {
		this.group = group;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
