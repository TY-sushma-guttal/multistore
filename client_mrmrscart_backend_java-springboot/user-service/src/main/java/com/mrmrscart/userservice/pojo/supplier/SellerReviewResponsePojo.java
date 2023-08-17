package com.mrmrscart.userservice.pojo.supplier;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewResponsePojo {

	private Long sellerReviewsId;
	private String customerName;
	private String emailId;
	private String mobileNumber;
	//
	private String productImageUrl;
	//
	private String variationId;
	//
	private String skuId;
	private double sellerRatings;
	private String customerReview;
	private String sellerResponse;
	private LocalDateTime reviewedAt;
	private LocalDateTime repliedAt;
}
