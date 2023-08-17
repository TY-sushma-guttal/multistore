package com.mrmrscart.productcategoriesservice.feign.response;

import com.mrmrscart.productcategoriesservice.feign.pojo.SellerReviewProductVariationPojo;

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
