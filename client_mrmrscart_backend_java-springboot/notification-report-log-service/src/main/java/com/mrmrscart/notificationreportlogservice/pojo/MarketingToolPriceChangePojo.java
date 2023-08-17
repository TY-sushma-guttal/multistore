package com.mrmrscart.notificationreportlogservice.pojo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingToolPriceChangePojo {

	private BigDecimal oldPrice;
	private BigDecimal newPrice;
	private Long toolId;
	private String toolType;
	private String createdBy;
}
