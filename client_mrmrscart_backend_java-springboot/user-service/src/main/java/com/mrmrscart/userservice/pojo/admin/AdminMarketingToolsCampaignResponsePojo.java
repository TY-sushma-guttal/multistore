package com.mrmrscart.userservice.pojo.admin;

import java.math.BigDecimal;
import java.util.List;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMarketingToolsCampaignResponsePojo {

	private Long adminMarketingToolsCampaignId;

	private String title;

	private String days;

	private BigDecimal price;

	private String storeType;
	
	private String expiryDuration;

	private EMarketingToolStatus status;

	private List<String> adminMarketingTools;
}
