package com.mrmrscart.userservice.pojo.admin;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMarketingToolCountPojo {

	private EMarketingToolStatus toolStatus;
	private Long subscriptionTypeId;
	private String adminMarketingToolName;
}
