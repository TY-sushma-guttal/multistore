package com.mrmrscart.userservice.service.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.userservice.entity.supplier.CouponInfo;
import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.CouponNotCreatedException;
import com.mrmrscart.userservice.exception.supplier.CouponNotFoundException;
import com.mrmrscart.userservice.exception.supplier.InvalidCouponCodeException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.pojo.supplier.CouponInfoPojo;
import com.mrmrscart.userservice.repository.supplier.CouponInfoRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;

@ExtendWith(MockitoExtension.class)
public class CouponInfoServiceImplTest {

	@InjectMocks
	private CouponInfoServiceImpl service;

	@Mock
	private CouponInfoRepository couponInfoRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@BeforeEach
	public void setUp() {

	}// End of setUp method

	@Test
	public void createCoupon() {

		CouponInfoPojo couponInfoPojo = new CouponInfoPojo();
		couponInfoPojo.setDiscountType(EProductCoupon.DRAFT);
		couponInfoPojo.setCouponStatus(EProductCoupon.DRAFT);

		CouponInfo couponInfo = new CouponInfo();

		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(couponInfoPojo.getDiscountType().name());
		couponInfo.setCouponStatus(couponInfoPojo.getCouponStatus().name());
		couponInfo.setCouponCode("CH001");

		when(couponInfoRepository.save(Mockito.any())).thenReturn(couponInfo);

		CouponInfo createCoupon = service.createCoupon(couponInfoPojo);

		assertThat(createCoupon.getCouponCode()).isEqualTo(couponInfo.getCouponCode());

		couponInfoPojo.setDiscountType(EProductCoupon.PUBLISHED);
		couponInfoPojo.setCouponStatus(EProductCoupon.PUBLISHED);

		couponInfo.setPublishedAt(LocalDateTime.now());

		when(couponInfoRepository.save(Mockito.any())).thenReturn(couponInfo);

		CouponInfo createCoupon1 = service.createCoupon(couponInfoPojo);

		assertThat(createCoupon1.getCouponCode()).isEqualTo(couponInfo.getCouponCode());

		couponInfoPojo.setDiscountType(EProductCoupon.ALL);
		couponInfoPojo.setCouponStatus(EProductCoupon.ALL);

		Assertions.assertThrows(CouponNotCreatedException.class, () -> service.createCoupon(couponInfoPojo));

		CouponNotCreatedException couponNotCreatedException = Assertions.assertThrows(CouponNotCreatedException.class,
				() -> service.createCoupon(couponInfoPojo));

		Assertions.assertEquals("Provide A Valid Coupon Status", couponNotCreatedException.getMessage());

		SupplierException exception = Assertions.assertThrows(SupplierException.class,
				() -> service.createCoupon(null));

		Assertions.assertEquals("Something Went Wrong. ", exception.getMessage());

	}// End of createCoupon method

	@Test
	public void publishCoupon() {

		CouponInfoPojo couponInfoPojo = new CouponInfoPojo();
		couponInfoPojo.setDiscountType(EProductCoupon.PUBLISHED);
		couponInfoPojo.setCouponStatus(EProductCoupon.PUBLISHED);

		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP0001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(couponInfoPojo.getDiscountType().name());
		couponInfo.setCouponCode("CH001");

		when(couponInfoRepository.findByCouponCodeAndCreatedBy("CH001", "SUP0001")).thenReturn(couponInfo);

		couponInfo.setCouponStatus(EProductCoupon.PUBLISHED.name());
		couponInfo.setPublishedAt(LocalDateTime.now());

		when(couponInfoRepository.save(Mockito.any())).thenReturn(couponInfo);

		CouponInfo publishCoupon = service.publishCoupon("SUP0001", "CH001");

		assertThat(publishCoupon.getCouponCode()).isEqualTo(couponInfo.getCouponCode());

		InvalidCouponCodeException assertThrows = Assertions.assertThrows(InvalidCouponCodeException.class,
				() -> service.publishCoupon(null, null));

		Assertions.assertEquals("Provide a valid coupon code for the supplier. ", assertThrows.getMessage());

	}// End of publishCoupon method

	@Test
	public void getCouponsByProduct() {

		List<CouponInfo> couponInfos = new ArrayList<>();

		List<String> list = new ArrayList<>();
		list.add("123");
		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP0001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(EProductCoupon.PUBLISHED.name());
		couponInfo.setCouponCode("CH001");
		couponInfo.setProductsIncluded(list);
		couponInfo.setCouponStatus(EProductCoupon.PUBLISHED.name());
		couponInfo.setCouponExpiryDate(LocalDate.MAX);

		couponInfos.add(couponInfo);

		when(couponInfoRepository.findByCreatedBy("SUP0001")).thenReturn(couponInfos);

		List<CouponInfo> couponsByProduct = service.getCouponsByProduct("123", "SUP0001");

		assertThat(couponsByProduct).isEqualTo(couponInfos);

		CouponNotFoundException assertThrows = Assertions.assertThrows(CouponNotFoundException.class,
				() -> service.getCouponsByProduct(null, null));

		Assertions.assertEquals("There Are No Coupons For The Supplier. ", assertThrows.getMessage());

	}// End of getCouponsByProduct method

	@Test
	public void getAllCoupons() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<CouponInfo> couponInfos = new ArrayList<>();

		List<String> list = new ArrayList<>();
		list.add("123");

		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP0001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(EProductCoupon.ALL.name());
		couponInfo.setCouponCode("CH001");
		couponInfo.setProductsIncluded(list);
		couponInfo.setCouponStatus(EProductCoupon.ALL.name());
		couponInfo.setCouponExpiryDate(LocalDate.MAX);

		couponInfos.add(couponInfo);

		when(couponInfoRepository.findByCreatedBy("SUP0001")).thenReturn(couponInfos);

		List<CouponInfo> allCoupons = service.getAllCoupons("SUP0001", 0, 1, null, EProductCoupon.ALL);

		assertThat(allCoupons).isEqualTo(couponInfos);

		InvalidCouponCodeException assertThrows = Assertions.assertThrows(InvalidCouponCodeException.class,
				() -> service.getAllCoupons("SUP0001", 0, 1, "CH001", EProductCoupon.CASH));

		Assertions.assertEquals("Provide a valid filter type", assertThrows.getMessage());

		SupplierNotFoundException exception = Assertions.assertThrows(SupplierNotFoundException.class,
				() -> service.getAllCoupons(null, 0, 1, "CH001", EProductCoupon.CASH));

		Assertions.assertEquals("Supplier Not Found!", exception.getMessage());

	}// End of getAllCoupons method

	@Test
	public void getAllCouponsI() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<CouponInfo> couponInfos = new ArrayList<>();

		List<String> list = new ArrayList<>();
		list.add("123");

		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(EProductCoupon.COUPON_CODE.name());
		couponInfo.setCouponCode("CH001");
		couponInfo.setProductsIncluded(list);
		couponInfo.setCouponStatus(EProductCoupon.COUPON_CODE.name());
		couponInfo.setCouponExpiryDate(LocalDate.MAX);

		couponInfos.add(couponInfo);

		when(couponInfoRepository.findByCreatedByAndCouponCodeIgnoreCaseContaining("SUP001", "CH001"))
				.thenReturn(couponInfos);

		List<CouponInfo> allCoupons = service.getAllCoupons("SUP001", 0, 1, "CH001", EProductCoupon.COUPON_CODE);

		assertThat(allCoupons).isEqualTo(couponInfos);

	}// End of getAllCouponsI method

	@Test
	public void getAllCouponsII() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<CouponInfo> couponInfos = new ArrayList<>();

		List<String> list = new ArrayList<>();
		list.add("123");

		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(EProductCoupon.STATUS.name());
		couponInfo.setCouponCode("CH001");
		couponInfo.setProductsIncluded(list);
		couponInfo.setCouponStatus(EProductCoupon.STATUS.name());
		couponInfo.setCouponExpiryDate(LocalDate.MAX);

		couponInfos.add(couponInfo);

		when(couponInfoRepository.findByCreatedByAndIgnoreCaseCouponStatusContaining("SUP001",
				EProductCoupon.STATUS.name())).thenReturn(couponInfos);

		List<CouponInfo> allCoupons = service.getAllCoupons("SUP001", 0, 1, EProductCoupon.STATUS.name(),
				EProductCoupon.STATUS);

		assertThat(allCoupons).isEqualTo(couponInfos);

	}// End of getAllCouponsII method

	@Test
	public void getAllCouponsIII() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<CouponInfo> couponInfos = new ArrayList<>();

		List<String> list = new ArrayList<>();
		list.add("123");

		CouponInfo couponInfo = new CouponInfo();
		couponInfo.setCreatedBy("SUP001");
		couponInfo.setCouponId(1l);
		couponInfo.setDiscountType(EProductCoupon.DISCOUNT_TYPE.name());
		couponInfo.setCouponCode("CH001");
		couponInfo.setProductsIncluded(list);
		couponInfo.setCouponStatus(EProductCoupon.DISCOUNT_TYPE.name());
		couponInfo.setCouponExpiryDate(LocalDate.MAX);

		couponInfos.add(couponInfo);

		when(couponInfoRepository.findByCreatedByAndIgnoreCaseDiscountTypeContaining("SUP001",
				EProductCoupon.DISCOUNT_TYPE.name())).thenReturn(couponInfos);

		List<CouponInfo> allCoupons = service.getAllCoupons("SUP001", 0, 1, EProductCoupon.DISCOUNT_TYPE.name(),
				EProductCoupon.DISCOUNT_TYPE);

		assertThat(allCoupons).isEqualTo(couponInfos);

	}// End of getAllCouponsIII method

}// End of CouponInfoServiceImplTest class
