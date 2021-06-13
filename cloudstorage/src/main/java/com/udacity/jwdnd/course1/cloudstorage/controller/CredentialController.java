package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private StorageService storageService;
    private SignUpService signUpService;
    private NotesService notesService;
    private CredentialService credentialService;


    public CredentialController(StorageService storageService, SignUpService signUpService, NotesService notesService, CredentialService credentialService) {
        this.storageService = storageService;
        this.signUpService = signUpService;
        this.notesService = notesService;
        this.credentialService = credentialService;
    }

    @PostMapping("/add")
    public String addCredentials(Model model, Authentication authentication, @ModelAttribute("noteForm") NoteForm noteFields, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm, RedirectAttributes redirectAttributes)
    {
        int userId= signUpService.getUserId(authentication.getName());
        model.addAttribute("notes",notesService.getAllNotes(userId));
        model.addAttribute("fileList",storageService.getFiles(userId));
        model.addAttribute("credentialslist",credentialService.getAllCredentials(userId));
        if(credentialsForm.getCredentialId()==null)
        {
            credentialService.insert(credentialsForm,userId);
            redirectAttributes.addFlashAttribute("success","Credential inserted successfully");
            return "redirect:/home";
        }
        else{

            credentialService.updateById(credentialsForm,userId);
            return "redirect:/home";
        }


    }

   @GetMapping("/add")
   public String getCredentials(Model model, Authentication authentication, @ModelAttribute("noteForm") NoteForm noteFields, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm,RedirectAttributes redirectAttributes)
   {
        int userId= signUpService.getUserId(authentication.getName());
        model.addAttribute("notes",notesService.getAllNotes(userId));
        model.addAttribute("fileList",storageService.getFiles(userId));
        model.addAttribute("credentialslist",credentialService.getAllCredentials(userId));
        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(Model model, Authentication authentication, @PathVariable("credentialId") int credentialId,@ModelAttribute("noteForm") NoteForm noteFields, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm,RedirectAttributes redirectAttributes){
        int userId=signUpService.getUserId(authentication.getName());
        model.addAttribute("notes",notesService.getAllNotes(userId));
        model.addAttribute("fileList",storageService.getFiles(userId));
        model.addAttribute("credentialslist",credentialService.getAllCredentials(userId));
        if(credentialService.isExists(userId,credentialId)!=null)
        {
            credentialService.deleteCredentialById(userId,credentialId);
            redirectAttributes.addFlashAttribute("success","Credential Inserted successfully");
            return "redirect:/home";
        }
        else
        {
            redirectAttributes.addFlashAttribute("error","Credential deletion failed");
            return "redirect:/home";
        }


    }


}
