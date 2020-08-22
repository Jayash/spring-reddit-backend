package com.project.reddit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.reddit.model.Post;
import com.project.reddit.model.Subreddit;
import com.project.reddit.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	public List<Post> findAllByUserId(User user);
	
	public List<Post> findAllBySubreddit(Subreddit subreddit);
}
