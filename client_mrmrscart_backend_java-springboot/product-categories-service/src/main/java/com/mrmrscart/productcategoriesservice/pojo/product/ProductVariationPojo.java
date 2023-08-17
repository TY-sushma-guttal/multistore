package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariationPojo {

	private String productVariationId;

	private String skuId;
	private String productTitle;

	private String productCode;
	private String shippingClass;
	private String businessProcessingDays;
	private List<String> seoTitle;
	private String metaDescription;
	private String metaKeywords;

	private boolean rtoAccepted;
	private int rtoDays;

	private boolean codAvailable;

	private boolean isDelete;

	private boolean isStoreFDR;
	private BigDecimal salePriceWithLogistics;

	private String deliveryCharge;
	private BigDecimal packageLength;
	private BigDecimal packageWidth;
	private BigDecimal packageHeight;
	private BigDecimal weightInclusivePackage;
 	private BigDecimal salePrice;
	private BigDecimal mrp;

	private BigDecimal stockQty;
	private String stockStatus;
	private String allowBackOrders;
	private int backOrders;
	private String margeProductId;
	private String modelName;

	private boolean sellWithMrMrsCart;
	private BigDecimal mrmrscartSalePriceWithFDR;
	private BigDecimal mrmrscartSalePriceWithOutFDR;
	private boolean mrmrscartRtoAccepted;
	private int mrmrscartRtoDays;
	private boolean mrmrscartCodAvailable;

	private boolean isDisable;
	private EStatus status;
	private String masterProductId;
	private List<String> variationMedia;

	private boolean isFlagged;
	
	private List<VariationPropertyPojo> variationProperty;

	private LocalDateTime approvedAt;
	private String approvedBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
}
