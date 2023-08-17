package com.mrmrscart.productcategoriesservice.feign.response;

import java.util.List;

import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsResponse {

	private boolean error;
	private String message;
	private List<MasterProductPojo> data;
}
