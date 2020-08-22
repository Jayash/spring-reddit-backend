package com.project.reddit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.reddit.model.Post;
import com.project.reddit.model.User;
import com.project.reddit.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{
	
	public Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
