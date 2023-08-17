package com.mrmrscart.userservice.repository.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;

@Repository
public interface AdminMarketingToolsRepository extends JpaRepository<AdminMarketingTools, Long> {

	public AdminMarketingTools findByAdminMarketingToolNameAndDaysAndStoreType(String adminMarketingToolName,
			String days, String storeType);

	public List<AdminMarketingTools> findByStoreType(String name);

	public List<AdminMarketingTools> findByDaysAndStoreType(String days, String storeType);

	@Query("select m from MarketingToolPurchaseHistory m INNER JOIN AdminMarketingTools a ON m.subscriptionTypeId=a.adminMarketingToolId AND a.adminMarketingToolName=?1")
	public List<MarketingToolPurchaseHistory> getSubscriptionsByToolName(String name);

	@Query("select m from MarketingToolPurchaseHistory m INNER JOIN AdminMarketingTools a ON m.subscriptionTypeId=a.adminMarketingToolId AND a.adminMarketingToolName=?1 AND m.toolStatus=?2")
	public List<MarketingToolPurchaseHistory> getSubscriptionsByToolNameAndStatus(String name,
			EMarketingToolStatus toolStatus);

	@Query("select m from MarketingToolPurchaseHistory m INNER JOIN AdminMarketingTools a ON m.subscriptionTypeId=a.adminMarketingToolId AND a.adminMarketingToolName=?1 AND m.purchasedByType=?2")
	public List<MarketingToolPurchaseHistory> getSubscriptionsByToolNameAndUserType(String name, String userType);

	@Query("select m from MarketingToolPurchaseHistory m INNER JOIN AdminMarketingTools a ON m.subscriptionTypeId=a.adminMarketingToolId AND a.adminMarketingToolName=?1 AND m.toolStatus=?2 AND m.purchasedByType=?3")
	public List<MarketingToolPurchaseHistory> getSubscriptionsByToolNameAndToolStatusAndUserType(String name,
			EMarketingToolStatus toolStatus, String userType);

	public List<AdminMarketingTools> findByStoreTypeAndIsDisabled(String storeType, boolean b);

	public AdminMarketingTools findByAdminMarketingToolNameAndIsDisabledAndStatusAndDays(String adminMarketingToolName,
			boolean isDisabled, EMarketingToolStatus status,String days);

	public AdminMarketingTools findByAdminMarketingToolIdAndStoreTypeAndIsDisabled(Long subscriptionTypeId,
			String purchasedByType, boolean b);
	
	public AdminMarketingTools findByAdminMarketingToolNameAndIsDisabledAndStatusAndDaysAndStoreType(String adminMarketingToolName,boolean isDisabled,EMarketingToolStatus status,String days, String storeType);

}
