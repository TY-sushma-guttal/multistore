package com.mrmrscart.userservice.feign.pojo;

import java.math.BigDecimal;

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
	private BigDecimal commissionPercentage;
	private BigDecimal resellerProfitPercentage;
	private BigDecimal adminProfitPercentage;
}
