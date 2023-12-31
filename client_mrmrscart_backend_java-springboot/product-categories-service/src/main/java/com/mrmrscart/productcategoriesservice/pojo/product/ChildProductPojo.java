package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildProductPojo {

	private String childProductId;
	private BigDecimal discountPercentage;
}
