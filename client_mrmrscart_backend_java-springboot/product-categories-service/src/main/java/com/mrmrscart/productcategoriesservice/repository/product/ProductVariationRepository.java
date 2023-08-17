package com.mrmrscart.productcategoriesservice.repository.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;

public interface ProductVariationRepository extends MongoRepository<ProductVariation, String> {
	public List<ProductVariation> findByMasterProductId(String masterProductId);

	public ProductVariation findByProductVariationIdAndSkuId(String productVariationtId, String skuId);

	public List<ProductVariation> findByMasterProductIdAndStatus(String masterProductId, EStatus status);

	public ProductVariation findByProductVariationIdAndMasterProductId(String productVariationId,
			String masterProductId);

	public ProductVariation findByProductVariationIdAndStatus(String oldVariationId, EStatus approved);

	public List<ProductVariation> findByMasterProductIdAndStatusAndIsDelete(String masterProductId, EStatus approved,
			boolean b);

	@Query("{'productTitle':{'$regex':'?0','$options':'i'}}")
	public List<ProductVariation> findByTitle(String keyword);

	public ProductVariation findByProductTitle(String productTitle);

	public List<ProductVariation> findByProductVariationIdInAndStatus(List<String> productVariationIds, EStatus status);

	public List<ProductVariation> findByMasterProductIdInAndStatusAndIsDelete(List<String> masterProductIds,
			EStatus status, boolean b);

	public ProductVariation findByMasterProductIdAndProductVariationIdAndStatusAndIsDelete(String masterProductId,
			String productVariationId, EStatus status, boolean isDelete);

	public ProductVariation findByProductVariationIdAndStatusAndIsDelete(String oldVariationId, EStatus approved,
			boolean isDelete);

	public ProductVariation findByProductVariationId(String productVariationId);

	public List<ProductVariation> findByMasterProductIdAndIsDelete(String masterProductId, boolean b);

	public ProductVariation findBySkuId(String skuId);

	public List<ProductVariation> findByMasterProductIdAndStatusAndIsDeleteAndSalePriceBetween(String masterProductId,
			EStatus approved, boolean b, BigDecimal priceStartRange, BigDecimal priceEndRange);

	@Query("{'masterProductId':?0, 'status':?1, 'isDelete':?2}")
	public List<ProductVariation> checkPriceRange(String masterProductId, EStatus approved, boolean b);
}
