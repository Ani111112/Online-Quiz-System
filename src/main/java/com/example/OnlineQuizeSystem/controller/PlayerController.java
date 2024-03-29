package com.example.OnlineQuizeSystem.controller;

import com.example.OnlineQuizeSystem.model.Player;
import com.example.OnlineQuizeSystem.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    @Autowired
    PlayerService playerService;
    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody Player player) {
        ResponseEntity response = null;
        Player savedPlayer = playerService.singUp(player);
        if (savedPlayer != null) {
            response = new ResponseEntity<>("Registration Successfull", HttpStatus.ACCEPTED);
        }else response = new ResponseEntity<>("Registration not successful", HttpStatus.BAD_REQUEST);
        return response;
    }
    @RequestMapping(value = "/xyz", method = RequestMethod.GET)
    public String login() {
        return "xyz";
    }

    @GetMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String email, @RequestParam String enteredOtp) {
        try {
            return new ResponseEntity<>(playerService.verifyEmail(email, enteredOtp), HttpStatus.ACCEPTED);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
