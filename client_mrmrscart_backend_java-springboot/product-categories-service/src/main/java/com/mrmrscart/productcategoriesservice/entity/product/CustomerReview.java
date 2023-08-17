package com.mrmrscart.productcategoriesservice.entity.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_customer_review")
public class CustomerReview {
	@Id
	private String customerReviewId;
	private BigDecimal customerRatings;
	private String headline;
	private String writtenReview;
	private String reviewerType;
	private String reviewerId;
	private String masterProductId;
	private String variationId;
	private LocalDateTime reviewedDateTime;
	private boolean isApproved;
	private List<String> reviewMediaUrl;
}
