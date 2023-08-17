package com.mrmrscart.productcategoriesservice.controller.product;

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

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.GroupedProduct;
import com.mrmrscart.productcategoriesservice.pojo.product.ChildProductVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductDropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.GroupedProductPojo;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.GroupedProductService;
import com.mrmrscart.productcategoriesservice.wrapper.product.ChildProductWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ParentProductWrapper;

import io.swagger.v3.oas.annotations.Operation;

import static com.mrmrscart.productcategoriesservice.common.product.GroupedProductConstant.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class GroupedProductController {

	@Autowired
	private GroupedProductService groupedProductService;

	/*
	 * @Author : Hemadri G
	 */
	@PostMapping("/grouped-product")
	public ResponseEntity<SuccessResponse> addGroupedProduct(@RequestBody GroupedProductPojo groupedProductPojo) {
		GroupedProduct addGroupedProduct = groupedProductService.addGroupedProduct(groupedProductPojo);
		return new ResponseEntity<>(new SuccessResponse(false, GROUPED_PRODUCT_SUCCESS, addGroupedProduct),
				HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@PutMapping("/grouped-product")
	public ResponseEntity<SuccessResponse> updateGroupedProduct(@RequestBody GroupedProductPojo groupedProductPojo) {
		GroupedProduct updateGroupedProduct = groupedProductService.updateGroupedProduct(groupedProductPojo);
		return new ResponseEntity<>(new SuccessResponse(false, GROUPED_PRODUCT_SUCCESS, updateGroupedProduct),
				HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@GetMapping("/grouped-product/{id}")
	public ResponseEntity<SuccessResponse> getGroupedProduct(@PathVariable String id) {
		List<ChildProductVariationPojo> groupedProduct = groupedProductService.getGroupedProduct(id);
		if (!groupedProduct.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, groupedProduct), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, FETCH_FAILURE, groupedProduct), HttpStatus.OK);
		}
	}

	
	/**
	 * @author Soumyajit
	 * @param supplierId
	 * @param status
	 * @return
	 */
	@Operation(summary = "This api will fetch all the products based on supplier id and the status for parent product")
	@PutMapping("/grouped-product/dropdown/parent")
	public ResponseEntity<SuccessResponse> getDropDownForParentProduct(@RequestParam String supplierId,@RequestParam EStatus status) {
		
		List<ParentProductWrapper> dropDownList = groupedProductService.getParentProductBySupplierId(supplierId, status);
		if(!dropDownList.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,
					dropDownList), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_FAILURE,
				null), HttpStatus.NOT_FOUND);
	}
	
	
	/**
	 * @author Soumyajit
	 * @param data
	 * @return
	 */
	@Operation(summary = "This api will fetch all the products based on supplier id and the status for child product")
	@PutMapping("/grouped-product/dropdown/child")
	public ResponseEntity<SuccessResponse> getDropDownForChildProduct(@RequestBody GroupedProductDropDownPojo data) {
		List<ChildProductWrapper> dropDownList = groupedProductService.getChildProductBySubCategory(data);
		if(!dropDownList.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,
					dropDownList), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_FAILURE,
				dropDownList), HttpStatus.NOT_FOUND);
	}
}
