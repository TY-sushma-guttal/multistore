package com.mrmrscart.productcategoriesservice.pojo.category;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariationOptionPojo {
	private String subCategoryId;
	private ECategory variationType;
	private String variationId;
	private String optionId;

}
