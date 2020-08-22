package com.project.reddit.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.dto.AuthenticationResponse;
import com.project.reddit.dto.LoginRequest;
import com.project.reddit.dto.RefreshTokenRequest;
import com.project.reddit.dto.RegisterRequest;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.model.NotificationEmail;
import com.project.reddit.model.User;
import com.project.reddit.model.VerificationToken;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.repository.VerificationTokenRepository;
import com.project.reddit.security.JwtProvider;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
	
	/*
	 * The field injection will work just fine but it is not 
	 * recommended thats why we will use constructor injection
	 * 
	 * @Autowired
	 * private PasswordEncoder passwordEncoder;
	 * @Autowired
	 * private UserRepository userRepository;
	*/
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailService mailService;
	private final RefreshTokenService refreshTokenService;
	
	// Since Authentication manager is a interface so we have to provide the implementation
	// we have provided the implementation in SecurityConfig
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	
	
	/*
	 * @Transactional annotation will define the scope of a single
	 * database transaction
	 * 
	 * it will handle the begin commit
	 */
	@Transactional
	public void signup(RegisterRequest registerRequest) {
		
			User user = User.builder().username(registerRequest.getUsername())
					.password(passwordEncoder.encode(registerRequest.getPassword()))
					.email(registerRequest.getEmail())
					.createdDate(Instant.now())
					.enabeled(false).build();
			
			userRepository.save(user);
			
			String token = genearteVarificationToken(user);
			
			mailService.sendMail(new NotificationEmail("Please Activate your account",
					user.getEmail(), getUrl(token)));
	}
	
	private String getUrl(String token) {
		return "Please click on the below url to activate your account: " + 
				"http://localhost:89/api/auth/accountVerification/" + token;
	}

	
	/*
	 * To verify token and save it to the database
	 * 
	 * We need to to save the token because if the user need to verify
	 * the token after 2 or 3 days we may not have this in the memory
	 */
	private String genearteVarificationToken(User user) {
		
		//To generate random uuid for verification token
		String token = UUID.randomUUID().toString();
		
		VerificationToken verificationToken = VerificationToken.builder().user(user).token(token).build();
		
		verificationTokenRepository.save(verificationToken);
		return token;
		
	}
	/*
	 * To verify the account from the email generated token
	 * 
	 * fetch token from database and set user to enable and if no token found
	 * we throw exception
	 */
	public void verifyAccount(String token) {
		
		Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
		
		verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
		fetchUserAndEnable(verificationToken.get());
		
	}
	
	@Transactional
	private void fetchUserAndEnable(VerificationToken verificationToken) {
		User user = verificationToken.getUser();
		user.setEnabeled(true);
		userRepository.save(user);
	}
	
	/*
	 * AuthenticationManager.authenticate() -> userDetailService(UserDetailServiceImpl).loadUserByUsername -> database
	 * 
	 * create and return the Jwt token with AuthenticateResponce dto
	 */
	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), 
																						loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		String token = jwtProvider.generateToken(authenticate); // creation of jwt token
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken(refreshTokenService.generateRefreshToken().getToken())
				.expiresAt(Instant.now().plusMillis((jwtProvider.getJwtExpirationTime())))
				.username(loginRequest.getUsername())
				.build();
	}
	
	@Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
		
		return userRepository
				.findByUsername(principal.getUsername())
				.orElseThrow(() -> new SpringRedditException("username not found " + principal.getUsername()));
	}

	public AuthenticationResponse refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
		refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
		
		String token = jwtProvider.generateToken(refreshTokenRequest.getUsername());
		
		return AuthenticationResponse.builder()
				.authenticationToken(token)
				.refreshToken("")
				.expiresAt(Instant.now().plusMillis((jwtProvider.getJwtExpirationTime())))
				.username(refreshTokenRequest.getUsername())
				.build();
				
	}
}
