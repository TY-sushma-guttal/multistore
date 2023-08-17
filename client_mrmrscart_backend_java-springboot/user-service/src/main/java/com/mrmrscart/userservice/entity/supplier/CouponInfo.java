package com.mrmrscart.userservice.entity.supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_coupon_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CouponInfo extends Audit {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long couponId;
	@Column(length = 14)
	private String couponCode;
	@Column(length = 255)
	private String description;
	private String discountType;
	private BigDecimal couponAmount;
	private LocalDate couponExpiryDate;
	private String categoryIncluded;
	private String subCategoryIncluded;
	@Convert(converter = ListToStringConverter.class)
	private List<String> productsIncluded;
	private int usageLimitPerCoupon;
	private int usageLimitPerUser;
	private int usageLimitToXItems;
	private String couponStatus;
	private LocalDateTime publishedAt;

}
