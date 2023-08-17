package com.mrmrscart.productcategoriesservice.exception.product;

@SuppressWarnings("serial")
public class ProductVariationNotFound extends RuntimeException{
	public ProductVariationNotFound(String message) {
		super(message);
	}
}
