package com.mrmrscart.userservice.entity.supplier;

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
@Table(name = "mmc_seller_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviews {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sellerReviewsId;
	private double sellerRatings;
	@Column(length = 255)
	private String customerReview;
	@Column(length = 255)
	private String sellerResponse;
	@Column(length = 45)
	private String reviewedById;
	@Column(length = 45)
	private String skuId;
	private LocalDateTime reviewedAt;
	private LocalDateTime repliedAt;
}
