package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.feign.pojo.SellerReviewProductVariationPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationResponse {

	private boolean error;
	private String message;
	private SellerReviewProductVariationPojo data;
}
