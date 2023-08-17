package com.mrmrscart.productcategoriesservice.controller.category;

import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_GET_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_SAVE_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.MainCategoryConstant.MAIN_CATEGORY_UPDATE_SUCCESS_MESSAGE;

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
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.MainCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationDisablePojo;
import com.mrmrscart.productcategoriesservice.response.category.MainCategoryResponse;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.category.MainCategoryService;
import com.mrmrscart.productcategoriesservice.wrapper.category.CategoryStatusInfo;
import com.mrmrscart.productcategoriesservice.wrapper.category.MainCategoryWrapper;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/products/")
@CrossOrigin(origins = "*")
@Slf4j
public class MainCategoryController {

	@Autowired
	private MainCategoryService mainCategoryService;

	@Operation(summary = "This API is used for adding a main-category ")
	@PostMapping("main-category")
	public ResponseEntity<MainCategoryResponse> addMainCategory(@RequestBody MainCategoryPojo mainCategory) {

		MainCategory addMainCategory = mainCategoryService.addMainCategory(mainCategory);
		return new ResponseEntity<>(
				new MainCategoryResponse(false, MAIN_CATEGORY_SAVE_SUCCESS_MESSAGE, addMainCategory), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the main-category details")
	@PostMapping("main-category/{pageNumber}/{pageSize}")
	public ResponseEntity<MainCategoryResponse> getAllMainCategory(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize, @RequestBody MainCategoryFilterPojo filterPojo) {
		List<MainCategoryPojo> allMainCatrgory = mainCategoryService.getAllMainCategory(pageNumber, pageSize,
				filterPojo);
		if (!allMainCatrgory.isEmpty()) {
			return new ResponseEntity<>(
					new MainCategoryResponse(false, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new MainCategoryResponse(true, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for updating the main-category")
	@PutMapping("main-category")
	public ResponseEntity<MainCategoryResponse> updateMainCategory(@RequestBody MainCategoryPojo mainCategory) {
		MainCategory addMainCategory = mainCategoryService.updateMainCategory(mainCategory);
		return new ResponseEntity<>(
				new MainCategoryResponse(false, MAIN_CATEGORY_UPDATE_SUCCESS_MESSAGE, addMainCategory), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for disabling a main-category/category-set/sub-category")
	@PutMapping("category-status")
	public ResponseEntity<MainCategoryResponse> disableCategory(@RequestBody CategoryStatusInfo categoryStatusInfo) {
		String result = mainCategoryService.disableCategory(categoryStatusInfo);
		return new ResponseEntity<>(new MainCategoryResponse(false, result, null), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the enabled main-category details")
	@GetMapping("main-category-enabled/{pageNumber}/{pageSize}")
	public ResponseEntity<MainCategoryResponse> getAllEnabledMainCategory(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize) {
		List<MainCategoryPojo> allMainCatrgory = mainCategoryService.getAllEnabledMainCategory(pageNumber, pageSize);
		if (!allMainCatrgory.isEmpty()) {
			return new ResponseEntity<>(
					new MainCategoryResponse(false, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new MainCategoryResponse(true, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching a main-category based on id")
	@GetMapping("main-category/{mainCategoryId}")
	public ResponseEntity<MainCategoryResponse> getMainCategory(@PathVariable String mainCategoryId) {
		MainCategoryPojo mainCategory = mainCategoryService.getMainCategory(mainCategoryId);
		return new ResponseEntity<>(new MainCategoryResponse(false, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, mainCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for disabling a particular variation")
	@PutMapping("category/variation/status")
	public ResponseEntity<MainCategoryResponse> disableVariation(
			@RequestBody VariationDisablePojo variationDisablePojo) {
		log.info("controller");
		String result = mainCategoryService.disableVariation(variationDisablePojo);
		return new ResponseEntity<>(new MainCategoryResponse(false, result, null), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for disabling a particular variation's option")
	@PutMapping("category/variation/option/status")
	public ResponseEntity<MainCategoryResponse> disableVariationOption(
			@RequestBody VariationDisablePojo variationDisablePojo) {
		log.info("controller");
		String result = mainCategoryService.disableVariationOption(variationDisablePojo);
		return new ResponseEntity<>(new MainCategoryResponse(false, result, null), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the enabled main-category for dropdown")
	@GetMapping("main-category/drop-down-list")
	public ResponseEntity<MainCategoryResponse> getAllMainCategoryDropdown() {
		List<MainCategoryWrapper> allMainCatrgory = mainCategoryService.getAllMainCategoryDropdown();
		if (!allMainCatrgory.isEmpty()) {
			return new ResponseEntity<>(
					new MainCategoryResponse(false, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new MainCategoryResponse(true, MAIN_CATEGORY_GET_SUCCESS_MESSAGE, allMainCatrgory), HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for fetching all main-category for filter dropdown")
	@GetMapping("main-category/filter-drop-down-list")
	public ResponseEntity<SuccessResponse> getFilterMainCategoryDropdown() {
		return new ResponseEntity<>(new SuccessResponse(false, MAIN_CATEGORY_GET_SUCCESS_MESSAGE,
				mainCategoryService.getFilterMainCategoryDropdown()), HttpStatus.OK);
	}

}
