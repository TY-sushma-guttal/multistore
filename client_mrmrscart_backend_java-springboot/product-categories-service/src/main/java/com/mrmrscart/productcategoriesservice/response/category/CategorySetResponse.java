package com.mrmrscart.productcategoriesservice.response.category;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySetResponse {

	private boolean error;
	private String message;
	private Object data;
}
