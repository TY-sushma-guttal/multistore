package com.mrmrscart.userservice.feign.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariation{
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
	
	private boolean sellWithMrMrsCart;
	private BigDecimal mrmrscartSalePriceWithFDR;
	private BigDecimal mrmrscartSalePriceWithOutFDR;
	private boolean mrmrscartRtoAccepted;
	private int mrmrscartRtoDays;
	private boolean mrmrscartCodAvailable;

	private LocalDateTime expiryDate;
	private String countryOfOrigin;
	
	private EStatus status;
	private String masterProductId;
	private List<String> variationMedia;
	private boolean isDisable;
	
	private List<VariationPropertyPojo> variationProperty;

	private LocalDateTime approvedAt;
	private String approvedBy;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;

}
