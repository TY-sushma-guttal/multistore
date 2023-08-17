package com.mrmrscart.userservice.repository.admin;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.AdminMarketingToolsCampaign;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

@Repository
public interface AdminMarketingToolsCampaignRepository extends JpaRepository<AdminMarketingToolsCampaign, Long> {

	public List<AdminMarketingToolsCampaign> findByStoreType(String storeType);

	public List<AdminMarketingToolsCampaign> findByDays(String days);

	@Query(value = "select * from mmc_admin_marketing_tools_campaign where price between price=:priceStart and price=:priceEnd", nativeQuery = true)
	public List<AdminMarketingToolsCampaign> findByPriceRange(BigDecimal priceStart, BigDecimal priceEnd);

	public List<AdminMarketingToolsCampaign> findByStatus(EMarketingToolStatus status);

	public List<AdminMarketingToolsCampaign> findByStoreTypeAndStatus(String storeType, EMarketingToolStatus status);

	public List<AdminMarketingToolsCampaign> findByStoreTypeAndStatusAndDays(String storeType,
			EMarketingToolStatus status, String days);

	public AdminMarketingToolsCampaign findByAdminMarketingToolsCampaignIdAndStoreTypeAndIsDisabled(
			Long subscriptionTypeId, String purchasedByType, boolean b);
}
