package com.mrmrscart.userservice.service.reseller;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolPojo;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.PurchaseMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolMainResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolUpdatePojo;
import com.mrmrscart.userservice.wrapper.reseller.MarketingToolStatusWrapper;

public interface UserMarketingToolService {
	public List<UserMarketingTool> addMarketingTool(UserMarketingToolPojo data);

	public UserMarketingTool deleteMarketingTool(Long marketingToolId);

	public List<PurchasedMarketingToolResponsePojo> getAllSubscriptions(
			PurchasedMarketingToolPojo purchasedMarketingToolPojo, int pageNumber, int pageSize);

	public UserMarketingTool userMarketingToolUpdate(UserMarketingToolUpdatePojo data);

	public UserMarketingToolMainResponsePojo getMarketingTool(String userTypeId, EMarketingTools toolType,
			int pageNumber, int pageSize);

	public List<MarketingToolPurchaseHistory> purchaseMarketingTool(PurchaseMarketingToolPojo marketingTool);

	public MarketingToolStatusWrapper validateMarketingToolStatus(String supplierId);

}
