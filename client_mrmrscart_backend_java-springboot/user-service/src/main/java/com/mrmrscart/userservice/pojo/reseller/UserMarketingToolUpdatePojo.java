package com.mrmrscart.userservice.pojo.reseller;


import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMarketingToolUpdatePojo {
	private Long marketingToolId;
	private String description;
	private String campaignTitle;
	private EMarketingToolStatus toolStatus;
	private boolean isDelete;
}
