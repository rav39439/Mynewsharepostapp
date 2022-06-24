package com.mynewapplication.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mynewapplication.web.entities.User;

public interface Databaseusers extends JpaRepository<User, Integer> {
	@Query("select u from User u where u.email =:email")
	public User getUserByUserName(@Param("email") String email);
	
	@Query("select u from User u where u.id =:id")
	public User getUserByUserId(@Param("id") int id);
	
	
	@Query("select u from User u where u.name =:name")
	
	    public User getuserbyname(@Param("name") String name);
	
	
	@Query("select u from User u where u.email =:email")
	
    public User getuserbyemail(@Param("email") String email);
	}
	
