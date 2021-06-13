package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @GetMapping
    public String signup() {
        return "signup";
    }

    //added flash attributes as per https://docs.spring.io/spring-framework/docs/3.2.4.RELEASE_to_4.0.0.M3/Spring%20Framework%203.2.4.RELEASE/org/springframework/web/servlet/mvc/support/RedirectAttributes.html#addAttribute(java.lang.String,%20java.lang.Object
    @PostMapping
    public String signUpUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        String signuperror ="no";

        if (signUpService.isUserNameAvailable(user.getUsername())) {
            System.out.println("is user available"+signUpService.isUserNameAvailable(user.getUsername()));
            signuperror = "Username already exists";
            redirectAttributes.addFlashAttribute("signuperror", signuperror);
            return "redirect:/signup";

        }
        if (signuperror.equals("no")) {
            int rowsAdded = signUpService.createUser(user);
            System.out.println("rows added are"+rowsAdded);
            if (rowsAdded < 0) {
                System.out.println("Inside signup user creation");
                signuperror = "There was an error.html signing you up. Please try again.";
                redirectAttributes.addFlashAttribute("signuperror", signuperror);
                return "redirect:/signup";

            }

            redirectAttributes.addFlashAttribute("signupsuccess", true);
            return "redirect:/login";

        }

        return "redirect:/signup";

    }

}
