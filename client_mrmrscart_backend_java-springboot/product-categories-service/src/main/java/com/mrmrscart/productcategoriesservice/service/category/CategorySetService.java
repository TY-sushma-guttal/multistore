package com.mrmrscart.productcategoriesservice.service.category;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilter;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilterResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetPojo;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetResponseWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;

public interface CategorySetService {

	public CategorySet saveCategorySet(CategorySetPojo categorySetPojo);

	public CategorySet updateCategorySet(CategorySetPojo categorySetPojo);

	public CategorySet getCategorySet(String categorySetId);

	public List<CategorySet> getAllCategorySetsByMainCategoryId(String mainCategoryId);

	public List<CategorySet> getEnabledCategorySets(String mainCategoryId);

	public List<CategorySetWrapper> getEnabledCategorySetsForDropdown(String mainCategoryId);

	public List<CategorySetResponseWrapper> getAllCategorySets(int pageNumber, int pageSize, LocalDateTime fromDate,
			LocalDateTime toDate, CategorySetFilter filterPojo);

	public CategorySetFilterResponsePojo getCategorySetFilterData();

	public List<CategorySetWrapper> getSetsByCategoryForFilter(List<String> mainCategory);
}
