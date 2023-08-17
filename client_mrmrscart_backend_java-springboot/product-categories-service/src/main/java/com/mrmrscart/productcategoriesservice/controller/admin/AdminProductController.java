package com.mrmrscart.productcategoriesservice.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.pojo.admin.AdminProductCountPojo;
import com.mrmrscart.productcategoriesservice.pojo.admin.FromAndToDatePojo;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.admin.AdminProductService;

import io.swagger.v3.oas.annotations.Operation;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class AdminProductController {

	@Autowired
	private AdminProductService adminProductService;

	@Operation(summary = "This Api will fetch the product count based on category (Admin Products Dashboard)")
	@PostMapping("/admin/category-count")
	public ResponseEntity<SuccessResponse> getProductCountByCategory(
			@RequestBody AdminProductCountPojo adminProductCountPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, COUNT_SUCCESS,
				adminProductService.getProductCountByCategory(adminProductCountPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This Api will fetch the product count based on SubCategory  (Admin Products Dashboard)")
	@PostMapping("/admin/subcategory-count")
	public ResponseEntity<SuccessResponse> getProductCountBySubCategory(
			@RequestBody AdminProductCountPojo adminProductCountPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, COUNT_SUCCESS,
				adminProductService.getProductCountBySubCategory(adminProductCountPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This Api will fetch the fixed-margin products and zero-commission products count (Admin Products Dashboard)")
	@PostMapping("/admin/margin-count")
	public ResponseEntity<SuccessResponse> getMarginCount(@RequestBody FromAndToDatePojo fromAndToDatePojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, COUNT_SUCCESS, adminProductService.getMarginCount(fromAndToDatePojo)),
				HttpStatus.OK);
	}

	@Operation(summary = "This Api will fetch the Total fixed-margin products and zero-commission products count (Admin Products Dashboard)")
	@GetMapping("/admin/margin-count")
	public ResponseEntity<SuccessResponse> getTotalMarginCount() {
		return new ResponseEntity<>(
				new SuccessResponse(false, COUNT_SUCCESS, adminProductService.getTotalMarginCount()), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for enabling or disabling the product")
	@GetMapping("/admin/product/product-status")
	public ResponseEntity<SuccessResponse> enableDisableProduct(
			@RequestParam("productVariationId") String productVariationId, @RequestParam("status") boolean status) {
		boolean disableProduct = adminProductService.enableDisableProduct(productVariationId, status);
		if (disableProduct) {
			return new ResponseEntity<>(new SuccessResponse(false, "Product Disabled Successfully", null),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(false, "Product Enabled Successfully", null),
					HttpStatus.OK);
	}
}
