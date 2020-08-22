package com.project.reddit.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private JavaMailSender mailSender;
	private MailContentBuilder mailContentBuilder;
	
	/*
	 * This method will send the verifaication mail to the user
	 */
	@Async
	public void sendMail(NotificationEmail notificationEmail) {
		
		MimeMessagePreparator messagePreparator = mimeMessage ->  {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("verify@reddit.com");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
		};
		
		try {
			mailSender.send(messagePreparator);
			log.info("Activation email sent");
		} catch (MailException e) {
			throw new SpringRedditException("Exception occured when sending mail");
		}
	}
}
