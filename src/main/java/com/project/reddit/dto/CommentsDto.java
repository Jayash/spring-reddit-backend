package com.project.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
	
	private Long commentId;
	private String text;
	private Long postId;
	private String username;
	private String createdDate;
}
