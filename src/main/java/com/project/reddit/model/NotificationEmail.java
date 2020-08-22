package com.project.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class NotificationEmail {
	private String subject;
	private String recipient;
	private String body;
}
