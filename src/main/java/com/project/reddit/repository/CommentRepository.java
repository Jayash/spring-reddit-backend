package com.project.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.reddit.model.Comment;
import com.project.reddit.model.Post;
import com.project.reddit.model.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	public List<Comment> findAllByPost(Post post);
	
	public List<Comment> findAllByUser(User user);
}
