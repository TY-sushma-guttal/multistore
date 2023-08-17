package com.mrmrscart.userservice.pojo.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewPojo {

	private double sellerRatings;
	private String customerReview;
	private String reviewedById;
	private String skuId;
	private String supplierId;
}
