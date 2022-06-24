package com.mynewapplication.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mynewapplication.web.entities.Mychatmessages;

public interface Allmessagesrepo  extends JpaRepository<Mychatmessages, Integer> {

}
