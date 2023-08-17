package com.mrmrscart.userservice.service.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.supplier.CouponNotFoundException;
import com.mrmrscart.userservice.exception.supplier.DuplicateCouponCodeException;
import com.mrmrscart.userservice.exception.supplier.InvalidCouponCodeException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreCouponPojo;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreCouponRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreInfoRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierStoreCouponServiceImplTest {

	@InjectMocks
	private SupplierStoreCouponServiceImpl couponServiceImpl;

	@Mock
	private SupplierStoreCouponRepository supplierStoreCouponRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Mock
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	private SupplierRegistration registration;

	private SupplierStoreInfo storeInfo;

	@BeforeEach
	public void setUp() {

		registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		storeInfo = new SupplierStoreInfo();
		storeInfo.setSupplierStoreInfoId(1l);
		storeInfo.setSupplierStoreCode("SUPSTRCODE1");
		storeInfo.setStoreActive(true);
		storeInfo.setActiveProductCount(50);
		

		registration.setSupplierStoreInfo(storeInfo);

	}// End of setUp method

	@Test
	public void createStoreCoupon() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCouponPojo couponPojo = new SupplierStoreCouponPojo();
		couponPojo.setSupplierId("SUP0001");
		couponPojo.setStoreCouponCode("SUPSTRCODE1");
		couponPojo.setDiscountType(EProductCoupon.PUBLISHED);
		couponPojo.setCouponStatus(EProductCoupon.PUBLISHED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE2");
		supplierStoreCoupon.setDiscountType(couponPojo.getDiscountType().name());
		supplierStoreCoupon.setCouponStatus(couponPojo.getCouponStatus().name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierStoreInfoRepository.save(storeInfo)).thenReturn(storeInfo);

		SupplierStoreInfo createStoreCoupon = couponServiceImpl.createStoreCoupon(couponPojo);

		assertThat(createStoreCoupon.getSupplierStoreCode()).isEqualTo(couponPojo.getStoreCouponCode());

	}// End of createStoreCoupon method

	@Test
	public void createStoreCouponWithSupplierIdNotFoundException() {

		registration.setStatus(ESupplierStatus.APPROVED);
		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");

		storeInfo.setSupplierStoreCoupons(List.of(supplierStoreCoupon));

		SupplierStoreCouponPojo couponPojo = new SupplierStoreCouponPojo();
		couponPojo.setSupplierId("SUP0001");
		couponPojo.setStoreCouponCode("SUPSTRCODE1");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(null);

		SupplierIdNotFoundException assertThrows = Assertions.assertThrows(SupplierIdNotFoundException.class,
				() -> couponServiceImpl.createStoreCoupon(couponPojo));

		Assertions.assertEquals("Supplier Not Found!", assertThrows.getMessage());

	}// End of createStoreCouponWithSupplierIdNotFoundException method

	@Test
	public void createStoreCouponWithDuplicateCouponCodeException() {

		registration.setStatus(ESupplierStatus.APPROVED);
		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");

		storeInfo.setSupplierStoreCoupons(List.of(supplierStoreCoupon));

		SupplierStoreCouponPojo couponPojo = new SupplierStoreCouponPojo();
		couponPojo.setSupplierId("SUP0001");
		couponPojo.setStoreCouponCode("SUPSTRCODE1");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		DuplicateCouponCodeException assertThrows = Assertions.assertThrows(DuplicateCouponCodeException.class,
				() -> couponServiceImpl.createStoreCoupon(couponPojo));

		Assertions.assertEquals("Coupon code already exists!", assertThrows.getMessage());

	}// End of createStoreCouponWithDuplicateCouponCodeException method

	@Test
	public void createStoreCouponWithSupplierException() {

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");

		storeInfo.setSupplierStoreCoupons(List.of(supplierStoreCoupon));

		SupplierStoreCouponPojo couponPojo = new SupplierStoreCouponPojo();
		couponPojo.setSupplierId("SUP0001");
		couponPojo.setStoreCouponCode("SUPSTRCODE");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		SupplierException assertThrows = Assertions.assertThrows(SupplierException.class,
				() -> couponServiceImpl.createStoreCoupon(couponPojo));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of createStoreCouponWithSupplierException class

	@Test
	public void publishStoreCoupon() {

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setCouponStatus(EProductCoupon.DRAFT.name());

		when(supplierStoreCouponRepository.findByStoreCouponIdAndCouponStatus(1l, EProductCoupon.DRAFT.name()))
				.thenReturn(supplierStoreCoupon);

		supplierStoreCoupon.setCouponStatus(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		when(supplierStoreCouponRepository.save(supplierStoreCoupon)).thenReturn(supplierStoreCoupon);

		SupplierStoreCoupon publishStoreCoupon = couponServiceImpl.publishStoreCoupon(1l);

		Assertions.assertEquals(publishStoreCoupon.getStoreCouponId(), supplierStoreCoupon.getStoreCouponId());

	}// End of publishStoreCoupon method

	@Test
	public void publishStoreCouponWithCouponNotFoundException() {

		when(supplierStoreCouponRepository.findByStoreCouponIdAndCouponStatus(1l, EProductCoupon.DRAFT.name()))
				.thenReturn(null);

		CouponNotFoundException assertThrows = Assertions.assertThrows(CouponNotFoundException.class,
				() -> couponServiceImpl.publishStoreCoupon(1l));

		Assertions.assertEquals("Supplier store coupon not found", assertThrows.getMessage());

	}// End of publishStoreCouponWithCouponNotFoundException method

	@Test
	public void publishStoreCouponWithSupplierException() {

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setCouponStatus(EProductCoupon.DRAFT.name());

		when(supplierStoreCouponRepository.findByStoreCouponIdAndCouponStatus(1l, EProductCoupon.DRAFT.name()))
				.thenReturn(supplierStoreCoupon);

		supplierStoreCoupon.setCouponStatus(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		when(supplierStoreCouponRepository.save(supplierStoreCoupon)).thenReturn(supplierStoreCoupon);

		Exception assertThrows = Assertions.assertThrows(Exception.class,
				() -> couponServiceImpl.publishStoreCoupon(0l));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of publishStoreCouponWithSupplierException method

	@Test
	public void getAllStoreCouponForCustomer() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCouponForCustomer = couponServiceImpl.getAllStoreCouponForCustomer("SUP0001");

		Assertions.assertEquals(allStoreCouponForCustomer, infos);

	}// End of getAllStoreCouponForCustomer method

	@Test
	public void getAllStoreCouponForCustomerWithSupplierIdNotFoundException() {

		registration.setStatus(ESupplierStatus.APPROVED);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(null);

		SupplierIdNotFoundException assertThrows = Assertions.assertThrows(SupplierIdNotFoundException.class,
				() -> couponServiceImpl.getAllStoreCouponForCustomer("SUP0001"));

		Assertions.assertEquals("Supplier Not Found!", assertThrows.getMessage());

	}// End of getAllStoreCouponForCustomerWithSupplierIdNotFoundException method

	@Test
	public void getAllStoreCouponForCustomerWithSupplierException() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.PUBLISHED.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		Exception assertThrows = Assertions.assertThrows(Exception.class,
				() -> couponServiceImpl.getAllStoreCouponForCustomer("SUP001"));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of getAllStoreCouponForCustomerWithSupplierException method

	@Test
	public void getAllStoreWithSupplierException() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.DRAFT.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.DRAFT.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		SupplierException assertThrows = Assertions.assertThrows(SupplierException.class,
				() -> couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 0, "SUPSTRCODE1", EProductCoupon.DRAFT));

		Assertions.assertEquals("Something Went Wrong. ", assertThrows.getMessage());

	}// End of getAllStoreWithSupplierException

	@Test
	public void getAllStoreWithInvalidCouponCodeException() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.DRAFT.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.DRAFT.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		InvalidCouponCodeException assertThrows = Assertions.assertThrows(InvalidCouponCodeException.class,
				() -> couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 0, "SUPSTRCODE1", EProductCoupon.DRAFT));

		Assertions.assertEquals("Provide a valid filter type", assertThrows.getMessage());

	}// End of getAllStoreWithInvalidCouponCodeException method

	@Test
	public void getAllStoreWithStoreCouponStore() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.STORE_COUPON_CODE.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.STORE_COUPON_CODE.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCoupons = couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 1, "SUPSTRCODE1",
				EProductCoupon.STORE_COUPON_CODE);

		Assertions.assertEquals(infos, allStoreCoupons);

	}// End of getAllStoreWithStoreCouponStore method

	@Test
	public void getAllStoreWithNullCase() {

		registration.setStatus(ESupplierStatus.APPROVED);
		registration.getSupplierStoreInfo().setSupplierStoreCoupons(new ArrayList<>());

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCoupons = couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 1, "SUPSTRCODE1",
				EProductCoupon.DISCOUNT_TYPE);

		Assertions.assertEquals(allStoreCoupons, registration.getSupplierStoreInfo().getSupplierStoreCoupons());

	}// End of getAllStoreWithStoreCouponStore method

	@Test
	public void getAllStoreWithEProductCouponAll() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.ALL.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.ALL.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCoupons = couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 1, "SUPSTRCODE1",
				EProductCoupon.ALL);

		Assertions.assertEquals(infos, allStoreCoupons);

	}// End of getAllStoreWithEProductCouponAll method

	@Test
	public void getAllStoreWithEProductCouponStatus() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.STATUS.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.STATUS.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCoupons = couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 1,
				EProductCoupon.STATUS.name(), EProductCoupon.STATUS);

		Assertions.assertEquals(infos, allStoreCoupons);

	}// End of getAllStoreWithEProductCouponStatus method

	@Test
	public void getAllStoreWithEProductCouponDiscountType() {

		registration.setStatus(ESupplierStatus.APPROVED);

		SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
		supplierStoreCoupon.setStoreCouponId(1l);
		supplierStoreCoupon.setStoreCouponCode("SUPSTRCODE1");
		supplierStoreCoupon.setDiscountType(EProductCoupon.DISCOUNT_TYPE.name());
		supplierStoreCoupon.setCouponStatus(EProductCoupon.DISCOUNT_TYPE.name());
		supplierStoreCoupon.setPublishedAt(LocalDateTime.now());

		List<SupplierStoreCoupon> infos = new ArrayList<>();
		infos.add(supplierStoreCoupon);

		storeInfo.setSupplierStoreCoupons(infos);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<SupplierStoreCoupon> allStoreCoupons = couponServiceImpl.getAllStoreCoupons("SUP0001", 0, 1,
				EProductCoupon.DISCOUNT_TYPE.name(), EProductCoupon.DISCOUNT_TYPE);

		Assertions.assertEquals(infos, allStoreCoupons);

	}// End of getAllStoreWithEProductCouponDiscountType method

}// End of SupplierStoreCouponServiceImplTest class
