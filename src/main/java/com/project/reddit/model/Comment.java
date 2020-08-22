package com.project.reddit.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "commenttable")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "commentid")
	private Long commentId;
	
	@NotNull(message = "comment cannot be null")
	private String text;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "userid")
	private User user;
	
	private Integer commentCount;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commentId", referencedColumnName = "commentId")
	private Comment parent;*/
	
	@Column(name = "createddate")
	private Instant createdDate;
	
	@ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "postid", referencedColumnName = "postid")
	private Post post;
	
}
