package com.mynewapplication.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="NEXTRACT")
public class Extracts {

	
	
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int eId;
		private String subject;
		

		private String eimage;
		@Column(length = 8000)
		private String details;
		
		@ManyToOne	
		@JsonIgnore
		public Books book;

		public int geteId() {
			return eId;
		}

		public void seteId(int eId) {
			this.eId = eId;
		}

		public String getEimage() {
			return eimage;
		}

		public void setEimage(String eimage) {
			this.eimage = eimage;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public Books getBook() {
			return book;
		}

		public void setBook(Books book) {
			this.book = book;
		}
		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}
		
	
}
