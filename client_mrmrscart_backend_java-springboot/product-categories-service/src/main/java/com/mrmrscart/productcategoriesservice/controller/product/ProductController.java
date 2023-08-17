package com.mrmrscart.productcategoriesservice.controller.product;

import java.util.List;
import java.util.Set;

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

import com.mrmrscart.productcategoriesservice.common.product.ProductConstant;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.FilteredProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFilterPojo;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Operation(summary = "This method is used to get data for categories dropdown")
	@GetMapping("/categories")
	public ResponseEntity<SuccessResponse> getCategories(@RequestParam ECategory commissionType) {
		List<DropDownPojo> categories = productService.getCategories(commissionType);
		if (!categories.isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, ProductConstant.CATEGORY_FETCH_SUCCESS, categories),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, ProductConstant.CATEGORY_FETCH_FAILURE, categories),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get data for sub-categories dropdown ")
	@PostMapping("/sub-categories")
	public ResponseEntity<SuccessResponse> getSubCategories(@RequestBody List<String> categoryIds,
			@RequestParam ECategory commissionType) {
		List<DropDownPojo> subCategories = productService.getSubCategories(categoryIds, commissionType);
		if (!subCategories.isEmpty())
			return new ResponseEntity<>(
					new SuccessResponse(false, ProductConstant.SUB_CATEGORY_FETCH_SUCCESS, subCategories),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SuccessResponse(true, ProductConstant.SUB_CATEGORY_FETCH_FAILURE, subCategories),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get data for brands dropdown ")
	@PostMapping("/brands")
	public ResponseEntity<SuccessResponse> getBrands(@RequestBody ProductFilterPojo filterPojo) {
		Set<DropDownPojo> brands = productService.getBrands(filterPojo);
		if (!brands.isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, ProductConstant.BRAND_FETCH_SUCCESS, brands),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, ProductConstant.BRAND_FETCH_FAILURE, brands),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to data for product titles dropdown ")
	@PostMapping("/product-titles")
	public ResponseEntity<SuccessResponse> getProductTitles(@RequestBody ProductFilterPojo filterPojo) {
		List<DropDownPojo> productTitles = productService.getProductTitles(filterPojo);
		if (!productTitles.isEmpty())
			return new ResponseEntity<>(
					new SuccessResponse(false, ProductConstant.PRODUCT_TITLE_FETCH_SUCCESS, productTitles),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SuccessResponse(true, ProductConstant.PRODUCT_TITLE_FETCH_FAILURE, productTitles),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the products based on filters")
	@PostMapping("/admin/products/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getProducts(@PathVariable int pageNumber, @PathVariable int pageSize,
			@RequestBody ProductFilterPojo filterPojo) {
		FilteredProductPojo products = productService.getProducts(pageNumber, pageSize, filterPojo);
		if (!products.getProducts().isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, ProductConstant.PRODUCTS_FETCH_SUCCESS, products),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, ProductConstant.PRODUCTS_FETCH_FAILURE, products),
					HttpStatus.OK);
	}
	
	@Operation(summary = "This method is used to change the product status")
	@PutMapping("/admin/products/{productVariationId}")
	public ResponseEntity<SuccessResponse> setProductStatus(String productVariationId){
		return new ResponseEntity<>(
				new SuccessResponse(false, ProductConstant.PRODUCTS_FETCH_SUCCESS, productService.setProductStatus(productVariationId)),
				HttpStatus.OK);
	}

}
