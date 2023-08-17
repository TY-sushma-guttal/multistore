package com.mrmrscart.productcategoriesservice.service.category;

import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_NOT_FOUND;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.CATEGORY_DISABLED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.CATEGORY_ENABLED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.COMMISSION_TYPE;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.CREATED_AT;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.ENABLE_SET_FAILED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.ENABLE_SUBCATEGORY_FAILED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.INVALID_FIXED_OR_ZERO_COMMISSION_FIELD_INITIALIZATION;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_GET_FAIL_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_NAME;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_SAVE_FAIL_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.SET_DISABLED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.SET_ENABLED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.SUBCATEGORY_DISABLED;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.SUBCATEGORY_ENABLED;
import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.INVALID_SUBCATEGORY_ID;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.entity.category.CategoryPriceRange;
import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EPriceRange;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;
import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.EnableSetException;
import com.mrmrscart.productcategoriesservice.exception.category.EnableSubCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.EqualPriceAndMaxPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.EqualPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.FixedOrZeroCommissionException;
import com.mrmrscart.productcategoriesservice.exception.category.InvalidVariationTypeException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategorySaveException;
import com.mrmrscart.productcategoriesservice.exception.category.MinPriceAndEqualPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.PriceRangeListNotFound;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.PriceRange;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationDisablePojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategoryPriceRangeRepository;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.OtherVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.category.SubCategoryRepository;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategoryStatusInfo;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryFilterWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;

@Service
public class MainCategoryServiceImpl implements MainCategoryService {

	@Autowired
	private CategoryPriceRangeRepository categoryPriceRangeRepository;

	@Autowired
	private MainCategoryRepository mainCategoryRepository;

	@Autowired
	private CategorySetRepository categorySetRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private OtherVariationRepository otherVariationRepository;
	@Autowired
	private OtherVariationOptionRepository otherVariationOptionRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public CategoryPriceRange saveCategoryPriceRange(List<PriceRange> priceRanges) {
		CategoryPriceRange categoryPriceRange = new CategoryPriceRange();
		categoryPriceRange.setPriceRange(priceRanges);
		return categoryPriceRangeRepository.save(categoryPriceRange);
	}

	public boolean priceComparission(BigDecimal minEndPrice, BigDecimal equalStartPrice, BigDecimal equalEndPrice,
			BigDecimal maxStartPrice) {
		try {
			if (!(equalStartPrice.compareTo(minEndPrice) == 1)) {
				throw new MinPriceAndEqualPriceException(
						"Min start price is greater than Equal start price or vice versa. ");
			} else if (!(equalEndPrice.compareTo(equalStartPrice) == 1)) {
				throw new EqualPriceException("Equal start price is less that equal end price. ");
			} else if (!(maxStartPrice.compareTo(equalEndPrice) == 1)) {
				throw new EqualPriceAndMaxPriceException(
						"Equal end price is greater that Max start price or vice versa. ");
			} else {
				return true;
			}
		} catch (MinPriceAndEqualPriceException | EqualPriceException | EqualPriceAndMaxPriceException e1) {
			throw e1;
		} catch (Exception e) {
			throw new PriceRangeListNotFound("Something went with the price comparission. ");
		}
	}

	public boolean priceRangeObjCheck(List<PriceRange> priceRangeList) {
		try {
			int count = 0;
			BigDecimal minEndPrice = null;
			BigDecimal equalStartPrice = null;
			BigDecimal equalEndPrice = null;
			BigDecimal maxStartPrice = null;
			for (PriceRange priceRange : priceRangeList) {
				if (priceRange.getPriceRangeType().equals(EPriceRange.MIN)
						&& priceRange.getPriceStart().compareTo(new BigDecimal(0)) == 0) {
					minEndPrice = priceRange.getPriceEnd();
					count++;
				}
				if (priceRange.getPriceRangeType().equals(EPriceRange.EQUAL)) {
					equalStartPrice = priceRange.getPriceStart();
					equalEndPrice = priceRange.getPriceEnd();
					count++;
				}
				if (priceRange.getPriceRangeType().equals(EPriceRange.MAX) && priceRange.getPriceEnd() == null) {
					maxStartPrice = priceRange.getPriceStart();
					count++;
				}
			}

			if (priceRangeList.size() == count
					&& priceComparission(minEndPrice, equalStartPrice, equalEndPrice, maxStartPrice)) {
				return true;
			} else {
				throw new PriceRangeListNotFound("Invalid price range type. ");
			}
		} catch (PriceRangeListNotFound e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PriceRangeListNotFound("Something went wrong... ");
		}
	}

	public boolean commissionModeCheck(PriceRange priceRange, MainCategoryPojo mainCategory) {
		boolean flag = false;
		System.err.println(mainCategory.getCommissionType());
		if (mainCategory.getCommissionType().equals(ECategory.ZERO_COMMISSION)
				&& (priceRange.getAdminProfitPercentage() != null
						|| priceRange.getStoreOwnerProfitPercentage() != null)) {
			flag = true;
		}
		if (mainCategory.getCommissionType().equals(ECategory.FIXED_COMMISSION)
				&& ((priceRange.getAdminProfitPercentage().doubleValue()
						+ priceRange.getStoreOwnerProfitPercentage().doubleValue() != 100))) {
			flag = true;
		}
		return flag;
	}

	@Override
	public MainCategory addMainCategory(MainCategoryPojo mainCategory) {

		try {
			List<PriceRange> priceRangeList = mainCategory.getPriceRangeList();
			MainCategory category = new MainCategory();
			CategoryPriceRange save = null;
			boolean flag = false;
			if (!priceRangeList.isEmpty()) {
				for (PriceRange priceRange : priceRangeList) {
					flag = commissionModeCheck(priceRange, mainCategory);
					if (flag) {
						break;
					}
				}
				if (!flag && priceRangeObjCheck(priceRangeList)) {
					save = saveCategoryPriceRange(priceRangeList);
					category.setPriceRangeId(save.getPriceRangeId());
				} else {
					throw new FixedOrZeroCommissionException(INVALID_FIXED_OR_ZERO_COMMISSION_FIELD_INITIALIZATION);
				}
			} else {
				category.setPriceRangeId(null);
			}
			BeanUtils.copyProperties(mainCategory, category);
			category.setMainCategoryCode("C0" + mainCategoryRepository.findAll().size());
			return mainCategoryRepository.save(category);
		} catch (FixedOrZeroCommissionException | PriceRangeListNotFound e) {
			throw e;

		} catch (MainCategorySaveException e) {
			throw new MainCategorySaveException(MAIN_CATEGORY_SAVE_FAIL_MESSAGE);
		}
	}

	@Override
	public MainCategory updateMainCategory(MainCategoryPojo mainCategory) {

		try {
			Optional<MainCategory> findById = mainCategoryRepository.findById(mainCategory.getMainCategoryId());

			if (findById.isPresent()) {
				if (findById.get().getPriceRangeId() != null) {
					Optional<CategoryPriceRange> categoryPriceRange = categoryPriceRangeRepository
							.findById(findById.get().getPriceRangeId());
					boolean flag = false;
					if (categoryPriceRange.isPresent() && !mainCategory.getPriceRangeList().isEmpty()) {
						for (PriceRange priceRange : mainCategory.getPriceRangeList()) {
							flag = commissionModeCheck(priceRange, mainCategory);
							if (flag) {
								break;
							}
						}
						if (!flag && priceRangeObjCheck(mainCategory.getPriceRangeList())) {
							categoryPriceRange.get().setPriceRange(mainCategory.getPriceRangeList());
							CategoryPriceRange save = categoryPriceRangeRepository.save(categoryPriceRange.get());
							findById.get().setPriceRangeId(save.getPriceRangeId());
						} else {
							throw new FixedOrZeroCommissionException("Fixed or zero commission exception. ");
						}
						BeanUtils.copyProperties(mainCategory, findById.get(), "createdAt");
						return mainCategoryRepository.save(findById.get());

					} else {
						BeanUtils.copyProperties(mainCategory, findById.get());
						return mainCategoryRepository.save(findById.get());
					}
				} else {
					boolean flag = false;
					if (!mainCategory.getPriceRangeList().isEmpty()) {
						CategoryPriceRange categoryPriceRange = new CategoryPriceRange();
						for (PriceRange priceRange : mainCategory.getPriceRangeList()) {
							flag = commissionModeCheck(priceRange, mainCategory);
							if (flag) {
								break;
							}
						}
						if (!flag && priceRangeObjCheck(mainCategory.getPriceRangeList())) {
							categoryPriceRange.setPriceRange(mainCategory.getPriceRangeList());
							CategoryPriceRange save = categoryPriceRangeRepository.save(categoryPriceRange);
							findById.get().setPriceRangeId(save.getPriceRangeId());
						} else {
							throw new FixedOrZeroCommissionException("Fixed or zero commission exception. ");
						}
						BeanUtils.copyProperties(mainCategory, findById.get(), "createdAt");
						return mainCategoryRepository.save(findById.get());

					} else {
						throw new PriceRangeListNotFound("Price range list not found. ");
					}
				}
			} else {
				throw new MainCategoryNotFoundException(MAIN_CATEGORY_SAVE_FAIL_MESSAGE);
			}
		} catch (FixedOrZeroCommissionException | MainCategoryNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MainCategorySaveException("Something went wrong. ");
		}
	}

	@Override
	public List<MainCategoryPojo> getAllEnabledMainCategory(int pageNumber, int pageSize) {
		return getAllMainCategory(pageNumber, pageSize).stream().filter(e -> !e.isDisabled())
				.collect(Collectors.toList());

	}

	@Override
	public MainCategoryPojo getMainCategory(String mainCategoryId) {
		Optional<MainCategory> findById = mainCategoryRepository.findById(mainCategoryId);
		if (findById.isPresent()) {
			MainCategoryPojo mainCategoryPojo = new MainCategoryPojo();

			BeanUtils.copyProperties(findById.get(), mainCategoryPojo);

			Optional<CategoryPriceRange> priceRange = categoryPriceRangeRepository
					.findById(findById.get().getPriceRangeId());

			if (priceRange.isPresent()) {
				mainCategoryPojo.setPriceRangeList(priceRange.get().getPriceRange());

				return mainCategoryPojo;
			} else {
				return mainCategoryPojo;
			}
		} else {
			throw new MainCategoryNotFoundException(MAIN_CATEGORY_GET_FAIL_MESSAGE);

		}
	}

	@Transactional
	@Override
	public String disableCategory(CategoryStatusInfo categoryStatusInfo) {

		try {
			if (categoryStatusInfo.getCategoryType() == ECategory.CATEGORY) {

				return disableMainCategory(categoryStatusInfo.getCategoryId(), categoryStatusInfo.isStatus());

			} else if (categoryStatusInfo.getCategoryType() == ECategory.SET) {

				return disableSet(categoryStatusInfo.getCategoryId(), categoryStatusInfo.isStatus());

			} else if (categoryStatusInfo.getCategoryType() == ECategory.SUB_CATEGORY) {

				return disableSubCategory(categoryStatusInfo.getCategoryId(), categoryStatusInfo.isStatus());

			} else {
				throw new InvalidVariationTypeException("Provide a valid category type");

			}
		} catch (MainCategoryNotFoundException | CategorySetNotFoundException | SubCategoryIdNotFoundException
				| EnableSubCategoryException | EnableSetException | InvalidVariationTypeException e) {
			throw e;

		} catch (Exception e) {
			throw e;
		}
	}

	private String disableMainCategory(String categoryId, boolean status) {

		Optional<MainCategory> findById = mainCategoryRepository.findById(categoryId);

		if (findById.isPresent()) {

			findById.get().setDisabled(status);
			findById.get().setLastUpdatedBy("");
			findById.get().setLastUpdatedAt(LocalDateTime.now());
			mainCategoryRepository.save(findById.get());

			List<CategorySet> setList = categorySetRepository.findByMainCategoryId(findById.get().getMainCategoryId());

			if (!setList.isEmpty()) {

				setList.forEach(e -> {
					e.setDisabled(status);
					e.setLastUpdatedBy("");
					e.setLastUpdatedAt(LocalDateTime.now());
					categorySetRepository.save(e);

					List<SubCategory> subCategoryList = subCategoryRepository.findBySetId(e.getCategorySetId());

					if (!subCategoryList.isEmpty()) {
						subCategoryList.forEach(f -> {
							f.setDisable(status);
							List<SubCategoryStandardVariation> arrayList = new ArrayList<>();
							f.getStandardVariationList().stream().forEach(g -> {
								g.setDisable(status);

								List<SubCategoryVariationOption> list = new ArrayList<>();
								g.getOptionList().stream().forEach(h -> {
									h.setDisable(status);
									list.add(h);
								});
								g.setOptionList(list);
								arrayList.add(g);
							});
							f.setStandardVariationList(arrayList);
							f.setLastUpdatedBy("");
							f.setLastUpdatedAt(LocalDateTime.now());
							SubCategory save = subCategoryRepository.save(f);

							Optional<List<OtherVariation>> otherVariation = Optional
									.of(otherVariationRepository.findBySubCategoryId(save.getSubCategoryId()));

							if (otherVariation.isPresent()) {
								List<OtherVariation> variationList = new ArrayList<>();
								otherVariation.get().stream().forEach(i -> {
									i.setDisable(status);
									i.setLastUpdatedBy("");
									i.setLastUpdatedAt(LocalDateTime.now());
									Optional<List<OtherVariationOption>> variationOption = Optional
											.of(otherVariationOptionRepository
													.findByOtherVariationId(i.getOtherVariationId()));
									if (variationOption.isPresent()) {
										List<OtherVariationOption> optionList = new ArrayList<>();
										variationOption.get().stream().forEach(j -> {
											j.setDisable(status);
											j.setLastUpdatedBy("");
											j.setLastUpdatedAt(LocalDateTime.now());
											optionList.add(j);
										});
										otherVariationOptionRepository.saveAll(optionList);
									}
									variationList.add(i);
								});
								otherVariationRepository.saveAll(variationList);
							}

						});
					}

				});

			} else {
				return status ? CATEGORY_DISABLED : CATEGORY_ENABLED;
			}
			return status ? CATEGORY_DISABLED : CATEGORY_ENABLED;
		} else {
			throw new MainCategoryNotFoundException(MAIN_CATEGORY_GET_FAIL_MESSAGE);
		}

	}

	private String disableSet(String setId, boolean status) {

		Optional<CategorySet> findById = categorySetRepository.findById(setId);
		if (findById.isPresent()) {
			if (!status) {
				MainCategory mainCategory = mainCategoryRepository.findById(findById.get().getMainCategoryId())
						.orElse(null);
				if (mainCategory != null) {
					if (mainCategory.isDisabled()) {
						throw new EnableSetException(ENABLE_SET_FAILED);
					}
				} else {
					throw new MainCategoryNotFoundException(MAIN_CATEGORY_GET_FAIL_MESSAGE);
				}

			}
			findById.get().setDisabled(status);
			findById.get().setLastUpdatedBy("");
			findById.get().setLastUpdatedAt(LocalDateTime.now());
			categorySetRepository.save(findById.get());

			List<SubCategory> subCategoryList = subCategoryRepository.findBySetId(setId);

			if (!subCategoryList.isEmpty()) {

				subCategoryList.forEach(f -> {
					f.setDisable(status);
					List<SubCategoryStandardVariation> arrayList = new ArrayList<>();
					f.getStandardVariationList().stream().forEach(g -> {
						g.setDisable(status);

						List<SubCategoryVariationOption> list = new ArrayList<>();
						g.getOptionList().stream().forEach(h -> {
							h.setDisable(status);
							list.add(h);
						});
						g.setOptionList(list);
						arrayList.add(g);
					});
					f.setStandardVariationList(arrayList);
					f.setLastUpdatedBy("");
					f.setLastUpdatedAt(LocalDateTime.now());
					SubCategory save = subCategoryRepository.save(f);

					Optional<List<OtherVariation>> otherVariation = Optional
							.of(otherVariationRepository.findBySubCategoryId(save.getSubCategoryId()));

					if (otherVariation.isPresent()) {
						List<OtherVariation> variationList = new ArrayList<>();
						otherVariation.get().stream().forEach(i -> {
							i.setDisable(status);
							i.setLastUpdatedBy("");
							i.setLastUpdatedAt(LocalDateTime.now());
							Optional<List<OtherVariationOption>> variationOption = Optional
									.of(otherVariationOptionRepository.findByOtherVariationId(i.getOtherVariationId()));
							if (variationOption.isPresent()) {
								List<OtherVariationOption> optionList = new ArrayList<>();
								variationOption.get().stream().forEach(j -> {
									j.setDisable(status);
									j.setLastUpdatedBy("");
									j.setLastUpdatedAt(LocalDateTime.now());
									optionList.add(j);
								});
								otherVariationOptionRepository.saveAll(optionList);
							}
							variationList.add(i);
						});
						otherVariationRepository.saveAll(variationList);
					}

				});

			} else {
				return status ? SET_DISABLED : SET_ENABLED;
			}

			return status ? SET_DISABLED : SET_ENABLED;

		} else {
			throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
		}

	}

	private String disableSubCategory(String subCategoryId, boolean status) {

		Optional<SubCategory> findById = subCategoryRepository.findById(subCategoryId);
		if (findById.isPresent()) {
			if (!status) {
				CategorySet categorySet = categorySetRepository.findById(findById.get().getSetId()).orElse(null);
				if (categorySet != null) {
					if (categorySet.isDisabled()) {
						throw new EnableSubCategoryException(ENABLE_SUBCATEGORY_FAILED);
					}
				} else {
					throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
				}

			}
			findById.get().setDisable(status);
			List<SubCategoryStandardVariation> standardVariationList = new ArrayList<>();
			findById.get().getStandardVariationList().stream().forEach(g -> {
				g.setDisable(status);

				List<SubCategoryVariationOption> list = new ArrayList<>();
				g.getOptionList().stream().forEach(h -> {
					h.setDisable(status);
					list.add(h);
				});
				g.setOptionList(list);
				standardVariationList.add(g);
			});

			findById.get().setStandardVariationList(standardVariationList);
			findById.get().setLastUpdatedBy("");
			findById.get().setLastUpdatedAt(LocalDateTime.now());
			SubCategory save = subCategoryRepository.save(findById.get());

			Optional<List<OtherVariation>> otherVariation = Optional
					.of(otherVariationRepository.findBySubCategoryId(save.getSubCategoryId()));

			if (otherVariation.isPresent()) {
				List<OtherVariation> variationList = new ArrayList<>();
				otherVariation.get().stream().forEach(i -> {
					i.setDisable(status);
					i.setLastUpdatedBy("");
					i.setLastUpdatedAt(LocalDateTime.now());
					Optional<List<OtherVariationOption>> variationOption = Optional
							.of(otherVariationOptionRepository.findByOtherVariationId(i.getOtherVariationId()));
					if (variationOption.isPresent()) {
						List<OtherVariationOption> optionList = new ArrayList<>();
						variationOption.get().stream().forEach(j -> {
							j.setDisable(status);
							j.setLastUpdatedBy("");
							j.setLastUpdatedAt(LocalDateTime.now());
							optionList.add(j);
						});
						otherVariationOptionRepository.saveAll(optionList);
					}
					variationList.add(i);
				});
				otherVariationRepository.saveAll(variationList);
			}
			return status ? SUBCATEGORY_DISABLED : SUBCATEGORY_ENABLED;

		} else {
			throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
		}
	}

	@Transactional
	@Override
	public String disableVariation(VariationDisablePojo variationDisablePojo) {

		try {
			if (variationDisablePojo.getVariationType() == ECategory.STANDARD_VARIATION) {

				SubCategory subCategory = getBySubcategoryId(variationDisablePojo.getSubcategoryId());

				List<SubCategoryVariationOption> list = new ArrayList<>();

				List<SubCategoryStandardVariation> collect = filterSubcategoryVariations(
						subCategory.getStandardVariationList(), variationDisablePojo.getVariationId());

				subCategory.getStandardVariationList().remove(collect.get(0));
				SubCategoryStandardVariation subCategoryStandardVariation = collect.get(0);
				subCategoryStandardVariation.getOptionList().stream().forEach(f -> {
					f.setDisable(variationDisablePojo.isStatus());
					list.add(f);
				});
				collect.get(0).setDisable(variationDisablePojo.isStatus());
				collect.get(0).setOptionList(list);
				subCategory.getStandardVariationList().add(collect.get(0));
				subCategory.setLastUpdatedBy("");
				subCategory.setLastUpdatedAt(LocalDateTime.now());
				subCategoryRepository.save(subCategory);
				return variationDisablePojo.isStatus() ? "Variation successfully disabled"
						: "Variation successfully enabled";

			} else if (variationDisablePojo.getVariationType() == ECategory.OTHER_VARIATION) {

				OtherVariation optional = otherVariationRepository.findBySubCategoryIdAndOtherVariationId(
						variationDisablePojo.getSubcategoryId(), variationDisablePojo.getVariationId());

				if (optional != null) {

					optional.setDisable(variationDisablePojo.isStatus());
					OtherVariation save = otherVariationRepository.save(optional);

					List<OtherVariationOption> findByOtherVariationId = findByOtherVariationId(
							save.getOtherVariationId());

					List<OtherVariationOption> list = new ArrayList<>();
					findByOtherVariationId.stream().forEach(e -> {
						e.setDisable(variationDisablePojo.isStatus());
						list.add(e);
					});
					otherVariationOptionRepository.saveAll(list);
					return variationDisablePojo.isStatus() ? "Variation successfully disabled"
							: "Variation successfully enabled";
				} else {
					throw new OtherVariationNotFoundException("Provide a valid variation id");
				}
			} else {
				throw new InvalidVariationTypeException("Provide a valid variation type");
			}
		} catch (OtherVariationNotFoundException | InvalidVariationTypeException | StandardVariationIdNotFoundException
				| OtherVariationOptionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new StandardVariationOptionNotFoundException(
					"Failed to" + variationDisablePojo.isStatus() + " the variation option");
		}
	}

	@Transactional
	@Override
	public String disableVariationOption(VariationDisablePojo variationDisablePojo) {

		try {
			if (variationDisablePojo.getVariationType() == ECategory.STANDARD_VARIATION) {

				SubCategory subCategory = getBySubcategoryId(variationDisablePojo.getSubcategoryId());

				List<SubCategoryStandardVariation> collect = filterSubcategoryVariations(
						subCategory.getStandardVariationList(), variationDisablePojo.getVariationId());

				subCategory.getStandardVariationList().remove(collect.get(0));
				collect.get(0).getOptionList().forEach(e -> {
					if (e.getOptionId().equals(variationDisablePojo.getOptionId())) {
						e.setDisable(variationDisablePojo.isStatus());
					}
				});
				collect.get(0).setDisable(variationDisablePojo.isStatus());
				subCategory.getStandardVariationList().add(collect.get(0));
				subCategory.setLastUpdatedBy("");
				subCategory.setLastUpdatedAt(LocalDateTime.now());
				subCategoryRepository.save(subCategory);
				return variationDisablePojo.isStatus() ? "Option successfully disabled" : "Option successfully enabled";

			} else if (variationDisablePojo.getVariationType() == ECategory.OTHER_VARIATION) {

				OtherVariationOption findById = getByOtherVariationOptionId(variationDisablePojo.getOptionId());

				findById.setDisable(variationDisablePojo.isStatus());
				otherVariationOptionRepository.save(findById);
				return variationDisablePojo.isStatus() ? "Option successfully disabled" : "Option successfully enabled";

			} else {
				throw new InvalidVariationTypeException("Provide a valid variation type");
			}
		} catch (SubCategoryIdNotFoundException | InvalidVariationTypeException | StandardVariationIdNotFoundException
				| OtherVariationOptionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new StandardVariationOptionNotFoundException(
					"Failed to" + variationDisablePojo.isStatus() + " the variation option");
		}

	}

	private SubCategory getBySubcategoryId(String subCategoryId) {
		Optional<SubCategory> findById = subCategoryRepository.findById(subCategoryId);
		if (findById.isPresent()) {
			return findById.get();
		} else {
			throw new SubCategoryIdNotFoundException(INVALID_SUBCATEGORY_ID);
		}
	}

	private List<SubCategoryStandardVariation> filterSubcategoryVariations(List<SubCategoryStandardVariation> list,
			String variationId) {
		List<SubCategoryStandardVariation> collect = list.stream().filter(e -> e.getVariationId().equals(variationId))
				.collect(Collectors.toList());
		if (collect.isEmpty()) {
			throw new StandardVariationIdNotFoundException("Standard option not found");
		} else {
			return collect;
		}
	}

	private OtherVariationOption getByOtherVariationOptionId(String optionId) {
		Optional<OtherVariationOption> findById = otherVariationOptionRepository.findById(optionId);
		if (!findById.isPresent()) {
			throw new OtherVariationOptionNotFoundException("Other variation option not found");
		} else {
			return findById.get();
		}
	}

	private List<OtherVariationOption> findByOtherVariationId(String otherVariationId) {
		return otherVariationOptionRepository.findByOtherVariationId(otherVariationId);
	}

	@Override
	public List<MainCategoryWrapper> getAllMainCategoryDropdown() {
		try {
			List<MainCategory> list = mainCategoryRepository.findEnabledForDropdown(false);
			List<MainCategoryWrapper> result = new ArrayList<>();
			list.stream().forEach(e -> {
				MainCategoryWrapper mainCategoryWrapper = new MainCategoryWrapper();
				BeanUtils.copyProperties(e, mainCategoryWrapper);
				result.add(mainCategoryWrapper);
			});
			return result;
		} catch (Exception e) {
			throw new MainCategoryNotFoundException(MAIN_CATEGORY_GET_FAIL_MESSAGE);
		}
	}

	@Override
	public List<MainCategoryPojo> getAllMainCategory(int pageNumber, int pageSize, MainCategoryFilterPojo filterPojo) {
		Criteria criteria = new Criteria();
		List<Criteria> orCriteria = new ArrayList<>();
		if (filterPojo.getFromDate() != null && filterPojo.getToDate() != null) {
			criteria.andOperator(Criteria.where(CREATED_AT).gt(filterPojo.getFromDate()).lt(filterPojo.getToDate()));
		}
		if (!filterPojo.getMainCategory().isEmpty()) {
			orCriteria.add(Criteria.where(MAIN_CATEGORY_NAME).in(filterPojo.getMainCategory()));
		}
		if (!filterPojo.getCommissionType().isEmpty()) {
			orCriteria.add(Criteria.where(COMMISSION_TYPE).in(filterPojo.getCommissionType()));
		}

		if (!orCriteria.isEmpty()) {
			criteria.orOperator(orCriteria.toArray(new Criteria[orCriteria.size()]));
		}
		List<MainCategory> find = mongoTemplate.find(new Query().addCriteria(criteria), MainCategory.class);
		List<MainCategory> mainCategoryPage = getMainCategoryPage(pageNumber, pageSize, find);
		List<MainCategoryPojo> result = new ArrayList<>();
		if (!mainCategoryPage.isEmpty()) {
			mainCategoryPage.forEach(e -> {
				MainCategoryPojo mainCategoryPojo = new MainCategoryPojo();
				BeanUtils.copyProperties(e, mainCategoryPojo);
				Optional<CategoryPriceRange> findById = categoryPriceRangeRepository.findById(e.getPriceRangeId());
				if (findById.isPresent()) {
					mainCategoryPojo.setPriceRangeList(findById.get().getPriceRange());
				}
				result.add(mainCategoryPojo);
			});
		}
		return result;
	}

	private List<MainCategory> getMainCategoryPage(int pageNumber, int pageSize, List<MainCategory> find) {
		if (pageNumber == 0) {
			return find.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return find.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	private List<MainCategoryPojo> getAllMainCategory(int pageNumber, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		List<MainCategoryPojo> result = new ArrayList<>();
		Page<MainCategory> findAll = mainCategoryRepository.findAll(pageRequest);
		if (!findAll.isEmpty()) {
			findAll.forEach(e -> {
				MainCategoryPojo mainCategoryPojo = new MainCategoryPojo();
				BeanUtils.copyProperties(e, mainCategoryPojo);
				if (e.getPriceRangeId() != null) {
					Optional<CategoryPriceRange> findById = categoryPriceRangeRepository.findById(e.getPriceRangeId());
					if (findById.isPresent()) {
						mainCategoryPojo.setPriceRangeList(findById.get().getPriceRange());
					}
				}
				result.add(mainCategoryPojo);
			});
			return result;
		} else {
			return result;
		}
	}

	@Override
	public MainCategoryFilterWrapper getFilterMainCategoryDropdown() {
		List<MainCategory> findAll = mainCategoryRepository.findAll();
		List<String> mainCategory = new ArrayList<>();
		List<String> commissionType = new ArrayList<>();
		commissionType.add("ZERO_COMMISSION");
		commissionType.add("FIXED_COMMISSION");
		findAll.forEach(e -> {
			mainCategory.add(e.getMainCategoryName());
		});
		return new MainCategoryFilterWrapper(mainCategory, commissionType);
	}
}
