package com.mrmrscart.productcategoriesservice.entity.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPropertyPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "mmc_product_variation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariation {

	@Id
	private String productVariationId;

	private String productCode;
	private String skuId;
	private String productTitle;
	private String shippingClass;
	private String businessProcessingDays;
	private List<String> seoTitle;
	private String metaDescription;
	private String metaKeywords;

	private boolean isStoreFDR;
	private BigDecimal salePriceWithLogistics;

	private boolean rtoAccepted;
	private int rtoDays;

	private boolean codAvailable;
	private boolean isDelete;

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
	private String modelName;

	private boolean sellWithMrMrsCart;
	private BigDecimal mrmrscartSalePriceWithFDR;
	private BigDecimal mrmrscartSalePriceWithOutFDR;
	private boolean mrmrscartRtoAccepted;
	private int mrmrscartRtoDays;
	private boolean mrmrscartCodAvailable;

	private String masterProductId;
	private List<String> variationMedia;

	private List<VariationPropertyPojo> variationProperty;

	private EStatus status;

	private String margeProductId;

	private LocalDateTime approvedAt;
	private String approvedBy;

	private String createdBy;
	private String updatedBy;

	private boolean isDisable;
	private boolean isFlagged;

	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;

}
