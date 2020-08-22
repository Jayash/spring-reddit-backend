package com.project.reddit.mapper;

import com.project.reddit.dto.SubredditDto;
import com.project.reddit.model.Subreddit;

public class SubredditMapper {

	public static Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {
		return Subreddit.builder()
				.description(subredditDto.getDescription())
				.name(subredditDto.getName())
				.build();
	}
	
	public static SubredditDto mapSubredditToDto(Subreddit subreddit) {
		return SubredditDto.builder()
				.name(subreddit.getName())
				.description(subreddit.getDescription())
				.subredditId(subreddit.getSubredditId())
				.numOfPosts(subreddit.getPosts().size())
				.build();
	}
	
}
