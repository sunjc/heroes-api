package org.itrunner.heroes.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
public class MimeMessageMailSender {

    private final JavaMailSenderImpl mailSender;

    @Autowired
    public MimeMessageMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(MailMessage mailMessage) {
        try {
            log.info("Sending email ...");
            mailSender.send(createMimeMessage(mailMessage));
            log.info("Email has been sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email", e);
        }
    }

    private MimeMessage createMimeMessage(MailMessage mailMessage) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(mailMessage.getTo());
        helper.setSubject(mailMessage.getSubject());

        if (mailMessage.getText() != null) {
            helper.setText(mailMessage.getText(), true);
        }

        if (mailMessage.getFrom() == null) {
            mailMessage.setFrom(mailSender.getUsername());
        }

        if (mailMessage.getName() != null) {
            try {
                helper.setFrom(mailMessage.getFrom(), mailMessage.getName());
            } catch (UnsupportedEncodingException e) {
                helper.setFrom(mailMessage.getFrom());
            }
        } else {
            helper.setFrom(mailMessage.getFrom());
        }

        if (mailMessage.getCc() != null) {
            helper.setCc(mailMessage.getCc());
        }

        if (mailMessage.getBcc() != null) {
            helper.setBcc(mailMessage.getBcc());
        }

        if (mailMessage.getReplyTo() != null) {
            helper.setReplyTo(mailMessage.getReplyTo());
        }

        if (mailMessage.getAttachments() != null) {
            for (File file : mailMessage.getAttachments()) {
                FileSystemResource resource = new FileSystemResource(file);
                helper.addAttachment(file.getName(), resource);
            }
        }

        if (mailMessage.getSentDate() != null) {
            helper.setSentDate(mailMessage.getSentDate());
        }
        return message;
    }
}