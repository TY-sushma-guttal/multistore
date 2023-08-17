//package com.mrmrscart.userservice.entity.admin;
//
//import java.math.BigInteger;
//import java.time.LocalDateTime;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//
//import lombok.Data;
//
//@Entity(name = "mmc_admin_articles")
//@Data
//public class AdminArticles {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private BigInteger articleId;
//	@Column(length = 20)
//	private String articleTitle;
//	@Column(length = 50)
//	private String productCategory;
//	@Column(length = 20)
//	private String articleType;
//	@Column(length = 255)
//	private String externalLink;
//	@Column(length = 20)
//	private String status;
//	@Column(length = 90)
//	private String shortDescription;
//	@Column(length = 255)
//	private String longDescription;
//	@Column(length = 45)
//	private String createdBy;
//	@Column(length = 45)
//	private String lastUpdatedBy;
//	@Column(length = 50)
//	private String displayFor;
//	private LocalDateTime createdAt;
//	private LocalDateTime lastUpdatedAt;
//	@OneToMany(cascade = CascadeType.ALL)
//	private ArticlesMedia articlesMedia;
//}


package com.mrmrscart.userservice.entity.admin;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Entity(name = "mmc_admin_articles")
@Data
public class AdminArticles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger articleId;
	@Column(length = 20)
	private String articleTitle;
	@Column(length = 50)
	private String productCategory;
	@Column(length = 20)
	private String articleType;
	@Column(length = 255)
	private String externalLink;
	@Column(length = 20)
	private String status;
	@Column(length = 90)
	private String shortDescription;
	@Column(length = 255)
	private String longDescription;
	@Column(length = 45)
	private String createdBy;
	@Column(length = 45)
	private String lastUpdatedBy;
	@Column(length = 50)
	private String displayFor;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	@OneToMany(cascade = CascadeType.ALL)
	private List<ArticlesMedia> articlesMedia;
}

