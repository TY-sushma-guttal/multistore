package com.mrmrscart.userservice.service.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.pojo.admin.MarketingToolPurchaseHistoryPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolDisableEnablePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPricePojo;
import com.mrmrscart.userservice.pojo.admin.IndividualPricingPojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolApprovalPojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolSubscriptionPojo;
import com.mrmrscart.userservice.pojo.admin.SubscriptionCountPojo;
import com.mrmrscart.userservice.pojo.reseller.MarketingToolCommentPojo;

public interface AdminMarketingToolService {

	public AdminMarketingTools addIndividualPricing(AdminMarketingToolsPojo adminMarketingToolsPojo);

	public List<IndividualPricingPojo> getIndividualPricing(EUserRole userRole);

	public UserMarketingTool approveRejectMarketingTool(EMarketingToolStatus status, Long marketingToolId,
			String userId);

	List<MarketingToolApprovalPojo> getAllUnapprovedMarketingTools(int pageNumber, int pageSize,
			EMarketingTools marketingToolType);

	public AdminMarketingTools updatePrice(AdminMarketingToolsPricePojo adminMarketingToolsPricePojo);

	public boolean enableDisableToolSubscription(Long purchaseId, boolean status, EMarketingTools marketingTool);

	public List<AdminMarketingTools> enableOrDisableTool(
			AdminMarketingToolDisableEnablePojo adminMarketingToolDisableEnablePojo);

	public List<MarketingToolPurchaseHistory> getSubscriptionsByTool(MarketingToolSubscriptionPojo pojo, int pageNumber,
			int pageSize);

	public List<IndividualPricingPojo> getEnabledIndividualPricing(EUserRole storeType, int pageNumber, int pageSize);

	public List<SubscriptionCountPojo> getSubscriptionCount(EStoreType type);

	public Object addMarketingToolComments(MarketingToolCommentPojo marketingToolCommentPojo);
	
	public MarketingToolPurchaseHistoryPojo getSubscription(String purchasedById, String purchasedByType,
			String adminMarketingToolName);

}
