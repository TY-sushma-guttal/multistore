package com.mrmrscart.userservice.pojo.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedMarketingToolResponsePojo {
	private Long purchaseId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime purchasedAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime activatedAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime expirationDate;
	
	private EMarketingToolStatus toolStatus;
	private BigDecimal subscriptionAmount;
	private String subscriptionType;
	private Long subscriptionTypeId;
	private String subscriptionTitle;

}
