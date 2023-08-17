package com.mrmrscart.userservice.pojo.reseller;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.customer.CustomerMarketingTools;
import com.mrmrscart.userservice.entity.reseller.ECustomerType;
import com.mrmrscart.userservice.entity.reseller.LeadResellerMarketingTool;
import com.mrmrscart.userservice.entity.reseller.MarketingToolProduct;
import com.mrmrscart.userservice.entity.reseller.MarketingToolQuestionAnswer;
import com.mrmrscart.userservice.entity.reseller.SplitDiscountDetail;
import com.mrmrscart.userservice.entity.supplier.EMarginType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMarketingToolPojo {
	private Long marketingToolId;
	private EMarketingTools toolType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
	
	private String description;
	private String campaignTitle;
	private EMarketingToolStatus toolStatus;
	private EMarketingToolStatus adminApprovalStatus;
	private double totalDiscountValue;
	private EMarketingTools splitType;
	private ECustomerType customerType;
	private int couponUsageLimit;
	private int customerUsageLimit;
	private String mainCategoryId;
	private String subCategoryId;
	private EMarginType marginType;
	private String couponCode;
	private String productCatalogueUrl;
	private String userType;
	private String userTypeId;
	private boolean isDelete;
	private Long marketingToolThemeId;
	private double priceStartRange;
	private double priceEndRange;

	private List<MarketingToolProduct> marketingToolProductList;
	private List<LeadResellerMarketingTool> leadResellerMarketingToolList;
	private List<SplitDiscountDetail> splitDiscountDetailList;
	private List<MarketingToolQuestionAnswer> marketingToolQuestionAnswerList;
	private List<CustomerMarketingTools> customerMarketingToolList;
}
