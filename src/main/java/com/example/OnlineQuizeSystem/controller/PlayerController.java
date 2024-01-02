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
}
