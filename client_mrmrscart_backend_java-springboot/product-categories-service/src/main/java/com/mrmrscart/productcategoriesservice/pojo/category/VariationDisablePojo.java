package com.mrmrscart.productcategoriesservice.pojo.category;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariationDisablePojo {
	private ECategory variationType;
	private String variationId;
	private String subcategoryId;
	private boolean status;
	private String optionId;

}
