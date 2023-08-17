package com.mrmrscart.productcategoriesservice.response.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.pojo.product.ProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductAndVariationResponse {
	private boolean error;
	private String message;
	private List<ProductPojo> data;
}
