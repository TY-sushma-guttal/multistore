package com.mrmrscart.productcategoriesservice.service.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.SupplierVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionPojo;

public interface SupplierVariationOptionService {

	public SupplierVariationOption addSupplierVariationOption(SupplierVariationOptionPojo variationOptionPojo);

	public List<SupplierVariationOption> getAllInitiatedVariationOption(int pageNumber, int pageSize);
	
	public List<SupplierVariationOption> getAllInitiatedVariationOption(int pageNumber, int pageSize, SupplierVariationOptionFilterPojo filterPojo);

	public SupplierVariationOption approveRejectVariation(SupplierVariationOptionPojo supplierVariationOption);

}
