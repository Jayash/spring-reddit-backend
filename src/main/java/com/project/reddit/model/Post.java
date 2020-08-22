package com.project.reddit.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "postid")
	private Long postId;
	
	@NotBlank(message = "name cannot be empty or blank")
	@Column(name = "postname")
	private String postName;
	
	@Nullable
    private String url;
	
	@Lob
	private String description;
	
	@Column(name = "votecount")
	private Integer voteCount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "userid")
	private User userId;
	
	@Column(name = "commentcount")
	private Integer commentCount;
	
	@Column(name = "createddate")
	private Instant createdDate;
	
	//while lazy is used to query this only fetches if we have used getter for this field 
	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "subredditid", referencedColumnName = "subredditid")
	private Subreddit subreddit;
	
	
}
