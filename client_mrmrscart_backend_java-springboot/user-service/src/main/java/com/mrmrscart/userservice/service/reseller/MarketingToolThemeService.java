package com.mrmrscart.userservice.service.reseller;

import java.util.List;

import com.mrmrscart.userservice.entity.reseller.MarketingToolTheme;
import com.mrmrscart.userservice.pojo.reseller.MarketingToolThemePojo;

public interface MarketingToolThemeService {
	public MarketingToolTheme addMarketingToolTheme(MarketingToolThemePojo data);

	public MarketingToolTheme getMarketingToolThemeById(Long id);

	public List<MarketingToolTheme> getAllMarketingToolTheme();
}
