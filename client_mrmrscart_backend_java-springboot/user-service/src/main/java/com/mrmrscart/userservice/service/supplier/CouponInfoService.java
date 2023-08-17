package com.mrmrscart.userservice.service.supplier;

import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.DecoderException;

import com.mrmrscart.userservice.entity.supplier.CouponInfo;
import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.pojo.supplier.CouponInfoPojo;

public interface CouponInfoService {

	public CouponInfo createCoupon(CouponInfoPojo couponInfoPojo) throws DecoderException, IOException;

	public CouponInfo publishCoupon(String supplierId, String couponCode);

	public List<CouponInfo> getCouponsByProduct(String productVariationId, String supplierId);

	public List<CouponInfo> getAllCoupons(String supplierId, int pageNumber, int pageSize, String keyword,
			EProductCoupon type);
}
