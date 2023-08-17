package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.EMPTY_LIST_AND_INVALID_SUPPLIER;
import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.GROUPED_PRODUCT_ALREADY_PRESENT;
import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.INVALID_CHILD_PRODUCT_ID;
import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.INVALID_GROUPED_PRODUCT_ID;
import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.SIMILAR_PRODUCT_ERROR;
import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_OTHER_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_OTHER_VARIATION_OPTION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_STANDARD_VARIATION_ID;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_STANDARD_VARIATION_OPTION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.OTHER_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.STANDARD_VARIATION;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.entity.product.GroupedProduct;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyMasterProductListException;
import com.mrmrscart.productcategoriesservice.exception.product.GroupedProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.pojo.product.ChildProduct;
import com.mrmrscart.productcategoriesservice.pojo.product.ChildProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ChildProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductDropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPropertyPojo;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.product.GroupedProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.wrapper.product.ChildProductWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ParentProductWrapper;

@Service
public class GroupedProductServiceImpl implements GroupedProductService {

	@Autowired
	private MasterProductRepository masterProductRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Autowired
	private GroupedProductRepository groupedProductRepository;

	@Autowired
	private OtherVariationRepository otherVariationRepository;

	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Autowired
	private StandardOptionRepository standardOptionRepository;

	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Override
	public GroupedProduct addGroupedProduct(GroupedProductPojo groupedProductPojo) {
		try {
			ProductVariation findById = productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(
					groupedProductPojo.getGroupedProductId(), EStatus.APPROVED, false);
			if (findById != null) {
				GroupedProduct findByGroupedProductId = groupedProductRepository
						.findByGroupedProductIdAndIsDelete(groupedProductPojo.getGroupedProductId(), false);
				if (findByGroupedProductId == null) {
					GroupedProduct groupedProduct = new GroupedProduct();
					BeanUtils.copyProperties(groupedProductPojo, groupedProduct);
					List<ChildProductPojo> childProducts2 = groupedProductPojo.getChildProducts();
					List<ChildProductPojo> collect2 = childProducts2.stream()
							.filter(e -> e.getChildProductId().equals(groupedProductPojo.getGroupedProductId()))
							.collect(Collectors.toList());

					TreeSet<ChildProductPojo> collect = childProducts2.stream().collect(Collectors.toCollection(
							() -> new TreeSet<>(Comparator.comparing(ChildProductPojo::getChildProductId))));
					if (collect.size() != groupedProductPojo.getChildProducts().size() || !collect2.isEmpty()) {
						throw new GroupedProductException(SIMILAR_PRODUCT_ERROR);
					}
					List<ChildProduct> childProducts = new ArrayList<>();
					groupedProductPojo.getChildProducts().forEach(e -> {
						ProductVariation findById3 = productVariationRepository
								.findByProductVariationIdAndStatusAndIsDelete(e.getChildProductId(), EStatus.APPROVED,
										false);
						if (findById3 != null) {
							ChildProduct childProduct = new ChildProduct();
							BeanUtils.copyProperties(e, childProduct);
							childProducts.add(childProduct);

						} else {
							throw new GroupedProductException(INVALID_CHILD_PRODUCT_ID);
						}
					});
					groupedProduct.setChildProducts(childProducts);
					return groupedProductRepository.save(groupedProduct);
				} else {
					throw new GroupedProductException(GROUPED_PRODUCT_ALREADY_PRESENT);
				}
			} else {
				throw new GroupedProductException(INVALID_GROUPED_PRODUCT_ID);
			}
		} catch (GroupedProductException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GroupedProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public GroupedProduct updateGroupedProduct(GroupedProductPojo groupedProductPojo) {
		GroupedProduct findByGroupedProductId = groupedProductRepository
				.findByGroupedProductIdAndIsDelete(groupedProductPojo.getGroupedProductId(), false);
		if (findByGroupedProductId != null) {
			List<ChildProductPojo> childProducts2 = groupedProductPojo.getChildProducts();
			List<ChildProductPojo> collect2 = childProducts2.stream()
					.filter(e -> e.getChildProductId().equals(groupedProductPojo.getGroupedProductId()))
					.collect(Collectors.toList());

			TreeSet<ChildProductPojo> collect = childProducts2.stream().collect(Collectors
					.toCollection(() -> new TreeSet<>(Comparator.comparing(ChildProductPojo::getChildProductId))));
			if (collect.size() != groupedProductPojo.getChildProducts().size() || !collect2.isEmpty()) {
				throw new GroupedProductException(SIMILAR_PRODUCT_ERROR);
			}
			List<ChildProduct> childProducts = new ArrayList<>();
			childProducts2.forEach(e -> {
				ProductVariation findById3 = productVariationRepository
						.findByProductVariationIdAndStatusAndIsDelete(e.getChildProductId(), EStatus.APPROVED, false);
				if (findById3 != null) {
					ChildProduct childProduct = new ChildProduct();
					BeanUtils.copyProperties(e, childProduct);
					childProducts.add(childProduct);
				} else {
					throw new GroupedProductException(INVALID_CHILD_PRODUCT_ID);
				}
			});
			findByGroupedProductId.setChildProducts(childProducts);
			return groupedProductRepository.save(findByGroupedProductId);

		} else {
			throw new GroupedProductException(INVALID_GROUPED_PRODUCT_ID);
		}
	}

	@Override
	public List<ChildProductVariationPojo> getGroupedProduct(String id) {
		try {
			List<ChildProductVariationPojo> childProductVariationPojos = new ArrayList<>();
			GroupedProduct groupedProduct = groupedProductRepository.findByGroupedProductIdAndIsDelete(id, false);
			if (groupedProduct != null) {
				List<ChildProduct> childProducts = groupedProduct.getChildProducts();
				childProducts.forEach(e -> {
					ProductVariation productVariation = productVariationRepository
							.findByProductVariationIdAndStatusAndIsDelete(e.getChildProductId(), EStatus.APPROVED,
									false);
					if (productVariation != null) {
						ChildProductVariationPojo childProductVariationPojo = new ChildProductVariationPojo();
						BeanUtils.copyProperties(productVariation, childProductVariationPojo);
						childProductVariationPojo.setVariationMedia(productVariation.getVariationMedia());
						childProductVariationPojo.setVariationProperty(
								searchingAndAddingOptions(productVariation.getVariationProperty()));
						childProductVariationPojo.setChildProductPrice(e.getDiscountPercentage());
						childProductVariationPojos.add(childProductVariationPojo);
					}
				});
				return childProductVariationPojos;
			} else {
				throw new GroupedProductException(INVALID_GROUPED_PRODUCT_ID);
			}
		} catch (GroupedProductException | StandardVariationIdNotFoundException
				| StandardVariationOptionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GroupedProductException(SOMETHING_WENT_WRONG);
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

	@Override
	public List<ParentProductWrapper> getParentProductBySupplierId(String supplierId, EStatus status) {
		try {
			List<MasterProduct> masterProductList = masterProductRepository.findBySupplierIdAndIsDelete(supplierId,
					false);
			if (!masterProductList.isEmpty()) {
				List<ParentProductWrapper> parentProductList = new ArrayList<>();
				masterProductList.forEach(e -> {
					List<ProductVariation> productVariationList = productVariationRepository
							.findByMasterProductIdAndStatusAndIsDelete(e.getMasterProductId(), status, false);
					productVariationList.forEach(f -> {
						ParentProductWrapper wrapper = new ParentProductWrapper();
						wrapper.setImageUrl(f.getVariationMedia().get(0));
						wrapper.setProductTitle(f.getProductTitle());
						wrapper.setProductVariationId(f.getProductVariationId());
						wrapper.setSalePrice(f.getSalePrice());
						wrapper.setShortDescription(e.getShortDescription());
						wrapper.setSubCategoryId(e.getSubCategoryId());
						parentProductList.add(wrapper);
					});
				});
				return parentProductList;

			} else {
				throw new EmptyMasterProductListException(EMPTY_LIST_AND_INVALID_SUPPLIER);
			}
		} catch (EmptyMasterProductListException e) {
			throw e;
		} catch (Exception e) {
			throw new GroupedProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<ChildProductWrapper> getChildProductBySubCategory(GroupedProductDropDownPojo data) {
		/* Fetch all the products based on the selected parent product */
		try {
			List<MasterProduct> masterProductList = masterProductRepository
					.findBySubCategoryIdAndSupplierIdAndIsDelete(data.getSubCategoryId(), data.getSupplierId(), false);
			List<ChildProductWrapper> childProductList = new ArrayList<>();
			masterProductList.forEach(e -> {
				List<ProductVariation> productVariationList = productVariationRepository
						.findByMasterProductIdAndStatusAndIsDelete(e.getMasterProductId(), EStatus.APPROVED, false);
				productVariationList.forEach(f -> {
					ChildProductWrapper wrapper = new ChildProductWrapper();
					wrapper.setImageUrl(f.getVariationMedia().get(0));
					wrapper.setProductTitle(f.getProductTitle());
					wrapper.setProductVariationId(f.getProductVariationId());
					wrapper.setSalePrice(f.getSalePrice());
					childProductList.add(wrapper);
				});
			});
			return childProductList;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

}
