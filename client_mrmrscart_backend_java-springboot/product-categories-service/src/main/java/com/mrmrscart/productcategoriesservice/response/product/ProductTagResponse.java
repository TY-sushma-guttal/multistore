package com.mrmrscart.productcategoriesservice.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductTagResponse {
	
	private boolean error;
	private String message;
	private Object data;

}
