//package com.mrmrscart.productcategoriesservice.entity.product;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import lombok.Data;
//
//@Data
//@Document(collection = "mmc_coupon_info")
//public class CouponInfo {
//	@Id
//	private String couponId;
//	private String couponCode;
//	private String description;
//	private String discountType;
//	private BigDecimal couponAmount;
//	private LocalDateTime couponExpiryDate;
//	private List<String> categoryIncluded;
//	private List<String> productsIncluded;
//	private int usageLimitPerCoupon;
//	private int usageLimitPerUser;
//	private int usageLimitToXItems;
//	private EProductCoupon couponStatus;
//	@CreatedDate
//	private LocalDateTime createdAt;
//	private LocalDateTime publishedAt;
//	private String createdByType;
//	private String createdById;
//}
