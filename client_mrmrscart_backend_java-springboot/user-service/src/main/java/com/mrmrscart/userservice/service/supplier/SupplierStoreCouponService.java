package com.mrmrscart.userservice.service.supplier;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreCouponPojo;

public interface SupplierStoreCouponService {

	public SupplierStoreInfo createStoreCoupon(SupplierStoreCouponPojo storeCouponPojo);

	public SupplierStoreCoupon publishStoreCoupon(Long storeCouponId);

	public List<SupplierStoreCoupon> getAllStoreCoupons(String supplierId, int pageNumber, int pageSize, String keyword,
			EProductCoupon type);

	public List<SupplierStoreCoupon> getAllStoreCouponForCustomer(String supplierId);

}
