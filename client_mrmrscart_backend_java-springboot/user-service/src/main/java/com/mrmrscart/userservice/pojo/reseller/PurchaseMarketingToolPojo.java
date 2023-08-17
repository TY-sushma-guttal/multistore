package com.mrmrscart.userservice.pojo.reseller;

import com.mrmrscart.userservice.entity.admin.EMarketingToolType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseMarketingToolPojo {

	private String purchasedByType;
	private String purchasedById;
	private EMarketingToolType subscriptionType;
	private Long subscriptionTypeId;

}
