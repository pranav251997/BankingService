package com.citco.sprinBootDemo.service;

import org.springframework.stereotype.Service;

import com.citco.sprinBootDemo.dto.EmailDetails;

@Service
public interface EmailService {
	void sendEmailAlert(EmailDetails emailDetails);

	void sendEmailWithAttachment(EmailDetails emailDetails);
}
