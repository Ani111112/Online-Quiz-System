package com.example.OnlineQuizeSystem.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp() {
        return "SignUp sucessfull";
    }
}
