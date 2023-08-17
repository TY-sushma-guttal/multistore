package com.mrmrscart.productcategoriesservice.feign.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mrmrscart.productcategoriesservice.feign.enums.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingToolPurchaseHistoryPojo {

	private Long purchaseId;

	private String purchasedByType;

	private String purchasedById;

	private LocalDateTime purchasedAt;

	private EMarketingToolStatus toolStatus;

	private BigDecimal subscriptionAmount;

	private LocalDateTime activatedAt;

	private LocalDateTime expirationDate;

	private String subscriptionType;

	private Long subscriptionTypeId;

	private boolean isDisabled;

	private String comments;

	private String commentsAttachment;

	private String orderId;

	private String adminMarketingToolName;

}
