package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.SignUpService;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/home")
@Controller
public class HomeController {

   StorageService storageService;

   SignUpService signUpService;
   NotesService notesService;
   CredentialService credentialService;


    public HomeController(SignUpService signUpService, NotesService notesService,StorageService storageService,CredentialService credentialService ){
        this.storageService = storageService;
        this.signUpService = signUpService;
        this.notesService=notesService;
        this.credentialService = credentialService;
    }


    @GetMapping()
    public String home(Model model, @ModelAttribute("noteForm") NoteForm noteFields, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm, Authentication authentication){
        String username=authentication.getName();
        int userid=signUpService.getUserId(username);
        model.addAttribute("fileList",storageService.getFiles(userid));
        model.addAttribute("notes",notesService.getAllNotes(userid));
        model.addAttribute("credentialslist",credentialService.getAllCredentials(userid));
        return "home";
    }

    @PostMapping("/logout")
    public String logout(Model model,@ModelAttribute("noteForm") NoteForm noteFields){
        model.addAttribute("logout",true);
        return "login";

    }


}
