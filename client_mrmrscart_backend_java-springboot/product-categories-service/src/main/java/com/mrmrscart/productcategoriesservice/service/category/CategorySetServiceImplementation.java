package com.mrmrscart.productcategoriesservice.service.category;

import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.*;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.INVALID_CATEGORY_SET_ID;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.INVALID_MAIN_CATEGORY_ID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilter;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilterResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetPojo;
import com.mrmrscart.productcategoriesservice.repository.category.CategorySetRepository;
import com.mrmrscart.productcategoriesservice.repository.category.MainCategoryRepository;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetResponseWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;

@Service
public class CategorySetServiceImplementation implements CategorySetService {

	@Autowired
	private CategorySetRepository categorySetRepository;

	@Autowired
	private MainCategoryRepository mainCategoryRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public CategorySet saveCategorySet(CategorySetPojo categorySetPojo) {
		Optional<MainCategory> findById = mainCategoryRepository.findById(categorySetPojo.getMainCategoryId());
		if (findById.isPresent()) {
			CategorySet categorySet = new CategorySet();
			BeanUtils.copyProperties(categorySetPojo, categorySet);

			return categorySetRepository.save(categorySet);
		} else {
			throw new MainCategoryNotFoundException(INVALID_MAIN_CATEGORY_ID);
		}

	}

	@Override
	public CategorySet updateCategorySet(CategorySetPojo categorySetPojo) {
		Optional<CategorySet> findById = categorySetRepository.findById(categorySetPojo.getCategorySetId());
		if (findById.isEmpty()) {
			throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
		} else {
			CategorySet categorySet = findById.get();
			BeanUtils.copyProperties(categorySetPojo, categorySet, "categorySetId", "createdAt", "createdBy",
					"mainCategoryId", "isDisabled");
			return categorySetRepository.save(categorySet);
		}
	}

	@Override
	public CategorySet getCategorySet(String categorySetId) {
		Optional<CategorySet> findById = categorySetRepository.findById(categorySetId);
		if (findById.isPresent()) {
			return findById.get();
		} else {
			throw new CategorySetNotFoundException(INVALID_MAIN_CATEGORY_ID);
		}
	}

	@Override
	public List<CategorySet> getAllCategorySetsByMainCategoryId(String mainCategoryId) {
		Optional<MainCategory> findById = mainCategoryRepository.findById(mainCategoryId);
		if (findById.isPresent()) {
			return categorySetRepository.findByMainCategoryId(mainCategoryId);
		} else {
			throw new MainCategoryNotFoundException(INVALID_MAIN_CATEGORY_ID);
		}
	}

	@Override
	public List<CategorySet> getEnabledCategorySets(String mainCategoryId) {
		Optional<MainCategory> findById = mainCategoryRepository.findById(mainCategoryId);
		if (findById.isPresent()) {
			return categorySetRepository.findByMainCategoryIdAndIsDisabled(mainCategoryId, false);
		} else {
			throw new MainCategoryNotFoundException(INVALID_MAIN_CATEGORY_ID);
		}
	}

	@Override
	public List<CategorySetWrapper> getEnabledCategorySetsForDropdown(String mainCategoryId) {
		try {
			List<CategorySet> enabledCategorySets = getEnabledCategorySets(mainCategoryId);
			List<CategorySetWrapper> arrayList = new ArrayList<>();
			enabledCategorySets.stream().forEach(e -> {
				CategorySetWrapper categorySetWrapper = new CategorySetWrapper();
				BeanUtils.copyProperties(e, categorySetWrapper);
				arrayList.add(categorySetWrapper);
			});
			return arrayList;
		} catch (Exception e) {
			throw new MainCategoryNotFoundException(INVALID_CATEGORY_SET_ID);

		}
	}

	@Override
	public List<CategorySetResponseWrapper> getAllCategorySets(int pageNumber, int pageSize, LocalDateTime fromDate,
			LocalDateTime toDate, CategorySetFilter filterPojo) {

		List<String> categorySet = filterPojo.getCategorySet();
		List<String> mainCategory = filterPojo.getMainCategory();

		try {

			if (categorySet.isEmpty() && mainCategory.isEmpty()) {

				return findAllSets(pageNumber, pageSize, fromDate, toDate);

			} else if (!mainCategory.isEmpty() && (!categorySet.isEmpty())) {

				return findMainCategoryAndSetByFilter(mainCategory, categorySet, pageNumber, pageSize, fromDate,
						toDate);

			} else if (!mainCategory.isEmpty()) {

				return findMainCategoryByFilter(pageNumber, pageSize, mainCategory, fromDate, toDate);

			} else if (!categorySet.isEmpty()) {

				return findCategorySetByFilter(categorySet, pageNumber, pageSize, fromDate, toDate);

			} else {
				throw new CategorySetNotFoundException(INVALID_SET_FILTER);
			}
		} catch (CategorySetNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new CategorySetNotFoundException(INVALID_SET_FILTER);
		}
	}

	private List<CategorySetResponseWrapper> findAllSets(int pageNumber, int pageSize, LocalDateTime fromDate,
			LocalDateTime toDate) {
		List<CategorySetResponseWrapper> result = new ArrayList<>();

		List<MainCategory> enabledCategory = mainCategoryRepository.findEnabledForDropdown(false);

		if (enabledCategory.isEmpty()) {
			return result;
		} else {
			enabledCategory.forEach(e -> {

				if (fromDate == null && toDate == null) {
					List<CategorySet> categorySets = categorySetRepository
							.findByMainCategoryIdAndIsDisabled(e.getMainCategoryId(), false);
					result.addAll(categorySetResponse(e, categorySets));
				} else {
					List<CategorySet> categorySets = categorySetRepository
							.findByMainCategoryIdAndIsDisabledAndCreatedAtBetween(e.getMainCategoryId(), false,
									fromDate, toDate);
					result.addAll(categorySetResponse(e, categorySets));
				}

			});
			return getPaginatedResponse(pageNumber, pageSize, result);
		}
	}

	private List<CategorySetResponseWrapper> findMainCategoryAndSetByFilter(List<String> mainCategory,
			List<String> categorySet, int pageNumber, int pageSize, LocalDateTime fromDate, LocalDateTime toDate) {
		List<CategorySetResponseWrapper> result = new ArrayList<>();
		List<MainCategory> filteredCategory = findFilteredCategory(mainCategory);

		if (!filteredCategory.isEmpty()) {

			List<CategorySet> filterSet = filterSet(categorySet, fromDate, toDate);

			filteredCategory.forEach(e -> result.addAll(categorySetResponse(e, filterSet)));
			return getPaginatedResponse(pageNumber, pageSize, result);

		} else {
			return result;
		}
	}

	private List<CategorySetResponseWrapper> findMainCategoryByFilter(int pageNumber, int pageSize,
			List<String> mainCategory, LocalDateTime fromDate, LocalDateTime toDate) {
		List<CategorySetResponseWrapper> result = new ArrayList<>();

		List<MainCategory> filteredCategory = findFilteredCategory(mainCategory);
		if (filteredCategory.isEmpty()) {
			return result;
		} else {
			filteredCategory.forEach(e -> {
				List<CategorySet> categorySets = new ArrayList<>();
				if (fromDate != null && toDate != null) {
					categorySets = categorySetRepository.findByMainCategoryIdAndIsDisabledAndCreatedAtBetween(
							e.getMainCategoryId(), false, fromDate, toDate);
				} else {
					categorySets = categorySetRepository.findByMainCategoryIdAndIsDisabled(e.getMainCategoryId(),
							false);
				}
				result.addAll(categorySetResponse(e, categorySets));

			});

			return getPaginatedResponse(pageNumber, pageSize, result);
		}
	}

	private List<CategorySetResponseWrapper> findCategorySetByFilter(List<String> categorySet, int pageNumber,
			int pageSize, LocalDateTime fromDate, LocalDateTime toDate) {
		List<CategorySetResponseWrapper> result = new ArrayList<>();
		List<CategorySet> filterSet = filterSet(categorySet, fromDate, toDate);
		if (filterSet.isEmpty()) {
			return result;
		} else {
			filterSet.forEach(g -> {
				MainCategory mainCategories = mainCategoryRepository
						.findByMainCategoryIdAndIsDisabled(g.getMainCategoryId(), false);
				if (mainCategories != null) {
					CategorySetResponseWrapper categorySetResponseWrapper = new CategorySetResponseWrapper();
					BeanUtils.copyProperties(g, categorySetResponseWrapper);
					categorySetResponseWrapper.setMainCategoryName(mainCategories.getMainCategoryName());
					result.add(categorySetResponseWrapper);
				}
			});
			return getPaginatedResponse(pageNumber, pageSize, result);
		}
	}

	private List<CategorySetResponseWrapper> categorySetResponse(MainCategory mainCategory,
			List<CategorySet> categorySets) {
		List<CategorySetResponseWrapper> result = new ArrayList<>();
		categorySets.forEach(f -> {
			CategorySetResponseWrapper categorySetResponseWrapper = new CategorySetResponseWrapper();
			BeanUtils.copyProperties(f, categorySetResponseWrapper);
			categorySetResponseWrapper.setMainCategoryName(mainCategory.getMainCategoryName());
			result.add(categorySetResponseWrapper);
		});
		return result;

	}

	private List<CategorySet> filterSet(List<String> categorySet, LocalDateTime fromDate, LocalDateTime toDate) {
		Criteria criteria = new Criteria();
		if (fromDate != null && toDate != null) {
			criteria.andOperator(Criteria.where("createdAt").gt(fromDate).lt(toDate));
		}
		criteria.orOperator(Criteria.where("setName").in(categorySet));
		return mongoTemplate.find(new Query().addCriteria(criteria), CategorySet.class);
	}

	private List<CategorySetResponseWrapper> getPaginatedResponse(int pageNumber, int pageSize,
			List<CategorySetResponseWrapper> list) {
		if (pageNumber == 0) {
			return list.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	private List<MainCategory> findFilteredCategory(List<String> mainCategory) {
		Query query = new Query();
		Query addCriteria = query.addCriteria(Criteria.where("mainCategoryName").in(mainCategory));
		return mongoTemplate.find(addCriteria, MainCategory.class);
	}

	@Override
	public CategorySetFilterResponsePojo getCategorySetFilterData() {
		CategorySetFilterResponsePojo result = new CategorySetFilterResponsePojo();
		try {
			List<MainCategory> list = mainCategoryRepository.findEnabledForDropdown(false);
			if (!list.isEmpty()) {

				List<MainCategoryWrapper> collect = list.stream()

						.map(e -> new MainCategoryWrapper(e.getMainCategoryId(), e.getMainCategoryName(),
								e.getCommissionType()))
						.collect(Collectors.toList());
				result.setMainCategory(collect);
			}
			List<CategorySet> categorySet = categorySetRepository.findByIsDisabled(false);
			if (!categorySet.isEmpty()) {
				List<CategorySetWrapper> collect = categorySet.stream()
						.map(e -> new CategorySetWrapper(e.getCategorySetId(), e.getSetName()))
						.collect(Collectors.toList());
				result.setCategorySet(collect);
			}
			return result;
		} catch (Exception e) {
			throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
		}
	}

	@Override
	public List<CategorySetWrapper> getSetsByCategoryForFilter(List<String> mainCategory) {
		try {
			List<CategorySetWrapper> collect = new ArrayList<>();
			mainCategory.forEach(e -> {
				List<CategorySet> list = categorySetRepository.findByMainCategoryIdAndIsDisabled(e, false);
				if (!list.isEmpty()) {
					collect.addAll(list.stream().map(f -> new CategorySetWrapper(f.getCategorySetId(), f.getSetName()))
							.collect(Collectors.toList()));
				}
			});
			return collect;
		} catch (Exception e) {
			throw new CategorySetNotFoundException(CATEGORY_SET_NOT_FOUND);
		}
	}
}
