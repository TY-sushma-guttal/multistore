package com.mrmrscart.userservice.repository.supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreCoupon;

@Repository
public interface SupplierStoreCouponRepository extends JpaRepository<SupplierStoreCoupon, Long> {

	SupplierStoreCoupon findByStoreCouponIdAndCouponStatus(Long storeCouponId, String draft);

	List<SupplierStoreCoupon> findByCouponStatusIn(List<EProductCoupon> arrayList);

}
