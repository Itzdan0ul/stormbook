package com.upiicsa.stormbook.models;

import java.util.Date;

/**
 * @authors 
 * 			Urtiz Lopez Dan Jair, 
 * 			Rayas Batalla Luis Alejandro,
 * 			Huerta Mancilla Jonatan Ivan
 * @group 3NM31
 * */
public class Loan {
	private String folio;
	private Date returnDate;
	private String state;
	private String studentEnrollment;
	private String studentName;
	private String studentGrade;
	private String studentGroup;
	private String bookIsbn;
	private Date createdAt;
	private Date updatedAt;
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public Date getReturnDate() {
		return returnDate;
	}
	
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getStudentEnrollment() {
		return studentEnrollment;
	}
	
	public void setStudentEnrollment(String studentEnrollment) {
		this.studentEnrollment = studentEnrollment;
	}
	
	public String getStudentName() {
		return studentName;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getStudentGrade() {
		return studentGrade;
	}
	
	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}
	
	public String getStudentGroup() {
		return studentGroup;
	}
	
	public void setStudentGroup(String studentGroup) {
		this.studentGroup = studentGroup;
	}
	
	public String getBookIsbn() {
		return bookIsbn;
	}
	
	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
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
