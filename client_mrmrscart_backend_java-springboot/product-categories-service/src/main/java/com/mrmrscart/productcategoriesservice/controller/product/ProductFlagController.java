package com.mrmrscart.productcategoriesservice.controller.product;

import static com.mrmrscart.productcategoriesservice.common.product.ProductFlagConstant.PRODUCT_FLAG_SUCCESS;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.common.product.ProductFlagConstant;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagingPojo;
import com.mrmrscart.productcategoriesservice.response.product.ProductFlagResponse;
import com.mrmrscart.productcategoriesservice.service.product.ProductFlagService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductFlagController {

	@Autowired
	private ProductFlagService productFlagService;

	@Operation(summary = "This method is used to add flags for products")
	@PostMapping("/product-flag")
	public ResponseEntity<ProductFlagResponse> addProductFlag(@RequestBody ProductFlagPojo flagPojo) {

		return new ResponseEntity<>(
				new ProductFlagResponse(false, PRODUCT_FLAG_SUCCESS, productFlagService.addProductFlag(flagPojo)),
				org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to modify flags for products")
	@PutMapping("/product-flag")
	public ResponseEntity<ProductFlagResponse> updateProductFlag(@RequestBody ProductFlagPojo flagPojo) {

		return new ResponseEntity<>(
				new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_UPDATE_SUCCESS_MESSAGE, productFlagService.updateProductFlag(flagPojo)),
				org.springframework.http.HttpStatus.OK);
	}
	
	@Operation(summary = "This method is used to add products in the flag")
	@PutMapping("/product-flaged")
	public ResponseEntity<ProductFlagResponse> updateProductFlag(@RequestBody ProductFlagingPojo flagPojo) {

		return new ResponseEntity<>(
				new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAGED_SUCCESS, productFlagService.updateProductFlag(flagPojo)),
				org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the product flags")
	@GetMapping("/product-flags")
	public ResponseEntity<ProductFlagResponse> getAllFlags() {
		List<ProductFlag> flags = productFlagService.getAllFlags();
		if (!flags.isEmpty())
			return new ResponseEntity<>(
					new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_GET_SUCCESS_MESSAGE, flags),
					org.springframework.http.HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE, flags),
					org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to enable/disable flags")
	@GetMapping("/produt-flag/{flagId}/{isEnabled}")
	public ResponseEntity<ProductFlagResponse> enableProductFlag(@PathVariable String flagId,
			@PathVariable boolean isEnabled) {
		return new ResponseEntity<>(new ProductFlagResponse(false,
				ProductFlagConstant.PRODUCT_FLAG_DISABLE_SUCCESS_MESSAGE, productFlagService.enableProductFlag(flagId, isEnabled)),
				org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to delete the flag")
	@DeleteMapping("/produt-flag/{flagId}")
	public ResponseEntity<ProductFlagResponse> deleteProductFlag(@PathVariable String flagId) {
		return new ResponseEntity<>(new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_DELETE_SUCCESS,
				productFlagService.deleteProductFlag(flagId)), org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get flag by flagId")
	@GetMapping("/produt-flag/{flagId}")
	public ResponseEntity<ProductFlagResponse> getById(@PathVariable String flagId) {
		return new ResponseEntity<>(new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_GET_SUCCESS_MESSAGE,
				productFlagService.getById(flagId)), org.springframework.http.HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get flags filtered on dates")
	@GetMapping("/produt-flags/{dateFrom}/{dateTo}")
	public ResponseEntity<ProductFlagResponse> getAllFilteredFlags(
			@PathVariable @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") LocalDateTime dateFrom,
			@PathVariable @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss") LocalDateTime dateTo) {
		List<ProductFlag> flags = productFlagService.getAllFilteredFlags(dateFrom, dateTo);
		if (!flags.isEmpty())
			return new ResponseEntity<>(
					new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_GET_SUCCESS_MESSAGE, flags),
					org.springframework.http.HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new ProductFlagResponse(false, ProductFlagConstant.PRODUCT_FLAG_GET_FAIL_MESSAGE, flags),
					org.springframework.http.HttpStatus.OK);
	}
}
