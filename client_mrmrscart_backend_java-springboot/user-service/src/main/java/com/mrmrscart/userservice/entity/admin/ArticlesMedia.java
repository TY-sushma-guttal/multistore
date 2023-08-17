package com.mrmrscart.userservice.entity.admin;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_articles_media")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesMedia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger mediaId;
	@Column(length = 255)
	private String mediaUrl;
	@Column(length = 45)
	private String createdBy;
	@Column(length = 45)
	private String lastUpdatedBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
}
