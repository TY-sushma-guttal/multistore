package com.mrmrscart.productcategoriesservice.service.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.UpdateVariationPojo;

public interface StandardVariationService {
	public StandardVariation addStandardVariation(StandardVariationPojo data);
	public StandardVariation getStandardVariation(String id);
	public SubCategoryPojo updateVariation(UpdateVariationPojo data);
	public List<StandardVariation> getStandardVariations();
}
