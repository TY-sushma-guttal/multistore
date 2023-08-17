package com.mrmrscart.productcategoriesservice.controller.product;

import static com.mrmrscart.productcategoriesservice.common.product.MarketingToolFlagConstant.FLAG_FETCH_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.product.MarketingToolFlagConstant.FLAG_FETCH_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.MarketingToolFlagConstant.FLAG_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.ProductFlagConstant.PRODUCT_FLAG_GET_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.ProductFlagConstant.PRODUCT_FLAG_SAVE_FAIL_MESSAGE;

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

import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagPojo;
import com.mrmrscart.productcategoriesservice.response.product.ProductFlagResponse;
import com.mrmrscart.productcategoriesservice.service.product.MarketingToolFlagService;
import com.mrmrscart.productcategoriesservice.wrapper.product.SupplierFlagWrapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class MarketingToolFlagController {

	@Autowired
	private MarketingToolFlagService marketingToolFlagService;

	@Operation(summary = "This API is used to get all the flags created by Admin for supplier/reseller")
	@GetMapping("/supplier-flag/{userType}/{userId}")
	public ResponseEntity<ProductFlagResponse> getAllFlagByUserType(@PathVariable EUserType userType,
			@PathVariable String userId) {
		List<DropDownPojo> list = marketingToolFlagService.getAllFlagByUserType(userType, userId);
		if (!list.isEmpty())
			return new ResponseEntity<>(new ProductFlagResponse(false, PRODUCT_FLAG_GET_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new ProductFlagResponse(false, PRODUCT_FLAG_SAVE_FAIL_MESSAGE, list),
					HttpStatus.NOT_FOUND);
	}
	
	@Operation(summary = "This API is used to add/update products to the flag")
	@PostMapping("/supplier-flag")
	public ResponseEntity<ProductFlagResponse> addProductsToFlag(@RequestBody SupplierFlagPojo supplierFlagPojo) {

		return new ResponseEntity<>(new ProductFlagResponse(false, FLAG_SUCCESS,
				marketingToolFlagService.addProductsToFlag(supplierFlagPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to update flag")
	@PutMapping("/supplier-flag")
	public ResponseEntity<ProductFlagResponse> updateFlag(@RequestBody SupplierFlagPojo supplierFlagPojo) {

		return new ResponseEntity<>(
				new ProductFlagResponse(false, FLAG_SUCCESS, marketingToolFlagService.updateFlag(supplierFlagPojo)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get all the flags created by the user")
	@PostMapping("/supplier-flag/{pageNumber}/{pageSize}")
	public ResponseEntity<ProductFlagResponse> getAllSupplierFlag(@PathVariable int pageNumber,
			@PathVariable int pageSize, @RequestBody SupplierFlagFilterPojo supplierFlagFilterPojo) {

		List<SupplierFlagWrapper> list = marketingToolFlagService.getAllSupplierFlag(pageNumber, pageSize,
				supplierFlagFilterPojo);
		if (!list.isEmpty())
			return new ResponseEntity<>(new ProductFlagResponse(false, FLAG_FETCH_SUCCESS, list), HttpStatus.OK);
		else
			return new ResponseEntity<>(new ProductFlagResponse(false, FLAG_FETCH_FAILURE, list), HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "This API is used to disable/enable the flag")
	@PutMapping("/supplier-flag/{supplierFlagId}/{isDisabled}")
	public ResponseEntity<ProductFlagResponse> disableFlag(@PathVariable String supplierFlagId,
			@PathVariable boolean isDisabled) {

		return new ResponseEntity<>(new ProductFlagResponse(false, FLAG_SUCCESS,
				marketingToolFlagService.disableFlag(supplierFlagId, isDisabled)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get the flag by Id")
	@GetMapping("/supplier-flag/{flagId}/{purchaseId}/{supplierStoreId}")
	public ResponseEntity<ProductFlagResponse> getFlagById(@PathVariable String flagId, @PathVariable Long purchaseId,
			@PathVariable String supplierStoreId) {

		return new ResponseEntity<>(new ProductFlagResponse(false, FLAG_FETCH_SUCCESS,
				marketingToolFlagService.getFlagById(flagId, purchaseId, supplierStoreId)), HttpStatus.OK);
	}

}
