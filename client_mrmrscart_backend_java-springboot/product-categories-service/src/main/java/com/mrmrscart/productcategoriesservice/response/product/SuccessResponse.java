package com.mrmrscart.productcategoriesservice.response.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
	private boolean error;
	private String message;
	private Object data;
}
