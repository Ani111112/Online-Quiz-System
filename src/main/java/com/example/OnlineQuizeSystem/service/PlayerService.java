package com.example.OnlineQuizeSystem.service;

import com.example.OnlineQuizeSystem.model.Otp;
import com.example.OnlineQuizeSystem.model.Player;
import com.example.OnlineQuizeSystem.repository.PlayerRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@Service
public class PlayerService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    HashMap<String, Otp> otpHashMap = new HashMap<>();
    public Player singUp(Player player) {
        String otp = generateOTP();
        String username = player.getEmail();
        List<Player> players = playerRepository.findByEmail(username);
        if (players.size() > 0) throw new RuntimeException("Email Id Already Registered");
        otpHashMap.put(username, new Otp(otp, System.currentTimeMillis()));
        sendMailToUser(username, otp);
        String hashPassword = passwordEncoder.encode(player.getPassword());
        player.setPassword(hashPassword);
        return playerRepository.save(player);
    }
    public String verifyEmail(String email, String enteredOtp) {
        Otp otpData = otpHashMap.get(email);
        if (otpData != null && otpData.getOtp().equals(enteredOtp) && isOtpValid(otpData)) {
            List<Player> players = playerRepository.findByEmail(email);
            players.get(0).setVerified(true);
            return "Email is Verified Successfully";
        }else if (!otpData.getOtp().equals(enteredOtp)) throw new RuntimeException("You Entered Wrong OTP");
        else throw new RuntimeException("Otp is Expired");
    }

    private boolean isOtpValid(Otp otpData) {
        long currentSystemTime = System.currentTimeMillis();
        return (currentSystemTime - otpData.getTimeStamp()) <= 300000;
    }

    private String generateOTP() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    private Pair sendMailToUser(String username, String otp) {
        // Replace these with your email credentials
        String senderEmail = "aniruddhamukherjee232@gmail.com";
        String appPassword = "sszv fvjp hzsy iaby";
        String recipientEmail = username;
        String emailContent = String.format(
                "Thank you for choosing MindMingle Quiz!\n\n" +
                        "To ensure the security of your account, we require you to verify your email address. Please use the following One-Time Password (OTP) to complete the verification process.\n\n" +
                        "Your OTP: " + otp + "\n" +
                        "This OTP is valid for the next 5 minutes. If you do not complete the verification within this time frame, you may request a new OTP on our website.\n\n" +
                        "If you did not attempt to verify your email or if you encounter any issues, please contact our support team immediately.\n\n" +
                        "Thank you for being a part of MindMingle Quiz. We look forward to providing you with a secure and enjoyable experience.\n\n" +
                        "Best Regards,\n" +
                        "MindMingle Quiz\n");

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
            message.setSubject("Your One-Time Password (OTP) for Email Verification");
            message.setText(emailContent);

            // Send the message
            Transport.send(message);
            System.out.println("OTP sent successfully!");
            return new Pair<>(otp, true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
