package com.mrmrscart.productcategoriesservice.feign.pojo;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreInfo {
	private Long supplierStoreInfoId;

	private String supplierStoreCode;

	private String supplierStoreName;

	private BigDecimal minimumOrderAmount;

	private String maxOrderProcessingTime;

	private String maxOrderDeliveryRange;

	private List<String> shopOpeningDays;

	private String shopTimings;

	private String shopDescription;

	private String shopDescriptionImageUrl;

	private boolean isStoreActive;

	private String supplierStoreLogo;

	private int activeProductCount;
}
