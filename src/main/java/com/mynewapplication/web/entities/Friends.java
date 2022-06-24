package com.mynewapplication.web.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mynewapplication.web.entities.User;


@Entity
@Table(name="NFRIENDS")
public class Friends {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int fid;
	
	@JsonIgnore
	@ManyToOne
	private User user;
	private String allfriends;
	private int friendid;
	
	
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getAllfriends() {
		return allfriends;
	}
	public void setAllfriends(String allfriends) {
		this.allfriends = allfriends;
	}
	public int getFriendid() {
		return friendid;
	}
	public void setFriendid(int friendid) {
		this.friendid = friendid;
	}
	public String getFriendreqname() {
		return friendreqname;
	}
	public void setFriendreqname(String friendreqname) {
		this.friendreqname = friendreqname;
	}
	private String friendreqname;
	
	
	
	
	
}
