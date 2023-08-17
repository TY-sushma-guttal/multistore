package com.mrmrscart.productcategoriesservice.pojo.category;

import java.math.BigDecimal;

import com.mrmrscart.productcategoriesservice.entity.category.EPriceRange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceRangePojo {
	private BigDecimal priceStart;
	private BigDecimal priceEnd;
	private EPriceRange priceRangeType;
	private BigDecimal commissionPercentage;
	private BigDecimal resellerProfitPercentage;
	private BigDecimal adminProfitPercentage;
}
