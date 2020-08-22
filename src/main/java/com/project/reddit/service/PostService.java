package com.project.reddit.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.dto.PostRequest;
import com.project.reddit.dto.PostResponse;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.model.Post;
import com.project.reddit.model.Subreddit;
import com.project.reddit.model.User;
import com.project.reddit.model.Vote;
import com.project.reddit.model.VoteType;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.repository.SubredditRepository;
import com.project.reddit.repository.UserRepository;
import com.project.reddit.repository.VoteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
	
	private final PostRepository postRepository;
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final UserRepository userRepository;
	private final VoteRepository voteRepository;
	
	@Transactional
	public void save(PostRequest postRequest) {
		Post post = mapDtoToPost(postRequest);
		post.setUserId(authService.getCurrentUser());
		post.setCreatedDate(Instant.now());
		postRepository.save(post);
	}
	
	
	private Post mapDtoToPost(PostRequest postRequest) {
		return Post.builder()
				.description(postRequest.getDescription())
				.postName(postRequest.getPostName())
				.url(postRequest.getUrl())
				.subreddit(subredditRepository
						.findByName(postRequest.getSubredditName())
						.orElseThrow(() -> new SpringRedditException("subreddit not found " + postRequest.getSubredditName())))
				.voteCount(0)
				.commentCount(0)
				.build();
	}
	
	private PostResponse mapPostToDto(Post post) {
		
		Optional<Vote> vote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		boolean upVote = false;
		boolean downVote = false;
		if(vote.isPresent()) {
			if(VoteType.UPVOTE.equals(vote)) upVote = true;
			else downVote = true;
		}
		
		return PostResponse.builder()
				.subredditName(post.getSubreddit().getName())
				.postId(post.getPostId())
				.description(post.getDescription())
				.postName(post.getPostName())
				.username(post.getUserId().getUsername())
				.url(post.getUrl())
				.commentCount(post.getCommentCount())
				.voteCount(post.getVoteCount())
				.upVote(upVote)
				.downVote(downVote)
				.build();
	}

	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("Post not found " + id));
		return mapPostToDto(post);
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getAllPosts() {
		return postRepository
				.findAll()
				.stream()
				.map(this::mapPostToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long id) {
		Subreddit subreddit = subredditRepository
				.findById(id)
				.orElseThrow(() -> new SpringRedditException("subreddit not  found " + id));
		
		
		return postRepository
				.findAllBySubreddit(subreddit)
				.stream()
				.map(this::mapPostToDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<PostResponse> getPostsbyUser(String name) {
		User user = userRepository
				.findByUsername(name)
				.orElseThrow(() -> new SpringRedditException("user not foiund " + name));
		
		return postRepository.findAllByUserId(user)
				.stream()
				.map(this::mapPostToDto).collect(Collectors.toList());
	}
	
}
