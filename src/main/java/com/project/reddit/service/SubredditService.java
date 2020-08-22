package com.project.reddit.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.reddit.dto.SubredditDto;
import com.project.reddit.exceptions.SpringRedditException;
import com.project.reddit.mapper.SubredditMapper;
import com.project.reddit.model.Subreddit;
import com.project.reddit.repository.SubredditRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SubredditService {
	
	private final SubredditRepository subredditRepository;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = SubredditMapper.mapDtoToSubreddit((subredditDto));
		Subreddit save = subredditRepository.save(subreddit);
		subredditDto.setSubredditId((save.getSubredditId()));
		return subredditDto;
	}
	
	@Transactional(readOnly = true)
	public List<SubredditDto> getAll() {
		return subredditRepository.findAll()
				.stream().
				map(SubredditMapper::mapSubredditToDto)
				.collect(Collectors.toList());
	}
	
	@Transactional
	public SubredditDto getSubreddit(Long id) {
		
		Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No Subreddit found by id" + id));
		
		return SubredditMapper.mapSubredditToDto(subreddit);
		
	}
	
}
