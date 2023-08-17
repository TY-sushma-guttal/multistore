package com.mrmrscart.userservice.pojo.admin;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingToolSubscriptionPojo {
	EMarketingTools marketingTool;
	EMarketingToolStatus toolStatus;
	EUserRole userType;
}
