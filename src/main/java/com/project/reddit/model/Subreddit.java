package com.project.reddit.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "subreddit")
public class Subreddit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "subredditid")
	private Long subredditId;
	
	@NotBlank(message = "Community Name is Required")
	private String name;
	
	@NotBlank(message = "Description is Requiredd")
	private String description;
	
	@OneToMany(targetEntity = Post.class,  mappedBy = "subreddit", fetch = FetchType.LAZY)
	private List<Post> posts;
	
	@Column(name = "createddate")
	private Instant createdDate;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "userid", referencedColumnName = "userid")
	private User user;
	
}
