package com.project.reddit.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "usertable")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "userid")
	private Long userId;
	
	@NotBlank(message = "Username Required")
	private String username;
	
	@NotBlank(message = "Password Required") // doesn't allows spaces
	private String password;
	
	@Email
	@NotEmpty(message = "Email Required")  // allows spaces
	private String email;
	
	@Column(name = "createddate")
	private Instant createdDate;
	
	private Boolean enabeled;
}
