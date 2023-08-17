package com.mrmrscart.userservice.repository.supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.CouponInfo;

@Repository
public interface CouponInfoRepository extends JpaRepository<CouponInfo, Long> {

	CouponInfo findByCouponCodeAndCreatedBy(String couponCode, String supplierId);

	List<CouponInfo> findByCreatedBy(String supplierId);

	List<CouponInfo> findByCreatedByAndCouponCodeIgnoreCaseContaining(String supplierId, String keyword);

	List<CouponInfo> findByCreatedByAndIgnoreCaseCouponStatusContaining(String supplierId, String keyword);

	List<CouponInfo> findByCreatedByAndIgnoreCaseDiscountTypeContaining(String supplierId, String keyword);

}
