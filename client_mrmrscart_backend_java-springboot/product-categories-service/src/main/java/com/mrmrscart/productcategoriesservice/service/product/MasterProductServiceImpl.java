package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_NOT_FOUND;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_GET_FAIL_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID_OR_NAME;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.INVALID_FILE_PATH;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.audit.BaseConfigController;
import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.entity.collection.ColorCollection;
import com.mrmrscart.productcategoriesservice.entity.product.EFilterStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EMasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.MergedProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.media.InvalidFilePathException;
import com.mrmrscart.productcategoriesservice.exception.media.MediaException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyMasterProductListException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyProductVariationListException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidCommissionMode;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidKeywordException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidSkuException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidTrademarkLetterException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidWarrantyException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.MergeProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MergeProductIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductTagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.SalePriceWithOrWithoutFDRException;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierNotFoundException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.enums.ESupplierStatus;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierStoreInfoResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.CrossSellsPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductIdAndSkuIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTitlePojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductVariationIdAndStatusPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPriceRangePojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPropertyPojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.collection.ColorCollectionRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MergedProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.util.PaginatedResponse;
import com.mrmrscart.productcategoriesservice.util.SSSFileUpload;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductCountWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductVariationWrapper;

import feign.FeignException;

@Service
public class MasterProductServiceImpl extends BaseConfigController implements MasterProductService {

	@Autowired
	private MasterProductRepository masterProductRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private CategorySetRepository categorySetRepository;

	@Autowired
	private MainCategoryRepository mainCategoryRepository;

	@Autowired
	private ColorCollectionRepository colorCollectionRepository;

	@Autowired
	private StandardOptionRepository standardOptionRepository;

	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Autowired
	private OtherVariationRepository otherVariationRepository;

	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MergedProductRepository mergedProductRepository;

	@Autowired
	private UserServiceClient userServiceClient;

	@Autowired
	private SSSFileUpload sssObj;

	@Override
	@Transactional
	public MasterProduct addSimpleAndVariableProduct(MasterProductPojo data) {
		try {
			MasterProduct masterProduct = new MasterProduct();
			BeanUtils.copyProperties(data, masterProduct);
			userServiceClient.getSupplierById(data.getSupplierId(), ESupplierStatus.APPROVED);
			SubCategory getSubCategory = subCategoryRepository
					.findBySubCategoryNameAndSubCategoryId(data.getSubCategoryName(), data.getSubCategoryId());
			if (getSubCategory == null) {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID_OR_NAME);
			}
			Optional<CategorySet> getSet = categorySetRepository.findById(getSubCategory.getSetId());
			if (getSet.isPresent()) {
				Optional<MainCategory> getMainCategory = mainCategoryRepository
						.findById(getSet.get().getMainCategoryId());
				if (!getMainCategory.isPresent()
						|| !getMainCategory.get().getCommissionType().equals(data.getCommissionMode())) {
					throw new InvalidCommissionMode(INVALID_COMMISSION_MODE);
				}
			}

			/* Warranty validation code start */
			if (data.getProductPolicies().isWarrantyAvailable() && data.getProductPolicies().getWarrantyPeriod() == 0) {
				throw new InvalidWarrantyException(INVALID_WARRANTY_PERIOD);
			}
			/* Warranty validation code end */

			/* Trade Mark Letter validation code start */

			if (data.isTrademarkLetterAvailable()
					&& (data.isGenericProduct() || data.getTrademarkLetterIdList().isEmpty())) {
				throw new InvalidTrademarkLetterException(INVALID_TRADEMARK_LETTER);
			}
			/* Trade mark letter validation code end */

			MasterProduct savedMasterProduct = masterProductRepository.save(masterProduct);

			data.getProductVariations().forEach(e -> {

				ProductVariation productVariation = new ProductVariation();
				BeanUtils.copyProperties(e, productVariation);

				if (e.isSellWithMrMrsCart() && (e.getMrmrscartSalePriceWithFDR().doubleValue() <= 0
						|| e.getMrmrscartSalePriceWithOutFDR().doubleValue() <= 0)) {
					throw new SalePriceWithOrWithoutFDRException(INVALID_SALE_PRICE_WITH_OR_WITHOUT_FDR);
				}

				/* sell price and MRP calculation starts */
				if (data.getCommissionMode().equals(ECategory.ZERO_COMMISSION)) {
					double supplierStorePercentage = getSubCategory.getSupplierStorePercentage();
					BigDecimal supStoragePercentage = BigDecimal.valueOf(supplierStorePercentage);
					BigDecimal salePrice = e.getSalePrice();
					BigDecimal supplierStorageActualAmount = salePrice
							.multiply(supStoragePercentage.divide(new BigDecimal(100)));
					BigDecimal salePriceWithSupplierCommission = salePrice.add(supplierStorageActualAmount);
					BigDecimal gstAmountOfSupplierStorage = supplierStorageActualAmount
							.multiply(BigDecimal.valueOf(0.18));
					productVariation.setSalePrice(salePriceWithSupplierCommission.add(gstAmountOfSupplierStorage));
				}
				if (data.getCommissionMode().equals(ECategory.FIXED_COMMISSION)) {
					productVariation.setSalePrice(e.getSalePrice());
				}
				/* sell price and MRP calculation ends */

				List<VariationPropertyPojo> variationPropertyPojoList = new ArrayList<>();
				e.getVariationProperty().forEach(variationPropertyPojoList::add);
				productVariation.setVariationProperty(variationPropertyPojoList);
				productVariation.setMasterProductId(savedMasterProduct.getMasterProductId());
				productVariation.setProductCode("P0" + productVariationRepository.findAll().size());
				ProductVariation productVariation2 = productVariationRepository.save(productVariation);
				productVariation.setSkuId(getSkuid(data, productVariation2));
				productVariation.setStatus(EStatus.INITIATED);
				productVariationRepository.save(productVariation);
			});
			return masterProduct;
		} catch (SalePriceWithOrWithoutFDRException | SupplierNotFoundException | SubCategoryIdNotFoundException
				| InvalidCommissionMode | InvalidWarrantyException | InvalidTrademarkLetterException | FeignException
				| MasterProductException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	private String getSkuid(MasterProductPojo masterProductPojo, ProductVariation productVariation) {
		Optional<SubCategory> findById = subCategoryRepository.findById(masterProductPojo.getSubCategoryId());
		if (findById.isPresent()) {
			Optional<CategorySet> findById2 = categorySetRepository.findById(findById.get().getSetId());
			if (findById2.isPresent()) {
				Optional<MainCategory> findById3 = mainCategoryRepository.findById(findById2.get().getMainCategoryId());
				if (findById3.isPresent()) {
					return getSkuidHelper(findById3.get(), findById.get(), masterProductPojo, productVariation);

				} else {
					throw new MasterProductException(INVALID_MAIN_CATEGORY_ID);
				}
			} else {
				throw new MasterProductException(INVALID_SET_ID);
			}
		} else {
			throw new MasterProductException(INVALID_SUB_CATEGORY_ID);
		}

	}

	private String getSkuidHelper(MainCategory mainCategory, SubCategory findById, MasterProductPojo masterProductPojo,
			ProductVariation productVariation) {
		String path = "-";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mainCategory.getMainCategoryCode());
		stringBuilder.append(path);
		stringBuilder.append(masterProductPojo.getSupplierId());
		stringBuilder.append(productVariation.getProductCode());
		stringBuilder.append(path);
		stringBuilder.append(findById.getSubCategoryName().substring(0, 3));
		List<VariationPropertyPojo> variationProperty = productVariation.getVariationProperty();
		for (VariationPropertyPojo variationPropertyPojo : variationProperty) {
			stringBuilder.append(path);
			if (variationPropertyPojo.getVariationType().equals(ECategory.STANDARD_VARIATION.name())) {
				stringBuilder.append(getSkuidSubHelper(variationPropertyPojo));
			} else if (variationPropertyPojo.getVariationType().equals(ECategory.OTHER_VARIATION.name())) {
				stringBuilder.append(getSkuidSubHelper1(variationPropertyPojo));
			}
		}
		return stringBuilder.toString().toUpperCase();
	}

	private String getSkuidSubHelper(VariationPropertyPojo variationPropertyPojo) {
		StringBuilder stringBuilder = new StringBuilder();
		Optional<StandardVariation> findById2 = standardVariationRepository
				.findById(variationPropertyPojo.getVariationId());
		if (findById2.isPresent()) {
			Optional<StandardOption> findById3 = standardOptionRepository.findById(variationPropertyPojo.getOptionId());
			if (findById3.isPresent()) {
				if (findById2.get().getVariationName().equalsIgnoreCase("color")) {
					ColorCollection findByColorNameIgnoreCase = colorCollectionRepository
							.findByColorNameIgnoreCase(findById3.get().getOptionName());
					if (findByColorNameIgnoreCase != null) {
						stringBuilder.append(findByColorNameIgnoreCase.getColorCode());
					} else {
						stringBuilder.append(findById3.get().getOptionName().substring(0, 3));
					}
				} else {

					stringBuilder.append(getStandardOptionName(findById3.get()));
				}
			} else {
				throw new MasterProductException(INVALID_STANDARD_OPTION_ID);
			}
		} else {
			throw new MasterProductException(INVALID_STANDARD_VARIATION_ID);
		}
		return stringBuilder.toString();
	}

	private String getStandardOptionName(StandardOption findById3) {
		if (findById3.getOptionName().length() >= 3) {
			return findById3.getOptionName().substring(0, 3);
		} else {
			return findById3.getOptionName();
		}
	}

	private String getSkuidSubHelper1(VariationPropertyPojo variationPropertyPojo) {
		StringBuilder stringBuilder = new StringBuilder();
		Optional<OtherVariation> findById2 = otherVariationRepository.findById(variationPropertyPojo.getVariationId());
		if (findById2.isPresent()) {
			Optional<OtherVariationOption> findById3 = otherVariationOptionRepository
					.findById(variationPropertyPojo.getOptionId());
			if (findById3.isPresent()) {
				if (findById2.get().getVariationName().equalsIgnoreCase("color")) {
					ColorCollection findByColorNameIgnoreCase = colorCollectionRepository
							.findByColorNameIgnoreCase(findById3.get().getOptionName());
					if (findByColorNameIgnoreCase != null) {
						stringBuilder.append(findByColorNameIgnoreCase.getColorCode());
					} else {
						stringBuilder.append(findById3.get().getOptionName().substring(0, 3));
					}
				} else {
					stringBuilder.append(getOtherVariationName(findById3.get()));
				}
			} else {
				throw new MasterProductException(INVALID_STANDARD_OPTION_ID);
			}
		} else {
			throw new MasterProductException(INVALID_STANDARD_VARIATION_ID);
		}
		return stringBuilder.toString();
	}

	private String getOtherVariationName(OtherVariationOption findById3) {
		if (findById3.getOptionName().length() >= 3) {
			return findById3.getOptionName().substring(0, 3);
		} else {
			return findById3.getOptionName();
		}
	}

	private List<VariationPropertyPojo> searchingAndAddingOptions(
			List<VariationPropertyPojo> variationPropertyPojoList) {
		List<VariationPropertyPojo> pojoList = new ArrayList<>();
		variationPropertyPojoList.forEach(f -> {

			if (f.getVariationType().equals(STANDARD_VARIATION)) {
				pojoList.add(getStandardVariation(f));
			}
			if (f.getVariationType().equals(OTHER_VARIATION)) {
				pojoList.add(getOtherVariation(f));
			}
		});
		return pojoList;
	}

	private VariationPropertyPojo getStandardVariation(VariationPropertyPojo f) {
		StandardOption standardOption = standardOptionRepository
				.findByStandardOptionIdAndStandardVariationId(f.getOptionId(), f.getVariationId());
		if (standardOption != null) {

			Optional<StandardVariation> standardVariation = standardVariationRepository.findById(f.getVariationId());

			if (!standardVariation.isPresent()) {
				throw new StandardVariationIdNotFoundException(INVALID_STANDARD_VARIATION_ID);
			}
			VariationPropertyPojo variationPropertyPojo = new VariationPropertyPojo();
			variationPropertyPojo.setOptionId(standardOption.getStandardOptionId());
			variationPropertyPojo.setVariationId(standardVariation.get().getStandardVariationId());
			variationPropertyPojo.setOptionName(standardOption.getOptionName());
			variationPropertyPojo.setVariationType(f.getVariationType());
			variationPropertyPojo.setVariationName(standardVariation.get().getVariationName());
			return variationPropertyPojo;
		} else {
			throw new StandardVariationOptionNotFoundException(INVALID_STANDARD_VARIATION_OPTION);
		}
	}

	private VariationPropertyPojo getOtherVariation(VariationPropertyPojo f) {
		OtherVariationOption otherOption = otherVariationOptionRepository
				.findByOtherVariationOptionIdAndOtherVariationId(f.getOptionId(), f.getVariationId());
		if (otherOption != null) {
			Optional<OtherVariation> otherVariation = otherVariationRepository.findById(f.getVariationId());

			if (!otherVariation.isPresent()) {
				throw new StandardVariationIdNotFoundException(INVALID_OTHER_VARIATION);
			}
			VariationPropertyPojo variationPropertyPojo = new VariationPropertyPojo();
			variationPropertyPojo.setOptionId(otherOption.getOtherVariationOptionId());
			variationPropertyPojo.setVariationId(otherVariation.get().getOtherVariationId());
			variationPropertyPojo.setOptionName(otherOption.getOptionName());
			variationPropertyPojo.setVariationType(f.getVariationType());
			variationPropertyPojo.setVariationName(otherVariation.get().getVariationName());
			return variationPropertyPojo;
		} else {
			throw new StandardVariationOptionNotFoundException(INVALID_OTHER_VARIATION_OPTION);
		}
	}

	private MasterProductPojo addProductAndVariation(MasterProduct masterProduct) {

		MasterProductPojo masterProductPojo = new MasterProductPojo();
		List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();

		productVariationRepository.findByMasterProductId(masterProduct.getMasterProductId()).forEach(e -> {
			ProductVariationPojo pojo = new ProductVariationPojo();

			BeanUtils.copyProperties(e, pojo);
			pojo.setVariationProperty(searchingAndAddingOptions(e.getVariationProperty()));
			productVariationPojoList.add(pojo);
		});
		BeanUtils.copyProperties(masterProduct, masterProductPojo);
		masterProductPojo.setProductVariations(productVariationPojoList);

		return masterProductPojo;

	}

	private MasterProductPojo searchingAndAddingStatusBasedVariation(MasterProduct masterProduct, EStatus status) {
		try {
			MasterProductPojo masterProductPojo = new MasterProductPojo();
			List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();

			List<ProductVariation> productVariationList = productVariationRepository
					.findByMasterProductIdAndStatusAndIsDelete(masterProduct.getMasterProductId(), status, false);
			if (!productVariationList.isEmpty()) {
				for (ProductVariation productVariation : productVariationList) {
					ProductVariationPojo pojo = new ProductVariationPojo();
					BeanUtils.copyProperties(productVariation, pojo);
					pojo.setVariationProperty(searchingAndAddingOptions(productVariation.getVariationProperty()));
					productVariationPojoList.add(pojo);
				}
				BeanUtils.copyProperties(masterProduct, masterProductPojo);
				masterProductPojo.setProductVariations(productVariationPojoList);
			}
			return masterProductPojo;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public MasterProductPojo getSimpleOrVariableProduct(String id) {
		try {
			Optional<MasterProduct> getMasterProduct = masterProductRepository.findById(id);
			if (getMasterProduct.isPresent()) {
				return addProductAndVariation(getMasterProduct.get());
			} else {
				throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
			}
		} catch (MasterProductNotFound | StandardVariationIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public MasterProductPojo approveOrRejectMasterProduct(ProductVariationIdAndStatusPojo data, String userId) {
		try {

			ProductVariation productVariation = productVariationRepository
					.findByProductVariationId(data.getProductVariationId());
			if (productVariation != null) {
				productVariation.setStatus(data.getStatus());
				productVariation.setApprovedBy(userId);
				productVariation.setApprovedAt(LocalDateTime.now());
				productVariationRepository.save(productVariation);
			} else {
				throw new StandardVariationIdNotFoundException(INVALID_PRODUCT_VARIATION_ID);
			}

			int productCount = 0;
			String masterProductId = productVariation.getMasterProductId();

			Optional<MasterProduct> masterProduct = masterProductRepository.findById(masterProductId);

			if (!masterProduct.isPresent()) {
				throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
			}

			MasterProductPojo addProductAndVariation = addProductAndVariation(masterProduct.get());

			List<MasterProduct> list = masterProductRepository.findBySupplierId(masterProduct.get().getSupplierId());

			for (MasterProduct e : list) {
				MasterProductPojo searchingAndAddingStatusBasedVariation = searchingAndAddingStatusBasedVariation(e,
						EStatus.APPROVED);
				if (searchingAndAddingStatusBasedVariation.getProductVariations() != null) {
					productCount += (searchingAndAddingStatusBasedVariation.getProductVariations().size());
				}
			}

			ResponseEntity<SupplierStoreInfoResponse> updateProductCount = userServiceClient
					.updateProductCount(masterProduct.get().getSupplierId(), productCount, getUserId());

			SupplierStoreInfoResponse body = updateProductCount.getBody();

			if (body != null) {
				if (body.getData() != null) {
					return addProductAndVariation;
				} else {
					throw new SupplierNotFoundException("Failed To Update Product Count");
				}
			} else {
				throw new SupplierNotFoundException("Failed To Update Product Count");
			}
		} catch (MasterProductNotFound | StandardVariationIdNotFoundException | SupplierNotFoundException
				| FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MasterProductPojo> getAllProductBySupplierId(String supplierId) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository.findBySupplierId(supplierId);
			if (!masterProductList.isEmpty()) {
				List<MasterProductPojo> masterProductPojoList = new ArrayList<>();
				for (MasterProduct masterProduct : masterProductList) {
					masterProductPojoList.add(addProductAndVariation(masterProduct));
				}
				return masterProductPojoList;
			} else {
				throw new EmptyMasterProductListException(EMPTY_MASTER_PRODUCT_LIST);
			}
		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MasterProductPojo> getProductBySupIdAndStatus(String supplierId, EStatus status, int pageNumber,
			int pageSize) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository.findBySupplierId(supplierId);
			if (!masterProductList.isEmpty()) {
				List<MasterProductPojo> masterProductPojoList = new ArrayList<>();
				masterProductList.forEach(e -> {
					MasterProductPojo pojo = searchingAndAddingStatusBasedVariation(e, status);
					if (!pojo.getProductVariations().isEmpty()) {
						masterProductPojoList.add(pojo);
					}
				});

				if (pageNumber == 0) {

					return masterProductPojoList.stream().limit(pageSize).collect(Collectors.toList());

				} else {
					int skipCount = (pageNumber) * pageSize;

					return masterProductPojoList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
				}

			} else {
				throw new EmptyMasterProductListException(EMPTY_MASTER_PRODUCT_LIST);
			}
		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MasterProductPojo> getAllMasterProduct(int pageNumber, int pageSize) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository
					.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
			if (!masterProductList.isEmpty()) {
				List<MasterProductPojo> masterProductPojoList = new ArrayList<>();
				masterProductList.forEach(e -> masterProductPojoList.add(addProductAndVariation(e)));
				return masterProductPojoList;
			} else {
				throw new EmptyMasterProductListException(EMPTY_MASTER_PRODUCT_LIST);
			}
		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ProductVariation> setOutOfStock(List<ProductIdAndSkuIdPojo> data) {
		try {
			List<ProductVariation> productVariationList = new ArrayList<>();
			data.forEach(e -> {

				ProductVariation productVariation = productVariationRepository
						.findByProductVariationIdAndSkuId(e.getProductVariationId(), e.getSkuId());
				if (productVariation != null) {
					productVariation.setStatus(EStatus.OUTOFSTOCK);
					productVariationList.add(productVariationRepository.save(productVariation));
				} else {
					throw new InvalidSkuException(INVALID_SKUID);
				}
			});
			return productVariationList;
		} catch (InvalidSkuException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean deleteSimpleOrVariableProduct(String variationId) {
		try {
			Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
			if (productVariation.isPresent()) {
				productVariation.get().setDelete(true);
				productVariationRepository.save(productVariation.get());
				return true;
			} else {
				throw new ProductTagNotFoundException(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (ProductTagNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public MasterProductPojo getMasterProductByIdAndStatus(String id, EStatus status) {
		try {

			Optional<MasterProduct> masterProduct = masterProductRepository.findById(id);
			if (masterProduct.isPresent()) {
				return searchingAndAddingStatusBasedVariation(masterProduct.get(), status);
			} else {
				throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
			}
		} catch (EmptyProductVariationListException | MasterProductNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public MasterProduct duplicateProducts(MasterProductPojo masterProductPojo, String oldSupplierId,
			String oldVariationId) {
		try {
			ProductVariation productVariation = productVariationRepository
					.findByProductVariationIdAndStatus(oldVariationId, EStatus.APPROVED);
			if (productVariation != null) {
				return addSimpleAndVariableProduct(masterProductPojo);
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (ProductVariationNotFound e) {
			throw e;

		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	public List<MasterProduct> bulkUpload(List<MasterProductPojo> masterProductPojos) {
		if (masterProductPojos.isEmpty()) {
			throw new MasterProductException(EMPTY_MASTER_PRODUCT_LIST);
		}
		List<MasterProduct> masterProducts = new ArrayList<>();
		masterProductPojos.forEach(e -> masterProducts.add(addSimpleAndVariableProduct(e)));
		return masterProducts;
	}

	@Override
	public List<MasterProductPojo> getAllProductsByStatus(EStatus status, int pageNumber, int pageSize) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository.findAll();

			List<MasterProductPojo> arrayList = new ArrayList<>();

			masterProductList.forEach(e -> {
				MasterProductPojo pojo = searchingAndAddingStatusBasedVariation(e, status);
				if (!pojo.getProductVariations().isEmpty()) {
					arrayList.add(pojo);
				}
			});

			if (pageNumber == 0) {

				return arrayList.stream().limit(pageSize).collect(Collectors.toList());

			} else {
				int skipCount = (pageNumber) * pageSize;

				return arrayList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
			}

		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);

		}
	}

	@Override
	public List<MasterProductPojo> getAllProductsByStatusAndFilter(EStatus status, int pageNumber, int pageSize,
			EFilterStatus filterStatus, String keyword) {

		List<MasterProductPojo> result = new ArrayList<>();

		if ((filterStatus == EFilterStatus.LISTING_PRICE || filterStatus == EFilterStatus.PRODUCT_TITLE
				|| filterStatus == EFilterStatus.SKU || filterStatus == EFilterStatus.MRP) && keyword == null) {
			throw new InvalidKeywordException("Search keyword is mandatory!");
		}

		try {

			if (filterStatus == EFilterStatus.ALL && keyword == null) {

				return getAllProductsByStatus(status, pageNumber, pageSize);

			} else if (filterStatus == EFilterStatus.ALL) {
				List<MasterProduct> findAll = getAllMasterProducts();
				findAll.forEach(e -> {

					Query query = new Query();
					Criteria criteria = new Criteria();

					criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId())
							.andOperator(Criteria.where(STATUS).is(status), Criteria.where("isDelete").is(false))
							.orOperator(Criteria.where("productVariationId").regex(keyword, "i"),
									Criteria.where("skuId").regex(keyword, "i"),
									Criteria.where("productTitle").regex(keyword, "i"),
									Criteria.where("salePrice").regex(keyword, "i"),
									Criteria.where("mrp").regex(keyword, "i")));

					List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();
					MasterProductPojo masterProductPojo = new MasterProductPojo();

					Query addCriteria = query.addCriteria(criteria);

					result.addAll(returnFilterList(addCriteria, e, productVariationPojoList, masterProductPojo));
				});
				return getPaginatedResponse(pageNumber, pageSize, result);

			} else if (filterStatus == EFilterStatus.PRODUCT_TITLE) {
				List<MasterProduct> findAll = getAllMasterProducts();
				findAll.forEach(e -> {
					List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					Query query = new Query();
					Criteria criteria = new Criteria();

					criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()).andOperator(
							Criteria.where(STATUS).is(status), Criteria.where("isDelete").is(false),
							Criteria.where("productTitle").regex(keyword, "i")));

					Query addCriteria = query.addCriteria(criteria);
					result.addAll(returnFilterList(addCriteria, e, productVariationPojoList, masterProductPojo));

				});
				return getPaginatedResponse(pageNumber, pageSize, result);

			} else if (filterStatus == EFilterStatus.SKU) {
				List<MasterProduct> allMasterProducts = getAllMasterProducts();
				allMasterProducts.forEach(e -> {
					List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					Query query = new Query();
					Criteria criteria = new Criteria();

					criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()).andOperator(
							Criteria.where(STATUS).is(status), Criteria.where("isDelete").is(false),
							Criteria.where("skuId").regex(keyword, "i")));

					Query addCriteria = query.addCriteria(criteria);
					result.addAll(returnFilterList(addCriteria, e, productVariationPojoList, masterProductPojo));
				});
				return getPaginatedResponse(pageNumber, pageSize, result);

			} else if (filterStatus == EFilterStatus.LISTING_PRICE) {
				List<MasterProduct> allMasterProducts = getAllMasterProducts();
				allMasterProducts.forEach(e -> {
					List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					Query query = new Query();
					Criteria criteria = new Criteria();

					criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()).andOperator(
							Criteria.where(STATUS).is(status), Criteria.where("isDelete").is(false),
							Criteria.where("salePrice").regex(keyword, "i")));

					Query addCriteria = query.addCriteria(criteria);
					result.addAll(returnFilterList(addCriteria, e, productVariationPojoList, masterProductPojo));
				});
				return getPaginatedResponse(pageNumber, pageSize, result);

			} else if (filterStatus == EFilterStatus.MRP) {
				List<MasterProduct> allMasterProducts = getAllMasterProducts();
				allMasterProducts.forEach(e -> {
					List<ProductVariationPojo> productVariationPojoList = new ArrayList<>();
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					Query query = new Query();
					Criteria criteria = new Criteria();

					criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()).andOperator(
							Criteria.where(STATUS).is(status), Criteria.where("isDelete").is(false),
							Criteria.where("mrp").regex(keyword, "i")));

					Query addCriteria = query.addCriteria(criteria);
					result.addAll(returnFilterList(addCriteria, e, productVariationPojoList, masterProductPojo));
				});
				return getPaginatedResponse(pageNumber, pageSize, result);

			} else {
				return getPaginatedResponse(pageNumber, pageSize, result);
			}
		} catch (InvalidKeywordException | StandardVariationIdNotFoundException
				| StandardVariationOptionNotFoundException e) {
			throw e;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}

	}

	private List<MasterProductPojo> returnFilterList(Query criteria, MasterProduct e,
			List<ProductVariationPojo> productVariationPojoList, MasterProductPojo masterProductPojo) {

		List<MasterProductPojo> result = new ArrayList<>();
		List<ProductVariation> variationList = mongoTemplate.find(criteria, ProductVariation.class);

		if (!variationList.isEmpty()) {
			variationList.forEach(f -> {
				ProductVariationPojo pojo = new ProductVariationPojo();
				BeanUtils.copyProperties(f, pojo);
				pojo.setVariationProperty(searchingAndAddingOptions(pojo.getVariationProperty()));
				productVariationPojoList.add(pojo);
			});
			BeanUtils.copyProperties(e, masterProductPojo);
			masterProductPojo.setProductVariations(productVariationPojoList);
			result.add(masterProductPojo);
		}
		return result;
	}

	private List<MasterProduct> getAllMasterProducts() {
		return masterProductRepository.findAll();
	}

	private List<MasterProductPojo> getPaginatedResponse(int pageNumber, int pageSize, List<MasterProductPojo> result) {
		if (pageNumber == 0) {
			return result.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return result.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	public ProductPojo mergeProduct(ProductPojo data) {
		try {
			Optional<ProductVariation> productVariation = productVariationRepository
					.findById(data.getVariationData().getProductVariationId());

			if (productVariation.isPresent()) {
				Optional<MasterProduct> masterProduct = masterProductRepository
						.findById(productVariation.get().getMasterProductId());
				ProductPojo productPojo = null;
				if (masterProduct.isPresent()) {
					productPojo = new ProductPojo();
					BeanUtils.copyProperties(masterProduct.get(), productPojo);
				} else {
					throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
				}

				/* Merge product entity code starts */
				String mergeProductId = productVariation.get().getMargeProductId();
				if (mergeProductId != null) {
					Optional<MergedProduct> getMergeProduct = mergedProductRepository.findById(mergeProductId);
					if (getMergeProduct.isPresent()) {
						getMergeProduct.get().getProductIdList().add(productVariation.get().getProductVariationId());
						MergedProduct savedObj = mergedProductRepository.save(getMergeProduct.get());
						productVariation.get().setMargeProductId(savedObj.getMergedProductId());
					} else {
						throw new MergeProductIdNotFoundException(INVALID_MERGE_PRODUCT_ID);
					}
				} else {
					MergedProduct newMergeProduct = new MergedProduct();
					List<String> mergeProductIdList = new ArrayList<>();
					mergeProductIdList.add(productVariation.get().getProductVariationId());
					newMergeProduct.setProductIdList(mergeProductIdList);
					MergedProduct savedObj = mergedProductRepository.save(newMergeProduct);
					productVariation.get().setMargeProductId(savedObj.getMergedProductId());
				}
				/* Merge product entity code ends */

				ProductVariation savedObj = productVariationRepository.save(productVariation.get());
				ProductVariationPojo variationPojo = new ProductVariationPojo();
				BeanUtils.copyProperties(savedObj, variationPojo);
				productPojo.setVariationData(variationPojo);
				return productPojo;
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (MasterProductNotFound | MergeProductIdNotFoundException | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ProductPojo> getMergeProducts(String id) {
		try {
			Optional<MergedProduct> getMergeProduct = mergedProductRepository.findById(id);
			if (getMergeProduct.isPresent()) {
				MergedProduct product = getMergeProduct.get();
				List<ProductPojo> productPojoList = new ArrayList<>();
				for (String variationId : product.getProductIdList()) {
					Optional<ProductVariation> variation = productVariationRepository.findById(variationId);
					if (variation.isPresent()) {
						ProductVariationPojo variationPojo = new ProductVariationPojo();
						BeanUtils.copyProperties(variation.get(), variationPojo);
						Optional<MasterProduct> masterProduct = masterProductRepository
								.findById(variation.get().getMasterProductId());
						if (masterProduct.isPresent()) {
							ProductPojo productPojo = new ProductPojo();
							BeanUtils.copyProperties(masterProduct.get(), productPojo);
							productPojo.setVariationData(variationPojo);
							productPojoList.add(productPojo);
						} else {
							throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
						}
					} else {
						throw new ProductVariationNotFound(INVALID_STANDARD_VARIATION_ID);
					}
				}
				return productPojoList;
			} else {
				throw new MergeProductIdNotFoundException(INVALID_MERGE_PRODUCT_ID);
			}
		} catch (MergeProductIdNotFoundException | MasterProductNotFound | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public ProductPojo getProductByProductTitle(ProductTitlePojo data) {
		try {
			ProductVariation getProductVariation = productVariationRepository
					.findByProductTitle(data.getProductTitle());
			if (getProductVariation != null) {
				if (getProductVariation.getMargeProductId() != null) {
					ProductVariationPojo variationPojo = new ProductVariationPojo();
					BeanUtils.copyProperties(getProductVariation, variationPojo);
					Optional<MasterProduct> masterProduct = masterProductRepository
							.findById(getProductVariation.getMasterProductId());
					if (masterProduct.isPresent()) {
						ProductPojo productPojo = new ProductPojo();
						BeanUtils.copyProperties(masterProduct, productPojo);
						productPojo.setVariationData(variationPojo);
						return productPojo;
					} else {
						throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
					}
				} else {
					throw new MergeProductException(MERGE_PRODUCT_IS_NOT_PRESENT);
				}
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_TITLE);
			}
		} catch (MasterProductNotFound | MergeProductException | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MasterProductPojo> getAllProductsByStatusAndFilterAndSupplierId(EStatus status, int pageNumber,
			int pageSize, EMasterProduct filterStatus, String keyword, String supplierId) {
		List<MasterProductPojo> masterProductPojos = new ArrayList<>();
		if (filterStatus == EMasterProduct.ALL) {
			List<MasterProduct> find = new ArrayList<>();
			find.addAll(getMasterProduct(keyword, supplierId));
			if (find.isEmpty()) {
				find.addAll(masterProductRepository.findBySupplierIdAndIsDelete(supplierId, false));
			}
			find.forEach(e -> {

				List<ProductVariation> find2 = mongoTemplate.find(new Query().addCriteria(new Criteria()
						.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()),
								Criteria.where(STATUS).is(status), Criteria.where(IS_DELETE).is(false))
						.orOperator(Criteria.where(PRODUCT_TITLE).regex(keyword, I),
								Criteria.where(SKUID).regex(keyword, I), Criteria.where(MRP).regex(keyword, I),
								Criteria.where(SALE_PRICE).regex(keyword, I))),
						ProductVariation.class);
				masterProductPojos.addAll(getMasterProductPojoList(find2, e, status));

			});
		} else if (filterStatus == EMasterProduct.PRODUCT_TYPE) {
			List<MasterProduct> masterProduct = getMasterProduct(keyword, supplierId);
			masterProduct.forEach(e -> {
				MasterProductPojo masterProductPojo = getMasterProductPojo(e, status);
				if (masterProductPojo != null) {
					masterProductPojos.add(masterProductPojo);
				}
			});
		} else {

			List<MasterProduct> findBySupplierId = masterProductRepository.findBySupplierIdAndIsDelete(supplierId,
					false);
			findBySupplierId.forEach(e -> {
				Criteria criteria = getCriteria(filterStatus, keyword, e, status);
				List<ProductVariation> find = mongoTemplate.find(new Query().addCriteria(criteria),
						ProductVariation.class);
				if (!find.isEmpty()) {
					List<ProductVariationPojo> variationPojos = new ArrayList<>();
					find.forEach(f -> {
						ProductVariationPojo variationPojo = new ProductVariationPojo();
						BeanUtils.copyProperties(f, variationPojo);
						variationPojo
								.setVariationProperty(searchingAndAddingOptions(variationPojo.getVariationProperty()));
						variationPojos.add(variationPojo);
					});
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					BeanUtils.copyProperties(e, masterProductPojo);
					masterProductPojo.setProductVariations(variationPojos);
					masterProductPojos.add(masterProductPojo);
				}
			});

		}

		return getMasterProductPojoPage(pageNumber, pageSize, masterProductPojos);
	}

	private List<MasterProduct> getMasterProduct(String keyword, String supplierId) {
		return mongoTemplate.find(new Query().addCriteria(new Criteria()
				.andOperator(Criteria.where(SUPPLIER_ID).is(supplierId), Criteria.where(IS_DELETE).is(false))
				.orOperator(Criteria.where(PRODUCT_TYPE).regex(keyword, I))), MasterProduct.class);
	}

	private Criteria getCriteria(EMasterProduct filterStatus, String keyword, MasterProduct e, EStatus status) {
		Criteria criteria = new Criteria();
		criteria.andOperator(Criteria.where(MASTER_PRODUCT_ID).is(e.getMasterProductId()),
				Criteria.where(STATUS).is(status), Criteria.where(IS_DELETE).is(false));
		if (filterStatus == EMasterProduct.MRP) {
			criteria.orOperator(Criteria.where(MRP).regex(keyword, I));
		} else if (filterStatus == EMasterProduct.PRODUCT_NAME) {
			criteria.orOperator(Criteria.where(PRODUCT_TITLE).regex(keyword, I));
		} else if (filterStatus == EMasterProduct.SALE_PRICE) {
			criteria.orOperator(Criteria.where(SALE_PRICE).regex(keyword, I));
		} else if (filterStatus == EMasterProduct.SKUID) {
			criteria.orOperator(Criteria.where(SKUID).regex(keyword, I));
		}
		return criteria;
	}

	private MasterProductPojo getMasterProductPojo(MasterProduct e, EStatus status) {
		List<ProductVariation> findByMasterProductIdAndStatus = productVariationRepository
				.findByMasterProductIdAndStatusAndIsDelete(e.getMasterProductId(), status, false);
		if (!findByMasterProductIdAndStatus.isEmpty()) {
			List<ProductVariationPojo> variationPojos = new ArrayList<>();
			findByMasterProductIdAndStatus.forEach(f -> {
				ProductVariationPojo variationPojo = new ProductVariationPojo();
				BeanUtils.copyProperties(f, variationPojo);
				variationPojo.setVariationProperty(searchingAndAddingOptions(variationPojo.getVariationProperty()));
				variationPojos.add(variationPojo);
			});
			MasterProductPojo masterProductPojo = new MasterProductPojo();
			BeanUtils.copyProperties(e, masterProductPojo);
			masterProductPojo.setProductVariations(variationPojos);
			return masterProductPojo;
		}
		return null;
	}

	private List<MasterProductPojo> getMasterProductPojoPage(int pageNumber, int pageSize,
			List<MasterProductPojo> masterProductPojos) {
		if (pageNumber == 0) {
			return masterProductPojos.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return masterProductPojos.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	private List<MasterProductPojo> getMasterProductPojoList(List<ProductVariation> find2, MasterProduct e,
			EStatus status) {
		List<MasterProductPojo> masterProductPojos = new ArrayList<>();
		if (!find2.isEmpty()) {
			List<ProductVariationPojo> variationPojos = new ArrayList<>();
			find2.forEach(f -> {
				ProductVariationPojo variationPojo = new ProductVariationPojo();
				BeanUtils.copyProperties(f, variationPojo);
				variationPojo.setVariationProperty(searchingAndAddingOptions(variationPojo.getVariationProperty()));
				variationPojos.add(variationPojo);
			});
			MasterProductPojo masterProductPojo = new MasterProductPojo();
			BeanUtils.copyProperties(e, masterProductPojo);
			masterProductPojo.setProductVariations(variationPojos);
			masterProductPojos.add(masterProductPojo);
		} else {
			MasterProductPojo masterProductPojo = getMasterProductPojo(e, status);
			if (masterProductPojo != null) {
				masterProductPojos.add(masterProductPojo);
			}
		}
		return masterProductPojos;
	}

	@SuppressWarnings("unchecked")
	private List<MasterProductPojo> findVariations(List<MasterProduct> masterProductList, EStatus status,
			int pageNumber, int pageSize) {
		List<MasterProductPojo> masterProductPojos = new ArrayList<>();
		try {
			if (masterProductList.isEmpty()) {
				return masterProductPojos;
			} else {
				masterProductList.forEach(e -> {
					List<ProductVariation> list = productVariationRepository
							.findByMasterProductIdAndStatusAndIsDelete(e.getMasterProductId(), status, false);
					masterProductPojos.addAll(getMasterProductPojoList(list, e, status));
				});
				return (List<MasterProductPojo>) PaginatedResponse.getPaginatedResponse(masterProductPojos, pageNumber,
						pageSize);
			}
		} catch (Exception e) {
			throw new ProductVariationNotFound(INVALID_MASTER_PRODUCT_ID);
		}
	}

	@Override
	public List<MasterProductPojo> getAllProductsByColumnName(String supplierId, EStatus status,
			EMasterProduct columnName, String keyword, int pageNumber, int pageSize) {

		try {
			switch (columnName) {

			case ALL:
				List<MasterProduct> masterProductList = masterProductRepository
						.findBySupplierIdAndIsDeleteAndProductType(supplierId, false, VARIABLE_PRODUCT);
				return findVariations(masterProductList, status, pageNumber, pageSize);
			case BRAND:
				List<MasterProduct> masterProductList2 = masterProductRepository
						.findBySupplierIdAndIsDeleteAndProductTypeAndBrandIgnoreCaseContaining(supplierId, false,
								VARIABLE_PRODUCT, keyword);
				return findVariations(masterProductList2, status, pageNumber, pageSize);

			case SUB_CATEGORY_NAME:
				List<MasterProduct> masterProductList3 = masterProductRepository
						.findBySupplierIdAndIsDeleteAndProductTypeAndSubCategoryNameIgnoreCaseContaining(supplierId,
								false, VARIABLE_PRODUCT, keyword);
				return findVariations(masterProductList3, status, pageNumber, pageSize);

			default:
				throw new MasterProductNotFound("Provide A Valid Filter Type");
			}
		} catch (MasterProductNotFound | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public ProductVariation getProductVariationById(String id) {
		try {

			Optional<ProductVariation> findById = productVariationRepository.findById(id);
			if (findById.isPresent()) {
				return findById.get();
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ProductPojo> getProductList(List<MasterProductIdAndVariationIdPojo> data) {
		try {
			List<ProductPojo> productPojoList = new ArrayList<>();
			data.forEach(e -> {
				Optional<MasterProduct> masterProduct = masterProductRepository.findById(e.getMasterProductId());
				Optional<ProductVariation> productVariation = productVariationRepository.findById(e.getVariationId());
				ProductVariationPojo variationPojo = new ProductVariationPojo();
				BeanUtils.copyProperties(productVariation.get(), variationPojo);
				ProductPojo productPojo = new ProductPojo();
				BeanUtils.copyProperties(masterProduct.get(), productPojo);
				productPojo.setVariationData(variationPojo);
				productPojoList.add(productPojo);
			});
			return productPojoList;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	private String filePathDeleteForUpdateProduct(String path) {
		try {
			String[] pathArray = path.split(sssObj.bucketName + ".s3.ap-south-1.amazonaws.com/");
			if (pathArray[1].isBlank() || pathArray[1].isEmpty()) {
				throw new InvalidFilePathException(INVALID_FILE_PATH);
			} else {
				return path.replace("https://" + sssObj.bucketName + ".s3.ap-south-1.amazonaws.com/", "");
			}
		} catch (InvalidFilePathException e) {
			throw e;
		} catch (Exception e) {
			throw new MediaException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public MasterProduct updateMasterProduct(MasterProductPojo data) {
		try {
			MasterProduct getMasterProduct = masterProductRepository.findByMasterProductIdAndIsDeleteAndCommissionMode(
					data.getMasterProductId(), false, data.getCommissionMode());
			if (getMasterProduct != null) {
				/* Variation update code starts */
				for (ProductVariationPojo variation : data.getProductVariations()) {
					if (variation.getProductVariationId() != null) {
						ProductVariation getProductVariation = productVariationRepository
								.findByMasterProductIdAndProductVariationIdAndStatusAndIsDelete(
										data.getMasterProductId(), variation.getProductVariationId(), EStatus.APPROVED,
										false);
						if (getProductVariation != null) {
							getProductVariation.getVariationMedia().forEach(e -> {
								sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e));
							});
							BeanUtils.copyProperties(variation, getProductVariation);
							getProductVariation.setStatus(EStatus.UPDATED);
							getProductVariation.setVariationMedia(variation.getVariationMedia());
							productVariationRepository.save(getProductVariation);
						} else {
							throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION);
						}
					} else {
						throw new ProductVariationNotFound(UNACCEPTED_PRODUCT_VARIATION);
					}
				}
				/* Variation update code ends */

				/* delete old images code start */
				getMasterProduct.getLongDescriptionFileUrls()
						.forEach(e -> sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e)));
				getMasterProduct.getShortDescriptionFileUrls()
						.forEach(e -> sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e)));
				getMasterProduct.getProductPolicies().getShippingPolicyMediaUrls()
						.forEach(e -> sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e)));
				getMasterProduct.getProductPolicies().getRefundPolicyMediaUrls()
						.forEach(e -> sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e)));
				getMasterProduct.getProductPolicies().getCancellationPolicyMediaUrls()
						.forEach(e -> sssObj.deleteS3Folder(sssObj.bucketName, filePathDeleteForUpdateProduct(e)));
				/* delete new image code end */
				BeanUtils.copyProperties(data, getMasterProduct);
				return masterProductRepository.save(getMasterProduct);
			} else {
				throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
			}
		} catch (ProductVariationNotFound | MasterProductNotFound e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MasterProductPojo> getProductsBySubCategoryIdAndStatus(String id, EStatus status) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository.findBySubCategoryIdAndIsDelete(id, false);
			if (!masterProductList.isEmpty()) {
				List<MasterProductPojo> pojoList = new ArrayList<>();
				for (MasterProduct masterProduct : masterProductList) {
					pojoList.add(searchingAndAddingStatusBasedVariation(masterProduct, status));
				}
				return pojoList;
			} else {
				throw new EmptyMasterProductListException(EMPTY_MASTER_PRODUCT_LIST);
			}
		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public MasterProductPojo getAllVariationByVariationIdAndStatus(String id, EStatus status) {
		try {
			ProductVariation productVariation = productVariationRepository
					.findByProductVariationIdAndStatusAndIsDelete(id, status, false);
			if (productVariation != null) {
				Optional<MasterProduct> masterProduct = masterProductRepository
						.findById(productVariation.getMasterProductId());
				if (masterProduct.isPresent()) {
					return searchingAndAddingStatusBasedVariation(masterProduct.get(), status);
				} else {
					throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
				}
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION);
			}
		} catch (MasterProductNotFound | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ProductVariationWrapper> getProductsBySupplierIdAndSubCategoryIdAndStatus(String supplierId,
			String subCategoryId) {
		try {

			userServiceClient.getSupplierById(supplierId, ESupplierStatus.APPROVED);
			findSubCategory(subCategoryId);

			List<MasterProduct> masterProductList = masterProductRepository
					.findBySubCategoryIdAndSupplierIdAndIsDelete(subCategoryId, supplierId, false);
			if (!masterProductList.isEmpty()) {
				List<ProductVariation> productVariationList = new ArrayList<>();
				for (MasterProduct masterProduct : masterProductList) {
					productVariationList.addAll(productVariationRepository.findByMasterProductIdAndStatusAndIsDelete(
							masterProduct.getMasterProductId(), EStatus.APPROVED, false));
				}
				if (!productVariationList.isEmpty()) {
					return productVariationList.stream()
							.map(e -> new ProductVariationWrapper(e.getProductVariationId(), e.getProductTitle(),
									e.getVariationMedia().get(0), e.getMasterProductId(), null, null))
							.collect(Collectors.toList());
				} else {
					throw new EmptyProductVariationListException(EMPTY_VARIATION_LIST);
				}
			} else {
				throw new EmptyProductVariationListException(EMPTY_VARIATION_LIST);
			}
		} catch (EmptyProductVariationListException | SubCategoryIdNotFoundException | FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public ProductCountWrapper getProductCountByStatus(String supplierId) {
		long activeCount = 0;
		long rejectedCount = 0;
		long initiatedCount = 0;
		long disabledCount = 0;
		long outOfStockCount = 0;
		try {
			List<MasterProduct> list = masterProductRepository.findBySupplierId(supplierId);
			for (MasterProduct masterProduct : list) {
				List<ProductVariation> status = productVariationRepository
						.findByMasterProductIdAndIsDelete(masterProduct.getMasterProductId(), false);
				activeCount += status.stream().filter(e -> e.getStatus() == EStatus.APPROVED).count();
				rejectedCount += status.stream().filter(e -> e.getStatus() == EStatus.REJECTED).count();
				initiatedCount += status.stream().filter(e -> e.getStatus() == EStatus.INITIATED).count();
				disabledCount += status.stream().filter(e -> e.getStatus() == EStatus.DISABLED).count();
				outOfStockCount += status.stream().filter(e -> e.getStatus() == EStatus.OUTOFSTOCK).count();

			}
			return new ProductCountWrapper(activeCount, outOfStockCount, initiatedCount, rejectedCount, disabledCount);
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<ProductVariationWrapper> getProductsBySupplierIdAndMainCategoryIdAndStatus(String supplierId,
			String mainCategoryId) {

		try {
			userServiceClient.getSupplierById(supplierId, ESupplierStatus.APPROVED);
			findEnabledMainCategory(mainCategoryId);
			List<CategorySet> set = findEnabledCategorySets(mainCategoryId);
			List<SubCategory> subCategory = subCategoryRepository.findByIsDisableAndSetIdIn(false,
					set.stream().map(CategorySet::getCategorySetId).collect(Collectors.toList()));

			if (subCategory.isEmpty()) {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID_OR_NAME);
			} else {

				List<MasterProduct> masterProducts = masterProductRepository.findBySupplierIdAndSubCategoryIdIn(
						supplierId,
						subCategory.stream().map(SubCategory::getSubCategoryId).collect(Collectors.toList()));
				List<ProductVariationWrapper> productVariationWrapperList = null;
				if (masterProducts.isEmpty()) {
					Collections.emptyList();
				} else {
					productVariationWrapperList = new ArrayList<>();
					for (MasterProduct masterProduct : masterProducts) {
						List<ProductVariation> list = productVariationRepository
								.findByMasterProductIdAndStatusAndIsDelete(masterProduct.getMasterProductId(),
										EStatus.APPROVED, false);
						if (!list.isEmpty()) {
							productVariationWrapperList.addAll(list.stream()
									.map(e -> new ProductVariationWrapper(e.getProductVariationId(),
											e.getProductTitle(), null, e.getMasterProductId(), null,
											masterProduct.getSubCategoryId()))
									.collect(Collectors.toList()));
						}
					}
				}
				return productVariationWrapperList;
			}
		} catch (FeignException | CategorySetNotFoundException | MainCategoryNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	private MainCategory findEnabledMainCategory(String mainCategory) {
		MainCategory category = mainCategoryRepository.findByMainCategoryIdAndIsDisabled(mainCategory, false);
		if (category != null) {
			return category;
		} else {
			throw new MainCategoryNotFoundException(MAIN_CATEGORY_GET_FAIL_MESSAGE);
		}
	}

	private List<CategorySet> findEnabledCategorySets(String mainCategoryId) {
		List<CategorySet> set = categorySetRepository.findByMainCategoryIdAndIsDisabled(mainCategoryId, false);
		if (!set.isEmpty()) {
			return set;
		} else
			throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
	}

	private SubCategory findSubCategory(String subCategoryId) {
		SubCategory subCategory = subCategoryRepository.findBySubCategoryIdAndIsDisable(subCategoryId, false);
		if (subCategory == null) {
			throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
		} else
			return subCategory;
	}

	@Override
	public List<ProductVariationWrapper> getProductsBySupIdAndSubIdAndPriceRange(VariationPriceRangePojo pojo) {
		try {
			userServiceClient.getSupplierById(pojo.getSupplierId(), ESupplierStatus.APPROVED);
			findSubCategory(pojo.getSubCategoryId());
			List<MasterProduct> masterProducts = masterProductRepository
					.findBySubCategoryIdAndSupplierIdAndIsDelete(pojo.getSubCategoryId(), pojo.getSupplierId(), false);

			if (masterProducts.isEmpty()) {
				return Collections.emptyList();
			} else {
				List<ProductVariation> productVariationList = new ArrayList<>();
				for (MasterProduct masterProduct : masterProducts) {
					productVariationList.addAll(productVariationRepository
							.checkPriceRange(masterProduct.getMasterProductId(), EStatus.APPROVED, false));
				}

				List<ProductVariation> collect = productVariationList.stream()
						.filter(e -> (e.getSalePrice().compareTo(pojo.getPriceStartRange()) > 0
								&& e.getSalePrice().compareTo(pojo.getPriceEndRange()) < 0))
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					return collect.stream()
							.map(e -> new ProductVariationWrapper(e.getProductVariationId(), e.getProductTitle(),
									e.getVariationMedia().get(0), e.getMasterProductId(), e.getSalePrice(), null))
							.collect(Collectors.toList());
				} else {
					return Collections.emptyList();
				}
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ProductVariationWrapper> getProductsBySubcategoryIdsAndSupplierId(CrossSellsPojo pojo) {
		try {
			userServiceClient.getSupplierById(pojo.getSupplierId(), ESupplierStatus.APPROVED);
			pojo.getSubCategoryId().forEach(this::findSubCategory);
			List<MasterProduct> masterProducts = masterProductRepository
					.findBySupplierIdAndSubCategoryIdIn(pojo.getSupplierId(), pojo.getSubCategoryId());
			if (masterProducts.isEmpty()) {
				return Collections.emptyList();
			} else {
				List<ProductVariation> productVariationList = new ArrayList<>();
				for (MasterProduct product : masterProducts) {
					productVariationList.addAll(productVariationRepository.findByMasterProductIdAndStatusAndIsDelete(
							product.getMasterProductId(), EStatus.APPROVED, false));
				}
				if (!productVariationList.isEmpty()) {
					return productVariationList.stream().map(e -> new ProductVariationWrapper(e.getProductVariationId(),
							e.getProductTitle(), null, null, null, null)).collect(Collectors.toList());
				} else {
					return Collections.emptyList();
				}
			}
		} catch (FeignException | CategorySetNotFoundException | MainCategoryNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}
}
