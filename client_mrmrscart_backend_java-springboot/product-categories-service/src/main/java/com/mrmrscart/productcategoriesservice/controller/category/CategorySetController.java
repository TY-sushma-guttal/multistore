package com.mrmrscart.productcategoriesservice.controller.category;

import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_FOUND;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_NOT_FOUND;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.category.CategorySetConstant.CATEGORY_SET_UPDATED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilter;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetFilterResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.CategorySetPojo;
import com.mrmrscart.productcategoriesservice.response.category.CategorySetResponse;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.category.CategorySetService;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetResponseWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategorySetWrapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class CategorySetController {

	@Autowired
	private CategorySetService categorySetService;

	@Operation(summary = "This API is used for saving a category set for a particular main-category")
	@PostMapping("/category-set")
	public ResponseEntity<CategorySetResponse> saveCategorySet(@RequestBody CategorySetPojo categorySetPojo) {

		CategorySet categorySet = categorySetService.saveCategorySet(categorySetPojo);
		if (categorySet != null) {
			return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_SUCCESS, categorySet),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CategorySetResponse(true, CATEGORY_SET_FAILURE, categorySet),
					HttpStatus.BAD_REQUEST);
		}

	}

	@Operation(summary = "This API is used for updating a category set of a particular main-category")
	@PutMapping("/category-set")
	public ResponseEntity<CategorySetResponse> updateCategorySet(@RequestBody CategorySetPojo categorySetPojo)
			throws CategorySetNotFoundException {
		CategorySet categorySet = categorySetService.updateCategorySet(categorySetPojo);
		return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_UPDATED, categorySet), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching a category set based on set id")
	@GetMapping("/category-set/{categorySetId}")
	public ResponseEntity<CategorySetResponse> getCategorySetById(@PathVariable String categorySetId) {
		CategorySet categorySet = categorySetService.getCategorySet(categorySetId);
		if (categorySet != null) {
			return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_FOUND, categorySet), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CategorySetResponse(true, CATEGORY_SET_NOT_FOUND, categorySet),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the category set with filter")
	@PostMapping("/category-sets/{pageNumber}/{pageSize}")
	public ResponseEntity<CategorySetResponse> getAllCategorySets(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize, @RequestBody CategorySetFilter filterPojo) {
		List<CategorySetResponseWrapper> allCategorySets = categorySetService.getAllCategorySets(pageNumber, pageSize,
				filterPojo.getFromDate(), filterPojo.getToDate(), filterPojo);
		if (allCategorySets.isEmpty()) {
			return new ResponseEntity<>(new CategorySetResponse(true, CATEGORY_SET_NOT_FOUND, allCategorySets),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_FOUND, allCategorySets),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the category set based on main-category id")
	@GetMapping("/category-set")
	public ResponseEntity<CategorySetResponse> getAllCategorySetsByMainCategoryId(
			@RequestParam(value = "mainCategoryId") String mainCategoryId) {
		List<CategorySet> allCategorySetsByMainCategoryId = categorySetService
				.getAllCategorySetsByMainCategoryId(mainCategoryId);
		if (allCategorySetsByMainCategoryId.isEmpty()) {
			return new ResponseEntity<>(
					new CategorySetResponse(true, CATEGORY_SET_NOT_FOUND, allCategorySetsByMainCategoryId),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new CategorySetResponse(false, CATEGORY_SET_FOUND, allCategorySetsByMainCategoryId), HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the enabled category set ")
	@GetMapping("/category-set-enabled")
	public ResponseEntity<CategorySetResponse> getEnabledCategorySets(
			@RequestParam(value = "mainCategoryId") String mainCategoryId) {
		List<CategorySet> enabledCategorySets = categorySetService.getEnabledCategorySets(mainCategoryId);
		if (enabledCategorySets.isEmpty()) {
			return new ResponseEntity<>(new CategorySetResponse(true, CATEGORY_SET_NOT_FOUND, enabledCategorySets),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_FOUND, enabledCategorySets),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the enabled category set for dropdown")
	@GetMapping("/category-set" + "/drop-down-list")
	public ResponseEntity<CategorySetResponse> getEnabledCategorySetsForDropdown(
			@RequestParam("mainCategoryId") String mainCategoryId) {
		List<CategorySetWrapper> enabledCategorySets = categorySetService
				.getEnabledCategorySetsForDropdown(mainCategoryId);
		if (enabledCategorySets.isEmpty()) {
			return new ResponseEntity<>(new CategorySetResponse(true, CATEGORY_SET_NOT_FOUND, enabledCategorySets),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new CategorySetResponse(false, CATEGORY_SET_FOUND, enabledCategorySets),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the category and set details for filter")
	@GetMapping("/category-set/filter-list")
	public ResponseEntity<SuccessResponse> getCategoryAndSetsForFilter() {
		CategorySetFilterResponsePojo data = categorySetService.getCategorySetFilterData();
		if (data != null) {
			return new ResponseEntity<>(new SuccessResponse(false, CATEGORY_SET_FOUND, data), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, CATEGORY_SET_NOT_FOUND, data), HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all the set details based on main categories for filter")
	@PostMapping("/category-set/main-category")
	public ResponseEntity<SuccessResponse> getSetsByCategoryForFilter(@RequestBody List<String> mainCategory) {
		List<CategorySetWrapper> sets = categorySetService.getSetsByCategoryForFilter(mainCategory);
		if (!sets.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, CATEGORY_SET_FOUND, sets), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, CATEGORY_SET_NOT_FOUND, sets), HttpStatus.OK);
		}
	}

}
