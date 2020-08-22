package com.project.reddit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.reddit.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * since it is a interface we have to provide a implementation in UserdetailServiceImpl
	 */
	private final UserDetailsService userdetailService;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	// authentication manager bean
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		/* csrf attach mainly occur when there are session
		 * and when we are using cookies to authenticate the session
		 * As rest Api are stateless and we are using JWT for authorization 
		 * so we can safely disable this feature.
		 * 
		 * with csrf disabled we permit all the request url that
		 * Matches /api/auth
		 */
		httpSecurity.csrf().disable()
						.authorizeRequests()
						.antMatchers("/api/auth/**")
						.permitAll()
		                .antMatchers(HttpMethod.GET, "/api/subreddit")
		                .permitAll()
		                .antMatchers(HttpMethod.GET, "/api/posts/")
		                .permitAll()
		                .antMatchers(HttpMethod.GET, "/api/posts/**")
		                .permitAll()
		                .antMatchers("/v2/api-docs",
		                		"/configuration/ui",
		                		"/swagger-resources/**",
		                		"/configuration/security",
		                		"/swagger-ui.html",
		                		"/webjars/**")
		                .permitAll()
						.anyRequest()
						.authenticated();
		
		httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	/*
	 * Method injection
	 * using object in parameter and adding Autowired annotation
	 * 
	 * AuthenticationManagerBuilder will be injected at runtime
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userdetailService).passwordEncoder(passwordEncoder());
	}
	
	/*
	 * @ Bean is a method-level annotation.
	 * we can autowire the returned object
	 * 
	 * BCryptPasswordEncoder used to encrypt password
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
