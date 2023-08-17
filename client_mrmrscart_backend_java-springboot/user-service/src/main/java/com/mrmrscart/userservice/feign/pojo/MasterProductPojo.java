package com.mrmrscart.userservice.feign.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductPojo {
	private String masterProductId;

	private ECategory commissionMode;
	private String productType;
	private String brand;
	private String subCategoryId;
	private String subCategoryName;
	private String supplierId;

	private BigDecimal limitsPerOrder;

	/* new fields added 03-08-2022 start */
	private boolean trademarkLetterAvailable;
	private List<String> trademarkLetterIdList;
	private List<String> bTobInvoiceIdList;

	private ZoneChargePojo zoneChargeInfo;

	private boolean isGenericProduct;
	/* new fields added 03-08-2022 end */

	/* new fields added 06-09-2022 start */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime expiryDate;
	private String countryOfOrigin;
	/* new fields added 06-09-2022 end */

	private String longDescription;
	private List<String> longDescriptionFileUrls;

	private String shortDescription;
	private List<String> shortDescriptionFileUrls;

	private List<String> tags;
	private EStatus productStatus;

	private LinkedProductPojo linkedProducts;
	private ProductPolicyPojo productPolicies;
	private List<ProductVariationPojo> productVariations;

	private boolean isMerged;
	private boolean isDelete;

	private Object otherInformation;

}
