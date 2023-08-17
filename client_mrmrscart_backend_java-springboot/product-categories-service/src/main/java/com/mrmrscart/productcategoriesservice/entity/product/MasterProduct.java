package com.mrmrscart.productcategoriesservice.entity.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.pojo.product.LinkedProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductPolicyPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ZoneChargePojo;

import lombok.Data;

@Data
@Document(collection = "mmc_master_product")
public class MasterProduct {

	@Id
	private String masterProductId;
	
	private ECategory commissionMode;
	private String productType;
	private String brand;
	private String subCategoryId;
	private String subCategoryName;
	private String supplierId;
	
	private BigDecimal limitsPerOrder;
	
	private String longDescription;
	private List<String> longDescriptionFileUrls;
	
	private String shortDescription;
	private List<String> shortDescriptionFileUrls;
	
	private List<String> tags;
	private String productStatus;

	/*new fields added 03-08-2022 start*/
	private boolean trademarkLetterAvailable;
	private List<String> trademarkLetterIdList;
	private List<String> btobInvoiceList;

	private ZoneChargePojo zoneChargeInfo;
	
	private boolean isGenericProduct;
	/*new fields added 03-08-2022 end*/
	
	private LinkedProductPojo linkedProducts;
	private ProductPolicyPojo productPolicies;
	
	private boolean isDelete;
	private boolean isMerged;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime expiryDate;
	private String countryOfOrigin;
	
	private Object otherInformation;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	@CreatedDate
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	@LastModifiedDate
	private LocalDateTime lastUpdatedAt;
	
	private String createdBy;
	private String updatedBy;
}
