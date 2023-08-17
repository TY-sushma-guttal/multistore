package com.mrmrscart.userservice.controller.supplier;

import static com.mrmrscart.userservice.common.supplier.CouponConstant.*;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.apache.commons.codec.DecoderException;
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

import com.mrmrscart.userservice.entity.supplier.CouponInfo;
import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.pojo.supplier.CouponInfoPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.CouponInfoService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * @author Sudharshan B S
 *
 */
@RestController
@RequestMapping("/api/v1/users/")
@CrossOrigin(origins = "*")
public class CouponInfoController {

	@Autowired
	private CouponInfoService couponInfoService;

	@Operation(summary = "This API is used for creating a coupon for products")
	@PostMapping("/supplier/product-coupon")
	public ResponseEntity<SuccessResponse> createCoupon(@RequestBody CouponInfoPojo couponInfoPojo)
			throws DecoderException, IOException {
		CouponInfo createCoupon = couponInfoService.createCoupon(couponInfoPojo);
		return new ResponseEntity<>(new SuccessResponse(false, CREATE_COUPON_SUCCESS_MESSAGE, createCoupon),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for publishing a drafted coupon")
	@PutMapping("/supplier/product-coupon/publish")
	public ResponseEntity<SuccessResponse> publishCoupon(@RequestParam("supplierId") String supplierId,
			@RequestParam("couponCode") String couponCode) {
		CouponInfo publishCoupon = couponInfoService.publishCoupon(supplierId, couponCode);
		return new ResponseEntity<>(new SuccessResponse(false, PUBLISH_COUPON_SUCCESS_MESSAGE, publishCoupon),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for getting all the coupons of a particular supplier")
	@GetMapping("/supplier/product-coupon/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllCoupon(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize, @RequestParam("supplierId") String supplierId,
			@RequestParam(name = "keyword") String keyword, @RequestParam("filterType") EProductCoupon filterType) {
		List<CouponInfo> list = couponInfoService.getAllCoupons(supplierId, pageNumber, pageSize, keyword, filterType);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_COUPON_SUCCESS_MESSAGE, list), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, GET_COUPON_FAIL_MESSAGE, list), HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for getting all the coupon for a specific product")
	@GetMapping("/customer/product-coupon")
	public ResponseEntity<SuccessResponse> getCouponsByProduct(
			@QueryParam("productVariationId") String productVariationId, @QueryParam("supplierId") String supplierId) {
		List<CouponInfo> list = couponInfoService.getCouponsByProduct(productVariationId, supplierId);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_PRODUCTCOUPON_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, GET_PRODUCTCOUPON_FAIL_MESSAGE, list), HttpStatus.OK);
		}
	}
}
