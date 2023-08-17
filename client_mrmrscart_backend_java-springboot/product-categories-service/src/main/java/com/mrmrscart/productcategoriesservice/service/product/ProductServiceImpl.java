package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_MASTER_PRODUCT_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_PRODUCT_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_PRODUCT_VARIATION_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_SKUID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.SOMETHING_WENT_WRONG;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyProductVariationListException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierNotFoundException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.enums.ESupplierStatus;
import com.mrmrscart.productcategoriesservice.feign.pojo.IncreaseStockQuantityPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrderedProductsPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrdersRequestPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.SellerReviewProductVariationPojo;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierRegistrationGetResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.FilteredProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ViewProductPojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;

@Service
public class ProductServiceImpl implements ProductService {

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
	public List<DropDownPojo> getCategories(ECategory commissionType) {
		try {
			List<MainCategory> mainCategories = mainCategoryRepository.findEnabledForDropdown(false).stream()
					.filter(s -> s.getCommissionType().equals(commissionType)).collect(Collectors.toList());

			List<DropDownPojo> downPojos = new ArrayList<>();
			mainCategories.forEach(category -> {
				DropDownPojo downPojo = new DropDownPojo(category.getMainCategoryId(), category.getMainCategoryName());
				downPojos.add(downPojo);
			});
			return downPojos;
		} catch (MainCategoryException e) {
			throw new MainCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<DropDownPojo> getSubCategories(List<String> categoryIds, ECategory commissionType) {
		try {
			List<DropDownPojo> downPojos = new ArrayList<>();
			if (!categoryIds.isEmpty()) {
				mainCategoryRepository
						.findByMainCategoryIdInAndIsDisabledAndCommissionType(categoryIds, false, commissionType)
						.forEach(category -> categorySetRepository
								.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false)
								.forEach(set -> subCategoryRepository
										.findBySetIdAndisDisable(set.getCategorySetId(), false).forEach(sub -> {
											DropDownPojo downPojo = new DropDownPojo(sub.getSubCategoryId(),
													sub.getSubCategoryName());
											downPojos.add(downPojo);
										})));
			} else {
				mainCategoryRepository.findByCommissionTypeAndIsDisabled(commissionType, false)
						.forEach(category -> categorySetRepository
								.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false)
								.forEach(set -> subCategoryRepository
										.findBySetIdAndisDisable(set.getCategorySetId(), false).forEach(sub -> {
											DropDownPojo downPojo = new DropDownPojo(sub.getSubCategoryId(),
													sub.getSubCategoryName());
											downPojos.add(downPojo);
										})));

			}
			return downPojos;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Set<DropDownPojo> getBrands(ProductFilterPojo filterPojo) {
		try {
			Set<DropDownPojo> downPojos = new LinkedHashSet<>();
			if (!filterPojo.getSubCategoryIds().isEmpty()) {
				return getBrandNames(filterPojo.getSubCategoryIds(), filterPojo);
			} else if (!filterPojo.getCategoryIds().isEmpty()) {
				List<String> subCategoryIds = new ArrayList<>();
				mainCategoryRepository
						.findByMainCategoryIdInAndIsDisabledAndCommissionType(filterPojo.getCategoryIds(), false,
								filterPojo.getCommissionType())
						.forEach(main -> categorySetRepository
								.findByMainCategoryIdAndIsDisabled(main.getMainCategoryId(), false)
								.forEach(set -> subCategoryRepository
										.findBySetIdAndisDisable(set.getCategorySetId(), false)
										.forEach(sub -> subCategoryIds.add(sub.getSubCategoryId()))));
				return getBrandNames(subCategoryIds, filterPojo);

			} else {
				masterProductRepository.findByIsDeleteAndCommissionMode(false, filterPojo.getCommissionType())
						.forEach(product -> {
							ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
									.getSupplierById(product.getSupplierId());
							SupplierRegistrationGetResponse body = supplier.getBody();
							if ((body != null && body.getData() != null)
									&& body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
								DropDownPojo downPojo = new DropDownPojo();
								downPojo.setName(product.getBrand());
								downPojos.add(downPojo);
							}
						});
				return downPojos;
			}
		} catch (Exception e) {
			throw new EmptyProductVariationListException(SOMETHING_WENT_WRONG);
		}
	}

	private Set<DropDownPojo> getBrandNames(List<String> subCategoryIds, ProductFilterPojo filterPojo) {
		Set<DropDownPojo> downPojos = new LinkedHashSet<>();
		masterProductRepository.findBySubCategoryIdInAndIsDeleteAndCommissionMode(subCategoryIds, false,
				filterPojo.getCommissionType()).forEach(product -> {
					ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
							.getSupplierById(product.getSupplierId());
					SupplierRegistrationGetResponse body = supplier.getBody();
					if ((body != null && body.getData() != null)
							&& body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
						DropDownPojo downPojo = new DropDownPojo();
						downPojo.setName(product.getBrand());
						downPojos.add(downPojo);
					}
				});
		return downPojos;
	}

	@Override
	public List<DropDownPojo> getProductTitles(ProductFilterPojo filterPojo) {
		try {
			if (!filterPojo.getBrandNames().isEmpty()) {
				List<MasterProduct> masterProducts = masterProductRepository.findByBrandInAndIsDeleteAndCommissionMode(
						filterPojo.getBrandNames(), false, filterPojo.getCommissionType());
				return getProductTitlesBasedOnSubCategory(filterPojo, masterProducts);
			} else if (!filterPojo.getSubCategoryIds().isEmpty()) {
				List<MasterProduct> masterProducts = masterProductRepository
						.findBySubCategoryIdInAndIsDeleteAndCommissionMode(filterPojo.getSubCategoryIds(), false,
								filterPojo.getCommissionType());
				return getProductTitlesBasedOnSubCategory(filterPojo, masterProducts);
			} else if (!filterPojo.getCategoryIds().isEmpty()) {
				return getProductTitlesBasedOnCategory(filterPojo);
			} else {
				return getProductTitlesBasedOnCommissionType(filterPojo);
			}
		} catch (Exception e) {
			throw new EmptyProductVariationListException(SOMETHING_WENT_WRONG);
		}
	}

	private List<DropDownPojo> getProductTitlesBasedOnCommissionType(ProductFilterPojo filterPojo) {
		List<DropDownPojo> downPojos = new ArrayList<>();
		masterProductRepository.findByIsDeleteAndCommissionMode(false, filterPojo.getCommissionType())
				.forEach(product -> {
					ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
							.getSupplierById(product.getSupplierId());
					SupplierRegistrationGetResponse body = supplier.getBody();
					if (body != null) {
						if (body.getData() != null && body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
							productVariationRepository.findByMasterProductIdAndStatusAndIsDelete(
									product.getMasterProductId(), filterPojo.getStatus(), false).forEach(variation -> {
										DropDownPojo downPojo = new DropDownPojo(variation.getProductVariationId(),
												variation.getProductTitle());
										downPojos.add(downPojo);
									});
						}
					} else {
						return;
					}
				});
		return removeDuplicate(downPojos);
	}

	private List<DropDownPojo> getProductTitlesBasedOnSubCategory(ProductFilterPojo filterPojo,
			List<MasterProduct> masterProducts) {
		List<DropDownPojo> downPojos = new ArrayList<>();
		masterProducts.forEach(product -> {
			ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
					.getSupplierById(product.getSupplierId());
			SupplierRegistrationGetResponse body = supplier.getBody();
			if (body != null) {
				if (body.getData() != null && body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
					productVariationRepository.findByMasterProductIdAndStatusAndIsDelete(product.getMasterProductId(),
							filterPojo.getStatus(), false).forEach(variation -> {
								DropDownPojo downPojo = new DropDownPojo(variation.getProductVariationId(),
										variation.getProductTitle());
								downPojos.add(downPojo);
							});
				}
			} else {
				return;
			}
		});
		return removeDuplicate(downPojos);

	}

	private List<DropDownPojo> getProductTitlesBasedOnCategory(ProductFilterPojo filterPojo) {
		List<DropDownPojo> downPojos = new ArrayList<>();
		mainCategoryRepository
				.findByMainCategoryIdInAndIsDisabledAndCommissionType(filterPojo.getCategoryIds(), false,
						filterPojo.getCommissionType())
				.forEach(category -> categorySetRepository
						.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false)
						.forEach(set -> subCategoryRepository.findBySetIdAndisDisable(set.getCategorySetId(), false)
								.forEach(sub -> masterProductRepository
										.findBySubCategoryIdAndIsDelete(sub.getSubCategoryId(), false)
										.forEach(product -> {
											ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
													.getSupplierById(product.getSupplierId());
											SupplierRegistrationGetResponse body = supplier.getBody();
											if (body != null) {
												if (body.getData() != null && body.getData().getStatus()
														.equals(ESupplierStatus.APPROVED)) {
													productVariationRepository
															.findByMasterProductIdAndStatusAndIsDelete(
																	product.getMasterProductId(),
																	filterPojo.getStatus(), false)
															.forEach(variation -> {
																DropDownPojo downPojo = new DropDownPojo(
																		variation.getProductVariationId(),
																		variation.getProductTitle());
																downPojos.add(downPojo);
															});
												}
											} else {
												return;
											}
										}))));
		return removeDuplicate(downPojos);
	}

	@Override
	public FilteredProductPojo getProducts(int pageNumber, int pageSize, ProductFilterPojo filterPojo) {
		try {
			if (!filterPojo.getProductVariationIds().isEmpty())
				return new FilteredProductPojo(setCount(filterPojo),
						getProductsFilteredOnVariationId(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber,
								pageSize, filterPojo.getProductVariationIds(), filterPojo.getStatus(),
								filterPojo.getCommissionType()));
			else if (!filterPojo.getBrandNames().isEmpty()) {
				return new FilteredProductPojo(setCount(filterPojo),
						getProducts(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber, pageSize,
								filterPojo.getBrandNames(), filterPojo.getStatus(), filterPojo.getCommissionType()));
			} else if (!filterPojo.getSubCategoryIds().isEmpty()) {
				return new FilteredProductPojo(setCount(filterPojo),
						getProductFilteredOnSubCategory(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber,
								pageSize, filterPojo.getSubCategoryIds(), filterPojo.getStatus(),
								filterPojo.getCommissionType()));
			} else if (!filterPojo.getCategoryIds().isEmpty()) {
				return new FilteredProductPojo(setCount(filterPojo),
						getProductFilteredOnCategory(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber,
								pageSize, filterPojo.getCategoryIds(), filterPojo.getStatus(),
								filterPojo.getCommissionType()));
			} else {
				return new FilteredProductPojo(setCount(filterPojo),
						getProducts(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber, pageSize,
								filterPojo.getCommissionType(), filterPojo.getStatus()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}

	}

	private int setCount(ProductFilterPojo filterPojo) {
		List<String> masterProductIds = new ArrayList<>();
		masterProductRepository.findByIsDeleteAndCommissionMode(false, filterPojo.getCommissionType())
				.forEach(master -> {
					ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
							.getSupplierById(master.getSupplierId());
					SupplierRegistrationGetResponse body = supplier.getBody();
					if (body != null) {
						if (body.getData() != null && body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
							masterProductIds.add(master.getMasterProductId());
						}
					} else {
						return;
					}
				});
		List<ProductVariation> variations = new ArrayList<>();
		if (!masterProductIds.isEmpty()) {
			variations = productVariationRepository.findByMasterProductIdInAndStatusAndIsDelete(masterProductIds,
					filterPojo.getStatus(), false);

		}
		int count = 0;
		if (!variations.isEmpty()) {
			count = variations.size();
		}
		return count;
	}

	private List<ViewProductPojo> getProductsFilteredOnVariationId(LocalDateTime dateFrom, LocalDateTime dateTo,
			int pageNumber, int pageSize, List<String> productVariationIds, EStatus status, ECategory commissionType) {
		List<ViewProductPojo> products = new ArrayList<>();
		productVariationRepository.findByProductVariationIdInAndStatus(productVariationIds, status).forEach(product -> {
			ViewProductPojo pojo = new ViewProductPojo();
			MasterProduct master = masterProductRepository.findByMasterProductIdAndIsDeleteAndCommissionMode(
					product.getMasterProductId(), false, commissionType);
			if (master != null) {
				ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
						.getSupplierById(master.getSupplierId());
				SupplierRegistrationGetResponse body = supplier.getBody();
				if (body != null) {
					if (body.getData() != null && body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
						pojo.setSupplierId(body.getData().getSupplierId());
						pojo.setSupplierName(body.getData().getBusinessName());
					}
				} else {
					return;
				}
				ViewProductPojo productViewPojo = getCategoryIdAndName(master);
				pojo.setCategoryId(productViewPojo.getCategoryId());
				pojo.setCategoryName(productViewPojo.getCategoryName());
				pojo.setSubCategoryId(master.getSubCategoryId());
				pojo.setSubCategoryName(master.getSubCategoryName());
				pojo.setBrand(master.getBrand());
			} else {
				throw new MasterProductException("No master product found");
			}
			BeanUtils.copyProperties(product, pojo);

			pojo.setVolume(getVolume(product));

			products.add(pojo);
		});

		return getProductsPage(dateFrom, dateTo, pageNumber, pageSize, removeDuplicates(products));

	}

	private List<DropDownPojo> removeDuplicate(List<DropDownPojo> list) {
		List<DropDownPojo> downPojos = new ArrayList<>();
		for (DropDownPojo downPojo : list) {
			if (!(downPojos.stream().anyMatch(a -> a.getName().equals(downPojo.getName())))) {
				downPojos.add(downPojo);
			}

		}
		return downPojos;
	}

	private List<ViewProductPojo> removeDuplicates(List<ViewProductPojo> products) {
		List<ViewProductPojo> productPojos = new ArrayList<>();
		for (ViewProductPojo viewProductPojo : products) {
			if (!(productPojos.stream()
					.anyMatch(a -> a.getProductVariationId().equals(viewProductPojo.getProductVariationId())))) {
				productPojos.add(viewProductPojo);
			}

		}
		return productPojos;
	}

	private List<ViewProductPojo> getProducts(LocalDateTime dateFrom, LocalDateTime dateTo, int pageNumber,
			int pageSize, ECategory commissionType, EStatus status) {
		List<ViewProductPojo> products = new ArrayList<>();
		mainCategoryRepository.findByCommissionTypeAndIsDisabled(commissionType, false)
				.forEach(
						category -> categorySetRepository
								.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false)
								.forEach(set -> subCategoryRepository
										.findBySetIdAndisDisable(set.getCategorySetId(), false)
										.forEach(sub -> masterProductRepository
												.findBySubCategoryIdAndIsDelete(sub.getSubCategoryId(), false)
												.forEach(product -> productVariationRepository
														.findByMasterProductIdAndStatusAndIsDelete(
																product.getMasterProductId(), status, false)
														.forEach(variation -> {
															ViewProductPojo pojo = new ViewProductPojo();
															BeanUtils.copyProperties(variation, pojo);
															pojo.setVolume(getVolume(variation));
															ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
																	.getSupplierById(product.getSupplierId());
															SupplierRegistrationGetResponse body = supplier.getBody();
															if (body != null) {
																if (body.getData() != null && body.getData().getStatus()
																		.equals(ESupplierStatus.APPROVED)) {
																	pojo.setSupplierId(body.getData().getSupplierId());
																	pojo.setSupplierName(
																			body.getData().getBusinessName());
																}
															} else {
																return;
															}
															pojo.setBrand(product.getBrand());
															pojo.setSubCategoryId(product.getSubCategoryId());
															pojo.setSubCategoryName(product.getSubCategoryName());
															pojo.setCategoryId(category.getMainCategoryId());
															pojo.setCategoryName(category.getMainCategoryName());
															products.add(pojo);

														})))));
		return getProductsPage(dateFrom, dateTo, pageNumber, pageSize, removeDuplicates(products));
	}

	private List<ViewProductPojo> getProductFilteredOnCategory(LocalDateTime dateFrom, LocalDateTime dateTo,
			int pageNumber, int pageSize, List<String> categoryIds, EStatus status, ECategory commissionType) {
		List<ViewProductPojo> products = new ArrayList<>();
		mainCategoryRepository.findByMainCategoryIdInAndIsDisabledAndCommissionType(categoryIds, false, commissionType)
				.forEach(
						category -> categorySetRepository
								.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false)
								.forEach(set -> subCategoryRepository
										.findBySetIdAndisDisable(set.getCategorySetId(), false)
										.forEach(sub -> masterProductRepository
												.findBySubCategoryIdAndIsDelete(sub.getSubCategoryId(), false)
												.forEach(product -> productVariationRepository
														.findByMasterProductIdAndStatusAndIsDelete(
																product.getMasterProductId(), status, false)
														.forEach(variation -> {
															ViewProductPojo pojo = new ViewProductPojo();
															BeanUtils.copyProperties(variation, pojo);
															pojo.setVolume(getVolume(variation));
															ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
																	.getSupplierById(product.getSupplierId());
															SupplierRegistrationGetResponse body = supplier.getBody();
															if (body != null) {
																if (body.getData() != null && body.getData().getStatus()
																		.equals(ESupplierStatus.APPROVED)) {
																	pojo.setSupplierId(body.getData().getSupplierId());
																	pojo.setSupplierName(
																			body.getData().getBusinessName());
																}
															} else {
																return;
															}
															pojo.setBrand(product.getBrand());
															pojo.setSubCategoryId(product.getSubCategoryId());
															pojo.setSubCategoryName(product.getSubCategoryName());
															pojo.setCategoryId(category.getMainCategoryId());
															pojo.setCategoryName(category.getMainCategoryName());
															products.add(pojo);
														})))));
		return getProductsPage(dateFrom, dateTo, pageNumber, pageSize, removeDuplicates(products));
	}

	private List<ViewProductPojo> getProductFilteredOnSubCategory(LocalDateTime dateFrom, LocalDateTime dateTo,
			int pageNumber, int pageSize, List<String> subCategoryIds, EStatus status, ECategory commissionType) {
		List<ViewProductPojo> products = new ArrayList<>();
		masterProductRepository.findBySubCategoryIdInAndIsDeleteAndCommissionMode(subCategoryIds, false, commissionType)
				.forEach(product -> productVariationRepository
						.findByMasterProductIdAndStatusAndIsDelete(product.getMasterProductId(), status, false)
						.forEach(variation -> {
							ViewProductPojo pojo = new ViewProductPojo();
							BeanUtils.copyProperties(variation, pojo);
							pojo.setVolume(getVolume(variation));
							ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
									.getSupplierById(product.getSupplierId());
							SupplierRegistrationGetResponse body = supplier.getBody();
							if (body != null) {
								if (body.getData() != null
										&& body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
									pojo.setSupplierId(body.getData().getSupplierId());
									pojo.setSupplierName(body.getData().getBusinessName());
								}
							} else {
								return;
							}
							ViewProductPojo productViewPojo = getCategoryIdAndName(product);
							pojo.setCategoryId(productViewPojo.getCategoryId());
							pojo.setCategoryName(productViewPojo.getCategoryName());
							pojo.setSubCategoryId(product.getSubCategoryId());
							pojo.setSubCategoryName(product.getSubCategoryName());
							pojo.setBrand(product.getBrand());
							products.add(pojo);
						}));
		return getProductsPage(dateFrom, dateTo, pageNumber, pageSize, removeDuplicates(products));
	}

	private ViewProductPojo getCategoryIdAndName(MasterProduct product) {
		ViewProductPojo productViewPojo = new ViewProductPojo();
		Optional<SubCategory> subCategory = subCategoryRepository.findById(product.getSubCategoryId());
		if (subCategory.isPresent()) {
			Optional<CategorySet> set = categorySetRepository.findById(subCategory.get().getSetId());
			if (set.isPresent()) {
				Optional<MainCategory> mainCategory = mainCategoryRepository.findById(set.get().getMainCategoryId());
				if (mainCategory.isPresent()) {
					productViewPojo.setCategoryId(mainCategory.get().getMainCategoryId());
					productViewPojo.setCategoryName(mainCategory.get().getMainCategoryName());
				} else {
					throw new MainCategoryNotFoundException("No main category found");
				}
			} else {
				throw new CategorySetNotFoundException("No Category Set found");
			}
		} else {
			throw new SubCategoryException("No Sub category found");
		}
		return productViewPojo;
	}

	private List<ViewProductPojo> getProducts(LocalDateTime dateFrom, LocalDateTime dateTo, int pageNumber,
			int pageSize, List<String> brandNames, EStatus status, ECategory commissionType) {
		List<ViewProductPojo> products = new ArrayList<>();
		masterProductRepository.findByBrandInAndIsDeleteAndCommissionMode(brandNames, false, commissionType)
				.forEach(product -> productVariationRepository
						.findByMasterProductIdAndStatusAndIsDelete(product.getMasterProductId(), status, false)
						.forEach(variation -> {
							ViewProductPojo pojo = new ViewProductPojo();
							BeanUtils.copyProperties(variation, pojo);
							pojo.setVolume(getVolume(variation));
							ResponseEntity<SupplierRegistrationGetResponse> supplier = userServiceClient
									.getSupplierById(product.getSupplierId());
							SupplierRegistrationGetResponse body = supplier.getBody();
							if (body != null) {
								if (body.getData() != null
										&& body.getData().getStatus().equals(ESupplierStatus.APPROVED)) {
									pojo.setSupplierId(body.getData().getSupplierId());
									pojo.setSupplierName(body.getData().getBusinessName());
								}
							} else {
								return;
							}
							ViewProductPojo productViewPojo = getCategoryIdAndName(product);
							pojo.setCategoryId(productViewPojo.getCategoryId());
							pojo.setCategoryName(productViewPojo.getCategoryName());
							pojo.setSubCategoryId(product.getSubCategoryId());
							pojo.setSubCategoryName(product.getSubCategoryName());
							pojo.setBrand(product.getBrand());
							products.add(pojo);
						}));
		return getProductsPage(dateFrom, dateTo, pageNumber, pageSize, removeDuplicates(products));
	}

	private List<ViewProductPojo> getProductsPage(LocalDateTime dateFrom, LocalDateTime dateTo, int pageNumber,
			int pageSize, List<ViewProductPojo> find) {
		List<ViewProductPojo> list = new ArrayList<>();
		if (dateFrom != null && dateTo != null) {
			list = find.stream().filter(f -> dateFrom.isBefore(f.getCreatedAt()) && dateTo.isAfter(f.getCreatedAt()))
					.collect(Collectors.toList());
			if (list.isEmpty()) {
				return list;
			}
		}

		if (pageNumber == 0) {
			if (!list.isEmpty())
				return list.stream().limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			if (!list.isEmpty())
				return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	private BigDecimal getVolume(ProductVariation product) {
		BigDecimal height = product.getPackageHeight() != null ? product.getPackageHeight() : BigDecimal.valueOf(0);
		BigDecimal length = product.getPackageLength() != null ? product.getPackageLength() : BigDecimal.valueOf(0);
		BigDecimal width = product.getPackageWidth() != null ? product.getPackageWidth() : BigDecimal.valueOf(0);
		BigDecimal temp = height.multiply(length);
		BigDecimal multiply = temp.multiply(width);
		return multiply.divide(BigDecimal.valueOf(5000));
	}

	@Override
	public List<String> getProductIds(OrdersRequestPojo ordersRequestPojo) {
		List<String> productIds = new ArrayList<>();
		Iterable<ProductVariation> productVariations = productVariationRepository
				.findAllById(ordersRequestPojo.getProductIds());
		productVariations.forEach(e -> {
			Optional<MasterProduct> findById = masterProductRepository.findById(e.getMasterProductId());
			if (findById.isPresent()) {
				if (findById.get().getCommissionMode().name().equals(ordersRequestPojo.getCommissionType())) {
					productIds.add(e.getProductVariationId());
				}
			}
		});
		return productIds;
	}

	@Override
	public List<MasterProductPojo> validateOrderedProducts(OrderedProductsPojo orderedProductsPojo) {
		List<MasterProductPojo> result = new ArrayList<>();

		try {
			orderedProductsPojo.getProductId().forEach(e -> {
				ProductVariation productVariation = productVariationRepository.findByProductVariationIdAndStatus(e,
						EStatus.APPROVED);
				if (productVariation != null) {
					Optional<MasterProduct> masterProduct = masterProductRepository
							.findById(productVariation.getMasterProductId());

					List<ProductVariationPojo> list = new ArrayList<>();
					if (masterProduct.isPresent()) {
						ProductVariationPojo productVariationPojo = new ProductVariationPojo();
						BeanUtils.copyProperties(productVariation, productVariationPojo);
						list.add(productVariationPojo);
					} else {
						throw new MasterProductNotFound(INVALID_MASTER_PRODUCT_ID);
					}
					MasterProductPojo masterProductPojo = new MasterProductPojo();
					BeanUtils.copyProperties(masterProduct.get(), masterProductPojo);
					masterProductPojo.setProductVariations(list);
					result.add(masterProductPojo);
				} else {
					throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION);
				}
			});
			return result;
		} catch (MasterProductNotFound | ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	public ProductVariation increaseStockQuantity(IncreaseStockQuantityPojo increaseStockQuantityPojo) {

		try {
			ProductVariation productVariation = productVariationRepository
					.findByProductVariationId(increaseStockQuantityPojo.getProductId());
			if (productVariation != null) {
				productVariation.setStockQty(productVariation.getStockQty()
						.add(new BigDecimal(increaseStockQuantityPojo.getOrderQuantity())));
				return productVariationRepository.save(productVariation);
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new ProductVariationNotFound(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SellerReviewProductVariationPojo getProductVariationBySkuId(String skuId) {
		ProductVariation productVariation = productVariationRepository.findBySkuId(skuId);
		if (productVariation != null) {
			return new SellerReviewProductVariationPojo(productVariation.getProductVariationId(), skuId,
					productVariation.getVariationMedia().get(0));
		} else {
			throw new ProductVariationNotFound(INVALID_SKUID);
		}
	}

	@Override
	public boolean setProductStatus(String productVariationId) {
		try {
			ProductVariation productVariation = productVariationRepository.findByProductVariationId(productVariationId);
			if (productVariation != null) {
				productVariation.setStatus(EStatus.IN_QUERY);
				productVariationRepository.save(productVariation);
				return true;
			} else {
				throw new ProductVariationNotFound(INVALID_PRODUCT_VARIATION_ID);
			}
		} catch (Exception e) {
			throw new ProductVariationNotFound(SOMETHING_WENT_WRONG);
		}
	}

}
