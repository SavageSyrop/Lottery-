package ru.lot.service;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.time.LocalDateTime;

// TODO понять, какие именно письма отсылаться будут
@Component
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String serviceName;

    public void sendDrawResult() throws Exception {
//        MimeMessage mailMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
//        helper.setFrom(serviceName);
//        helper.setTo(user.getEmail());
//        helper.setSubject("Account activation");
//        helper.setText(code);
//        mailSender.send(mailMessage);
//        log.info("Email sent! To: " + user.getEmail() + ". Time: " + LocalDateTime.now());
    }

    public void sendWinnerLetter() throws Exception {
//        MimeMessage mailMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
//        helper.setFrom(serviceName);
//        helper.setTo(user.getEmail());
//        helper.setSubject("Account activation");
//        helper.setText(code);
//        mailSender.send(mailMessage);
//        log.info("Email sent! To: " + user.getEmail() + ". Time: " + LocalDateTime.now());
    }


    public void sendAdminError() throws Exception {
//        MimeMessage mailMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
//        helper.setFrom(serviceName);
//        helper.setTo(user.getEmail());
//        helper.setSubject("Account activation");
//        helper.setText(code);
//        mailSender.send(mailMessage);
//        log.info("Email sent! To: " + user.getEmail() + ". Time: " + LocalDateTime.now());
    }
}
