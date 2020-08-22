package com.project.reddit.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.project.reddit.exceptions.SpringRedditException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtProvider {
	
	private KeyStore keyStore;
	
	//To read property from property file
	@Value("${jwt.expiration.time}")
	private Long jwtExpirationTime;
	
	/*
	 * @PostConstruct to manage the lifecycle of the object and 
	 * initialize the keyStore as the object gets constructed
	 */
	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
			keyStore.load(resourceAsStream, "secret".toCharArray());
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new SpringRedditException("Exception while loading keystore");
		}
	}
	
	/*
	 * generating token using public key encryption algorithm i.e. signing with the private key
	 */
	public String generateToken(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return Jwts.builder().setSubject(principal.getUsername())
				.signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
				.compact();
	}
	
	/*
	 * generating token using public key encryption algorithm i.e. signing with the private key
	 */
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username)
				.signWith(getPrivateKey())
				.setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime)))
				.compact();
	}
	
	/*
	 * To get the private key from the jks certificate
	 */
	private Key getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new SpringRedditException("Error Occured while retrieving public key from keyStore");
		}
	}
	
	/*
	 * To validate if the jwt token is correct 
	 */
	public boolean validateToken(String jwt) {
		Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
		return true;
	}
	
	/*
	 * To get the public key from the jks certificate
	 */
	private PublicKey getPublicKey() {
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new SpringRedditException("Exception occured while retrieving public key from keystore");
		}
	}
	
	/*
	 * To fetch the username from the jwt token
	 */
	public String getUsernameFromJwt(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(getPublicKey())
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public Long getJwtExpirationTime() {
		return jwtExpirationTime;
	}
}
