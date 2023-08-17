package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.SupplierSideNavigation;

public interface SupplierSideNavigationService {

	public List<SupplierSideNavigation> saveSideNavigationInfo(List<SupplierSideNavigation> navigationPojo);

	public List<SupplierSideNavigation> getSideNavigationInfo(String userType);

}
