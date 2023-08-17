package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ViewProductPojo {

	private String supplierId;
	private String supplierName;
	private String businessName;
	private String productVariationId;
	private String productTitle;
	private List<String> variationMedia;
	private String skuId;
	private String categoryId;
	private String categoryName;
	private String subCategoryId;
	private String subCategoryName;
	private BigDecimal weightInclusivePackage;
	private BigDecimal salePrice;
	private BigDecimal mrp;
	private BigDecimal stockQty;
	private String brand;
	private BigDecimal volume;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	private List<String> seoTitle;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime approvedAt;

}
