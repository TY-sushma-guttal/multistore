package com.mrmrscart.productcategoriesservice.service.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.common.product.ProductFlagConstant;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.product.ProductFlagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagingPojo;
import com.mrmrscart.productcategoriesservice.repository.product.ProductFlagRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;

@Service
public class ProductFlagServiceImpl implements ProductFlagService {

	@Autowired
	private ProductFlagRepository productFlagRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Override
	@Transactional
	public ProductFlag addProductFlag(ProductFlagPojo productFlagPojo) {
		try {
			ProductFlag productFlag = new ProductFlag();
			BeanUtils.copyProperties(productFlagPojo, productFlag, "flagId");
			productFlag.setFlagStatus(EStatus.ACTIVE);
			productFlag.setEnabled(true);
			if (!productFlagPojo.getVariationId().isEmpty()) {
				productFlagPojo.getVariationId().forEach(variation -> {
					ProductVariation product = productVariationRepository
							.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
					if (product == null) {
						throw new ProductVariationNotFound("Product Is Not Approved");
					}
				});
			}
			ProductFlag save = productFlagRepository.save(productFlag);
			if (!save.getVariationId().isEmpty()) {
				List<ProductVariation> productVariations = new ArrayList<>();
				save.getVariationId().forEach(variation -> {
					ProductVariation productVariation = productVariationRepository
							.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
					if (productVariation != null) {
						productVariation.setFlagged(true);
						productVariations.add(productVariation);
					}
				});
				productVariationRepository.saveAll(productVariations);
			}
			return save;
		} catch (ProductVariationNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new ProductFlagNotFoundException(ProductFlagConstant.SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean enableProductFlag(String flagId, boolean isEnabled) {
		try {
			ProductFlag flag = productFlagRepository.findByFlagIdAndIsDeleted(flagId, false);
			if (flag != null) {
				flag.setEnabled(isEnabled);
				productFlagRepository.save(flag);
				return true;
			} else {
				throw new ProductFlagNotFoundException(ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE);
			}
		} catch (Exception e) {
			throw new ProductFlagNotFoundException(ProductFlagConstant.SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public ProductFlag updateProductFlag(ProductFlagPojo productFlagPojo) {
		ProductFlag flag = productFlagRepository.findByFlagIdAndIsDeleted(productFlagPojo.getFlagId(), false);
		if (flag != null) {
			BeanUtils.copyProperties(productFlagPojo, flag, "flagId");
			ProductFlag save = productFlagRepository.save(flag);
			List<ProductVariation> productVariations = new ArrayList<>();
			save.getVariationId().forEach(variation -> {
				ProductVariation product = productVariationRepository
						.findByProductVariationIdAndStatusAndIsDelete(variation, EStatus.APPROVED, false);
				if (product == null) {
					throw new ProductVariationNotFound("Product Is Not Approved");
				} else {
					if (!flag.getVariationId().contains(variation)) {
						flag.getVariationId().add(variation);
						product.setFlagged(true);
						productVariations.add(product);
					}
				}
			});
			productVariationRepository.saveAll(productVariations);
			return productFlagRepository.save(flag);
		} else
			throw new ProductFlagNotFoundException(ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE);
	}

	@Override
	public boolean deleteProductFlag(String flagId) {
		Optional<ProductFlag> flag = productFlagRepository.findById(flagId);
		if (flag.isPresent()) {
			flag.get().setDeleted(true);
			productFlagRepository.save(flag.get());
			return true;
		} else {
			throw new ProductFlagNotFoundException(ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE);
		}
	}

	@Override
	public List<ProductFlag> getAllFlags() {
		return productFlagRepository.findByisDeleted(false);
	}

	@Override
	public ProductFlag getById(String flagId) {
		Optional<ProductFlag> flag = productFlagRepository.findById(flagId);
		if (flag.isPresent() && !flag.get().isDeleted()) {
			return flag.get();
		} else
			throw new ProductFlagNotFoundException("Flag not found");
	}

	@Override
	public List<ProductFlag> getAllFilteredFlags(LocalDateTime dateFrom, LocalDateTime dateTo) {
		List<ProductFlag> flags = productFlagRepository.findAllFlags(dateFrom, dateTo);
		List<ProductFlag> productFlags = new ArrayList<>();
		flags.forEach(f -> {
			if (!f.isDeleted())
				productFlags.add(f);
		});
		return productFlags;
	}

	@Override
	public String updateProductFlag(ProductFlagingPojo productFlagingPojo) {
		Optional<ProductFlag> flag = productFlagRepository.findById(productFlagingPojo.getFlagId());
		if (flag.isPresent()) {
			productFlagingPojo.getVariationId().forEach(f -> {
				flag.get().getVariationId().add(f);
				productFlagRepository.save(flag.get());
			});
			return "Product Flag Successfully";
		} else {
			throw new ProductFlagNotFoundException(ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE);
		}

	}

}
