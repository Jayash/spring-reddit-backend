package com.project.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.reddit.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

	public Optional<VerificationToken> findByToken(String token);

}
