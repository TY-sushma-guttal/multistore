package com.mrmrscart.productcategoriesservice.wrapper.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentProductWrapper {
	private String productVariationId;
	private String imageUrl;
	private String productTitle;
	private String shortDescription;
	private String subCategoryId;
	private BigDecimal salePrice;
}
