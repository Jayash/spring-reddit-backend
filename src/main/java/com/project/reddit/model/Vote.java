package com.project.reddit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "vote")
public class Vote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "voteid")
	private Long voteId;
	
	@Column(name = "votetype")
	private VoteType voteType;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "userid")
	private User user;
	
	@ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "postid", referencedColumnName = "postid")
	private Post post;
	
}
