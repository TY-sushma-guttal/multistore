package com.mrmrscart.productcategoriesservice.response.category;

import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryResponsePojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryGetResponse {
	private boolean error;
	private String message;
	private SubCategoryResponsePojo data;
}
