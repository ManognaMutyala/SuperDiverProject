package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.SignUpService;
import com.udacity.jwdnd.course1.cloudstorage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/file")
@Controller
public class FileController {

    @Autowired
    StorageService storageService;

    @Autowired
    SignUpService signUpService;

    private NotesService notesService;

    private CredentialService credentialService;


    public FileController(StorageService storageService, SignUpService signUpService,NotesService notesService,CredentialService credentialService) {
        this.storageService = storageService;
        this.signUpService = signUpService;
        this.notesService=notesService;
        this.credentialService=credentialService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, Model model, Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm, RedirectAttributes redirectAttributes) throws IOException {
        int userId=signUpService.getUserId(authentication.getName());
        List<File> files =new ArrayList<File>();
        files=storageService.getFiles(userId);
        model.addAttribute("fileList",files);
        model.addAttribute("notes",notesService.getAllNotes(userId));
        model.addAttribute("credentialslist",credentialService.getAllCredentials(userId));

        if(multipartFile.isEmpty())
        {
            redirectAttributes.addFlashAttribute("error","Empty File");
            return "redirect:/home";

        }
        if(multipartFile.getSize()>5242880)
        {
            redirectAttributes.addFlashAttribute("error","File exceeds limit");
            throw new MaxUploadSizeExceededException(multipartFile.getSize());
        }

        if(storageService.getFileDetailsByName(multipartFile.getOriginalFilename())!=null) {
            model.addAttribute("uploadError",true);
            redirectAttributes.addFlashAttribute("error","File already exists");
            return "redirect:/home";
        }

        int r= storageService.insert(multipartFile,userId);
        if(r==1)
        {
            redirectAttributes.addFlashAttribute("success","File inserted successfully");
            return "redirect:/home";

        }
        redirectAttributes.addFlashAttribute("error","Error uploading file");
        return "redirect:/home";
    }

    @GetMapping("/{fileId}")
    public ResponseEntity viewFiles(@PathVariable("fileId") String fileid, Authentication authentication,  HttpServletResponse response, Model model) throws IOException {
        int userId=signUpService.getUserId(authentication.getName());
        File file=storageService.getFileDetails(fileid);
        if(file.getUserid()==userId)
        {
            String filename=file.getFilename();
            MediaType mediaType = MediaType.parseMediaType(file.getContenttype());
            String contentDisposition = "Content-Disposition: attachment; filename=\"" + filename + "\"";
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(file.getFiledata());

        }
        model.addAttribute("notes",notesService.getAllNotes(userId));
        response.sendRedirect("/home");
        return null;

    }

    @GetMapping("/delete/{filename}")
    public void deleteFile(@PathVariable("filename") String fileName,Authentication authentication,HttpServletResponse servletResponse,Model model,RedirectAttributes redirectAttributes) throws IOException {
        int userId= signUpService.getUserId(authentication.getName());
        File file=storageService.getFileDetailsByName(fileName);
        if(file.getUserid()==userId)
        {
            storageService.deleteFile(fileName);
        }
        model.addAttribute("notes",notesService.getAllNotes(userId));
        servletResponse.sendRedirect("/home");
    }
}
