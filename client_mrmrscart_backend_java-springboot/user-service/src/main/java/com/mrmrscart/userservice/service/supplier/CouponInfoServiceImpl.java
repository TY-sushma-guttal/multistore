package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.CouponConstant.*;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.mrmrscart.userservice.util.PaginatedResponse;

@Service
public class CouponInfoServiceImpl implements CouponInfoService {

	@Autowired
	private CouponInfoRepository couponInfoRepository;
	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Transactional
	@Override
	public CouponInfo createCoupon(CouponInfoPojo couponInfoPojo) {

		CouponInfo couponInfo = new CouponInfo();
		try {
			BeanUtils.copyProperties(couponInfoPojo, couponInfo);
			couponInfo.setDiscountType(couponInfoPojo.getDiscountType().name());
			couponInfo.setCouponStatus(couponInfoPojo.getCouponStatus().name());
			couponInfo.setCouponCode("CH001");
			if (Objects.equals(couponInfo.getCouponStatus(), EProductCoupon.DRAFT.name())) {
				return couponInfoRepository.save(couponInfo);
			} else if (Objects.equals(couponInfo.getCouponStatus(), EProductCoupon.PUBLISHED.name())) {
				couponInfo.setPublishedAt(LocalDateTime.now());
				return couponInfoRepository.save(couponInfo);
			} else {
				throw new CouponNotCreatedException(INVALID_COUPON_STATUS);
			}
		} catch (CouponNotCreatedException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	@Override
	public CouponInfo publishCoupon(String supplierId, String couponCode) {
		try {
			CouponInfo couponInfo = couponInfoRepository.findByCouponCodeAndCreatedBy(couponCode, supplierId);
			if (couponInfo != null) {
				couponInfo.setCouponStatus(EProductCoupon.PUBLISHED.name());
				couponInfo.setPublishedAt(LocalDateTime.now());
				return couponInfoRepository.save(couponInfo);
			} else {
				throw new InvalidCouponCodeException(INVALID_COUPON_CODE);
			}
		} catch (InvalidCouponCodeException e) {
			throw new InvalidCouponCodeException(INVALID_COUPON_CODE);
		}
	}

	@Transactional
	@Override
	public List<CouponInfo> getCouponsByProduct(String productVariationId, String supplierId) {

		try {
			List<CouponInfo> couponsBySupplier = couponInfoRepository.findByCreatedBy(supplierId);
			if (!couponsBySupplier.isEmpty()) {
				List<CouponInfo> arrayList = new ArrayList<>();
				couponsBySupplier.stream().forEach(e -> {

					List<String> collect = e.getProductsIncluded().stream().filter(f -> f.equals(productVariationId))
							.collect(Collectors.toList());
					if (!collect.isEmpty()) {
						arrayList.add(e);
					}
				});
				return arrayList.stream()
						.filter(h -> Objects.equals(h.getCouponStatus(), EProductCoupon.PUBLISHED.name())
								&& h.getCouponExpiryDate().compareTo(LocalDate.now()) > 0)
						.collect(Collectors.toList());

			} else {
				throw new CouponNotFoundException(GET_COUPON_FAIL_MESSAGE);
			}
		} catch (CouponNotFoundException e) {
			throw new CouponNotFoundException(GET_COUPON_FAIL_MESSAGE);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CouponInfo> getAllCoupons(String supplierId, int pageNumber, int pageSize, String keyword,
			EProductCoupon type) {
		try {
			findSupplier(supplierId);
			if (type == EProductCoupon.ALL) {

				return (List<CouponInfo>) PaginatedResponse
						.getPaginatedResponse(couponInfoRepository.findByCreatedBy(supplierId), pageNumber, pageSize);
				
			} else if ((type == EProductCoupon.COUPON_CODE) && keyword != null && !keyword.isBlank()) {
				
				return (List<CouponInfo>) PaginatedResponse.getPaginatedResponse(
						couponInfoRepository.findByCreatedByAndCouponCodeIgnoreCaseContaining(supplierId, keyword),
						pageNumber, pageSize);
				
			} else if (type == EProductCoupon.STATUS && keyword != null && !keyword.isBlank()) {
				return (List<CouponInfo>) PaginatedResponse.getPaginatedResponse(
						couponInfoRepository.findByCreatedByAndIgnoreCaseCouponStatusContaining(supplierId, keyword),
						pageNumber, pageSize);
			} else if (type == EProductCoupon.DISCOUNT_TYPE && keyword != null && !keyword.isBlank()) {
				return (List<CouponInfo>) PaginatedResponse.getPaginatedResponse(
						couponInfoRepository.findByCreatedByAndIgnoreCaseDiscountTypeContaining(supplierId, keyword),
						pageNumber, pageSize);
			} else {
				throw new InvalidCouponCodeException(INVALID_FILTER_TYPE);
			}
		} catch (InvalidCouponCodeException | SupplierNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration findSupplier(String supplierId) {
		try {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
			if (supplierRegistration != null) {
				return supplierRegistration;
			} else {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
		} catch (SupplierNotFoundException e) {
			throw new SupplierNotFoundException(INVALID_SUPPLIER);
		}
	}

}
