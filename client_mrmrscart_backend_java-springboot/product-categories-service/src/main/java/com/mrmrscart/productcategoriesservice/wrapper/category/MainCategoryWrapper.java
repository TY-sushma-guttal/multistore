package com.mrmrscart.productcategoriesservice.wrapper.category;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryWrapper {

	private String mainCategoryId;
	private String mainCategoryName;
	private ECategory commissionType;

}
