package com.mrmrscart.productcategoriesservice.wrapper.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySetAndMainCategoryWrapper {
	private String mainCategoryId;
	private String mainCategoryName;
	private String categorySetId;
	private String categorySetName;
}
