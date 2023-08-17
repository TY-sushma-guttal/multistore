package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_marketing_tool_price_change_history")
public class MarketingToolPriceChange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long priceChangeHistoryId;
	
	@Column(length = 255)
	private String description;
	
	private BigDecimal currentRate;
	
	private long toolId;
	
	@Column(length = 45)
	private String toolType;
	
	private LocalDateTime createdAt;
	
	@Column(length = 45)
	private String createdBy;
}
