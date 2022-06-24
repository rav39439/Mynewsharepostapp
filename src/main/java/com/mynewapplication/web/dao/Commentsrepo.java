package com.mynewapplication.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mynewapplication.web.entities.Comments;

public interface Commentsrepo extends JpaRepository<Comments, Integer>  {
	

	@Query("from Comments u where u.book.cId = :id")

	public List<Comments> findCommentsById(@Param("id") int id);
	

}
