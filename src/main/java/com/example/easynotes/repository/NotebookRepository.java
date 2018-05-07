package com.example.easynotes.repository;

import com.example.easynotes.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long> {

}
