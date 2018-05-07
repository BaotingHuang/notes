package com.example.easynotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;

import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.model.Notebook;
import com.example.easynotes.repository.NoteRepository;
import com.example.easynotes.repository.NotebookRepository;


@Controller
public class MainController {
    @Autowired
    NoteRepository noteRepository;
    @Autowired    
    NotebookRepository notebookRepository;
    private ArrayList<Note> notes;
    private ArrayList<Notebook> notebooks;
    private Notebook selectedNotebook;
		
  //  @RequestMapping("/")
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {

        model.addAttribute("name", name);
        System.out.println("In MainController.greeting()");
        return "greeting";
    }
    
    @GetMapping({"/","/home"})
    public String home(@RequestParam(name="showAdd", required=false, defaultValue="false") String showAdd, Model model) {
        //model.addAttribute("notes", updateNotes());
        model.addAttribute("notes", notesInSelectedNotebook());
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }
    
    @GetMapping({"/add"})
    public String add(Model model) {
    	// Display the Add section on the home page
        model.addAttribute("showAdd", true);
        model.addAttribute("notebooks", notebooks());
        
        return "home";
    }
    
    @PostMapping(value="/", params="action=Add")
    public String add(@ModelAttribute Note note,  Model model) {
    	try {
	        noteRepository.save(note);
    	}
    	catch(Exception e) {
    		System.err.println("Error saving - " + e);
    	}
	   
	    // we are returning the List page so we need to include the list in the model 
    	//model.addAttribute("notes", updateNotes());
    	model.addAttribute("notes", notesInSelectedNotebook());
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }
    
    @PostMapping(value="/", params="action=Cancel")
    public String cancel(Model model) {
    	// User was adding a note and cancelled
        //model.addAttribute("notes", notes());
    	model.addAttribute("notes", notesInSelectedNotebook());
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }    
    

    @GetMapping({"/edit"})
    public String edit(@RequestParam(name="noteId", required=false) Long noteId, Model model) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    	
        // Display the Add section on the home page
        model.addAttribute("showEdit", true);
        model.addAttribute("editnote", note); // editnote
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }

    // Saving an editted note
    @PostMapping(value="/", params="action=Save")
    public String save(@ModelAttribute Note note, Model model) {
    	//System.out.println("Action Saved - " + note.getId()); 
    	try {
            Note originalNote = noteRepository.findById(note.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Note", "id", note.getId()));
            originalNote.setTitle(note.getTitle());
            originalNote.setCategory(note.getCategory());
            originalNote.setContent(note.getContent());
            originalNote.setNotebook(note.getNotebook());  // set the notebook
	        noteRepository.save(originalNote);
    	}
    	catch(Exception e) {
    		System.err.println("Error saving - " + e);
    	}
	   
	   // we are returning the List page so we need to include the list in the model 
    	//model.addAttribute("notes", updateNotes());
    	model.addAttribute("notes", notesInSelectedNotebook());
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }
    @GetMapping({"/select"})
    public String select(@RequestParam(name="notebookId", required=false) Long notebookId, Model model) {
        if(notebookId == 0) {
        	selectedNotebook = null;
        }
        else {
        	selectedNotebook = notebookRepository.findById(notebookId)
                .orElseThrow(() -> new ResourceNotFoundException("Notebook", "id", notebookId));
        }
        model.addAttribute("notes", notesInSelectedNotebook());
        model.addAttribute("notebooks", notebooks());
        model.addAttribute("selectedNotebook", selectedNotebook);

        return "home";
    }
    public ArrayList<Note> updateNotes() {
 	   notes = (ArrayList<Note>)noteRepository.findAll();
 	   return notes;
    }
    
    public ArrayList<Note> notes() {
    	//if(notes == null) {
    		 notes = (ArrayList<Note>)noteRepository.findAll();
    	//}
    	return notes;
    }
            
    public ArrayList<Notebook> notebooks() {
    	// Do we want to cache notebooks
    	//if(notebooks == null) {
    		notebooks = (ArrayList<Notebook>)notebookRepository.findAll();
    	//}
    	return notebooks;
    }
    
    public ArrayList<Note> notesInSelectedNotebook() {
    	ArrayList<Note> returnVal = null;
        // note - this is handled automatically by Hibernate
    	if(selectedNotebook != null) {
    		returnVal = (ArrayList<Note>)noteRepository.findByNotebook(selectedNotebook);
    	}
    	else {
    		returnVal = notes();
    	}
    	return returnVal;
    }
}
