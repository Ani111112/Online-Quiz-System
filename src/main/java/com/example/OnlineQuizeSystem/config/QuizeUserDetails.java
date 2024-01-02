package com.example.OnlineQuizeSystem.config;

import com.example.OnlineQuizeSystem.model.Player;
import com.example.OnlineQuizeSystem.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizeUserDetails implements UserDetailsService {
    @Autowired
    PlayerRepository playerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userName, password = null;
        List<GrantedAuthority> authorities = null;
        List<Player> players = playerRepository.findByEmail(username);
        if (players.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user " + username);
        } else {
            userName = players.get(0).getEmail();
            password = players.get(0).getPassword();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(players.get(0).getRole()));
        }
        return new User(username, password, authorities);
    }
}
