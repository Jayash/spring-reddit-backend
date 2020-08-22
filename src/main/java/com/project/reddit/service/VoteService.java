package com.project.reddit.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.dto.VoteDto;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.model.Post;
import com.project.reddit.model.Vote;
import com.project.reddit.model.VoteType;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class VoteService {
	
	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	@Transactional
	public void vote(VoteDto voteDto) {
		Post post = postRepository.findById(voteDto.getPost())
		.orElseThrow(() -> new SpringRedditException("post not found " + voteDto.getPost()));
		
		Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		if(voteByPostAndUser.isPresent()) {
			if(voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
				throw new SpringRedditException("you have already " + voteDto.getVoteType() + " for this post");
			}
		}
		
		if(VoteType.UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount()  +1);
		} else {
			post.setVoteCount(post.getVoteCount() - 1);
		}
		Vote vote = mapDtoToVote(voteDto, post);
		vote.setUser(authService.getCurrentUser());
		postRepository.save(post);
		voteRepository.save(vote);
	}
	
	public Vote mapDtoToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.build();
	}
}
