package com.mrmrscart.productcategoriesservice.controller.product;

import java.util.List;

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

import com.mrmrscart.productcategoriesservice.entity.product.SupplierSideNavigation;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.SupplierSideNavigationService;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class SupplierSideNavigationController {

	@Autowired
	private SupplierSideNavigationService sideNavigationService;

	@PostMapping("/supplier/side-navigation")
	public ResponseEntity<SuccessResponse> saveSupplierNavigationInfo(
			@RequestBody List<SupplierSideNavigation> navigationPojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Success", sideNavigationService.saveSideNavigationInfo(navigationPojo)),
				HttpStatus.OK);
	}

	@GetMapping("/supplier/side-navigation")
	public ResponseEntity<SuccessResponse> getSupplierNavigationInfo(@RequestParam("userType") String userType) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Success", sideNavigationService.getSideNavigationInfo(userType)),
				HttpStatus.OK);
	}
}
