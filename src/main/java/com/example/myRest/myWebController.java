package com.example.myRest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class myWebController {

    @GetMapping("/load_profile")
    public String loadProfile(Model model){
        return "loadprofile";
    }

}
