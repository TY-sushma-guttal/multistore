package com.mrmrscart.productcategoriesservice.service.category;

import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.INVALID_MAIN_CATEGORY_ID;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.INVALID_PRICE_RANGE_ID;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.EMPTY_CATEGORY_SET_LIST;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.EMPTY_MAIN_CATEGORY_LIST;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.EMPTY_SUB_CATEGORY_LIST;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_CATEGORYSET_ID;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_OTHER_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_OTHER_VARIATION_FOR_SUBCATEGORY;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_STANDARD_VARIATION;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_STANDARD_VARIATION_OPTION;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_VARIATION_TYPE;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.MAIN_CATEGORY_ID_NOT_FOUND;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.CategoryPriceRange;
import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetException;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.InvalidVariationTypeException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.PriceRangeIdNotFound;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.category.CategoryPriceRangePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.OtherVariationOptionPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.OtherVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.PriceRange;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardOptionResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardVariationResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationOptionPojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategoryPriceRangeRepository;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetAndMainCategoryWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.StandardOthersVariationWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryVariationWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryWrapper;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private MainCategoryService mainCategoryService;

	@Autowired
	private CategoryPriceRangeRepository categoryPriceRangeRepository;

	@Autowired
	private CategorySetRepository categorySetRepository;

	@Autowired
	private MainCategoryRepository mainCategoryRepository;

	@Autowired
	private OtherVariationRepository otherVariationRepository;

	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Autowired
	private StandardOptionRepository standardOptionRepository;

	@Override
	public SubCategory addSubCategory(SubCategoryPojo data) {
		try {
			SubCategory subCategoryObj = new SubCategory();
			BeanUtils.copyProperties(data, subCategoryObj);

			Optional<CategorySet> categorySet = categorySetRepository.findById(data.getSetId());
			if (!categorySet.isPresent()) {
				throw new CategorySetNotFoundException(INVALID_CATEGORYSET_ID);
			}
			Optional<MainCategory> mainCategory = mainCategoryRepository
					.findById(categorySet.get().getMainCategoryId());

			if (!mainCategory.isPresent()) {
				throw new MainCategoryNotFoundException(MAIN_CATEGORY_ID_NOT_FOUND);
			}

			subCategoryObj.setPriceRangeId(mainCategory.get().getPriceRangeId());
			if (mainCategory.get().getPriceRangeId() == null && !data.getPriceRangeList().isEmpty()) {
				List<PriceRange> priceRanges = data.getPriceRangeList();
				CategoryPriceRange save = mainCategoryService.saveCategoryPriceRange(priceRanges);
				subCategoryObj.setPriceRangeId(save.getPriceRangeId());
			}

			subCategoryObj.setSupplierStorePercentage(mainCategory.get().getSupplierStorePercentage());
			if (mainCategory.get().getSupplierStorePercentage() == 0 && data.getSupplierStorePercentage() > 0.0) {
				subCategoryObj.setSupplierStorePercentage(data.getSupplierStorePercentage());
			}

			return subCategoryRepository.save(subCategoryObj);
		} catch (CategorySetNotFoundException | MainCategoryNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SubCategoryResponsePojo getSubCategory(String id) {
		try {
			Optional<SubCategory> findById = subCategoryRepository.findById(id);
			if (findById.isPresent()) {
				SubCategory subCategory = findById.get();
				SubCategoryResponsePojo pojo = new SubCategoryResponsePojo();
				BeanUtils.copyProperties(subCategory, pojo);

				Optional<CategoryPriceRange> priceRangeObj = categoryPriceRangeRepository
						.findById(subCategory.getPriceRangeId());
				if (priceRangeObj.isPresent()) {
					CategoryPriceRangePojo priceRangePojo = new CategoryPriceRangePojo();
					BeanUtils.copyProperties(priceRangeObj.get(), priceRangePojo);
					pojo.setCategoryPriceRange(priceRangePojo);
				} else {
					throw new PriceRangeIdNotFound(INVALID_PRICE_RANGE_ID);
				}

				Optional<CategorySet> getCategorySet = categorySetRepository.findById(subCategory.getSetId());
				if (getCategorySet.isPresent()) {
					Optional<MainCategory> getMainCategory = mainCategoryRepository
							.findById(getCategorySet.get().getMainCategoryId());
					if (getMainCategory.isPresent()) {
						MainCategoryPojo mainCategoryPojo = new MainCategoryPojo();
						BeanUtils.copyProperties(getMainCategory.get(), mainCategoryPojo);
						CategorySetPojo categorySetPojo = new CategorySetPojo();
						BeanUtils.copyProperties(getCategorySet.get(), categorySetPojo);
						pojo.setSetDetails(categorySetPojo);
						pojo.setMainCategoryDetails(mainCategoryPojo);
					} else {
						throw new MainCategoryNotFoundException(INVALID_MAIN_CATEGORY_ID);
					}
				} else {
					throw new CategorySetNotFoundException(INVALID_CATEGORYSET_ID);
				}
				List<StandardVariationResponsePojo> standardVariationResponsePojos = new ArrayList<>();

				subCategory.getStandardVariationList().forEach(e -> {
					StandardVariationResponsePojo standardVariationResponsePojo = new StandardVariationResponsePojo();
					Optional<StandardVariation> findById2 = standardVariationRepository.findById(e.getVariationId());
					if (findById2.isPresent()) {
						BeanUtils.copyProperties(findById2.get(), standardVariationResponsePojo);
						standardVariationResponsePojo.setVariationType(ECategory.STANDARD_VARIATION.name());
						List<StandardOptionResponsePojo> standardOptionResponsePojos = new ArrayList<>();
						e.getOptionList().forEach(f -> {

							Optional<StandardOption> findById3 = standardOptionRepository.findById(f.getOptionId());
							if (findById3.isPresent()) {
								StandardOptionResponsePojo standardOptionResponsePojo = new StandardOptionResponsePojo();
								BeanUtils.copyProperties(findById3.get(), standardOptionResponsePojo);
								standardOptionResponsePojos.add(standardOptionResponsePojo);
							}
						});
						standardVariationResponsePojo.setOptionList(standardOptionResponsePojos);
						standardVariationResponsePojos.add(standardVariationResponsePojo);
					}

				});
				pojo.setStandardVariationList(standardVariationResponsePojos);
				List<OtherVariation> findBySubCategoryId = otherVariationRepository.findBySubCategoryId(id);
				List<OtherVariationPojo> otherVariationPojos = new ArrayList<>();
				findBySubCategoryId.forEach(e -> {
					OtherVariationPojo otherVariationPojo = new OtherVariationPojo();
					BeanUtils.copyProperties(e, otherVariationPojo);
					List<OtherVariationOption> findByOtherVariationId = otherVariationOptionRepository
							.findByOtherVariationId(e.getOtherVariationId());
					List<OtherVariationOptionPojo> otherVariationOptionPojos = new ArrayList<>();
					findByOtherVariationId.forEach(f -> {
						OtherVariationOptionPojo otherVariationOptionPojo = new OtherVariationOptionPojo();
						BeanUtils.copyProperties(f, otherVariationOptionPojo);
						otherVariationOptionPojos.add(otherVariationOptionPojo);
					});
					otherVariationPojo.setOptionList(otherVariationOptionPojos);
					otherVariationPojos.add(otherVariationPojo);
				});
				pojo.setOthersVariationList(otherVariationPojos);
				return pojo;
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}

		} catch (SubCategoryIdNotFoundException | MainCategoryNotFoundException | CategorySetNotFoundException
				| PriceRangeIdNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SubCategory updateSubCategory(SubCategoryPojo data) {
		try {
			Optional<SubCategory> subCategoryObj = subCategoryRepository.findById(data.getSubCategoryId());
			if (subCategoryObj.isPresent()) {
				subCategoryObj.get().setSubCategoryName(data.getSubCategoryName());
				Optional<CategoryPriceRange> categoryPriceRange = categoryPriceRangeRepository
						.findById(subCategoryObj.get().getPriceRangeId());
				if (categoryPriceRange.isPresent()) {
					categoryPriceRange.get().setPriceRange(data.getPriceRangeList());
					if (!data.getPriceRangeList().isEmpty()) {
						categoryPriceRangeRepository.save(categoryPriceRange.get());
					}
				} else {
					throw new PriceRangeIdNotFound(INVALID_PRICE_RANGE_ID);
				}
				return subCategoryRepository.save(subCategoryObj.get());
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		} catch (SubCategoryIdNotFoundException | PriceRangeIdNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SubCategory deleteSubCategory(String id) {
		try {
			SubCategory subCategory = subCategoryRepository.findById(id).orElse(null);
			if (subCategory != null) {
				subCategory.setDisable(true);
				return subCategory;
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		} catch (SubCategoryIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SubCategory> getSubcategoryBySetId(String id) {
		try {
			List<SubCategory> subCategoryList = subCategoryRepository.findBySetIdAndisDisable(id, false);
			if (!subCategoryList.isEmpty()) {
				return subCategoryList;
			} else {
				throw new CategorySetNotFoundException(INVALID_CATEGORYSET_ID);
			}
		} catch (CategorySetNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SubCategoryResponsePojo> getAllSubCategories(int pageNumber, int pageSize) {
		try {
			List<SubCategory> allSubCategory = subCategoryRepository.findAll(PageRequest.of(pageNumber, pageSize))
					.getContent();
			List<SubCategoryResponsePojo> allData = new ArrayList<>();
			allSubCategory.forEach(e -> allData.add(getSubCategory(e.getSubCategoryId())));
			if (!allSubCategory.isEmpty() || !allData.isEmpty()) {
				return allData;
			} else {
				throw new SubCategoryException(EMPTY_SUB_CATEGORY_LIST);
			}
		} catch (SubCategoryException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	public List<OtherVariationPojo> otherVariationJsonCreation(List<OtherVariation> otherVariationOptionPojos,
			List<OtherVariationOption> otherVariationOptions) {
		List<OtherVariationPojo> otherVariationPojoList = new ArrayList<>();
		List<OtherVariationOptionPojo> otherVariationOptionPojoList = null;
		for (OtherVariation otherVariation : otherVariationOptionPojos) {
			OtherVariationPojo otherVariationPojo = new OtherVariationPojo();
			BeanUtils.copyProperties(otherVariation, otherVariationPojo);

			otherVariationOptionPojoList = new ArrayList<>();
			for (OtherVariationOption otherVariationOption : otherVariationOptions) {
				OtherVariationOptionPojo otherVariationOptionPojo = new OtherVariationOptionPojo();
				BeanUtils.copyProperties(otherVariationOption, otherVariationOptionPojo);
				otherVariationOptionPojoList.add(otherVariationOptionPojo);
			}
			otherVariationPojo.setOptionList(otherVariationOptionPojoList);
			otherVariationPojoList.add(otherVariationPojo);
		}
		return otherVariationPojoList;
	}

	@Override
	public SubCategoryVariationWrapper addVariations(SubCategoryVariation data) {
		try {
			SubCategoryVariationWrapper variationWrapper = new SubCategoryVariationWrapper();
			List<SubCategoryStandardVariation> standardVariationList = new ArrayList<>();
			List<SubCategoryVariationOption> standardVariationOptionList = null;
			List<OtherVariation> otherVariationList = new ArrayList<>();
			List<OtherVariationOption> otherVariationOptionList = null;

			Optional<SubCategory> subCategory = subCategoryRepository.findById(data.getSubCategoryId());

			if (subCategory.isPresent()) {
				for (SubCategoryStandardVariation standardVariation : data.getStandardVariationList()) {
					standardVariationOptionList = new ArrayList<>();
					standardVariation.setVariationType(ECategory.STANDARD_VARIATION);
					for (SubCategoryVariationOption standardVariationOption : standardVariation.getOptionList()) {
						standardVariationOptionList.add(standardVariationOption);
					}
					standardVariationList.add(standardVariation);
				}
				subCategory.get().setStandardVariationList(standardVariationList);

				for (OtherVariationPojo otherVariation : data.getOtherVariationList()) {
					OtherVariation otherVariationObj = new OtherVariation();
					otherVariationOptionList = new ArrayList<>();

					BeanUtils.copyProperties(otherVariation, otherVariationObj);
					otherVariationObj.setSubCategoryId(data.getSubCategoryId());
					otherVariationObj.setVariationType(ECategory.OTHER_VARIATION.name());
					OtherVariation savedObj = otherVariationRepository.save(otherVariationObj);

					for (OtherVariationOptionPojo option : otherVariation.getOptionList()) {
						OtherVariationOption otherVariationOption = new OtherVariationOption();
						BeanUtils.copyProperties(option, otherVariationOption);

						otherVariationOption.setOtherVariationId(savedObj.getOtherVariationId());
						otherVariationOptionList.add(otherVariationOptionRepository.save(otherVariationOption));
					}
					otherVariationList.add(savedObj);
				}
				subCategoryRepository.save(subCategory.get());
				variationWrapper.setSubCategoryId(data.getSubCategoryId());
				variationWrapper.setStandardVariationList(standardVariationList);
				variationWrapper.setOtherVariationList(
						otherVariationJsonCreation(otherVariationList, otherVariationOptionList));
				return variationWrapper;
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		} catch (SubCategoryIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	public boolean deleteVariation(VariationOptionPojo variationOptionPojo) {

		if (variationOptionPojo.getVariationType() == ECategory.STANDARD_VARIATION) {

			Optional<SubCategory> findById = subCategoryRepository.findById(variationOptionPojo.getSubCategoryId());

			if (findById.isPresent()) {
				List<SubCategoryStandardVariation> standardVariationList = findById.get().getStandardVariationList();

				List<SubCategoryStandardVariation> collect = standardVariationList.stream()
						.filter(e -> e.getVariationId().equals(variationOptionPojo.getVariationId()))
						.collect(Collectors.toList());

				if (!collect.isEmpty()) {
					SubCategoryStandardVariation subCategoryStandardVariation = collect.get(0);
					standardVariationList.remove(subCategoryStandardVariation);
					subCategoryStandardVariation.setDelete(true);
					List<SubCategoryVariationOption> optionList = subCategoryStandardVariation.getOptionList();
					optionList.stream().forEach(e -> e.setDelete(true));
					subCategoryStandardVariation.setOptionList(optionList);
					standardVariationList.add(subCategoryStandardVariation);
					findById.get().setStandardVariationList(standardVariationList);
					subCategoryRepository.save(findById.get());
					return true;
				} else {
					throw new StandardVariationIdNotFoundException(INVALID_STANDARD_VARIATION);
				}

			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}

		} else if (variationOptionPojo.getVariationType() == ECategory.OTHER_VARIATION) {

			Optional<OtherVariation> optional = Optional
					.of(otherVariationRepository.findBySubCategoryIdAndOtherVariationId(
							variationOptionPojo.getSubCategoryId(), variationOptionPojo.getVariationId()));

			if (optional.get() != null) {
				optional.get().setDelete(true);
				OtherVariation otherVariation = otherVariationRepository.save(optional.get());
				Optional<List<OtherVariationOption>> otherOption = Optional.of(
						otherVariationOptionRepository.findByOtherVariationId(otherVariation.getOtherVariationId()));
				List<OtherVariationOption> list = otherOption.get();
				list.stream().forEach(e -> e.setDelete(true));
				otherVariationOptionRepository.saveAll(list);
				return true;

			} else {
				throw new OtherVariationNotFoundException(INVALID_OTHER_VARIATION_FOR_SUBCATEGORY);
			}

		} else {
			throw new InvalidVariationTypeException(INVALID_VARIATION_TYPE);
		}
	}

	@Override
	public boolean deleteOption(VariationOptionPojo variationOptionPojo) {
		if (variationOptionPojo.getVariationType() == ECategory.STANDARD_VARIATION) {
			Optional<SubCategory> findById = subCategoryRepository.findById(variationOptionPojo.getSubCategoryId());

			if (findById.isPresent()) {
				SubCategory subCategory = findById.get();
				List<SubCategoryStandardVariation> standardVariationList = subCategory.getStandardVariationList();
				List<SubCategoryStandardVariation> list = subCategory.getStandardVariationList().stream()
						.filter(e -> e.getVariationId().equals(variationOptionPojo.getVariationId()))
						.collect(Collectors.toList());

				if (!list.isEmpty()) {
					standardVariationList.remove(list.get(0));
					SubCategoryStandardVariation subCategoryStandardVariation = list.get(0);
					List<SubCategoryVariationOption> option = subCategoryStandardVariation.getOptionList().stream()
							.filter(f -> f.getOptionId().equals(variationOptionPojo.getOptionId()))
							.collect(Collectors.toList());

					if (!option.isEmpty()) {
						subCategoryStandardVariation.getOptionList().remove(option.get(0));
						option.get(0).setDelete(true);
						subCategoryStandardVariation.getOptionList().add(option.get(0));
						standardVariationList.add(subCategoryStandardVariation);
						subCategory.setStandardVariationList(standardVariationList);
						subCategoryRepository.save(subCategory);
						return true;
					} else {
						throw new StandardVariationOptionNotFoundException(INVALID_STANDARD_VARIATION_OPTION);
					}
				} else {
					throw new StandardVariationIdNotFoundException(INVALID_STANDARD_VARIATION);
				}
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		} else if (variationOptionPojo.getVariationType() == ECategory.OTHER_VARIATION) {

			Optional<OtherVariationOption> findById = otherVariationOptionRepository
					.findById(variationOptionPojo.getOptionId());
			if (findById.isPresent()) {
				findById.get().setDelete(true);
				otherVariationOptionRepository.save(findById.get());
				return true;
			} else {
				throw new OtherVariationOptionNotFoundException(INVALID_OTHER_VARIATION);
			}

		} else {
			throw new InvalidVariationTypeException(INVALID_VARIATION_TYPE);

		}
	}

	@Override
	public List<SubCategoryWrapper> getSubCategoryBySetIdForDropdown(String setId) {
		try {
			List<SubCategoryWrapper> arrayList = new ArrayList<>();
			getSubcategoryBySetId(setId).stream().forEach(e -> {
				SubCategoryWrapper subCategoryWrapper = new SubCategoryWrapper();
				BeanUtils.copyProperties(e, subCategoryWrapper);
				arrayList.add(subCategoryWrapper);
			});
			return arrayList;
		} catch (Exception e) {
			throw new CategorySetNotFoundException(INVALID_CATEGORYSET_ID);
		}
	}

	private List<SubCategoryResponsePojo> findSubCategorySetId(CategorySet fetchedSet,
			SubCategoryFilterPojo filterData) {
		List<SubCategoryResponsePojo> pojoList = null;
		if (fetchedSet != null) {
			pojoList = new ArrayList<>();
			if (filterData.getFromDate() != null && filterData.getToDate() != null) {
				List<SubCategory> subCatList = subCategoryRepository
						.findBySetIdAndisDisable(fetchedSet.getCategorySetId(), false);
				List<SubCategory> resultSubCatList = subCatList.stream()
						.filter(f -> filterData.getFromDate().isBefore(f.getCreatedAt())
								&& filterData.getToDate().isAfter(f.getCreatedAt()))
						.collect(Collectors.toList());
				pojoList.addAll(subCategoryResponse(resultSubCatList));

			} else {
				List<SubCategory> subCatList = subCategoryRepository
						.findBySetIdAndisDisable(fetchedSet.getCategorySetId(), false);
				pojoList.addAll(subCategoryResponse(subCatList));
			}
		}
		return pojoList;
	}

	@Override
	public List<SubCategoryResponsePojo> getFilteredSubCategory(SubCategoryFilterPojo filterData, int pageNumber,
			int pageSize) {

		List<ECategory> commissionModeList = filterData.getCommissionModeList();
		List<String> mainCategoryList = filterData.getMainCategoryList();
		List<String> setList = filterData.getSetList();
		List<String> subCategoryList = filterData.getSubCategoryList();
		List<SubCategoryResponsePojo> pojoList = null;

		if (!subCategoryList.isEmpty()) {
			pojoList = new ArrayList<>();
			if (filterData.getFromDate() != null && filterData.getToDate() != null) {
				for (String id : subCategoryList) {
					SubCategory getSubCategory = subCategoryRepository.findBySubCategoryIdAndIsDisable(id, false);
					if (getSubCategory != null && (filterData.getToDate().isAfter(getSubCategory.getCreatedAt())
							&& filterData.getFromDate().isBefore(getSubCategory.getCreatedAt()))) {
						pojoList.add(getSubCategory(getSubCategory.getSubCategoryId()));
					}
				}
			} else {
				for (String id : subCategoryList) {
					SubCategory getSubCategory = subCategoryRepository.findBySubCategoryIdAndIsDisable(id, false);
					if (getSubCategory != null) {
						pojoList.add(getSubCategory(getSubCategory.getSubCategoryId()));
					}
				}
			}
			return getPaginatedResponse(pageNumber, pageSize, pojoList);
		}
		if (!setList.isEmpty()) {
			pojoList = new ArrayList<>();
			for (String id : setList) {
				CategorySet fetchedSet = categorySetRepository.findByCategorySetIdAndIsDisabled(id, false);
				pojoList.addAll(findSubCategorySetId(fetchedSet, filterData));
			}
			return getPaginatedResponse(pageNumber, pageSize, pojoList);
		}
		if (!mainCategoryList.isEmpty()) {
			pojoList = new ArrayList<>();
			for (String id : mainCategoryList) {
				Optional<MainCategory> findById = mainCategoryRepository.findById(id);
				if (findById.isPresent()) {
					List<CategorySet> findByMainCategoryIdAndIsDisabled = categorySetRepository
							.findByMainCategoryIdAndIsDisabled(findById.get().getMainCategoryId(), false);
					if (!findByMainCategoryIdAndIsDisabled.isEmpty()) {
						for (CategorySet categorySet : findByMainCategoryIdAndIsDisabled) {
							pojoList.addAll(findSubCategorySetId(categorySet, filterData));
						}
					}
				}
			}
			return getPaginatedResponse(pageNumber, pageSize, pojoList);
		}
		if (!commissionModeList.isEmpty()) {
			pojoList = new ArrayList<>();
			for (ECategory commissionMode : commissionModeList) {
				List<MainCategory> getMainCategoryList = mainCategoryRepository
						.findByCommissionTypeAndIsDisabled(commissionMode, false);
				for (MainCategory category : getMainCategoryList) {
					List<CategorySet> categorySetList = categorySetRepository
							.findByMainCategoryIdAndIsDisabled(category.getMainCategoryId(), false);
					for (CategorySet set : categorySetList) {
						pojoList.addAll(findSubCategorySetId(set, filterData));
					}
				}
			}
			return getPaginatedResponse(pageNumber, pageSize, pojoList);
		}
		return getPaginatedResponse(pageNumber, pageSize, pojoList);
	}

	private List<SubCategoryResponsePojo> subCategoryResponse(List<SubCategory> subCategoryList) {
		List<SubCategoryResponsePojo> resultList = new ArrayList<>();
		subCategoryList.forEach(e -> {
			resultList.add(getSubCategory(e.getSubCategoryId()));
		});
		return resultList;
	}

	private List<SubCategoryResponsePojo> getPaginatedResponse(int pageNumber, int pageSize,
			List<SubCategoryResponsePojo> responseList) {
		if (pageNumber == 0) {
			return responseList.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return responseList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	/* Drop Down Selected Data based service methods starts */
	@Override
	public List<MainCategoryWrapper> getMainCategoryByCommissionMode(ECategory commissionMode) {
		try {
			List<MainCategory> getAllMainCategory = mainCategoryRepository
					.findByCommissionTypeAndIsDisabled(commissionMode, false);
			if (!getAllMainCategory.isEmpty()) {
				List<MainCategoryWrapper> wrapperList = new ArrayList<>();
				for (MainCategory mainCategory : getAllMainCategory) {
					MainCategoryWrapper wrapper = new MainCategoryWrapper();
					BeanUtils.copyProperties(mainCategory, wrapper);
					wrapperList.add(wrapper);
				}
				return wrapperList;
			} else {
				throw new MainCategoryEmptyListException(EMPTY_MAIN_CATEGORY_LIST);
			}
		} catch (MainCategoryEmptyListException e) {
			throw e;
		} catch (Exception e) {
			throw new MainCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<CategorySetWrapper> getSetsByMainCategoryId(String mainCategoryId) {
		try {
			List<CategorySet> getAllCategorySet = categorySetRepository
					.findByMainCategoryIdAndIsDisabled(mainCategoryId, false);
			if (getAllCategorySet.isEmpty()) {
				List<CategorySetWrapper> wrapperList = new ArrayList<>();
				for (CategorySet categorySet : getAllCategorySet) {
					CategorySetWrapper wrapper = new CategorySetWrapper();
					BeanUtils.copyProperties(categorySet, wrapper);
					wrapperList.add(wrapper);
				}
				return wrapperList;
			} else {
				throw new CategorySetEmptyListException("Category Set List Is Empty");
			}
		} catch (CategorySetEmptyListException e) {
			throw e;
		} catch (Exception e) {
			throw new CategorySetException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SubCategoryWrapper> getAllSubCategoryBySetId(String setId) {
		try {
			List<SubCategory> getAllSubCategory = subCategoryRepository.findBySetIdAndisDisable(setId, false);
			if (!getAllSubCategory.isEmpty()) {
				List<SubCategoryWrapper> wrapperLIst = new ArrayList<>();
				for (SubCategory subCategory : getAllSubCategory) {
					SubCategoryWrapper wrapper = new SubCategoryWrapper();
					BeanUtils.copyProperties(subCategory, wrapper);
					wrapperLIst.add(wrapper);
				}
				return wrapperLIst;
			} else {
				throw new SubCategoryEmptyListException(EMPTY_CATEGORY_SET_LIST);
			}
		} catch (SubCategoryEmptyListException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<StandardOthersVariationWrapper> getAllStandardAndOtherVariationBySubId(String subCategoryId) {
		try {
			SubCategory getSubCategory = subCategoryRepository.findBySubCategoryIdAndIsDisable(subCategoryId, false);
			if (getSubCategory != null) {
				SubCategoryResponsePojo subCategory = getSubCategory(subCategoryId);
				List<StandardOthersVariationWrapper> wrapperList = new ArrayList<>();
				List<StandardVariationResponsePojo> standardVariationList = subCategory.getStandardVariationList();
				List<OtherVariationPojo> othersVariationList = subCategory.getOthersVariationList();

				for (StandardVariationResponsePojo standardVariationPojo : standardVariationList) {
					StandardOthersVariationWrapper wrapper = new StandardOthersVariationWrapper();
					wrapper.setVariationId(standardVariationPojo.getStandardVariationId());
					wrapper.setVariationName(standardVariationPojo.getVariationName());
					wrapper.setVariationType(standardVariationPojo.getVariationType());
					wrapperList.add(wrapper);
				}
				for (OtherVariationPojo otherVariationPojo : othersVariationList) {
					StandardOthersVariationWrapper wrapper = new StandardOthersVariationWrapper();
					wrapper.setVariationId(otherVariationPojo.getOtherVariationId());
					wrapper.setVariationName(otherVariationPojo.getVariationName());
					wrapper.setVariationType(otherVariationPojo.getVariationType());
					wrapperList.add(wrapper);
				}
				return wrapperList;
			} else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		} catch (SubCategoryIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	/* Drop Down Selected Data based service methods ends */

	@Override
	public List<SubCategoryWrapper> getSubcategoryByMainCategoryId(String mainCategoryId) {
		try {
			List<CategorySet> categorySets = categorySetRepository.findByMainCategoryIdAndIsDisabled(mainCategoryId,
					false);

			if (categorySets.isEmpty()) {
				throw new MainCategoryNotFoundException(MAIN_CATEGORY_ID_NOT_FOUND);
			} else {
				List<String> list = categorySets.stream().map(CategorySet::getCategorySetId)
						.collect(Collectors.toList());

				List<SubCategory> subCategories = subCategoryRepository.findBySetIdIn(list);
				return subCategories.stream()
						.map(e -> new SubCategoryWrapper(e.getSubCategoryId(), e.getSubCategoryName()))
						.collect(Collectors.toList());
			}
		} catch (MainCategoryNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public CategorySetAndMainCategoryWrapper getSetAndMainBySubId(String subCategoryId) {
		try {
			SubCategory subCategory = subCategoryRepository.findBySubCategoryIdAndIsDisable(subCategoryId, false);
			if(subCategory!=null) {
				CategorySet categorySet = categorySetRepository.findByCategorySetIdAndIsDisabled(subCategory.getSetId(), false);
				MainCategory mainCategory = mainCategoryRepository.findByMainCategoryIdAndIsDisabled(categorySet.getMainCategoryId(), false);
				CategorySetAndMainCategoryWrapper wrapper = new CategorySetAndMainCategoryWrapper();
				wrapper.setMainCategoryId(mainCategory.getMainCategoryId());
				wrapper.setMainCategoryName(mainCategory.getMainCategoryName());
				wrapper.setCategorySetId(categorySet.getCategorySetId());
				wrapper.setCategorySetName(categorySet.getSetName());
				return wrapper;
			}else {
				throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
			}
		}catch(SubCategoryIdNotFoundException e) {
			throw e;
		}catch(Exception e) {
			throw new SubCategoryException(SOMETHING_WENT_WRONG);
		}
	}
}
