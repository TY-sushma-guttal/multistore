package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.entity.product.SupplierFlag;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagPojo;
import com.mrmrscart.productcategoriesservice.wrapper.product.SupplierFlagWrapper;

public interface MarketingToolFlagService {
	
	public List<DropDownPojo> getAllFlagByUserType(EUserType userType, String supplierId);
	
	public SupplierFlag addProductsToFlag(SupplierFlagPojo supplierFlagPojo);
	
	public List<SupplierFlagWrapper> getAllSupplierFlag(int pageNumber, int pageSize, SupplierFlagFilterPojo supplierFlagFilterPojo);
	
	public SupplierFlag disableFlag (String supplierFlagId, boolean isDisabled);

	public SupplierFlag updateFlag(SupplierFlagPojo supplierFlagPojo);
	
	public SupplierFlag getFlagById(String flagId, Long purchaseId,String supplierStoreId);
}
