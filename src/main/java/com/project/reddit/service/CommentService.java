package com.project.reddit.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.dto.CommentsDto;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.model.Comment;
import com.project.reddit.model.NotificationEmail;
import com.project.reddit.model.Post;
import com.project.reddit.model.User;
import com.project.reddit.repository.CommentRepository;
import com.project.reddit.repository.PostRepository;
import com.project.reddit.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final AuthService authService;
	private final PostRepository postRepository;
	private final MailService mailService;
	private final MailContentBuilder mailContentBuilder;
	private final UserRepository userRepository;
	
	
	@Transactional
	public void createComment(CommentsDto commentDto) {
		Comment comment = mapDtoToComment(commentDto);
		comment.setUser(authService.getCurrentUser());
		comment.setCreatedDate(Instant.now());
		commentRepository.save(comment);
		
		String message = mailContentBuilder
				.build(comment.getUser().getUsername()  + " posted comment on your post " + comment.getPost().getPostName());
		
		mailService.sendMail(new NotificationEmail(comment.getUser().getUsername(), comment.getUser().getEmail(), message));
	}
	
	private Comment mapDtoToComment(CommentsDto commentsDto) {
		return Comment.builder()
				.text(commentsDto.getText())
				.post(postRepository
						.findById(commentsDto.getPostId())
						.orElseThrow(() -> new SpringRedditException("post not found " + commentsDto.getPostId())))
				.build();
	}
	
 	private CommentsDto mapCommentToDto(Comment comment) {
 		return CommentsDto.builder()
 				.username(comment.getUser().getUsername())
 				.createdDate(comment.getCreatedDate().toString())
 				.text(comment.getText())
 				.commentId(comment.getCommentId())
 				.build();
 	}
 	
 	@Transactional(readOnly = true)
	public List<CommentsDto> getAllCommetnForPost(Long id) {
		
		Post post = postRepository
				.findById(id)
				.orElseThrow(() -> new SpringRedditException("post not found " + id));
		
		return commentRepository.findAllByPost(post)
				.stream()
				.map(this::mapCommentToDto)
				.collect(Collectors.toList());
	}
 	
 	@Transactional(readOnly = true)
	public List<CommentsDto> getAllCommentByUser(String name) {
		User user = userRepository
				.findByUsername(name)
				.orElseThrow(() -> new SpringRedditException("user not found " + name));
		
		return commentRepository
				.findAllByUser(user)
				.stream()
				.map(this::mapCommentToDto)
				.collect(Collectors.toList());
	}
 	
 	

}
