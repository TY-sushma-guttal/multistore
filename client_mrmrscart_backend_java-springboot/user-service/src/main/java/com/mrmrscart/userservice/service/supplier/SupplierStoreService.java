package com.mrmrscart.userservice.service.supplier;

import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreInfoPojo;

public interface SupplierStoreService {

	public SupplierStoreInfo updateSupplierStoreConfiguration(SupplierStoreInfoPojo storeInfoPojo);

	public SupplierStoreInfo getSupplierStoreConfiguration(String storeCode);

	public SupplierStoreInfo updateProductCount(String supplierId, int productCount);

}
