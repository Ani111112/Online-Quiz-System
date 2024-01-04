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
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, authorities);
                if (usernamePasswordAuthenticationToken.isAuthenticated()) {
                    Pair<String, Boolean> pair = sendMailToUser(username);
                }
                return usernamePasswordAuthenticationToken;
            }else throw new BadCredentialsException("Password is not Matched");
        }else throw new BadCredentialsException("User Details Not Found");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Pair sendMailToUser(String username) {
        // Replace these with your email credentials
        String senderEmail = "aniruddhamukherjee232@gmail.com";
        String appPassword = "sszv fvjp hzsy iaby";
        String recipientEmail = username;

        // Generate OTP
        String otp = generateOTP();

        // Email configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Authenticate sender
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP");
            message.setText("Your OTP is: " + otp);

            // Send the message
            Transport.send(message);
            System.out.println("OTP sent successfully!");
            return new Pair<>(otp, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateOTP() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
