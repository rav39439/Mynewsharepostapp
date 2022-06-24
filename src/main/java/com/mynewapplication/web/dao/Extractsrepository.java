package com.mynewapplication.web.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mynewapplication.web.entities.Extracts;

public interface Extractsrepository extends JpaRepository<Extracts, Integer> {

	
	@Query("from Extracts u where u.book.cId = :id")

	public List<Extracts> getExtractsbyId(@Param("id") int id);
	
	
}
