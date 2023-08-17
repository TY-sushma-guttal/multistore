package com.mrmrscart.productcategoriesservice.controller.category;

import static com.mrmrscart.productcategoriesservice.common.category.StandardVariationConstant.STANDARD_VARIATION_ADD_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.StandardVariationConstant.STANDARD_VARIATION_FETCH_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.category.StandardVariationConstant.STANDARD_VARIATION_UPDATE_SUCCESS_MESSAGE;

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

import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.UpdateVariationPojo;
import com.mrmrscart.productcategoriesservice.response.category.StandardVariationResponse;
import com.mrmrscart.productcategoriesservice.service.category.StandardVariationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class StandardVariationController {

	@Autowired
	private StandardVariationService standardVariationService;

	@Operation(summary = "This API is used for saving all standard variation by admin")
	@PostMapping("/standard-variation")
	public ResponseEntity<StandardVariationResponse> addStandardVariation(@RequestBody StandardVariationPojo data) {
		StandardVariation standardVariation = standardVariationService.addStandardVariation(data);
		return new ResponseEntity<>(
				new StandardVariationResponse(false, STANDARD_VARIATION_ADD_SUCCESS_MESSAGE, standardVariation),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching a standard variation based on standard variation id")
	@GetMapping("/standard-variation/{id}")
	public ResponseEntity<StandardVariationResponse> getStandardVariation(@PathVariable String id) {
		StandardVariation standardVariation = standardVariationService.getStandardVariation(id);
		return new ResponseEntity<>(
				new StandardVariationResponse(false, STANDARD_VARIATION_FETCH_SUCCESS_MESSAGE, standardVariation),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for updating a standard variation")
	@PutMapping("/standard-variation")
	public ResponseEntity<StandardVariationResponse> updateStandardVariation(@RequestBody UpdateVariationPojo data) {
		SubCategoryPojo subCategoryPojo = standardVariationService.updateVariation(data);
		return new ResponseEntity<>(
				new StandardVariationResponse(false, STANDARD_VARIATION_UPDATE_SUCCESS_MESSAGE, subCategoryPojo),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the standard variations")
	@GetMapping("/standard-variation")
	public ResponseEntity<StandardVariationResponse> getAllStandardVariation() {
		List<StandardVariation> standardVariationList = standardVariationService.getStandardVariations();
		return new ResponseEntity<>(
				new StandardVariationResponse(false, STANDARD_VARIATION_FETCH_SUCCESS_MESSAGE, standardVariationList),
				HttpStatus.OK);
	}

}
