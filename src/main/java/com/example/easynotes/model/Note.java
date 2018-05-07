package com.example.easynotes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.io.Serializable;;

@Entity
@Table(name = "notes")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},  allowGetters = true)
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String category;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name="notebook_id")
    private Notebook notebook;

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
    public String getTitle() {
    	return title;
    }
    public void setTitle(String pTitle) {
    	title = pTitle;
    }
    
    public String getCategory() {
    	return category;
    }
    public void setCategory(String pCategory) {
    	category = pCategory;
    }
    public String getContent() {
    	return content;
    }
    public void setContent(String pContent) {
    	content = pContent;
    }
    public Notebook getNotebook() {
    	return notebook;
    }
    public void setNotebook(Notebook pNotebook) {
    	notebook = pNotebook;
    }
}