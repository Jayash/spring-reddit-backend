package com.project.reddit.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.model.User;
import com.project.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * 
	 * We create a spring provided wrapper class for user i.e. UserDetails
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> optional = userRepository.findByUsername(username);
		
		User user = optional.orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), 
																		user.getPassword(), user.getEnabeled(),
																		true, true, true, getAuthorities("User"));
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities(String role) {
		//we are providing simple granted authority for our user
		return Collections.singletonList(new SimpleGrantedAuthority(role));
	}

}
