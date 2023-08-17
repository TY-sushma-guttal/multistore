package com.mrmrscart.productcategoriesservice.feign.controller;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_VARIATION_GET_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.ProductVariationConstant.UPDATE_SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.feign.pojo.IncreaseStockQuantityPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrderedProductsPojo;
import com.mrmrscart.productcategoriesservice.feign.pojo.OrdersRequestPojo;
import com.mrmrscart.productcategoriesservice.feign.response.ProductVariationResponse;
import com.mrmrscart.productcategoriesservice.feign.response.ProductsResponse;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.ProductService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ProductFeignController {

	@Autowired
	private ProductService productService;

	/*
	 * @Author : Hemadri G
	 */
	@PostMapping("/product-feign")
	public ResponseEntity<SuccessResponse> getProductIds(@RequestBody OrdersRequestPojo ordersRequestPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, PRODUCT_VARIATION_GET_SUCCESS_MESSAGE,
				productService.getProductIds(ordersRequestPojo)), HttpStatus.OK);
	}

	@PostMapping("/order/order-products")
	public ResponseEntity<ProductsResponse> validateOrderedProducts(
			@RequestBody OrderedProductsPojo orderedProductsPojo) {
		return new ResponseEntity<>(new ProductsResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				productService.validateOrderedProducts(orderedProductsPojo)), HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 */
	@PutMapping("/product-quantity")
	public ResponseEntity<SuccessResponse> increaseStockQuantity(
			@RequestBody IncreaseStockQuantityPojo increaseStockQuantityPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, UPDATE_SUCCESS,
				productService.increaseStockQuantity(increaseStockQuantityPojo)), HttpStatus.OK);
	}

	@GetMapping("/variation")
	public ResponseEntity<ProductVariationResponse> getProductVariationBySkuId(@RequestParam("skuId") String skuId) {
		return new ResponseEntity<>(new ProductVariationResponse(false, PRODUCT_VARIATION_GET_SUCCESS_MESSAGE,
				productService.getProductVariationBySkuId(skuId)), HttpStatus.OK);
	}
}
