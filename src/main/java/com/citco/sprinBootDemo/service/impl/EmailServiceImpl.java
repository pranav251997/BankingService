package com.citco.sprinBootDemo.service.impl;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.citco.sprinBootDemo.dto.EmailDetails;
import com.citco.sprinBootDemo.service.EmailService;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmailAlert(EmailDetails emailDetails) {
		// TODO Auto-generated method stub
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipient());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());

			javaMailSender.send(mailMessage);
			System.out.println("Mail sent message");
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}

	}

	@Override
	public void sendEmailWithAttachment(EmailDetails emailDetails) {
		// TODO Auto-generated method stub
		MimeMessage mailMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mailMessage, true);
			mimeMessageHelper.setFrom(senderEmail);
			mimeMessageHelper.setTo(emailDetails.getRecipient());
			mimeMessageHelper.setText(emailDetails.getMessageBody());
			mimeMessageHelper.setSubject(emailDetails.getSubject());

			FileSystemResource file = new FileSystemResource(new File(emailDetails.getAttachment()));
			mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
			javaMailSender.send(mailMessage);
			log.info(file.getFilename() + " has been sent to user with email " + emailDetails.getRecipient());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
