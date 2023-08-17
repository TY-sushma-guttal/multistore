package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.BannerConstant.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.entity.admin.Banner;
import com.mrmrscart.userservice.pojo.admin.BannerPojo;
import com.mrmrscart.userservice.pojo.admin.BannersByDatePojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.BannerService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class BannerController {

	@Autowired
	private BannerService bannerService;

	@PostMapping("/banner")
	public ResponseEntity<SuccessResponse> addBanner(@RequestBody BannerPojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, BANNER_ADDED_SUCCESS_MESSAGE, bannerService.addBanner(data)), HttpStatus.OK);
	}

	@GetMapping("/banner/{id}")
	public ResponseEntity<SuccessResponse> getBanner(@PathVariable Long id) {
		return new ResponseEntity<>(
				new SuccessResponse(false, BANNER_FETCH_SUCCESS_MESSAGE, bannerService.getBanner(id)), HttpStatus.OK);
	}

	@DeleteMapping("/banner/{id}")
	public ResponseEntity<SuccessResponse> deleteBanner(@PathVariable Long id) {
		return new ResponseEntity<>(
				new SuccessResponse(false, BANNER_DELETE_SUCCESS_MESSAGE, bannerService.deleteBanner(id)),
				HttpStatus.OK);
	}

	@GetMapping("/banners")
	public ResponseEntity<SuccessResponse> getAllBanner() {
		return new ResponseEntity<>(
				new SuccessResponse(false, BANNER_FETCH_SUCCESS_MESSAGE, bannerService.getAllBanners()), HttpStatus.OK);
	}

	@PutMapping("/banner")
	public ResponseEntity<SuccessResponse> getBannersByDate(@RequestBody BannersByDatePojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, BANNER_FETCH_SUCCESS_MESSAGE, bannerService.getAllBannersByDate(data)),
				HttpStatus.OK);
	}

	@GetMapping("/supplier/banners")
	public ResponseEntity<SuccessResponse> getAllSupplierBanners(@RequestParam("supplierId") String supplierId) {
		List<Banner> banners = bannerService.getAllSupplierBanners(supplierId);
		if (!banners.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, BANNER_FETCH_SUCCESS_MESSAGE, banners),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, BANNER_FETCH_FAIL_MESSAGE, banners), HttpStatus.OK);
	}

	@PutMapping("/supplier/banner")
	public ResponseEntity<SuccessResponse> upadteBanner(@RequestHeader("userId") String userId,
			@RequestBody BannerPojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Banner Updated Successfully", bannerService.updateBanner(data, userId)),
				HttpStatus.OK);
	}

	@PutMapping("/banner/{id}/{isDisable}")
	public ResponseEntity<SuccessResponse> disableBanner(@PathVariable Long id,@PathVariable boolean isDisable) {
		int disabledBanner = bannerService.disableBanner(id,isDisable);
		if (disabledBanner==0) {
			return new ResponseEntity<>(new SuccessResponse(false, BANNER_DISABLE_SUCCESS_MESSAGE, disabledBanner),
					HttpStatus.OK);
		}else if(disabledBanner==1) {
			return new ResponseEntity<>(new SuccessResponse(false, BANNER_ENABLE_SUCCESS_MESSAGE, disabledBanner),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, BANNER_DISABLE_OR_ENABLE_FAILURE_MESSAGE, disabledBanner),
				HttpStatus.OK);
	}
}
