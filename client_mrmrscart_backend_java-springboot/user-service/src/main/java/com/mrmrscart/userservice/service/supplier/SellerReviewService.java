package com.mrmrscart.userservice.service.supplier;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.SellerReviews;
import com.mrmrscart.userservice.pojo.supplier.ReplySellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewFilterPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewResponsePojo;

public interface SellerReviewService {

	public List<SellerReviews> rateSupplier(SellerReviewPojo sellerReviewPojo);

	public SellerReviews replyCustomerReview(ReplySellerReviewPojo replySellerReviewPojo);

	public List<SellerReviewResponsePojo> getAllSellerReviews(String supplierId, int pageNumber, int pageSize,
			SellerReviewFilterPojo pojo);

}
