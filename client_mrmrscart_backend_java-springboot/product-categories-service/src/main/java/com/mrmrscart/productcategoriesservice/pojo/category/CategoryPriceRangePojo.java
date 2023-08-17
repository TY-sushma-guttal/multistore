package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPriceRangePojo {
	private String priceRangeId;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private List<PriceRange> priceRanges;
}
