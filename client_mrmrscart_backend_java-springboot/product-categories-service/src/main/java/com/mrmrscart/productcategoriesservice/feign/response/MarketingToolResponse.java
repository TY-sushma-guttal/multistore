package com.mrmrscart.productcategoriesservice.feign.response;

import com.mrmrscart.productcategoriesservice.feign.pojo.MarketingToolPurchaseHistoryPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingToolResponse {
	
	private boolean error;
	private String message;
	private MarketingToolPurchaseHistoryPojo data;

}
