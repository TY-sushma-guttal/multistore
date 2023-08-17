package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySetFilterResponsePojo {
	private List<MainCategoryWrapper> mainCategory;
	private List<CategorySetWrapper> categorySet;
}
