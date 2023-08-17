package com.mrmrscart.userservice.service.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.AdminMarketingToolsCampaign;
import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignResponsePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignUpdatePojo;
import com.mrmrscart.userservice.pojo.admin.AdminToolCampaignIdAndStatusPojo;
import com.mrmrscart.userservice.pojo.reseller.AdminCampaignToolFilterPojo;
import com.mrmrscart.userservice.wrapper.admin.AdminMarketingToolsWrapper;

public interface AdminMarketingToolsCampaignService {

	List<AdminMarketingToolsWrapper> getAdminMarketingTools(String days, EStoreType storeType);

	AdminMarketingToolsCampaign createAdminMarketingToolCampaign(
			AdminMarketingToolsCampaignPojo adminMarketingToolsCampaignPojo);

	public AdminMarketingToolsCampaign updateCampaignTool(AdminMarketingToolsCampaignUpdatePojo data);

	public AdminMarketingToolsCampaign enableDisableCampaign(AdminToolCampaignIdAndStatusPojo data);

	public List<AdminMarketingToolsCampaign> getAllAdminCampaignTool(String storeType, int pageNumber, int pageSize);

	public List<AdminMarketingToolsCampaign> getFilteredAdminCampaignTool(AdminCampaignToolFilterPojo data);
	

	List<AdminMarketingToolsCampaignResponsePojo> getEnabledAdminMarketingToolCampaign(String days, String storeType,
			int pageNumber, int pageSize);
}