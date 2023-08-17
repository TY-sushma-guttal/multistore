package com.mrmrscart.userservice.pojo.supplier;

import java.math.BigDecimal;
import java.util.List;

import com.mrmrscart.userservice.entity.supplier.SupplierStoreTheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreInfoPojo {

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

	private String supplierStoreLogo;

	private List<SupplierStoreTheme> storeThemes;
}
