package com.mrmrscart.productcategoriesservice.response.product;

import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductGetResponse {
	private boolean error;
	private String message;
	private MasterProductPojo data;
}
