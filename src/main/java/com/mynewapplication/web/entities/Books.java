package com.mynewapplication.web.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.mynewapplication.web.entities.Extracts;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynewapplication.web.entities.User;
import com.mynewapplication.web.entities.Comments;

@Entity
@Table(name = "NBOOK")
public class Books {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String title;
	private String description;
	private String subject;
	private String image;
	@Column(columnDefinition = "TEXT")
	private String note;
	private String author;

	private String status;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Extracts> extractnotes = new ArrayList<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Comments> mycomments = new ArrayList<>();

	@ManyToOne
	@JsonIgnore
	private User user;

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Extracts> getExtractnotes() {
		return extractnotes;
	}

	public void setExtractnotes(List<Extracts> extractnotes) {
		this.extractnotes = extractnotes;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Comments> getMycomments() {
		return mycomments;
	}

	public void setMycomments(List<Comments> mycomments) {
		this.mycomments = mycomments;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
