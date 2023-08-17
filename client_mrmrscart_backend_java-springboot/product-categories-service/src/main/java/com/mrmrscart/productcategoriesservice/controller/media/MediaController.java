package com.mrmrscart.productcategoriesservice.controller.media;

import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.ADMIN;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.CATEGORY;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.MEDIA_SUCCESS;

import java.io.IOException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mrmrscart.productcategoriesservice.response.media.MediaResponse;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.media.MediaService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class MediaController {

	@Autowired
	private MediaService mediaService;

	@Operation(summary = "This API is used for uploading image for a category or set or subcategory")
	@PutMapping("/category-media")
	public ResponseEntity<MediaResponse> uploadCategoryMedia(@RequestParam MultipartFile media,
			@RequestParam String categoryType) {
		String mediaUrl = mediaService.uploadMedia(ADMIN, CATEGORY, categoryType, media);
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, mediaUrl), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading images related to products")
	@PutMapping("/product-media")
	public ResponseEntity<MediaResponse> uploadProductMedia(@RequestParam List<MultipartFile> medias,
			@RequestParam String supplierId) {
		List<String> uploadProductMedia = mediaService.uploadProductMedia(supplierId, medias);
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, uploadProductMedia), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading media related to flag")
	@PutMapping("/flag-media")
	public ResponseEntity<MediaResponse> uploadFlagMedia(@RequestParam MultipartFile media,
			@RequestParam String supplierId) {
		String mediaUrl = mediaService.uploadFlagMedia(media, supplierId);
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, mediaUrl), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for converting image url to base64")
	@PostMapping("/url-to-baseurl")
	public ResponseEntity<MediaResponse> sendUrlGetBaseUrl(@RequestParam String data) throws IOException {
		return new ResponseEntity<>(
				new MediaResponse(false, "Media converted successfully. ", mediaService.sendUrlGetBaseUrl(data)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading invoice and trademark auth letters related to products")
	@PutMapping("/trademark-invoice-media")
	public ResponseEntity<MediaResponse> uploadInvoiceAndTrademarkMedia(@RequestParam List<MultipartFile> medias,
			@RequestParam String supplierId, @RequestParam String documentType) {
		List<String> uploadProductMedia = mediaService.uploadInvoiceAndTrademarkMedia(supplierId, medias, documentType);
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, uploadProductMedia), HttpStatus.OK);
	}

	@DeleteMapping("/media")
	public ResponseEntity<MediaResponse> deleteImageByUrl(@RequestParam String objectUrl) {
		return new ResponseEntity<>(
				new MediaResponse(false, "Image Deleted Successfully", mediaService.deleteImageObjectByUrl(objectUrl)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method upload the assets in the S3 bucket")
	@PutMapping("/media/asset")
	public ResponseEntity<MediaResponse> uploadAssets(@RequestParam MultipartFile[] imageList) {
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, mediaService.uploadAsste(imageList)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading supplier store logo and store related image")
	@PutMapping("/supplier-store-configuration")
	public ResponseEntity<MediaResponse> uploadStoreConfigurationImages(

			@RequestParam(name = "supplierLogo", required = false) MultipartFile supplierLogo,
			@RequestParam(name = "storeImage", required = false) MultipartFile storeImage,
			@RequestParam(required = true, name = "supplierId") String supplierId) {
		return new ResponseEntity<>(
				new MediaResponse(false, MEDIA_SUCCESS,
						mediaService.uploadStoreConfigurationImages(supplierLogo, storeImage, supplierId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading supplier profile image")
	@PutMapping("/supplier/supplier-profile")
	public ResponseEntity<MediaResponse> uploadSupplierProfileImage(
			@RequestParam("profileImage") MultipartFile profileImage,
			@RequestParam(required = true, name = "supplierId") String supplierId) {
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS,
				mediaService.uploadSupplierProfileImage(profileImage, supplierId)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for uploading banner image for a store ")
	@PutMapping("/supplier/banner-media")
	public ResponseEntity<MediaResponse> uploadBannerImage(@RequestParam MultipartFile media,
			@RequestParam(required = true, name = "userId") String userId) {
		String mediaUrl = mediaService.uploadBannerImage(media, userId);
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS, mediaUrl), HttpStatus.OK);
	}

	@Operation(summary = "This api will accepts multiple base url and return s3 bucket object url")
	@PutMapping("/supplier/product-media/{supplierId}")
	public ResponseEntity<MediaResponse> uploadBase64Url(@RequestBody List<String> baseUrlList,
			@PathVariable String supplierId) {
		return new ResponseEntity<>(
				new MediaResponse(false, MEDIA_SUCCESS, mediaService.getImageUrlFromBaseUrl(baseUrlList, supplierId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will help us to get immediate localdate time. ")
	@GetMapping("/local-date-time")
	public ResponseEntity<SuccessResponse> getLocalDateTime() {
		return new ResponseEntity<>(new SuccessResponse(false, "Time Fetched Successfully", LocalDateTime.now()),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will upload the help support images")
	@PutMapping("/media/help-support")
	public ResponseEntity<MediaResponse> uploadHelpSupportMedia(@RequestParam MultipartFile[] imageList,
			@RequestParam String ticketCreatedByType) {
		return new ResponseEntity<>(new MediaResponse(false, MEDIA_SUCCESS,
				mediaService.uploadHelpSupportUrl(imageList, ticketCreatedByType)), HttpStatus.OK);
	}

	@Operation(summary = "This api will upload the notification files")
	@PutMapping("/media/supplier/notification")
	public ResponseEntity<MediaResponse> uploadNotificationMedia(@RequestParam MultipartFile[] file,
			@RequestParam String supplierId) {
		return new ResponseEntity<>(
				new MediaResponse(false, MEDIA_SUCCESS, mediaService.uploadNotificationMedia(file, supplierId)),
				HttpStatus.OK);
	}
}
