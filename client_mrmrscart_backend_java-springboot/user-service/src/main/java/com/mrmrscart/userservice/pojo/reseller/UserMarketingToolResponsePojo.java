package com.mrmrscart.userservice.pojo.reseller;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.customer.CustomerMarketingTools;
import com.mrmrscart.userservice.entity.reseller.LeadResellerMarketingTool;
import com.mrmrscart.userservice.entity.reseller.MarketingToolQuestionAnswer;
import com.mrmrscart.userservice.entity.reseller.SplitDiscountDetail;
import com.mrmrscart.userservice.entity.supplier.EMarginType;
import com.mrmrscart.userservice.feign.pojo.ProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMarketingToolResponsePojo {

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

	private String category;

	private String subCategory;

	private double totalDiscountValue;

	private EMarketingTools splitType;

	private int couponUsageLimit;

	private int customerUsageLimit;

	private EMarginType marginType;

	private String couponCode;

	private String customerType;

	private String productCatalogueUrl;

	private String userType;

	private String userTypeId;

	private boolean isDelete;

	private double priceStartRange;

	private double priceEndRange;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastModifiedDate;

	private List<ProductPojo> marketingToolProductList;

	private List<LeadResellerMarketingTool> leadResellerMarketingToolList;

	private List<SplitDiscountDetail> splitDiscountDetailList;

	private List<MarketingToolQuestionAnswer> marketingToolQuestionAnswerList;

	private List<CustomerMarketingTools> customerMarketingToolList;
}
