package com.mrmrscart.productcategoriesservice.service.admin;

import com.mrmrscart.productcategoriesservice.pojo.admin.AdminMarginCountPojo;
import com.mrmrscart.productcategoriesservice.pojo.admin.AdminProductCountPojo;
import com.mrmrscart.productcategoriesservice.pojo.admin.FromAndToDatePojo;

public interface AdminProductService {

	long getProductCountByCategory(AdminProductCountPojo adminProductCountPojo);

	long getProductCountBySubCategory(AdminProductCountPojo adminProductCountPojo);

	AdminMarginCountPojo getMarginCount(FromAndToDatePojo fromAndToDatePojo);

	AdminMarginCountPojo getTotalMarginCount();

	public boolean enableDisableProduct(String productVariationId,boolean status);

}
