package com.project.reddit.dto;

import com.project.reddit.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
	
	private VoteType voteType;
	private Long post;
}
