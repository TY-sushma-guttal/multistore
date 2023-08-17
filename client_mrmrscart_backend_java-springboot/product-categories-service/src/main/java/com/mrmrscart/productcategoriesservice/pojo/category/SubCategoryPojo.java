package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryPojo {
	private String subCategoryId;
	private String subCategoryName;
	private String setId;
	private String priceRangeId;
	private List<SubCategoryStandardVariation> standardVariationList;
	private List<OtherVariationPojo> othersVariationList;
	private boolean isDisable;
	private String mediaUrl;
	private double supplierStorePercentage;
	private List<PriceRange> priceRangeList;
	private String createdBy;
	private String lastUpdatedBy;
}
