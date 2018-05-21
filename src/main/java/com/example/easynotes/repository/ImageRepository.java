package com.example.easynotes.repository;

import com.example.easynotes.model.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
import javax.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {  
	    //@Query("SELECT * FROM Image i where i.is_active = true") 
	    //List<Image> findByIsActive();
	     
	    //@Query
    //public Iterable<Entity> findByIsActiveTrue();
    public ArrayList<Image> findByIsActiveTrue();
}
