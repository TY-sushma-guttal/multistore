package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;
import java.util.Set;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.feign.pojo.IncreaseStockQuantityPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrderedProductsPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrdersRequestPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.SellerReviewProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.FilteredProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFilterPojo;

public interface ProductService {

	public List<DropDownPojo> getCategories(ECategory commissionType);

	public List<DropDownPojo> getSubCategories(List<String> categoryIds, ECategory commissionType);

	public Set<DropDownPojo> getBrands(ProductFilterPojo filterPojo);

	public List<DropDownPojo> getProductTitles(ProductFilterPojo filterPojo);

	public FilteredProductPojo getProducts(int pageNumber, int pageSize, ProductFilterPojo filterPojo);

	public List<String> getProductIds(OrdersRequestPojo ordersRequestPojo);

	public ProductVariation increaseStockQuantity(IncreaseStockQuantityPojo increaseStockQuantityPojo);

	public List<MasterProductPojo> validateOrderedProducts(OrderedProductsPojo orderedProductsPojo);

	public SellerReviewProductVariationPojo getProductVariationBySkuId(String skuId);
	
	public boolean setProductStatus(String productVariationId);

}
