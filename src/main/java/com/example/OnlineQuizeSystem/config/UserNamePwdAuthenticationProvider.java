package com.example.OnlineQuizeSystem.config;

import com.example.OnlineQuizeSystem.model.Player;
import com.example.OnlineQuizeSystem.repository.PlayerRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Component
public class UserNamePwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<Player> players = playerRepository.findByEmail(username);
        if (players.size() > 0) {
            if (passwordEncoder.matches(password, players.get(0).getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(players.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else throw new BadCredentialsException("Password is not Matched");
        }else throw new BadCredentialsException("User Details Not Found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
