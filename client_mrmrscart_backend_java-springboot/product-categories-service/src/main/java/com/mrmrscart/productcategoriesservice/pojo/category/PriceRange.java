package com.mrmrscart.productcategoriesservice.pojo.category;

import java.math.BigDecimal;

import com.mrmrscart.productcategoriesservice.entity.category.EPriceRange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceRange {
	
	private BigDecimal priceStart;
	private BigDecimal priceEnd;
	private EPriceRange priceRangeType;
	private BigDecimal adminProfitPercentage;
	private BigDecimal storeOwnerProfitPercentage;
	private BigDecimal commissionPercentage;
	private String storeType;
}
