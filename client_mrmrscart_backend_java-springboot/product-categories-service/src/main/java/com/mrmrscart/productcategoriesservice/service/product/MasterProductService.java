package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EFilterStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EMasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.pojo.product.CrossSellsPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductIdAndSkuIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTitlePojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductVariationIdAndStatusPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPriceRangePojo;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductCountWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductVariationWrapper;

public interface MasterProductService {
	public MasterProduct addSimpleAndVariableProduct(MasterProductPojo data);

	public MasterProductPojo getSimpleOrVariableProduct(String id);

	public MasterProductPojo approveOrRejectMasterProduct(ProductVariationIdAndStatusPojo data, String userId);

	public List<MasterProductPojo> getAllProductBySupplierId(String supplierId);

	public List<MasterProductPojo> getProductBySupIdAndStatus(String supplierId, EStatus status, int pageNumber,
			int pageSize);

	public List<MasterProductPojo> getAllMasterProduct(int pageNumber, int pageSize);

	public List<ProductVariation> setOutOfStock(List<ProductIdAndSkuIdPojo> data);

	public MasterProduct duplicateProducts(MasterProductPojo masterProductPojo, String oldSupplierId,
			String oldVariationId);

	public List<MasterProduct> bulkUpload(List<MasterProductPojo> masterProductPojos);

	public boolean deleteSimpleOrVariableProduct(String variationId);

	public MasterProductPojo getMasterProductByIdAndStatus(String id, EStatus status);

	public List<MasterProductPojo> getAllProductsByStatus(EStatus status, int pageNumber, int pageSize);

	public List<MasterProductPojo> getAllProductsByStatusAndFilter(EStatus status, int pageNumber, int pageSize,
			EFilterStatus filterStatus, String keyword);

	public ProductPojo mergeProduct(ProductPojo data);

	public List<ProductPojo> getMergeProducts(String id);

	public ProductPojo getProductByProductTitle(ProductTitlePojo data);

	public List<MasterProductPojo> getAllProductsByStatusAndFilterAndSupplierId(EStatus status, int pageNumber,
			int pageSize, EMasterProduct filterStatus, String keyword, String supplierId);

	public List<MasterProductPojo> getAllProductsByColumnName(String supplierId, EStatus status,
			EMasterProduct columnName, String keyword, int pageNumber, int pageSize);

	public ProductVariation getProductVariationById(String id);

	public List<ProductPojo> getProductList(List<MasterProductIdAndVariationIdPojo> data);

	public MasterProduct updateMasterProduct(MasterProductPojo data);

	public List<MasterProductPojo> getProductsBySubCategoryIdAndStatus(String id, EStatus status);

	public MasterProductPojo getAllVariationByVariationIdAndStatus(String id, EStatus status);

	public List<ProductVariationWrapper> getProductsBySupplierIdAndSubCategoryIdAndStatus(String supplierId,
			String subCategoryId);

	public ProductCountWrapper getProductCountByStatus(String supplierId);

	public List<ProductVariationWrapper> getProductsBySupplierIdAndMainCategoryIdAndStatus(String supplierId,
			String mainCategoryId);

	public List<ProductVariationWrapper> getProductsBySupIdAndSubIdAndPriceRange(VariationPriceRangePojo pojo);

	public List<ProductVariationWrapper> getProductsBySubcategoryIdsAndSupplierId(CrossSellsPojo pojo);
}
