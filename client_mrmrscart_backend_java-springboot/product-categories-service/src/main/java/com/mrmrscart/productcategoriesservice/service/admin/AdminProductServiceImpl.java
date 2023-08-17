package com.mrmrscart.productcategoriesservice.service.admin;

import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.FIXED_COMMISSION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_MAIN_CATEGORY_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_MASTER_PRODUCT_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_PRODUCT_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.ZERO_COMMISSION;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.audit.BaseConfigController;
import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.pojo.SupplierStoreInfo;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierStoreInfoResponse;
import com.mrmrscart.productcategoriesservice.pojo.admin.AdminMarginCountPojo;
import com.mrmrscart.productcategoriesservice.pojo.admin.AdminProductCountPojo;
import com.mrmrscart.productcategoriesservice.pojo.admin.FromAndToDatePojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.wrapper.product.UpdateProductCountWrapper;

import feign.FeignException;

@Service
public class AdminProductServiceImpl extends BaseConfigController implements AdminProductService {

	@Autowired
	private MainCategoryRepository mainCategoryRepository;

	@Autowired
	private CategorySetRepository categorySetRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private MasterProductRepository masterProductRepository;
	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Autowired
	private UserServiceClient userServiceClient;

	@Override
	public long getProductCountByCategory(AdminProductCountPojo adminProductCountPojo) {
		List<Long> count = new ArrayList<>();
		Optional<MainCategory> findById = mainCategoryRepository.findById(adminProductCountPojo.getId());
		if (findById.isPresent()) {
			List<CategorySet> findByMainCategoryId = categorySetRepository
					.findByMainCategoryId(findById.get().getMainCategoryId());
			findByMainCategoryId.forEach(e -> {
				List<SubCategory> findBySetId = subCategoryRepository.findBySetId(e.getCategorySetId());
				findBySetId.forEach(f -> count.add(masterProductRepository.countBySubCategoryIdAndCreatedAtBetween(
						f.getSubCategoryId(), adminProductCountPojo.getFromDate(), adminProductCountPojo.getToDate())));
			});
		} else {
			throw new MainCategoryNotFoundException(INVALID_MAIN_CATEGORY_ID);
		}
		long result = 0;
		for (Long long1 : count) {
			result += long1;
		}
		return result;
	}

	@Override
	public long getProductCountBySubCategory(AdminProductCountPojo adminProductCountPojo) {
		List<Long> count = new ArrayList<>();
		Optional<SubCategory> findById = subCategoryRepository.findById(adminProductCountPojo.getId());
		if (findById.isPresent()) {
			count.add(masterProductRepository.countBySubCategoryIdAndCreatedAtBetween(findById.get().getSubCategoryId(),
					adminProductCountPojo.getFromDate(), adminProductCountPojo.getToDate()));
		} else {
			throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
		}
		long result = 0;
		for (Long long1 : count) {
			result += long1;
		}
		return result;
	}

	@Override
	public AdminMarginCountPojo getMarginCount(FromAndToDatePojo fromAndToDatePojo) {
		long fixedCommission = masterProductRepository.countByCommissionModeAndCreatedAtBetween(FIXED_COMMISSION,
				fromAndToDatePojo.getFromDate(), fromAndToDatePojo.getToDate());
		long zeroCommission = masterProductRepository.countByCommissionModeAndCreatedAtBetween(ZERO_COMMISSION,
				fromAndToDatePojo.getFromDate(), fromAndToDatePojo.getToDate());

		return new AdminMarginCountPojo(fixedCommission, zeroCommission);
	}

	@Override
	public AdminMarginCountPojo getTotalMarginCount() {
		long fixedCommission = masterProductRepository.countByCommissionMode(FIXED_COMMISSION);
		long zeroCommission = masterProductRepository.countByCommissionMode(ZERO_COMMISSION);
		return new AdminMarginCountPojo(fixedCommission, zeroCommission);
	}

	@Override
	public boolean enableDisableProduct(String productVariationId, boolean status) {
		try {
			if (status) {
				ProductVariation productVariation = findProductByProductStatus(productVariationId, EStatus.APPROVED);
				productVariation.setStatus(EStatus.DISABLED);
				productVariationRepository.save(productVariation);
				UpdateProductCountWrapper productVariations = getProductCount(productVariation.getMasterProductId());
				updateProductCount(productVariations);
			} else {
				ProductVariation productVariation = findProductByProductStatus(productVariationId, EStatus.DISABLED);
				productVariation.setStatus(EStatus.APPROVED);
				productVariationRepository.save(productVariation);
				UpdateProductCountWrapper productVariations = getProductCount(productVariation.getMasterProductId());
				updateProductCount(productVariations);
			}
			return status;
		} catch (ProductVariationNotFound | FeignException | MasterProductNotFound e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierStoreInfo updateProductCount(UpdateProductCountWrapper productVariations) {
		try {
			ResponseEntity<SupplierStoreInfoResponse> updateProductCount = userServiceClient.updateProductCount(
					productVariations.getSupplierId(), productVariations.getProductCount(), getUserId());
			SupplierStoreInfoResponse body = updateProductCount.getBody();
			if (body != null) {
				SupplierStoreInfo data = body.getData();
				if (data != null) {
					return data;
				} else {
					throw new ProductVariationNotFound("Failed To Update Product Count");
				}
			} else {
				throw new ProductVariationNotFound("Failed To Update Product Count");
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	private UpdateProductCountWrapper getProductCount(String masterProductId) {
		Optional<MasterProduct> masterProduct = masterProductRepository.findById(masterProductId);
		if (masterProduct.isPresent()) {
			List<MasterProduct> list = masterProductRepository.findBySupplierId(masterProduct.get().getSupplierId());
			if (!list.isEmpty()) {
				List<String> collect = list.stream().map(MasterProduct::getMasterProductId)
						.collect(Collectors.toList());
				List<ProductVariation> variation = productVariationRepository
						.findByMasterProductIdInAndStatusAndIsDelete(collect, EStatus.APPROVED, false);
				return new UpdateProductCountWrapper(masterProduct.get().getSupplierId(), variation.size());
			} else {
				throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
			}
		} else {
			throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
		}
	}

	private ProductVariation findProductByProductStatus(String productVariationId, EStatus status) {
		ProductVariation productVariation = productVariationRepository
				.findByProductVariationIdAndStatusAndIsDelete(productVariationId, status, false);
		if (productVariation != null) {
			return productVariation;
		} else
			throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION);
	}

}
