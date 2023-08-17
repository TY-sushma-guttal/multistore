package com.mrmrscart.productcategoriesservice.service.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.UserIdNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.category.OtherVariationOptionPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.OtherVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.UpdateVariationOptionPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.UpdateVariationPojo;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;

import static com.mrmrscart.productcategoriesservice.common.category.StandardVariationConstant.*;

@Service
public class StandardVariationServiceImpl implements StandardVariationService {

	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private StandardOptionRepository standardOptionRepository;

	@Autowired
	private OtherVariationRepository otherVariationRepository;

	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Override
	public StandardVariation addStandardVariation(StandardVariationPojo data) {
		try {
			StandardVariation standardVariation = new StandardVariation();
			BeanUtils.copyProperties(data, standardVariation);
			standardVariation.setCreatedAt(LocalDateTime.now());
			return standardVariationRepository.save(standardVariation);
		} catch (UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new StandardVariationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public StandardVariation getStandardVariation(String id) {
		try {
			StandardVariation standardVariation = standardVariationRepository.findById(id).orElse(null);
			if (standardVariation != null) {
				return standardVariation;
			} else {
				throw new StandardVariationIdNotFoundException(INVALID_STANDARD_VARIATION_ID);
			}
		} catch (StandardVariationIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new StandardVariationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SubCategoryPojo updateVariation(UpdateVariationPojo data) {
		SubCategoryPojo subCategoryPojo = new SubCategoryPojo();
		if (data.getVariationType().equals(ECategory.STANDARD_VARIATION.name())) {
			Optional<StandardVariation> findById = standardVariationRepository.findById(data.getVariationId());
			if (findById.isPresent()) {
				StandardVariation standardVariation = findById.get();
				if (!standardVariation.getVariationName().equals(data.getVariationName())) {
					standardVariation.setVariationName(data.getVariationName());
					standardVariation.setLastUpdatedBy(data.getLastUpdatedBy());
					standardVariation.setLastUpdatedAt(LocalDateTime.now());
					standardVariationRepository.save(standardVariation);
				}
				SubCategoryPojo updateSubCategory = updateSubCategory(data, subCategoryPojo, standardVariation);
				BeanUtils.copyProperties(updateSubCategory, subCategoryPojo);

			} else {
				throw new StandardVariationException(INVALID_STANDARD_VARIATION_ID);
			}
		} else if (data.getVariationType().equals(ECategory.OTHER_VARIATION.name())) {
			SubCategoryPojo updateOtherVariation = updateOtherVariation(data, subCategoryPojo);
			BeanUtils.copyProperties(updateOtherVariation, subCategoryPojo);
		}
		return subCategoryPojo;
	}

	private List<OtherVariationPojo> getOtherVariationPojo(String subCategoryId) {
		List<OtherVariation> findBySubCategoryId = otherVariationRepository.findBySubCategoryId(subCategoryId);
		List<OtherVariationPojo> otherVariationPojos = new ArrayList<>();
		findBySubCategoryId.forEach(e -> {
			OtherVariationPojo otherVariationPojo = new OtherVariationPojo();
			BeanUtils.copyProperties(e, otherVariationPojo);
			List<OtherVariationOption> findByOtherVariationId = otherVariationOptionRepository
					.findByOtherVariationId(e.getOtherVariationId());
			List<OtherVariationOptionPojo> otherVariationOptionPojos = new ArrayList<>();
			findByOtherVariationId.forEach(x -> {
				OtherVariationOptionPojo otherVariationOptionPojo = new OtherVariationOptionPojo();
				BeanUtils.copyProperties(x, otherVariationOptionPojo);
				otherVariationOptionPojos.add(otherVariationOptionPojo);
			});
			otherVariationPojo.setOptionList(otherVariationOptionPojos);
			otherVariationPojos.add(otherVariationPojo);
		});
		return otherVariationPojos;
	}

	private SubCategoryPojo updateSubCategory(UpdateVariationPojo data, SubCategoryPojo subCategoryPojo,
			StandardVariation standardVariation) {
		Optional<SubCategory> findById2 = subCategoryRepository.findById(data.getSubCategoryId());
		if (findById2.isPresent()) {
			SubCategory subCategory = findById2.get();
			List<SubCategoryStandardVariation> standardVariationList = subCategory.getStandardVariationList();
			List<SubCategoryVariationOption> subCategoryVariationOptions = new ArrayList<>();
			standardVariationList.forEach(x -> {
				if (x.getVariationId().equals(data.getVariationId())) {
					subCategoryVariationOptions.addAll(x.getOptionList());
				}
			});
			List<UpdateVariationOptionPojo> updateVariationOptionPojos = data.getUpdateVariationOptionPojos();
			List<UpdateVariationOptionPojo> removeList = new ArrayList<>();
			updateVariationOptionPojos.forEach(e -> subCategoryVariationOptions.forEach(x -> {
				if (x.getOptionId().equals(e.getOptionId())) {
					updateStandardOption(e, x);
					BeanUtils.copyProperties(e, x);
					removeList.add(e);
				}
			}));
			updateVariationOptionPojos.removeAll(removeList);
			updateVariationOptionPojos.forEach(e -> {
				StandardOption standardOption = new StandardOption();
				BeanUtils.copyProperties(e, standardOption);
				standardOption.setCreatedAt(LocalDateTime.now());
				standardOption.setStandardVariationId(standardVariation.getStandardVariationId());
				StandardOption save = standardOptionRepository.save(standardOption);
				SubCategoryVariationOption subCategoryVariationOption = new SubCategoryVariationOption();
				subCategoryVariationOption.setOptionId(save.getStandardOptionId());
				subCategoryVariationOptions.add(subCategoryVariationOption);

			});
			standardVariationList.forEach(x -> {
				if (x.getVariationId().equals(data.getVariationId())) {
					x.setOptionList(subCategoryVariationOptions);
				}
			});
			SubCategory save = subCategoryRepository.save(subCategory);
			BeanUtils.copyProperties(save, subCategoryPojo);
			List<OtherVariationPojo> otherVariationPojo = getOtherVariationPojo(save.getSubCategoryId());
			subCategoryPojo.setOthersVariationList(otherVariationPojo);
			return subCategoryPojo;
		} else {
			throw new StandardVariationException(INVALID_SUB_CATEGORY_ID);
		}
	}

	private void updateStandardOption(UpdateVariationOptionPojo e, SubCategoryVariationOption x) {
		Optional<StandardOption> findById = standardOptionRepository.findById(x.getOptionId());
		if (findById.isPresent() && !findById.get().getOptionName().equals(e.getOptionName())) {
			findById.get().setOptionName(e.getOptionName());
			standardOptionRepository.save(findById.get());
		}
	}

	private SubCategoryPojo updateOtherVariation(UpdateVariationPojo data, SubCategoryPojo subCategoryPojo) {
		Optional<OtherVariation> findById = otherVariationRepository.findById(data.getVariationId());
		if (findById.isPresent()) {
			OtherVariation otherVariation = findById.get();
			if (!otherVariation.getVariationName().equals(data.getVariationName())) {
				otherVariation.setVariationName(data.getVariationName());
				otherVariation.setLastUpdatedAt(LocalDateTime.now());
				otherVariation.setLastUpdatedBy(data.getLastUpdatedBy());
				otherVariationRepository.save(otherVariation);
			}
			data.getUpdateVariationOptionPojos().forEach(e -> {
				if (e.getOptionId() == null) {
					OtherVariationOption otherVariationOption = new OtherVariationOption();
					otherVariationOption.setOptionName(e.getOptionName());
					otherVariationOption.setCreatedAt(LocalDateTime.now());
					otherVariationOption.setCreatedBy(data.getCreatedBy());
					otherVariationOption.setOtherVariationId(otherVariation.getOtherVariationId());
					otherVariationOptionRepository.save(otherVariationOption);
				} else {
					OtherVariationOption otherVariationOption = otherVariationOptionRepository.findById(e.getOptionId())
							.get();
					otherVariationOption.setLastUpdatedAt(LocalDateTime.now());
					otherVariationOption.setLastUpdatedBy(data.getLastUpdatedBy());
					otherVariationOption.setOptionName(e.getOptionName());
					otherVariationOption.setDelete(e.isDelete());
					otherVariationOptionRepository.save(otherVariationOption);
				}
			});
			Optional<SubCategory> findById2 = subCategoryRepository.findById(otherVariation.getSubCategoryId());
			if (findById2.isPresent()) {
				BeanUtils.copyProperties(findById2.get(), subCategoryPojo);
				List<OtherVariationPojo> otherVariationPojo = getOtherVariationPojo(findById2.get().getSubCategoryId());
				subCategoryPojo.setOthersVariationList(otherVariationPojo);
				return subCategoryPojo;
			} else {
				throw new StandardVariationException(INVALID_SUB_CATEGORY_ID);
			}
		} else {
			throw new StandardVariationException(INVALID_STANDARD_VARIATION_ID);
		}
	}

	@Override
	public List<StandardVariation> getStandardVariations() {
		try {
			List<StandardVariation> standardVariationList = standardVariationRepository.findAll();
			if (!standardVariationList.isEmpty()) {
				return standardVariationList;
			} else {
				throw new StandardVariationException(EMPTY_STANDARD_VARIATION_LIST);
			}
		} catch (StandardVariationException e) {
			throw e;
		} catch (Exception e) {
			throw new StandardVariationException(SOMETHING_WENT_WRONG);
		}
	}
}