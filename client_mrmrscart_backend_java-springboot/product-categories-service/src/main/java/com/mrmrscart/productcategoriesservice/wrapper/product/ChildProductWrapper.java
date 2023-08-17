package com.mrmrscart.productcategoriesservice.wrapper.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildProductWrapper {
	private String productVariationId;
	private String productTitle;
	private String imageUrl;
	private BigDecimal salePrice;
}
