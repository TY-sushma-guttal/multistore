package com.mrmrscart.userservice.entity.reseller;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.customer.CustomerMarketingTools;
import com.mrmrscart.userservice.entity.supplier.EMarginType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_user_marketing_tool")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserMarketingTool extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marketingToolId;

	@Column(length = 30)
	private EMarketingTools toolType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;

	@Column(length = 255)
	private String description;

	@Column(length = 20)
	private String campaignTitle;

	private EMarketingToolStatus toolStatus;
	
	private EMarketingToolStatus adminApprovalStatus;

	private String mainCategoryId;

	private String subCategoryId;

	private double totalDiscountValue;

	private double priceStartRange;
	
	private double priceEndRange;
	
	@Column(length = 20)
	private EMarketingTools splitType;

	private int couponUsageLimit;

	private int customerUsageLimit;

	private EMarginType marginType;

	@Column(length = 25)
	private String couponCode;
	
	@Column(length = 25)
	private String customerType;

	@Column(length = 255)
	private String productCatalogueUrl;

	@Column(length = 25)
	private String userType;

	@Column(length = 45)
	private String userTypeId;

	private boolean isDelete;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "marketing_tool_id")
	private List<MarketingToolProduct> marketingToolProductList;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "marketing_tool_id")
	private List<LeadResellerMarketingTool> leadResellerMarketingToolList;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "marketing_tool_id")
	private List<SplitDiscountDetail> splitDiscountDetailList;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "marketing_tool_id")
	private List<MarketingToolQuestionAnswer> marketingToolQuestionAnswerList;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "marketing_tool_id")
	private List<CustomerMarketingTools> customerMarketingToolList;

}
