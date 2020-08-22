package com.project.reddit.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {
 
	private final TemplateEngine templateEngine;
	/*
	 * we used mailtemplate html file for sending the mail
	 * 
	 * We used ThymLeaf dependency to create the template
	 */
	public String build(String message) {
		Context context = new Context();
		context.setVariable("message", message);
		return templateEngine.process("mailTemplate", context);
	}
}
