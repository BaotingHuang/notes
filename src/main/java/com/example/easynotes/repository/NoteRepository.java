package com.example.easynotes.repository;

import com.example.easynotes.model.Note;
import com.example.easynotes.model.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
	  List<Note> findByNotebook(Notebook notebookId);
}
