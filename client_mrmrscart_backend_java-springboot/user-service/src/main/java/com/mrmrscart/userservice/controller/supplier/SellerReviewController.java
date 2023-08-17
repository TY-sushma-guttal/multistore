package com.mrmrscart.userservice.controller.supplier;

import static com.mrmrscart.userservice.common.supplier.SellerReviewConstant.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.supplier.ReplySellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewFilterPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewResponsePojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.SellerReviewService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * @author Sudharshan B S
 *
 */

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class SellerReviewController {

	@Autowired
	private SellerReviewService sellerReviewService;

	@Operation(summary = "This API is used for rating a supplier after receiving the product")
	@PostMapping("/customer/rate-seller")
	public ResponseEntity<SuccessResponse> rateSupplier(@RequestBody SellerReviewPojo sellerReviewPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, REVIEW_SAVE_SUCCESS_MESSAGE,
				sellerReviewService.rateSupplier(sellerReviewPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for replying the review of a customer")
	@PutMapping("/customer/reply-customer-review")
	public ResponseEntity<SuccessResponse> replyCustomerReview(@RequestBody ReplySellerReviewPojo reviewPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, REVIEW_REPLY_SUCCESS_MESSAGE,
				sellerReviewService.replyCustomerReview(reviewPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching seller ratings and reviews of supplier from a customer")
	@PostMapping("/supplier/seller-review/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getSellerReview(@RequestParam("supplierId") String supplierId,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize,
			@RequestBody SellerReviewFilterPojo pojo) {
		List<SellerReviewResponsePojo> sellerReviews = sellerReviewService.getAllSellerReviews(supplierId, pageNumber,
				pageSize, pojo);
		if (!sellerReviews.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, REVIEW_GET_SUCCESS_MESSAGE, sellerReviews),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, REVIEW_GET_FAIL_MESSAGE, sellerReviews),
					HttpStatus.OK);
	}
}
