package com.mrmrscart.userservice.pojo.supplier;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreCouponPojo {
	private Long storeCouponId;
	private String storeCouponCode;
	private BigDecimal minimumOrderValue;
	private LocalDate expirationDate;
	private int couponUsageLimit;
	private int customerUsageLimit;
	private BigDecimal couponAmount;
	private EProductCoupon couponStatus;
	private EProductCoupon discountType;
	private BigDecimal maximumDiscountValue;
	private String supplierId;
	private String description;

}
