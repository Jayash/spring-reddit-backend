package com.project.reddit.model;

import java.util.Arrays;

import com.project.reddit.exceptions.VoteNotFoundException;

public enum VoteType {
	UPVOTE(1), DOWNVOTE(-1);
	
	public int direction;
	
	VoteType(int direction){}
	
	public static VoteType lookUp(Integer directions) {
		return Arrays.stream(VoteType.values()).filter(v -> v.getDirection().equals(directions)).
		findAny().orElseThrow(() -> new VoteNotFoundException("Vote not Found"));
	}
	
	public Integer getDirection() {
		return direction;
	}
}
