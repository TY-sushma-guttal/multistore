package com.mrmrscart.userservice.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewProductVariationPojo {

	private String variationId;
	private String skuId;
	private String imageUrl;
}
