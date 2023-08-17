package com.mrmrscart.userservice.pojo.reseller;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCampaignToolFilterPojo {
	private String fieldName;
	private String fieldValue;
	private int pageNumber;
	private int pageSize;
	private EMarketingToolStatus status;
}
