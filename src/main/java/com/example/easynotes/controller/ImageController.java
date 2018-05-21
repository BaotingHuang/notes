package com.example.easynotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.easynotes.repository.ImageRepository;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Image;
import com.example.easynotes.model.Note;
import com.example.easynotes.model.Notebook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.easynotes.storage.StorageService;
import com.example.easynotes.storage.StorageFileNotFoundException;

import java.util.*;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;



@Controller
public class ImageController {
    private final StorageService storageService;

    @Autowired
    ImageRepository imageRepository;

    /*
    public ImageController() {
        this.storageService = null;
    }
    */

    @Autowired
    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    private ArrayList<Image> images;

    // Carousel
    @GetMapping("/image")
    public String carousel(Model model) {
    	ArrayList<Image> activeImages = (ArrayList<Image>)imageRepository.findByIsActiveTrue();
        model.addAttribute("activeImages", activeImages);

        System.out.println("In ImageController.carousel()");
        return "carousel";
    }
    
    // Display list or Add (if action=Add)
    @GetMapping("/imageAdmin")
    public String admin(@RequestParam(name="action", required=false, defaultValue="list") String action, Model model) {
    	System.out.println("++++++ admin() ++++++++++");
    	
    	String lowerAction = action.toLowerCase();
    	if(lowerAction.equals("showadd")) {
    		System.out.println("admin() - action - " + action);
    		
            model.addAttribute("showAdd", true);
    	}

    	model.addAttribute("images", images());
        return "ImageAdmin";
    }
    
    // Display Edit section
    @GetMapping({"/editImage"})
    public String edit(@RequestParam(name="imageId", required=false) Long imageId, Model model) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", imageId));
    	
        // Display the Add section on the home page
        model.addAttribute("showEdit", true);
        model.addAttribute("editImage", image); // editnote

        return "ImageAdmin";
    }
    
    // Add new Image
    @PostMapping(value="/imageAdmin", params="action=Add")
    public String add(@ModelAttribute Image image,  Model model) {
    	try {
    		System.out.println("Add() Image - " + image.toString());
    		if(image.getIsActive() == true) {
    			System.out.println("It is TRUE");
    		}
	        imageRepository.save(image);
    	}
    	catch(Exception e) {
    		System.err.println("Error saving - " + e);
    	}
	   
        model.addAttribute("images", images());
        return "ImageAdmin";
    }
    
    // Upload
    @PostMapping(value="/imageAdmin", params="action=Upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, @ModelAttribute Image image,  Model model) {
    	try {
    		System.out.println("Upload() Image - " + image.toString());
    		if(image.getIsActive() == true) {
    			System.out.println("It is TRUE");
    			
    		}
    		
    		// getting current directory
    		Path currentRelativePath = Paths.get("");
    		String s = currentRelativePath.toAbsolutePath().toString();
    		System.out.println("Current relative path is: " + s);
    		//Current relative path is: /Users/kevinavoy/Downloads/easy-notes
    		
    		// probably don;t want a URL - rather a file dir/name
    		// dir = page name?
    		//image.setImageUrl("file:///Users/kevinavoy/Downloads/easy-notes/upload-dir/"+ file.getOriginalFilename());
    		image.setImageUrl("http://localhost/img/" + file.getOriginalFilename());
	        imageRepository.save(image);
	        storageService.store(file);

    	}
    	catch(Exception e) {
    		System.err.println("Error saving - " + e);
    	}

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/imageAdmin";
    }

    
    // Saving an editted Image
    @PostMapping(value="/imageAdmin", params="action=Save")
    public String save(@ModelAttribute Image image, Model model) {
    	System.out.println("Action Save - " + image.getId()); 
    	try {
    		Image originalImage = imageRepository.findById(image.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image", "id", image.getId()));
    		originalImage.setImageName(image.getImageName());
    		originalImage.setIsActive(image.getIsActive());
    		originalImage.setImageUrl(image.getImageUrl());
    		originalImage.setDescription(image.getDescription());  // set the notebook
	        imageRepository.save(originalImage);
    	}
    	catch(Exception e) {
    		System.err.println("Error saving - " + e);
    	}
	   
        model.addAttribute("images", images());
        return "ImageAdmin";
    }
    
    // Cancel
    @PostMapping(value="/imageAdmin", params="action=Cancel")
    public String cancel(Model model) {
    	System.out.println("Action = 'Cancel'"); 

    	// User was adding an Image and cancelled
        model.addAttribute("images", images());
        return "ImageAdmin";
    }    

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    
    public ArrayList<Image> images() {
    	// Do we want to cache images
    	images = (ArrayList<Image>)imageRepository.findAll();
    	return images;
    }
}