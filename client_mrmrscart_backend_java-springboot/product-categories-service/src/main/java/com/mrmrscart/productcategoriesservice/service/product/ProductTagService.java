package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.ProductTag;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagViewPojo;

public interface ProductTagService {
	
	public ProductTag addProductTag(ProductTagPojo productTagPojo);

	public List<ProductTag> getAllProductTags(int pageNumber, int pageSize);
	
	public List<ProductTagViewPojo> getProductTag(String tagId);
	
	public boolean deleteProductTag(String tagId);
	
	public List<ProductTag> getAllApprovedTags();
	
	public String approveProductTag(String tagId,boolean status);
}
