package com.mrmrscart.notificationreportlogservice.service;

import java.util.List;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolPriceChange;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.notificationreportlogservice.pojo.PriceChangeHistoryPojo;

public interface MarketingToolPriceChangeService {

	MarketingToolPriceChange addMarketingToolPriceChange(MarketingToolPriceChangePojo marketingToolPriceChangePojo);

	public List<MarketingToolPriceChange> viewPriceChangeHistory(PriceChangeHistoryPojo priceChangeHistoryPojo,
			int pageNumber, int pageSize);

}
