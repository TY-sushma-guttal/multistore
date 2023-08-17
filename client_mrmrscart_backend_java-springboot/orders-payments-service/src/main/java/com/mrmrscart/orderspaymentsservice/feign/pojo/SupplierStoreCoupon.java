package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreCoupon {

	private Long storeCouponId;
	private String storeCouponCode;
	private BigDecimal minimumOrderValue;
	private LocalDate expirationDate;
	private int couponUsageLimit;
	private int customerUsageLimit;
	private BigDecimal couponAmount;
	private String couponStatus;
	private String discountType;
	private BigDecimal maximumDiscountValue;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
	private String createdBy;
	private String lastUpdatedBy;
}
