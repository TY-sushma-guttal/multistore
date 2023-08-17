package com.mrmrscart.productcategoriesservice.pojo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryVariationOption{
	private String optionId;
	private boolean isDisable;
	private boolean isDelete;
}
