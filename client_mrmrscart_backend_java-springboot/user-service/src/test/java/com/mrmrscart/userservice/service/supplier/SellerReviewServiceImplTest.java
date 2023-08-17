package com.mrmrscart.userservice.service.supplier;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.userservice.entity.supplier.SellerReviews;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.SellerReviewNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.feign.client.ProductCategoryClient;
import com.mrmrscart.userservice.pojo.supplier.ReplySellerReviewPojo;
import com.mrmrscart.userservice.pojo.supplier.SellerReviewPojo;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SellerReviewRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;

@ExtendWith(MockitoExtension.class)
public class SellerReviewServiceImplTest {

	@InjectMocks
	private SellerReviewServiceImpl serviceImpl;

	@Mock
	private SellerReviewRepository sellerReviewRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Mock
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Mock
	private ProductCategoryClient productCategoryClient;

	private SupplierRegistration registration;

	@BeforeEach
	public void setUp() {

		registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");
		registration.setSellerReviews(new ArrayList<>());
	}// End of setUp method

	@Test
	public void rateSupplierWithSupplierNotFoundException() {

		SellerReviewPojo pojo = new SellerReviewPojo();

		pojo.setSupplierId("SUP0001");

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(Optional.ofNullable(null));

		SupplierNotFoundException assertThrows = Assertions.assertThrows(SupplierNotFoundException.class,
				() -> serviceImpl.rateSupplier(pojo));

		Assertions.assertEquals("Supplier Not Found!", assertThrows.getMessage());

	}// End of rateSupplierWithSupplierNotFoundException method

	@Test
	public void rateSupplier() {

		SellerReviewPojo pojo = new SellerReviewPojo();

		pojo.setSupplierId("SUP0001");
		pojo.setSkuId("SKU001");
		pojo.setReviewedById("ADM001");

		SellerReviews sellerReviews = new SellerReviews();
		sellerReviews.setSkuId("SKU001");
		sellerReviews.setReviewedById("ADM001");
		sellerReviews.setReviewedAt(LocalDateTime.now());

		List<SellerReviews> list = new ArrayList<>();
		list.add(sellerReviews);

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(Optional.of(registration));

		registration.getSellerReviews().add(sellerReviews);

		when(supplierRegistrationRepository.save(registration)).thenReturn(registration);

		List<SellerReviews> rateSupplier = serviceImpl.rateSupplier(pojo);

		Assertions.assertEquals(rateSupplier.get(0), sellerReviews);

	}// End of rateSupplier method

	@Test
	public void rateSupplierWithException() {

		SellerReviewPojo pojo = new SellerReviewPojo();

		pojo.setSupplierId("SUP0001");
		pojo.setSkuId("SKU001");
		pojo.setReviewedById("ADM001");

		SellerReviews sellerReviews = new SellerReviews();
		sellerReviews.setSkuId("SKU001");
		sellerReviews.setReviewedById("ADM001");
		sellerReviews.setReviewedAt(LocalDateTime.now());

		List<SellerReviews> list = new ArrayList<>();
		list.add(sellerReviews);

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(Optional.of(registration));

		Exception assertThrows = Assertions.assertThrows(Exception.class, () -> serviceImpl.rateSupplier(pojo));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of rateSupplierWithException method

	@Test
	public void replyCustomerReviewWithSellerReviewNotFoundException() {

		ReplySellerReviewPojo pojo = new ReplySellerReviewPojo();
		pojo.setSellerReviewId(1l);
		pojo.setSellerResponse("SELLRES");

		SellerReviews reviews = new SellerReviews();
		reviews.setSellerReviewsId(1l);
		reviews.setSellerResponse("SELLRES");
		reviews.setSkuId("SKU001");

		when(sellerReviewRepository.findById(pojo.getSellerReviewId())).thenReturn(Optional.ofNullable(null));

		SellerReviewNotFoundException assertThrows = Assertions.assertThrows(SellerReviewNotFoundException.class,
				() -> serviceImpl.replyCustomerReview(pojo));

		Assertions.assertEquals("Seller Review Not Found", assertThrows.getMessage());

	}// End of replyCustomerReview method

	@Test
	public void replyCustomerReviewPresentScenario() {

		ReplySellerReviewPojo pojo = new ReplySellerReviewPojo();
		pojo.setSellerReviewId(1l);
		pojo.setSellerResponse("SELLRES");

		SellerReviews reviews = new SellerReviews();
		reviews.setSellerReviewsId(1l);
		reviews.setSellerResponse("SELLRES");
		reviews.setSkuId("SKU001");

		when(sellerReviewRepository.findById(pojo.getSellerReviewId())).thenReturn(Optional.ofNullable(reviews));

		reviews.setReviewedAt(LocalDateTime.now());

		when(sellerReviewRepository.save(reviews)).thenReturn(reviews);

		SellerReviews replyCustomerReview = serviceImpl.replyCustomerReview(pojo);

		Assertions.assertEquals(reviews, replyCustomerReview);

	}// End of replyCustomerReviewPresentScenario method

	@Test
	public void replyCustomerReviewException() {

		ReplySellerReviewPojo pojo = new ReplySellerReviewPojo();
		pojo.setSellerReviewId(1l);
		pojo.setSellerResponse("SELLRES");

		SellerReviews reviews = new SellerReviews();
		reviews.setSellerReviewsId(1l);
		reviews.setSellerResponse("SELLRES");
		reviews.setSkuId("SKU001");

		when(sellerReviewRepository.findById(pojo.getSellerReviewId())).thenThrow(NullPointerException.class);

		reviews.setReviewedAt(LocalDateTime.now());

		lenient().when(sellerReviewRepository.save(reviews)).thenReturn(reviews);

		Exception assertThrows = Assertions.assertThrows(Exception.class, () -> serviceImpl.replyCustomerReview(pojo));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of replyCustomerReviewException method

}// End of SellerReviewServiceImplTest class
