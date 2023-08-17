package com.mrmrscart.productcategoriesservice.repository.product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.SupplierFlag;

public interface SupplierFlagRepository extends MongoRepository<SupplierFlag, String> {

	public List<SupplierFlag> findBySupplierStoreId(String supplierStoreId);

	public SupplierFlag findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(String flagId,
			Long purchaseId, boolean b, EStatus active, String supplierStoreId);

	public SupplierFlag findBySupplierFlagIdAndSupplierStoreIdAndIsDisabledAndStatus(String supplierFlagId,
			String supplierStoreId, boolean b, EStatus active);

}
