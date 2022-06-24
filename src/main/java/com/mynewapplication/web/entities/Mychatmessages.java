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
@Table(name="NMESSAGES")
public class Mychatmessages {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mjid;
	private int fromuserid;
	private int touserid;
	
	private String name;
	private String message;
	private MessageType messageType;
	private String date;
	
	
	
	@JsonIgnore
	@ManyToOne
	private User tuser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	

	


	public int getFromuserid() {
		return fromuserid;
	}

	public void setFromuserid(int fromuserid) {
		this.fromuserid = fromuserid;
	}

	public int getTouserid() {
		return touserid;
	}

	public void setTouserid(int touserid) {
		this.touserid = touserid;
	}

	public User getTuser() {
		return tuser;
	}

	public void setTuser(User tuser) {
		this.tuser = tuser;
	}

	public int getMjid() {
		return mjid;
	}

	public void setMjid(int mjid) {
		this.mjid = mjid;
	}

	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	
	
	
}
