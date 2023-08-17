package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.feign.pojo.ProductVariation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariationGetResponse {
	private boolean error;
	private String message;
	private ProductVariation data;
}
