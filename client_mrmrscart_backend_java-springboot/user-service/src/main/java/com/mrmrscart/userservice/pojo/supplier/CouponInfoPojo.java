package com.mrmrscart.userservice.pojo.supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.mrmrscart.userservice.entity.supplier.EProductCoupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfoPojo {

	private String description;
	private EProductCoupon discountType;
	private BigDecimal couponAmount;
	private LocalDate couponExpiryDate;
	private String categoryIncluded;
	private String subCategoryIncluded;
	private List<String> productsIncluded;
	private int usageLimitPerCoupon;
	private int usageLimitPerUser;
	private int usageLimitToXItems;
	private EProductCoupon couponStatus;
}
