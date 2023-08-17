package com.mrmrscart.userservice.pojo.reseller;

import java.util.List;

import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingToolThemePojo {

	private Long themeId;
	
	private String imageUrl;
	
	private List<String> colorCode;
	
	private String themeType;
	
	private List<UserMarketingTool> marketingTools;
}
