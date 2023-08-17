package com.mrmrscart.productcategoriesservice.wrapper.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCountWrapper {

	private long activeCount;
	private long outOfStockCount;
	private long initiatedCount;
	private long rejectedCount;
	private long disabledCount;
}
