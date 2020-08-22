package com.project.reddit.exceptions;

public class VoteNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VoteNotFoundException(String s) {
		super(s);
	}
}
