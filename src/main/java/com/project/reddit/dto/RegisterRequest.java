package com.project.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	/*
	 * user signup Data transfer object
	 */
	private String email;
	private String username;
	private String password;
	
}
