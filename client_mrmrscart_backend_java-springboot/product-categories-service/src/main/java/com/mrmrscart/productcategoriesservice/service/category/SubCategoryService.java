package com.mrmrscart.productcategoriesservice.service.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationOptionPojo;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetAndMainCategoryWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.StandardOthersVariationWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryVariationWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryWrapper;

public interface SubCategoryService {
	public SubCategory addSubCategory(SubCategoryPojo data);

	public SubCategoryResponsePojo getSubCategory(String id);

	public SubCategory updateSubCategory(SubCategoryPojo data);

	public SubCategory deleteSubCategory(String id);

	public List<SubCategory> getSubcategoryBySetId(String id);

	public List<SubCategoryResponsePojo> getAllSubCategories(int pageNumber, int pageSize);

	public boolean deleteVariation(VariationOptionPojo variationOptionPojo);

	public boolean deleteOption(VariationOptionPojo variationOptionPojo);

	public SubCategoryVariationWrapper addVariations(SubCategoryVariation data);

	public List<SubCategoryWrapper> getSubCategoryBySetIdForDropdown(String setId);

	public List<SubCategoryResponsePojo> getFilteredSubCategory(SubCategoryFilterPojo filterData, int pageNumber,
			int pageSize);

	/* Drop Down Selected Data based service methods starts */
	public List<MainCategoryWrapper> getMainCategoryByCommissionMode(ECategory commissionMode);

	public List<CategorySetWrapper> getSetsByMainCategoryId(String mainCategoryId);

	public List<SubCategoryWrapper> getAllSubCategoryBySetId(String setId);

	public List<StandardOthersVariationWrapper> getAllStandardAndOtherVariationBySubId(String subCategoryId);
	/* Drop Down Selected Data based service methods ends */

	public List<SubCategoryWrapper> getSubcategoryByMainCategoryId(String mainCategoryId);
	
	public CategorySetAndMainCategoryWrapper getSetAndMainBySubId(String subCategoryId);
}
