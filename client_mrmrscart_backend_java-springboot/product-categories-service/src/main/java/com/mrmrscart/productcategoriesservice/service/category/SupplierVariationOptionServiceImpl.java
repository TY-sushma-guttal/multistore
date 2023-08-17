package com.mrmrscart.productcategoriesservice.service.category;

import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.CREATED_AT;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.entity.category.SupplierVariationOption;
import com.mrmrscart.productcategoriesservice.exception.category.InvalidVariationTypeException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SupplierVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.VariationNameExistsException;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionPojo;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SupplierVariationOptionRepository;

@Service
public class SupplierVariationOptionServiceImpl implements SupplierVariationOptionService {

	@Autowired
	private SupplierVariationOptionRepository supplierVariationOptionRepository;

	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Autowired
	private StandardOptionRepository standardOptionRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private OtherVariationRepository otherVariationRepository;
	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Override
	public SupplierVariationOption addSupplierVariationOption(SupplierVariationOptionPojo variationOptionPojo) {
		try {
			SupplierVariationOption supplierVariationOption = new SupplierVariationOption();

			BeanUtils.copyProperties(variationOptionPojo, supplierVariationOption);
			supplierVariationOption.setOptionName(variationOptionPojo.getOptionName());
			supplierVariationOption.setApprovalStatus(EStatus.INITIATED);
			supplierVariationOption.setCreatedAt(LocalDateTime.now());
			return supplierVariationOptionRepository.save(supplierVariationOption);
		} catch (Exception e) {
			throw new StandardVariationException("Failed to add the variation");
		}

	}

	@Override
	public List<SupplierVariationOption> getAllInitiatedVariationOption(int pageNumber, int pageSize) {
		try {
			return supplierVariationOptionRepository
					.findByApprovalStatus(EStatus.INITIATED, PageRequest.of(pageNumber, pageSize)).getContent();
		} catch (Exception e) {
			throw new StandardVariationIdNotFoundException("Variations and options not found");
		}
	}

	@Transactional
	@Override
	public SupplierVariationOption approveRejectVariation(SupplierVariationOptionPojo supplierVariationOption) {
		if (supplierVariationOption.getApprovalStatus() == EStatus.REJECTED) {
			Optional<SupplierVariationOption> variatonApproval = supplierVariationOptionRepository
					.findById(supplierVariationOption.getVariationOptionId());
			if (variatonApproval.isPresent()) {
				// log it in the log database by whom at what time
				supplierVariationOptionRepository.deleteById(supplierVariationOption.getVariationOptionId());
				supplierVariationOption.setApprovalStatus(EStatus.REJECTED);
				SupplierVariationOption option = new SupplierVariationOption();
				BeanUtils.copyProperties(supplierVariationOption, option);
				return option;
			} else {
				throw new SupplierVariationOptionNotFoundException("Provide a valid supplier variation id");
			}

		} else if (supplierVariationOption.getApprovalStatus() == EStatus.APPROVED) {

			if (supplierVariationOption.getVariationId() == null) {

				Optional<StandardVariation> optional = Optional.of(
						standardVariationRepository.findByVariationName(supplierVariationOption.getVariationName()));
				if (optional.get() != null) {
					throw new VariationNameExistsException("Variation name already exists");
				} else {
					StandardVariation standardVariation = new StandardVariation();
					standardVariation.setVariationName(supplierVariationOption.getVariationName());
					standardVariation.setCreatedBy(supplierVariationOption.getSupplierId());
					standardVariation.setCreatedAt(LocalDateTime.now());
					StandardVariation save = standardVariationRepository.save(standardVariation);

					List<StandardOption> arrayList = new ArrayList<>();
					StandardOption standardOption = new StandardOption();
					standardOption.setStandardVariationId(save.getStandardVariationId());
					standardOption.setCreatedBy(save.getCreatedBy());

					supplierVariationOption.getOptionName().forEach(f -> {
						standardOption.setOptionName(f);
						standardOption.setCreatedAt(LocalDateTime.now());
						arrayList.add(standardOption);

					});
					List<StandardOption> saveAll = standardOptionRepository.saveAll(arrayList);

					Optional<SubCategory> findById = subCategoryRepository
							.findById(supplierVariationOption.getSubCategoryId());
					if (findById.isPresent()) {
						SubCategoryStandardVariation variation = new SubCategoryStandardVariation();
						variation.setVariationId(save.getStandardVariationId());
						variation.setVariationType(ECategory.STANDARD_VARIATION);
						List<SubCategoryVariationOption> optionList = new ArrayList<>();
						saveAll.forEach(e -> {
							SubCategoryVariationOption subCategoryVariationOption = new SubCategoryVariationOption();
							subCategoryVariationOption.setOptionId(e.getStandardOptionId());
							optionList.add(subCategoryVariationOption);
						});
						variation.setOptionList(optionList);
						findById.get().getStandardVariationList().add(variation);
						findById.get().setLastUpdatedAt(LocalDateTime.now());
						subCategoryRepository.save(findById.get());

						supplierVariationOptionRepository.deleteById(supplierVariationOption.getVariationOptionId());
						supplierVariationOption.setApprovalStatus(EStatus.APPROVED);
						SupplierVariationOption option = new SupplierVariationOption();
						BeanUtils.copyProperties(supplierVariationOption, option);
						return option;

					} else {
						throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
					}

				}

			} else {

				if (supplierVariationOption.getVariationType() == ECategory.STANDARD_VARIATION) {

					//
					Optional<StandardVariation> findById = standardVariationRepository
							.findById(supplierVariationOption.getVariationId());

					List<StandardOption> list = new ArrayList<>();
					if (findById.isPresent()) {
						supplierVariationOption.getOptionName().forEach(e -> {
							StandardOption standardOption = new StandardOption();

							standardOption.setOptionName(e);
							standardOption.setStandardVariationId(supplierVariationOption.getVariationId());
							standardOption.setCreatedBy(supplierVariationOption.getSupplierId());
							standardOption.setCreatedAt(LocalDateTime.now());

							list.add(standardOptionRepository.save(standardOption));
						});

						Optional<SubCategory> optional = subCategoryRepository
								.findById(supplierVariationOption.getSubCategoryId());

						if (optional.isPresent()) {

							List<SubCategoryStandardVariation> collect = optional.get().getStandardVariationList()
									.stream()
									.filter(x -> x.getVariationId().equals(supplierVariationOption.getVariationId()))
									.collect(Collectors.toList());
							if (collect.isEmpty()) {
								throw new StandardVariationIdNotFoundException("Standard variation not found");
							}
							SubCategoryStandardVariation variation = collect.get(0);
							optional.get().getStandardVariationList().remove(collect.get(0));

							list.forEach(e -> {
								SubCategoryVariationOption subCategoryVariationOption = new SubCategoryVariationOption();
								subCategoryVariationOption.setOptionId(e.getStandardOptionId());
								variation.getOptionList().add(subCategoryVariationOption);

							});

							optional.get().getStandardVariationList().add(variation);
							optional.get().setLastUpdatedAt(LocalDateTime.now());
							subCategoryRepository.save(optional.get());

							supplierVariationOptionRepository
									.deleteById(supplierVariationOption.getVariationOptionId());
							supplierVariationOption.setApprovalStatus(EStatus.APPROVED);
							SupplierVariationOption option = new SupplierVariationOption();
							BeanUtils.copyProperties(supplierVariationOption, option);
							return option;

						} else {
							throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
						}

					} else {
						throw new StandardVariationIdNotFoundException("Standard variation not found");
					}

				} else if (supplierVariationOption.getVariationType() == ECategory.OTHER_VARIATION) {

					Optional<OtherVariation> findById = otherVariationRepository
							.findById(supplierVariationOption.getVariationId());
					if (!findById.isPresent()) {
						throw new OtherVariationNotFoundException("Provide a valid other variation id");
					} else {
						if (!(findById.get().getSubCategoryId().equals(supplierVariationOption.getSubCategoryId()))) {
							throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
						}

						supplierVariationOption.getOptionName().stream().forEach(e -> {
							OtherVariationOption otherVariationOption = new OtherVariationOption();
							otherVariationOption.setOptionName(e);
							otherVariationOption.setCreatedBy(supplierVariationOption.getSupplierId());
							otherVariationOption.setOtherVariationId(supplierVariationOption.getVariationId());
							otherVariationOption.setCreatedAt(LocalDateTime.now());
							otherVariationOptionRepository.save(otherVariationOption);
						});

						supplierVariationOptionRepository.deleteById(supplierVariationOption.getVariationOptionId());
						supplierVariationOption.setApprovalStatus(EStatus.APPROVED);
						SupplierVariationOption option = new SupplierVariationOption();
						BeanUtils.copyProperties(supplierVariationOption, option);
						return option;

					}

				} else {
					throw new InvalidVariationTypeException("Provide a valid variation type");

				}

			}

		} else {
			throw new InvalidVariationTypeException("Provide a valid approval status");
		}

	}

	@Override
	public List<SupplierVariationOption> getAllInitiatedVariationOption(int pageNumber, int pageSize,
			SupplierVariationOptionFilterPojo filterPojo) {
		Criteria criteria = new Criteria();
		if (filterPojo.getDateFrom() != null && filterPojo.getDateTo() != null) {
			criteria.andOperator(Criteria.where(CREATED_AT).gt(filterPojo.getDateFrom()).lt(filterPojo.getDateTo()));
		}
		List<SupplierVariationOption> find = mongoTemplate.find(
				new Query().addCriteria(criteria),
				SupplierVariationOption.class);
		List<SupplierVariationOption> list = find.stream().filter(s->s.getApprovalStatus().equals(EStatus.INITIATED)).collect(Collectors.toList());
		if (pageNumber == 0) {
			return list.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

}
