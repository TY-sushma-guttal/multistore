package com.mrmrscart.productcategoriesservice.service.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.SOMETHING_WENT_WRONG;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.entity.product.SupplierFlag;
import com.mrmrscart.productcategoriesservice.exception.product.MarketingToolSubscriptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductFlagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierFlagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ToolSubscriptionExpiredException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.pojo.MarketingToolPurchaseHistoryPojo;
import com.mrmrscart.productcategoriesservice.feign.response.MarketingToolResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagPojo;
import com.mrmrscart.productcategoriesservice.repository.product.ProductFlagRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.product.SupplierFlagRepository;
import com.mrmrscart.productcategoriesservice.wrapper.product.SupplierFlagWrapper;

@Service
public class MarketingToolFlagServiceImpl implements MarketingToolFlagService {

	@Autowired
	private ProductFlagRepository productFlagRepository;

	@Autowired
	private SupplierFlagRepository supplierFlagRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Autowired
	private UserServiceClient userServiceClient;

	private static final String FLAGS = "FLAGS";

	@Override
	public List<DropDownPojo> getAllFlagByUserType(EUserType userType, String supplierId) {
		try {
			List<ProductFlag> flags = productFlagRepository.findByUserTypeAndIsDeletedAndIsEnabled(userType, false,
					true);
			List<DropDownPojo> downPojos = new ArrayList<>();
			ResponseEntity<MarketingToolResponse> subscription = userServiceClient.getSubscription(supplierId,
					userType.name(), FLAGS);
			MarketingToolResponse body = subscription.getBody();
			Long purchaseId = null;
			if (body != null && body.getData() != null) {
				MarketingToolPurchaseHistoryPojo data = body.getData();
				purchaseId = data.getPurchaseId();
			}
			for (ProductFlag f : flags) {
				DropDownPojo downPojo = new DropDownPojo(f.getFlagId(), f.getFlagTitle(), purchaseId);
				downPojo.setImageUrl(f.getFlagImageUrl());
				downPojos.add(downPojo);
			}
			return downPojos;
		} catch (Exception e) {
			throw new MarketingToolSubscriptionNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public SupplierFlag addProductsToFlag(SupplierFlagPojo supplierFlagPojo) {
		try {
			SupplierFlag flag = getFlagById(supplierFlagPojo.getFlagId(), supplierFlagPojo.getPurchaseId(),
					supplierFlagPojo.getSupplierStoreId());
			if (flag != null) {
				return updateProductsToFlag(supplierFlagPojo, flag);
			} else {
				return saveProductsToFlag(supplierFlagPojo);
			}
		} catch (ToolSubscriptionExpiredException | MarketingToolSubscriptionNotFoundException
				| ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierFlagNotFoundException("Something Went Wrong");
		}
	}

	private SupplierFlag updateProductsToFlag(SupplierFlagPojo supplierFlagPojo, SupplierFlag flag) {
		List<ProductVariation> productVariations = new ArrayList<>();
		supplierFlagPojo.getVariationList().forEach(variation -> {
			ProductVariation product = productVariationRepository
					.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
			if (product == null) {
				throw new ProductVariationNotFound("Product Is Not Approved");
			} else {
				if (!flag.getVariationList().contains(variation)) {
					flag.getVariationList().add(variation);
					product.setFlagged(true);
					productVariations.add(product);
				}
			}
		});
		SupplierFlag save = supplierFlagRepository.save(flag);
		productVariationRepository.saveAll(productVariations);
		return save;
	}

	private SupplierFlag saveProductsToFlag(SupplierFlagPojo supplierFlagPojo) {
		ResponseEntity<MarketingToolResponse> subscription = userServiceClient
				.getSubscription(supplierFlagPojo.getSupplierId(), supplierFlagPojo.getUserType().name(), FLAGS);
		MarketingToolResponse body = subscription.getBody();
		if (body != null && body.getData() != null) {
			MarketingToolPurchaseHistoryPojo data = body.getData();
			if (supplierFlagPojo.getEndDate().isAfter(data.getExpirationDate())) {
				throw new ToolSubscriptionExpiredException("End Date Should Be Less Than The Subscription Expiry Date");
			}
			supplierFlagPojo.getVariationList().forEach(variation -> {
				ProductVariation product = productVariationRepository
						.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
				if (product == null) {
					throw new ProductVariationNotFound("Product Is Not Approved");
				}
			});
			SupplierFlag supplierFlag = new SupplierFlag();
			BeanUtils.copyProperties(supplierFlagPojo, supplierFlag, "supplierFlagId");
			supplierFlag.setStatus(EStatus.ACTIVE);
			supplierFlag.setPurchaseId(data.getPurchaseId());
			SupplierFlag save = supplierFlagRepository.save(supplierFlag);
			List<ProductVariation> productVariations = new ArrayList<>();
			save.getVariationList().forEach(variation -> {
				ProductVariation productVariation = productVariationRepository
						.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
				if (productVariation != null) {
					productVariation.setFlagged(true);
					productVariations.add(productVariation);
				}
			});
			productVariationRepository.saveAll(productVariations);
			return save;
		} else {
			throw new MarketingToolSubscriptionNotFoundException("Marketing Tool Subscription Not Found");
		}

	}

	@Override
	public List<SupplierFlagWrapper> getAllSupplierFlag(int pageNumber, int pageSize,
			SupplierFlagFilterPojo supplierFlagFilterPojo) {
		List<SupplierFlagWrapper> supplierFlagWrappers = new ArrayList<>();
		if (supplierFlagFilterPojo.getSupplierStoreId() != null) {
			supplierFlagRepository.findBySupplierStoreId(supplierFlagFilterPojo.getSupplierStoreId()).forEach(flag -> {
				SupplierFlagWrapper wrapper = new SupplierFlagWrapper();
				BeanUtils.copyProperties(flag, wrapper);
				List<DropDownPojo> pojos = new ArrayList<>();
				flag.getVariationList().forEach(variation -> {
					ProductVariation product = productVariationRepository
							.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
					if (product != null) {
						DropDownPojo pojo = new DropDownPojo(product.getProductVariationId(), product.getProductTitle(),
								product.getVariationMedia().get(0));
						BigDecimal multiply = BigDecimal.valueOf(flag.getDiscount()).multiply(product.getSalePrice());
						BigDecimal divide = multiply.divideToIntegralValue(BigDecimal.valueOf(100)).setScale(2);
						pojo.setPrice(product.getSalePrice().subtract(divide));
						pojos.add(pojo);
					}
					wrapper.setProducts(pojos);
				});
				supplierFlagWrappers.add(wrapper);
			});
		}
		if (!supplierFlagWrappers.isEmpty()) {
			List<String> collect = new ArrayList<>();
			List<ProductFlag> flags = productFlagRepository.findByUserTypeAndIsDeletedAndIsEnabled(EUserType.SUPPLIER,
					false, true);
			for (SupplierFlagWrapper flag : supplierFlagWrappers) {
				collect = flags.stream().filter(f -> !flag.getFlagId().equals(f.getFlagId()))
						.map(ProductFlag::getFlagId).collect(Collectors.toList());
			}
			collect.forEach(id -> {
				SupplierFlagWrapper wrapper = new SupplierFlagWrapper();
				ProductFlag flag = productFlagRepository.findByFlagIdAndIsDeletedAndIsEnabledAndUserType(id, false,
						true, EUserType.SUPPLIER);
				if (flag != null) {
					wrapper.setFlagId(flag.getFlagId());
					wrapper.setFlagTitle(flag.getFlagTitle());
					wrapper.setImageUrl(flag.getFlagImageUrl());
				}
				supplierFlagWrappers.add(wrapper);
			});
		} else {
			List<ProductFlag> flags = productFlagRepository.findByUserTypeAndIsDeletedAndIsEnabled(EUserType.SUPPLIER,
					false, true);
			flags.forEach(flag -> {
				SupplierFlagWrapper wrapper = new SupplierFlagWrapper();
				wrapper.setFlagId(flag.getFlagId());
				wrapper.setFlagTitle(flag.getFlagTitle());
				wrapper.setImageUrl(flag.getFlagImageUrl());
				supplierFlagWrappers.add(wrapper);
			});
		}

		return getSupplierFlags(supplierFlagFilterPojo.getDateFrom(), supplierFlagFilterPojo.getDateTo(), pageNumber,
				pageSize, supplierFlagWrappers);
	}

	private List<SupplierFlagWrapper> getSupplierFlags(LocalDateTime dateFrom, LocalDateTime dateTo, int pageNumber,
			int pageSize, List<SupplierFlagWrapper> find) {
		List<SupplierFlagWrapper> list = new ArrayList<>();
		if (dateFrom != null && dateTo != null) {
			list = find.stream().filter(f -> dateFrom.isBefore(f.getCreatedAt()) && dateTo.isAfter(f.getCreatedAt()))
					.collect(Collectors.toList());
			if (list.isEmpty())
				return list;
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

	@Override
	public SupplierFlag disableFlag(String supplierFlagId, boolean isDisabled) {
		try {
			Optional<SupplierFlag> optional = supplierFlagRepository.findById(supplierFlagId);
			if (optional.isPresent()) {
				if (isDisabled) {
					optional.get().setDisabled(isDisabled);
					optional.get().setStatus(EStatus.INACTIVE);
					return supplierFlagRepository.save(optional.get());
				} else {
					optional.get().setDisabled(isDisabled);
					optional.get().setStatus(EStatus.ACTIVE);
					return supplierFlagRepository.save(optional.get());
				}
			} else {
				throw new ProductFlagNotFoundException("Invalid Flag Id");
			}
		} catch (ProductFlagNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierFlag updateFlag(SupplierFlagPojo supplierFlagPojo) {

		if (supplierFlagPojo.getSupplierFlagId() != null) {
			SupplierFlag flag = supplierFlagRepository.findBySupplierFlagIdAndSupplierStoreIdAndIsDisabledAndStatus(
					supplierFlagPojo.getSupplierFlagId(), supplierFlagPojo.getSupplierStoreId(), false, EStatus.ACTIVE);
			if (flag != null) {
				ResponseEntity<MarketingToolResponse> subscription = userServiceClient.getSubscription(
						supplierFlagPojo.getSupplierId(), supplierFlagPojo.getUserType().name(), FLAGS);
				MarketingToolResponse body = subscription.getBody();
				if (body != null && body.getData() != null) {
					MarketingToolPurchaseHistoryPojo data = body.getData();
					if (supplierFlagPojo.getEndDate().isAfter(data.getExpirationDate())) {
						throw new ToolSubscriptionExpiredException(
								"End Date Should Be Less Than The Subscription Expiry Date");
					}
				}
				flag.setStartDate(supplierFlagPojo.getStartDate());
				flag.setEndDate(supplierFlagPojo.getEndDate());
				return supplierFlagRepository.save(flag);
			} else {
				throw new SupplierFlagNotFoundException("No Supplier Flag Found");
			}
		} else {
			throw new SupplierFlagNotFoundException("No Supplier Flag Id Found");
		}

	}

	@Override
	public SupplierFlag getFlagById(String flagId, Long purchaseId, String supplierStoreId) {
		return supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(flagId,
				purchaseId, false, EStatus.ACTIVE, supplierStoreId);
	}

}
