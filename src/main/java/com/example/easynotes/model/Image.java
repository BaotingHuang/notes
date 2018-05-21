package com.example.easynotes.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

	@Entity
	@Table(name = "Image")
	@EntityListeners(AuditingEntityListener.class)
	@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},  allowGetters = true)
	public class Image implements Serializable {
		private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    //@Column(name = "is_active", columnDefinition = "boolean default true", nullable = false)
	    //select id, cast(is_active as unsigned) as IS_Active from image;
	    private boolean isActive = true;
	    
	    @NotBlank
	    private String imageName;

	    @NotBlank
	    private String description;
	    
	    @NotBlank
	    private String imageUrl;
	    	    
	    @Column(nullable = false, updatable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    @CreatedDate
	    private Date createdAt;

	    @Column(nullable = false)
	    @Temporal(TemporalType.TIMESTAMP)
	    @LastModifiedDate
	    private Date updatedAt;

	    public Long getId() {
	    	return id;
	    }
	    public void setId(Long pId) {
	    	id = pId;
	    }
	    public boolean getIsActive() {
	    	return isActive;
	    }
	    public boolean getIsNotActive() {
	    	return !isActive;
	    }
	    public void setIsActive(boolean pValue) {
	    	isActive = pValue;
	    }
	    public String getImageName() {
	    	return imageName;
	    }
	    public void setImageName(String pName) {
	    	imageName = pName;
	    }
	    public String getDescription() {
	    	return description;
	    }
	    public void setDescription(String pDescription) {
	    	description = pDescription;
	    }
	    public String getImageUrl() {
	    	return imageUrl;
	    }
	    public void setImageUrl(String pUrl) {
	    	imageUrl = pUrl;
	    }
	    
	    public String toString() {
	    	return "name: " +  getImageName() + "\nIsActive - " + getIsActive() + "\nDescription - " + getDescription() + "\nURL - " + getImageUrl(); 
	    }
}