package com.mynewapplication.web.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynewapplication.web.entities.Books;


@Entity
@Table(name="NCOMMENTS")
public class Comments {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int bId;
	
	@ManyToOne	
	@JsonIgnore
	public Books book;
	
	private String comments;
	private String name;
	private String Date;
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public Books getBook() {
		return book;
	}
	public void setBook(Books book) {
		this.book = book;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	
	
	
	
	
	

}
