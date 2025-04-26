package ru.lot.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.lot.entity.Ticket;

import java.time.Instant;

@Component
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String adminMail;

    @Value("${spring.mail.username}")
    private String serviceName;

    public void sendDrawResult(String winningCombination, Ticket ticket) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
        try {
            helper.setFrom(serviceName);
            helper.setTo(ticket.getUser().getEmail());
            helper.setSubject("Lottery");
            helper.setText("Hello!\nYou lost in a lottery!\nYour numbers: "
                    + ticket.getPickedNumbers() + "\nWinning combination: " + winningCombination);
        } catch (MessagingException ex) {
            log.warn("Mail was not sent " + ex);
        }
        mailSender.send(mailMessage);
        log.info("Email sent! To: " + ticket.getUser().getEmail() + ". Time: " + Instant.now());
    }

    public void sendWinnerLetter(Ticket ticket) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
        try {
            helper.setFrom(serviceName);
            helper.setTo(ticket.getUser().getEmail());
            helper.setSubject("Lottery");
            helper.setText("Hello!\nYou WON in a lottery!\nYour numbers: "
                    + ticket.getPickedNumbers() + "\nYou won " + ticket.getDraw().getLotteryType().getWinningAmount());

        } catch (MessagingException ex) {
            log.warn("Mail was not sent " + ex);
        }
        mailSender.send(mailMessage);
        log.info("Email sent! To: " + ticket.getUser().getEmail() + ". Time: " + Instant.now());
    }


    public void sendAdminError(Exception e) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");
        try {
            helper.setFrom(serviceName);
            helper.setTo(adminMail);
            helper.setSubject("Error Lottery");
            helper.setText("Error has occured! Description: " + e.getMessage() + "\nAt " + Instant.now());
        } catch (MessagingException ex) {
            log.warn("Mail was not sent: " + ex);
        }
        mailSender.send(mailMessage);
        log.info("Email sent! To: " + adminMail + ". Time: " + Instant.now());
    }
}
