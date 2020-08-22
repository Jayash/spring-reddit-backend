package com.project.reddit.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.reddit.dto.AuthenticationResponse;
import com.project.reddit.dto.LoginRequest;
import com.project.reddit.dto.RefreshTokenRequest;
import com.project.reddit.dto.RegisterRequest;
import com.project.reddit.service.AuthService;
import com.project.reddit.service.RefreshTokenService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
		 // @RequestBody maps the HttpRequest body to transfer or domain object,
		 //enabling automatic deserialization of the inbound HttpRequest body onto a Java object
		  
		authService.signup(registerRequest);
		return new ResponseEntity<>("User Registration sucessful", HttpStatus.OK);
	}
	/*
	 * To verify the user from the token that was send to the user via mail
	 */
	@GetMapping("/accountVerification/{token}")
	public ResponseEntity<String> verifyAccount(@PathVariable String token) {
		 
		authService.verifyAccount(token);
		
		return new ResponseEntity<String>("Account Activated Successfully", HttpStatus.OK);
	}
	
	/*	Flow of authenticate
	 * AuthService.login -> AuthenticationManager.authenticate() -> userDetailService(UserDetailServiceImpl).loadUserByUsername -> database
	 */
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
		return authService.login(loginRequest);
	}
	
	@PostMapping("refress/token")
	public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest requestTokenRequest) {
		return authService.refreshToken(requestTokenRequest);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
		
		return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully");
	}
}
