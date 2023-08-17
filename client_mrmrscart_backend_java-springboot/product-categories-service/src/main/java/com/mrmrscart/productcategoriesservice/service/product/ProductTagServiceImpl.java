package com.mrmrscart.productcategoriesservice.service.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.entity.product.ProductTag;
import com.mrmrscart.productcategoriesservice.exception.product.ProductTagNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagViewPojo;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductTagRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.util.PaginatedResponse;

import static com.mrmrscart.productcategoriesservice.common.product.ProductVariationConstant.SOMETHING_WENT_WRONG;

@Service
public class ProductTagServiceImpl implements ProductTagService {

	@Autowired
	private ProductTagRepository productTagRepository;

	@Autowired
	private MasterProductRepository masterProductRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Override
	public ProductTag addProductTag(ProductTagPojo productTagPojo) {
		try {
			ProductTag tag = new ProductTag();
			tag.setTagName(productTagPojo.getTagName());
			tag.setCreatedBy(productTagPojo.getCreatedBy());
			if (productTagPojo.getCreatedByType().equals(EUserType.ADMIN)) {
				tag.setApproved(true);
				tag.setStatus(EStatus.ACTIVE);
			} else {
				tag.setStatus(EStatus.INITIATED);
			}
			tag.setCreatedByType(productTagPojo.getCreatedByType());
			productTagRepository.save(tag);
			return tag;
		} catch (Exception e) {
			throw new ProductTagNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductTag> getAllProductTags(int pageNumber, int pageSize) {
		try {
			List<ProductTag> findByisDeleted = productTagRepository.findByisDeleted(false);
			Collections.sort(findByisDeleted, (b, a) -> a.getLastUpdatedAt().compareTo(b.getLastUpdatedAt()));
			return (List<ProductTag>) PaginatedResponse.getPaginatedResponse(findByisDeleted, pageNumber, pageSize);
		} catch (Exception e) {
			throw new ProductTagNotFoundException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<ProductTagViewPojo> getProductTag(String tagId) {
		try {
			List<ProductTagViewPojo> productTagViewPojos = new ArrayList<>();
			masterProductRepository.findAll().forEach(master -> {
				if (master.getTags().contains(tagId)) {
					productVariationRepository.findByMasterProductIdAndStatusAndIsDelete(master.getMasterProductId(),
							EStatus.APPROVED, false).forEach(product -> {
								ProductTagViewPojo pojo = new ProductTagViewPojo(product.getProductTitle(),
										product.getProductVariationId(), master.getSupplierId(), master.getCreatedBy(),
										product.getLastUpdatedAt());
								productTagViewPojos.add(pojo);
							});
				}
			});

			return productTagViewPojos;
		} catch (Exception e) {
			throw new ProductTagNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean deleteProductTag(String tagId) {
		try {
			Optional<ProductTag> tag = productTagRepository.findById(tagId);
			if (tag.isPresent()) {
				tag.get().setDeleted(true);
				productTagRepository.save(tag.get());
				return true;
			} else
				throw new ProductTagNotFoundException("Tag not found");
		} catch (Exception e) {
			throw new ProductTagNotFoundException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<ProductTag> getAllApprovedTags() {
		return productTagRepository.findByisDeletedAndIsApproved(false, true);
	}

	@Override
	public String approveProductTag(String tagId, boolean status) {
		try {
			Optional<ProductTag> tag = productTagRepository.findById(tagId);
			if (tag.isPresent()) {
				if (status) {
					tag.get().setApproved(true);
					tag.get().setStatus(EStatus.ACTIVE);
					productTagRepository.save(tag.get());
					return "Product Tag Approved Successfully";
				} else {
					tag.get().setStatus(EStatus.INACTIVE);
					productTagRepository.save(tag.get());
					return "Product Tag Rejected Successfully";
				}

			} else {
				throw new ProductTagNotFoundException("Product Tag not found");
			}
		} catch (Exception e) {
			throw new ProductTagNotFoundException(SOMETHING_WENT_WRONG);
		}

	}

}
