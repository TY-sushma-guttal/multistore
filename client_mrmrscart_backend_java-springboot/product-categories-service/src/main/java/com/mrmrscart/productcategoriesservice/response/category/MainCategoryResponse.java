package com.mrmrscart.productcategoriesservice.response.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryResponse {

	private boolean error;
	private String message;
	private Object data;
}
