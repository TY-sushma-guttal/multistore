package com.mrmrscart.productcategoriesservice.controller.category;

import static com.mrmrscart.productcategoriesservice.common.category.SubCategoryConstant.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryResponsePojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.VariationOptionPojo;
import com.mrmrscart.productcategoriesservice.response.category.SubCategoryGetResponse;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.category.SubCategoryService;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryVariationWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.category.SubCategoryWrapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class SubCategoryController {

	@Autowired
	private SubCategoryService subCategoryService;

	@Operation(summary = "This API is used for saving a sub-category based on a category set")
	@PostMapping("/sub-category")
	public ResponseEntity<SuccessResponse> addSubCategory(@RequestBody SubCategoryPojo data) {
		SubCategory subCategory = subCategoryService.addSubCategory(data);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_ADD_SUCCESS_MESSAGE, subCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching a sub-category based on id")
	@GetMapping("/sub-category/{id}")
	public ResponseEntity<SubCategoryGetResponse> getSubCategory(@PathVariable String id) {
		SubCategoryResponsePojo subCategory = subCategoryService.getSubCategory(id);
		return new ResponseEntity<>(new SubCategoryGetResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE, subCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for updating a sub-category based on id")
	@PutMapping("/sub-category")
	public ResponseEntity<SuccessResponse> updateSubCategory(@RequestBody SubCategoryPojo data) {
		SubCategory subCategory = subCategoryService.updateSubCategory(data);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_UPDATE_SUCCESS_MESSAGE, subCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for deleting a sub-category based on id")
	@PutMapping("/sub-category/{id}")
	public ResponseEntity<SuccessResponse> deleteSubCategory(@PathVariable String id) {
		SubCategory subCategory = subCategoryService.deleteSubCategory(id);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_DELETE_SUCCESS_MESSAGE, subCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the sub-category based on set id")
	@GetMapping("/sub-category")
	public ResponseEntity<SuccessResponse> getSubCategoryBySetId(@RequestParam String setId) {
		List<SubCategory> subCategoryList = subCategoryService.getSubcategoryBySetId(setId);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE, subCategoryList),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the sub-category details")
	@GetMapping("/sub-categories/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSubCategories(@PathVariable("pageNumber") int pageNumber,
			@PathVariable int pageSize) {
		List<SubCategoryResponsePojo> allSubCategories = subCategoryService.getAllSubCategories(pageNumber, pageSize);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE, allSubCategories),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for adding variations to a particular subcategory")
	@PutMapping("/sub-category/variation")
	public ResponseEntity<SuccessResponse> addVariationToSubCategory(@RequestBody SubCategoryVariation data) {
		SubCategoryVariationWrapper subCategory = subCategoryService.addVariations(data);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_VARIATION_ADDED_SUCCESSFULLY, subCategory),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for deleting a variation assigned to sub-category")
	@DeleteMapping("/sub-category/variation")
	public ResponseEntity<SuccessResponse> deleteVariation(@RequestBody VariationOptionPojo variationOptionPojo) {
		boolean standardVariation = subCategoryService.deleteVariation(variationOptionPojo);
		if (standardVariation) {
			return new ResponseEntity<>(new SuccessResponse(false, "Variation deleted successfully", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "Failed to delete the variation", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "This API is used for deleting a variation's option assigned to sub-category")
	@DeleteMapping("/sub-category/variation/option")
	public ResponseEntity<SuccessResponse> deleteOption(@RequestBody VariationOptionPojo variationOptionPojo) {
		boolean standardOption = subCategoryService.deleteOption(variationOptionPojo);
		if (standardOption) {
			return new ResponseEntity<>(new SuccessResponse(false, "Variation option deleted successfully", null),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "Failed to delete the variation option", null),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "This API is used for fetching all the subcategory of a particular category-set for dropdown")
	@GetMapping("/sub-category/drop-down-list")
	public ResponseEntity<SuccessResponse> getSubCategoryBySetIdForDropdown(@RequestParam String setId) {
		List<SubCategoryWrapper> subCategoryList = subCategoryService.getSubCategoryBySetIdForDropdown(setId);
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE, subCategoryList),
				HttpStatus.OK);
	}

	/* SubCategory filter apis starts */
	/*
	 * New implementation starts Commission Mode -> Main Category -> Set -> Sub
	 * Category -> Variations(Standard and others variations)
	 */
	@GetMapping("/sub-category/filter/commission-mode")
	public ResponseEntity<SuccessResponse> getMainCategoryByCommissionMode(@RequestParam ECategory commissionMode) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Main Category Fetched Successfully Based On Commission Mode for Filter. ",
						subCategoryService.getMainCategoryByCommissionMode(commissionMode)),
				HttpStatus.OK);
	}

	@GetMapping("/sub-category/filter/main-category")
	public ResponseEntity<SuccessResponse> getSetsByMainCategoryId(@RequestParam String mainCategoryId) {
		return new ResponseEntity<>(new SuccessResponse(false, "Sets Fetched Successfully By Main Category Id. ",
				subCategoryService.getSetsByMainCategoryId(mainCategoryId)), HttpStatus.OK);
	}

	@GetMapping("/sub-category/filter/set")
	public ResponseEntity<SuccessResponse> getAllSubCategoryBySetId(@RequestParam String setId) {
		return new ResponseEntity<>(new SuccessResponse(false, "Sub Category Fetched Successfully By Set Id. ",
				subCategoryService.getAllSubCategoryBySetId(setId)), HttpStatus.OK);
	}

	@GetMapping("/sub-category/filter/sub-category")
	public ResponseEntity<SuccessResponse> getAllStandardAndOtherVariationBySubId(@RequestParam String subCategoryId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Standard And Others Variation Fetched Successfully By Sub Category Id. ",
						subCategoryService.getAllStandardAndOtherVariationBySubId(subCategoryId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will fetch filtered sub category data")
	@GetMapping("/sub-category/filter/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getFilteredSubCategory(@RequestBody SubCategoryFilterPojo filterData,
			@PathVariable int pageNumber, @PathVariable int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE,
				subCategoryService.getFilteredSubCategory(filterData, pageNumber, pageSize)), HttpStatus.OK);
	}

	/* New Implementation added successfully ends. */

	@Operation(summary = "This API is used for fetching all the subcategory details for dropdown based on main category id")
	@GetMapping("/sub-category/main-category/{mainCategoryId}")
	public ResponseEntity<SuccessResponse> getSubcategoryByMainCategoryId(
			@PathVariable(name = "mainCategoryId") String mainCategoryId) {
		List<SubCategoryWrapper> list = subCategoryService.getSubcategoryByMainCategoryId(mainCategoryId);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, SUBCATEGORY_FETCH_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, EMPTY_SUB_CATEGORY_LIST, list), HttpStatus.OK);
		}
	}

	@GetMapping("/sub-category/category-set/main-category/dropdown/{subCategoryId}")
	public ResponseEntity<SuccessResponse> getCategorySetAndMainCategory(@PathVariable String subCategoryId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Category Fetched Successfully", subCategoryService.getSetAndMainBySubId(subCategoryId)), HttpStatus.OK);
	}
}