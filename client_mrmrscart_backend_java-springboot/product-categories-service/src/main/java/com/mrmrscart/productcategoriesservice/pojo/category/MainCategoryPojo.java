package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryPojo {
	private String mainCategoryId;
	private String mainCategoryName;
	private String mainCategoryCode;
	private ECategory commissionType;
	private boolean isDisabled;
	private String categoryImageUrl;
	private double supplierStorePercentage;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private List<PriceRange> priceRangeList;
}
