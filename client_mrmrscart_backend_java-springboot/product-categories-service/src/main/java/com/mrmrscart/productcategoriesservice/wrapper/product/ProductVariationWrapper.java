package com.mrmrscart.productcategoriesservice.wrapper.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariationWrapper {
	private String productVariationId;
	private String productTitle;
	private String imageUrl;
	private String masterProductId;
	private BigDecimal salePrice;
	private String subCategoryId;
}
