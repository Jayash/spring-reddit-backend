package com.project.reddit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.reddit.dto.CommentsDto;
import com.project.reddit.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
	
	private final CommentService commentService;
	
	@PostMapping
	public ResponseEntity<Void> createCommens(@RequestBody CommentsDto commentDto) {
		
		commentService.createComment(commentDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@GetMapping("by-post/{id}")
	public ResponseEntity<List<CommentsDto>> getAllCommentForPost(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommetnForPost(id));
	}
	
	@GetMapping("/by-username/{name}")
	public ResponseEntity<List<CommentsDto>> getAllCommentByUser(@PathVariable String name) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(commentService.getAllCommentByUser(name));
	}
	
}
