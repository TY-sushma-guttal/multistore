package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryResponsePojo {
	private String subCategoryId;
	private String subCategoryName;
	private String setId;
	private MainCategoryPojo mainCategoryDetails;
	private CategorySetPojo setDetails;
	private String priceRangeId;
	private List<StandardVariationResponsePojo> standardVariationList;
	private List<OtherVariationPojo> othersVariationList;
	private double supplierStorePercentage;
	private boolean isDisable;
	private String mediaUrl;
	private CategoryPriceRangePojo categoryPriceRange;
	private List<PriceRange> priceRangeList;
	
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
}
