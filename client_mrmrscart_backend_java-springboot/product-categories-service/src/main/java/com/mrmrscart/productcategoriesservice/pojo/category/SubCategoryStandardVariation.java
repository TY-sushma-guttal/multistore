package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryStandardVariation {
	private String variationId;
	private boolean isDisable;
	private List<SubCategoryVariationOption> optionList;
	private boolean isDelete;
	private ECategory variationType;
}
