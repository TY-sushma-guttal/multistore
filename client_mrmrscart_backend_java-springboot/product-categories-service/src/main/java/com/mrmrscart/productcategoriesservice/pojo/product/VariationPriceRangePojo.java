package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariationPriceRangePojo {
	private String supplierId;
	private String subCategoryId;
	private BigDecimal priceStartRange;
	private BigDecimal priceEndRange;

}
