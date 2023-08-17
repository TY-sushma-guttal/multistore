package com.mrmrscart.userservice.entity.supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_supplier_store_coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SupplierStoreCoupon extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private LocalDateTime publishedAt;
}
