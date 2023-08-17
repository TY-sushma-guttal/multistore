package com.mrmrscart.productcategoriesservice.controller.product;

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
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.common.product.ProductTagConstants;
import com.mrmrscart.productcategoriesservice.entity.product.ProductTag;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTagViewPojo;
import com.mrmrscart.productcategoriesservice.response.product.ProductFlagResponse;
import com.mrmrscart.productcategoriesservice.response.product.ProductTagResponse;
import com.mrmrscart.productcategoriesservice.service.product.ProductTagService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductTagController {

	@Autowired
	private ProductTagService productTagService;

	@Operation(summary = "This method is used to add tags for products")
	@PostMapping("/product-tag")
	public ResponseEntity<ProductFlagResponse> addProductFlag(@RequestBody ProductTagPojo productTagPojo) {

		return new ResponseEntity<>(new ProductFlagResponse(false, ProductTagConstants.PRODUCT_TAG_SUCCESS,
				productTagService.addProductTag(productTagPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get product tags")
	@GetMapping("/product-tags/{pageNumber}/{pageSize}")
	public ResponseEntity<ProductTagResponse> getAllProductTags(@PathVariable int pageNumber,@PathVariable int pageSize) {
		List<ProductTag> productTags = productTagService.getAllProductTags(pageNumber, pageSize);
		if (!productTags.isEmpty())
			return new ResponseEntity<>(
					new ProductTagResponse(false, ProductTagConstants.PRODUCT_TAG_GET_SUCCESS_MESSAGE, productTags),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new ProductTagResponse(true, ProductTagConstants.PRODUCT_TAG_GET_FAIL_MESSAGE, productTags),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to get product tag by tagId")
	@GetMapping("/product-tags/{tagId}")
	public ResponseEntity<ProductTagResponse> getProductTag(@PathVariable String tagId) {
		List<ProductTagViewPojo> productTagViewPojo = productTagService.getProductTag(tagId);
		if (!productTagViewPojo.isEmpty())
			return new ResponseEntity<>(new ProductTagResponse(false,
					ProductTagConstants.PRODUCT_TAG_GET_DETAILS_SUCCESS_MESSAGE, productTagViewPojo), HttpStatus.OK);
		else
			return new ResponseEntity<>(new ProductTagResponse(true,
					ProductTagConstants.PRODUCT_TAG_GET_DETAILS_FAIL_MESSAGE, productTagViewPojo),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to delete tags for products")
	@DeleteMapping("/produt-tag/{tagId}")
	public ResponseEntity<ProductFlagResponse> deleteProductTag(@PathVariable String tagId) {
		return new ResponseEntity<>(new ProductFlagResponse(false, ProductTagConstants.PRODUCT_TAG_DELETE_SUCCESS,
				productTagService.deleteProductTag(tagId)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all approved product tags")
	@GetMapping("/product-tag")
	public ResponseEntity<ProductTagResponse> getAllApprovedTags() {
		List<ProductTag> productTags = productTagService.getAllApprovedTags();
		if (!productTags.isEmpty())
			return new ResponseEntity<>(
					new ProductTagResponse(false, ProductTagConstants.PRODUCT_TAG_GET_SUCCESS_MESSAGE, productTags),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new ProductTagResponse(true, ProductTagConstants.PRODUCT_TAG_GET_FAIL_MESSAGE, productTags),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to approve tags for products")
	@PutMapping("/product-tag/status/{tagId}/{status}")
	public ResponseEntity<ProductTagResponse> approveProductTag(@PathVariable String tagId,
			@PathVariable boolean status) {
		return new ResponseEntity<>(
				new ProductTagResponse(false, ProductTagConstants.PRODUCT_TAG_APPROVE_SUCCESS_MESSAGE,
						productTagService.approveProductTag(tagId, status)),
				HttpStatus.OK);

	}

}
