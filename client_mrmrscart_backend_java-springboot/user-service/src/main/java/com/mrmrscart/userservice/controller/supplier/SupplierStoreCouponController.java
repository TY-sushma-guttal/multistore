package com.mrmrscart.userservice.controller.supplier;

import static com.mrmrscart.userservice.common.supplier.CouponConstant.CREATE_COUPON_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.CouponConstant.GET_COUPON_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.CouponConstant.GET_COUPON_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.CouponConstant.PUBLISH_COUPON_SUCCESS_MESSAGE;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreCoupon;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreCouponPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.SupplierStoreCouponService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/users/")
@CrossOrigin(origins = "*")
public class SupplierStoreCouponController {

	@Autowired
	private SupplierStoreCouponService service;

	@Operation(summary = "This API is used for creating coupon for a supplier's store")
	@PostMapping("/supplier/store-coupon")
	public ResponseEntity<SuccessResponse> createStoreCoupon(@RequestBody SupplierStoreCouponPojo supplierStoreCoupon) {
		return new ResponseEntity<>(new SuccessResponse(false, CREATE_COUPON_SUCCESS_MESSAGE,
				service.createStoreCoupon(supplierStoreCoupon)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for publishing a drafted supplier store coupon by the supplier")
	@PutMapping("/supplier/store-coupon")
	public ResponseEntity<SuccessResponse> publishStoreCoupon(@RequestParam("storeCouponId") Long storeCouponId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, PUBLISH_COUPON_SUCCESS_MESSAGE, service.publishStoreCoupon(storeCouponId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the supplier store coupon by the supplier")
	@GetMapping("/supplier/store-coupon/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllStoreCoupon(@RequestParam("supplierId") String supplierId,
			@RequestParam(name = "keyword") String keyword, @RequestParam("type") EProductCoupon type,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		List<SupplierStoreCoupon> storeCoupons = service.getAllStoreCoupons(supplierId, pageNumber, pageSize, keyword,
				type);
		if (!storeCoupons.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_COUPON_SUCCESS_MESSAGE, storeCoupons),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, GET_COUPON_FAIL_MESSAGE, storeCoupons),
					HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the supplier store coupon in product page of a customer")
	@GetMapping("/customer/store-coupon")
	public ResponseEntity<SuccessResponse> getAllStoreCouponForCustomer(@RequestParam("supplierId") String supplierId) {
		List<SupplierStoreCoupon> storeCoupons = service.getAllStoreCouponForCustomer(supplierId);
		if (!storeCoupons.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_COUPON_SUCCESS_MESSAGE, storeCoupons),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, GET_COUPON_FAIL_MESSAGE, storeCoupons),
					HttpStatus.OK);
	}

}
