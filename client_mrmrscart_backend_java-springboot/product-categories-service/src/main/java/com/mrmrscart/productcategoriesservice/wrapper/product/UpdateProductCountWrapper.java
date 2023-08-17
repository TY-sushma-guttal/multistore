package com.mrmrscart.productcategoriesservice.wrapper.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductCountWrapper {

	private String supplierId;
	private int productCount;
}
