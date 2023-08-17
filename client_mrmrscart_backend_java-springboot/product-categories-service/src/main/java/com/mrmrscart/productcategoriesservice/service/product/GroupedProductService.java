package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.GroupedProduct;
import com.mrmrscart.productcategoriesservice.pojo.product.ChildProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductDropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductPojo;
import com.mrmrscart.productcategoriesservice.wrapper.product.ChildProductWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ParentProductWrapper;

public interface GroupedProductService {

	public GroupedProduct addGroupedProduct(GroupedProductPojo groupedProductPojo);
	
	public GroupedProduct updateGroupedProduct(GroupedProductPojo groupedProductPojo);

	public List<ChildProductVariationPojo> getGroupedProduct(String id);
	
	public List<ParentProductWrapper> getParentProductBySupplierId(String supplierId,EStatus status);

	public List<ChildProductWrapper> getChildProductBySubCategory(GroupedProductDropDownPojo data);
}
