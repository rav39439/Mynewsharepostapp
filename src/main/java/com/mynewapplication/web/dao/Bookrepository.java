package com.mynewapplication.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mynewapplication.web.entities.Books;

public interface Bookrepository extends JpaRepository<Books, Integer> {

	@Query("from Books as c where c.user.id =:userId")
	//currentPage-page
	//Contact Per page - 5
	public List<Books> findBooksById(@Param("userId")int userId);
	
	
	
	@Query("from Books as c where c.user.name =:name")
	//currentPage-page
	//Contact Per page - 5
	public List<Books> findBooksByname(@Param("name")String name);
	
	
	
	@Query("from Books as c where c.cId =:bookid")
	//currentPage-page
	//Contact Per page - 5
	public Books findBookById(@Param("bookid")int bookid);
	
	
	
	
	//search
	//public List<Books> findByNameContainingAndUser(String name,User user);
	



	
}
