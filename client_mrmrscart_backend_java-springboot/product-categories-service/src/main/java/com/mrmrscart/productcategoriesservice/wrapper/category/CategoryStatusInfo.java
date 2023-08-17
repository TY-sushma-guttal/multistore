package com.mrmrscart.productcategoriesservice.wrapper.category;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatusInfo {
	private ECategory categoryType;
	private String categoryId;
	private boolean status;

}
