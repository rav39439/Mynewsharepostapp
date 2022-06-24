package com.mynewapplication.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mynewapplication.web.entities.Friends;

public interface Friendsrepo extends JpaRepository<Friends, Integer> {
	

	@Query("from Friends as c where c.user.id =:userId")
	//currentPage-page
	//Contact Per page - 5
	public List<Friends> findBooksById(@Param("userId")int userId);
	
	
	
}
