package com.mrmrscart.productcategoriesservice.repository.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;

public interface MasterProductRepository extends MongoRepository<MasterProduct, String> {

	public List<MasterProduct> findByMasterProductIdAndProductStatus(String masterProductId, EStatus productStatus);

	public List<MasterProduct> findBySupplierId(String supplierId);

	public List<MasterProduct> findBySupplierIdAndIsDelete(String supplierId, boolean isDelete);

	public Page<MasterProduct> findBySupplierIdAndProductStatus(String supplierId, EStatus productStatus,
			PageRequest pageRequest);

	public long countBySubCategoryIdAndCreatedAtBetween(String subCategoryId, LocalDateTime fromDate,
			LocalDateTime toDate);

	public long countByCommissionModeAndCreatedAtBetween(String commissionMode, LocalDateTime fromDate,
			LocalDateTime toDate);

	public long countByCommissionMode(String commissionMode);

	public List<MasterProduct> findBySubCategoryIdIn(List<String> subCategoryIds);

	public List<MasterProduct> findByBrandInAndIsDeleteAndCommissionMode(List<String> brandNames, boolean b,
			ECategory commissionMode);

	public List<MasterProduct> findBySubCategoryIdAndIsDelete(String subCategoryId, boolean b);

	public List<MasterProduct> findBySubCategoryIdInAndIsDeleteAndCommissionMode(List<String> subCategoryIds, boolean b,
			ECategory commissionMode);

	public MasterProduct findByMasterProductIdAndIsDeleteAndCommissionMode(String masterProductId, boolean b,
			ECategory commissionMode);

	public List<MasterProduct> findByIsDeleteAndCommissionMode(boolean b, ECategory commissionType);

	public List<MasterProduct> findBySubCategoryIdAndSupplierIdAndIsDelete(String subCategoryId, String supplierId,
			boolean isDelete);

	public List<MasterProduct> findBySupplierIdAndSubCategoryIdIn(String supplierId, List<String> collect);

	public List<MasterProduct> findBySupplierIdAndIsDeleteAndProductType(String supplierId, boolean b, String string);

	public List<MasterProduct> findBySupplierIdAndIsDeleteAndProductTypeAndBrandIgnoreCaseContaining(String supplierId,
			boolean b, String string, String keyword);

	public List<MasterProduct> findBySupplierIdAndIsDeleteAndProductTypeAndSubCategoryNameIgnoreCaseContaining(
			String supplierId, boolean b, String string, String keyword);


}
