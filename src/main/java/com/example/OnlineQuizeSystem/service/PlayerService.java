package com.example.OnlineQuizeSystem.service;

import com.example.OnlineQuizeSystem.model.Player;
import com.example.OnlineQuizeSystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public Player singUp(Player player) {
        String hashPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(hashPassword);
        return playerRepository.save(player);
    }
}
