
package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.CouponConstant.*;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.mrmrscart.userservice.util.PaginatedResponse;

@Service
public class SupplierStoreCouponServiceImpl implements SupplierStoreCouponService {

	@Autowired
	private SupplierStoreCouponRepository supplierStoreCouponRepository;
	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;
	@Autowired
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	@Override
	public SupplierStoreInfo createStoreCoupon(SupplierStoreCouponPojo storeCouponPojo) {

		try {
			SupplierRegistration findSupplier = findSupplier(storeCouponPojo.getSupplierId());
			SupplierStoreInfo supplierStoreInfo = findSupplier.getSupplierStoreInfo();
			if (!supplierStoreInfo.getSupplierStoreCoupons().isEmpty()) {
				List<SupplierStoreCoupon> list = supplierStoreInfo.getSupplierStoreCoupons().stream()
						.filter(e -> e.getStoreCouponCode().equalsIgnoreCase(storeCouponPojo.getStoreCouponCode()))
						.collect(Collectors.toList());
				if (!list.isEmpty()) {
					throw new DuplicateCouponCodeException(DUPLICATE_COUPON_CODE);
				}
			}
			SupplierStoreCoupon supplierStoreCoupon = new SupplierStoreCoupon();
			BeanUtils.copyProperties(storeCouponPojo, supplierStoreCoupon);
			supplierStoreCoupon.setDiscountType(storeCouponPojo.getDiscountType().name());
			supplierStoreCoupon.setCouponStatus(storeCouponPojo.getCouponStatus().name());
			if (storeCouponPojo.getCouponStatus() == EProductCoupon.PUBLISHED) {
				supplierStoreCoupon.setPublishedAt(LocalDateTime.now());
			}
			supplierStoreInfo.getSupplierStoreCoupons().add(supplierStoreCoupon);
			return supplierStoreInfoRepository.save(supplierStoreInfo);
		} catch (SupplierIdNotFoundException | DuplicateCouponCodeException e) {
			throw e;
		} catch (Exception exception) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration findSupplier(String supplierId) {
		SupplierRegistration supplierRegistration = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierId,
				ESupplierStatus.APPROVED);
		if (supplierRegistration != null) {
			return supplierRegistration;
		} else {
			throw new SupplierIdNotFoundException(INVALID_SUPPLIER);
		}
	}

	@Override
	public SupplierStoreCoupon publishStoreCoupon(Long storeCouponId) {
		try {
			SupplierStoreCoupon couponStatus = supplierStoreCouponRepository
					.findByStoreCouponIdAndCouponStatus(storeCouponId, EProductCoupon.DRAFT.name());
			if (couponStatus != null) {
				couponStatus.setCouponStatus(EProductCoupon.PUBLISHED.name());
				couponStatus.setPublishedAt(LocalDateTime.now());
				return supplierStoreCouponRepository.save(couponStatus);
			} else {
				throw new CouponNotFoundException(INVALID_STORE_COUPON);
			}
		} catch (CouponNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SupplierStoreCoupon> getAllStoreCoupons(String supplierId, int pageNumber, int pageSize, String keyword,
			EProductCoupon type) {
		List<SupplierStoreCoupon> coupons = new ArrayList<>();
		try {
			SupplierRegistration supplier = findSupplier(supplierId);
			List<SupplierStoreCoupon> supplierStoreCoupons = supplier.getSupplierStoreInfo().getSupplierStoreCoupons();

			if (!supplierStoreCoupons.isEmpty()) {
				if (type == EProductCoupon.ALL) {

					return (List<SupplierStoreCoupon>) PaginatedResponse.getPaginatedResponse(supplierStoreCoupons,
							pageNumber, pageSize);
				} else if (type == EProductCoupon.STORE_COUPON_CODE && keyword != null && !keyword.isBlank()) {
					return (List<SupplierStoreCoupon>) PaginatedResponse.getPaginatedResponse(supplierStoreCoupons
							.stream().filter(e -> e.getStoreCouponCode().toLowerCase().contains(keyword.toLowerCase()))
							.collect(Collectors.toList()), pageNumber, pageSize);
				} else if (type == EProductCoupon.STATUS && keyword != null && !keyword.isBlank()) {
					return (List<SupplierStoreCoupon>) PaginatedResponse.getPaginatedResponse(supplierStoreCoupons
							.stream().filter(e -> e.getCouponStatus().toLowerCase().contains(keyword.toLowerCase()))
							.collect(Collectors.toList()), pageNumber, pageSize);
				} else if (type == EProductCoupon.DISCOUNT_TYPE && keyword != null && !keyword.isBlank()) {
					return (List<SupplierStoreCoupon>) PaginatedResponse.getPaginatedResponse(supplierStoreCoupons
							.stream().filter(e -> e.getDiscountType().toLowerCase().contains(keyword.toLowerCase()))
							.collect(Collectors.toList()), pageNumber, pageSize);
				} else {
					throw new InvalidCouponCodeException(INVALID_FILTER_TYPE);
				}
			} else {
				return coupons;
			}
		} catch (InvalidCouponCodeException | SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SupplierStoreCoupon> getAllStoreCouponForCustomer(String supplierId) {
		try {
			SupplierRegistration supplier = findSupplier(supplierId);
			List<SupplierStoreCoupon> supplierStoreCoupons = supplier.getSupplierStoreInfo().getSupplierStoreCoupons();
			return supplierStoreCoupons.stream()
					.filter(e -> Objects.equals(e.getCouponStatus(), EProductCoupon.PUBLISHED.name()))
					.collect(Collectors.toList());
		} catch (SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

}
