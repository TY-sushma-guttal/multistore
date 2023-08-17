package com.mrmrscart.userservice.controller.supplier;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.feign.response.SupplierStoreInfoResponse;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreInfoPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.SupplierStoreService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class SupplierStoreController {

	@Autowired
	private SupplierStoreService service;

	@Operation(summary = "This API is used for updating the store configurations of a supplier")
	@PutMapping("/supplier/supplier-store-configuration")
	public ResponseEntity<SuccessResponse> updateSupplierStoreConfiguration(
			@RequestBody SupplierStoreInfoPojo storeInfoPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, STORE_SETTINGS_UPDATE_SUCCESS,
				service.updateSupplierStoreConfiguration(storeInfoPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching the store configuration details of a supplier")
	@GetMapping("/supplier/supplier-store-configuration")
	public ResponseEntity<SuccessResponse> getSupplierStoreConfiguration(@RequestParam("storeCode") String storeCode) {
		return new ResponseEntity<>(new SuccessResponse(false, STORE_SETTINGS_GET_SUCCESS,
				service.getSupplierStoreConfiguration(storeCode)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching the store configuration details of a supplier based on supplier id")
	@PutMapping("/supplier/supplier-store")
	public ResponseEntity<SupplierStoreInfoResponse> updateProductCount(@RequestParam String supplierId,
			@RequestParam int productCount) {
		return new ResponseEntity<>(new SupplierStoreInfoResponse(false, STORE_SETTINGS_GET_SUCCESS,
				service.updateProductCount(supplierId, productCount)), HttpStatus.OK);
	}

}
