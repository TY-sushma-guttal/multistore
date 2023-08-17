package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildProduct {

	private String childProductId;
	private BigDecimal discountPercentage;
}
