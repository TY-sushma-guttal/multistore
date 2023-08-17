package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.SellerReviewConstant.REVIEW_GET_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.supplier.ESellerReviewFilter;
import com.mrmrscart.userservice.entity.supplier.SellerReviews;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.reseller.ProductVariationNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SellerReviewNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.exception.supplier.UserIdNotFoundException;
import com.mrmrscart.userservice.feign.client.ProductCategoryClient;
import com.mrmrscart.userservice.feign.pojo.SellerReviewProductVariationPojo;
import com.mrmrscart.userservice.feign.response.ProductVariationResponse;
import com.mrmrscart.userservice.pojo.supplier.ReplySellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewFilterPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewResponsePojo;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SellerReviewRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.util.PaginatedResponse;

@Service
public class SellerReviewServiceImpl implements SellerReviewService {

	@Autowired
	private SellerReviewRepository sellerReviewRepository;
	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;
	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;
	@Autowired
	private ProductCategoryClient productCategoryClient;

	@Transactional
	@Override
	public List<SellerReviews> rateSupplier(SellerReviewPojo sellerReviewPojo) {
		/*
		 * to be validated whether customer has purchased that product before rating a
		 * supplier
		 */
		SellerReviews sellerReviews = new SellerReviews();
		BeanUtils.copyProperties(sellerReviewPojo, sellerReviews);
		sellerReviews.setReviewedAt(LocalDateTime.now());
		try {
			/**
			 * following line to fetch supplier not required. Use the supplier id from
			 * orders table
			 */

			SupplierRegistration supplier = findSupplier(sellerReviewPojo.getSupplierId());

			supplier.getSellerReviews().add(sellerReviews);
			SupplierRegistration save = supplierRegistrationRepository.save(supplier);
			return save.getSellerReviews().stream()
					.filter(e -> (e.getReviewedById().equals(sellerReviewPojo.getReviewedById())
							&& e.getSkuId().equals(sellerReviewPojo.getSkuId())))
					.collect(Collectors.toList());

		} catch (SupplierNotFoundException | UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	@Override
	public SellerReviews replyCustomerReview(ReplySellerReviewPojo replySellerReviewPojo) {
		try {
			Optional<SellerReviews> findById = sellerReviewRepository
					.findById(replySellerReviewPojo.getSellerReviewId());
			if (findById.isPresent()) {
				findById.get().setSellerResponse(replySellerReviewPojo.getSellerResponse());
				findById.get().setRepliedAt(LocalDateTime.now());
				return sellerReviewRepository.save(findById.get());
			} else {
				throw new SellerReviewNotFoundException(REVIEW_GET_FAIL_MESSAGE);
			}
		} catch (SellerReviewNotFoundException | UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private List<SellerReviews> filterReviews(List<Long> reviewId, SellerReviewFilterPojo pojo,
			List<SellerReviews> reviews) {

		if ((pojo.getFilterType() == null && pojo.getKeyword() == null)
				|| pojo.getFilterType() == ESellerReviewFilter.NAME && pojo.getKeyword() != null) {
			return reviews;

		} else if (pojo.getFilterType() == ESellerReviewFilter.RATINGS && pojo.getKeyword() != null) {
			return sellerReviewRepository.filterReviewByRatings(reviewId, pojo.getKeyword());
		} else {
			throw new SellerReviewNotFoundException("Provide a valid filter type");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SellerReviewResponsePojo> getAllSellerReviews(String supplierId, int pageNumber, int pageSize,
			SellerReviewFilterPojo pojo) {
		List<SellerReviewResponsePojo> result = new ArrayList<>();
		try {
			SupplierRegistration supplier = findSupplier(supplierId);
			List<Long> id = supplier.getSellerReviews().stream().map(SellerReviews::getSellerReviewsId)
					.collect(Collectors.toList());

			List<SellerReviews> list = filterReviews(id, pojo, supplier.getSellerReviews());
			if (list.isEmpty()) {
				return result;
			} else {
				list.forEach(e -> {
					Optional<CustomerRegistration> findById = customerRegistrationRepository
							.findById(e.getReviewedById());
					if (findById.isPresent()) {
						SellerReviewResponsePojo responsePojo = new SellerReviewResponsePojo();
						BeanUtils.copyProperties(findById.get(), responsePojo);
						responsePojo.setCustomerReview(e.getCustomerReview());
						BeanUtils.copyProperties(e, responsePojo, "customerName", "emailId", "mobileNumber");
						ResponseEntity<ProductVariationResponse> responseEntity = productCategoryClient
								.getProductVariationBySkuId(e.getSkuId());

						if (responseEntity.getBody() != null) {
							SellerReviewProductVariationPojo data = responseEntity.getBody().getData();
							if (data != null) {
								responsePojo.setProductImageUrl(data.getImageUrl());
								responsePojo.setVariationId(data.getVariationId());
								result.add(responsePojo);

							} else {
								throw new ProductVariationNotFoundException("Product Not Found");
							}
						} else {
							throw new ProductVariationNotFoundException("Product Not Found");
						}
					}
				});
				if (pojo.getFilterType() == ESellerReviewFilter.NAME) {
					return (List<SellerReviewResponsePojo>) PaginatedResponse.getPaginatedResponse(result.stream()
							.filter(e -> e.getCustomerName().toLowerCase().contains(pojo.getKeyword().toLowerCase()))
							.collect(Collectors.toList()), pageNumber, pageSize);
				}
				return (List<SellerReviewResponsePojo>) PaginatedResponse.getPaginatedResponse(result, pageNumber,
						pageSize);
			}

		} catch (UserIdNotFoundException | SupplierNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SellerReviewNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration findSupplier(String supplierId) {
		Optional<SupplierRegistration> findById = supplierRegistrationRepository.findById(supplierId);
		if (findById.isPresent()) {
			return findById.get();
		} else {
			throw new SupplierNotFoundException(INVALID_SUPPLIER);
		}
	}

}
