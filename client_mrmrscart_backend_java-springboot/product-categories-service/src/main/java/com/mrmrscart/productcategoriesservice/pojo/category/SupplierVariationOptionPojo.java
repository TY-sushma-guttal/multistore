package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierVariationOptionPojo {

	private String variationOptionId;
	private String mainCategoryId;
	private String setId;
	private String subCategoryId;
	private String supplierId;
	private String variationId;
	private String variationName;
	private List<String> optionName;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String lastUpdatedBy;
	private EStatus approvalStatus;
	private ECategory variationType;
}
