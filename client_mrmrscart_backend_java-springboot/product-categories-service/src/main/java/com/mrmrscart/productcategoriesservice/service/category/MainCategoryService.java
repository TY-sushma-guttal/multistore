package com.mrmrscart.productcategoriesservice.service.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.CategoryPriceRange;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.PriceRange;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationDisablePojo;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategoryStatusInfo;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryFilterWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;

public interface MainCategoryService {

	public MainCategory addMainCategory(MainCategoryPojo mainCategory);

	public List<MainCategoryPojo> getAllMainCategory(int pageNumber, int pageSize, MainCategoryFilterPojo filterPojo);

	public MainCategory updateMainCategory(MainCategoryPojo mainCategory);

	public String disableCategory(CategoryStatusInfo categoryStatusInfo);

	public List<MainCategoryPojo> getAllEnabledMainCategory(int pageNumber, int pageSize);

	public MainCategoryPojo getMainCategory(String mainCategoryId);

	public CategoryPriceRange saveCategoryPriceRange(List<PriceRange> priceRanges);

	public String disableVariation(VariationDisablePojo variationDisablePojo);

	public String disableVariationOption(VariationDisablePojo variationDisablePojo);

	public List<MainCategoryWrapper> getAllMainCategoryDropdown();

	public MainCategoryFilterWrapper getFilterMainCategoryDropdown();

}
