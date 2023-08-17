package com.mrmrscart.userservice.repository.reseller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolCountPojo;

public interface MarketingToolPurchaseHistoryRepository extends JpaRepository<MarketingToolPurchaseHistory, Long> {

	List<MarketingToolPurchaseHistory> findByPurchasedByIdAndToolStatus(String userId, EMarketingToolStatus active);

	List<MarketingToolPurchaseHistory> findByPurchasedByIdAndPurchasedByType(String userId, String userType);

	List<MarketingToolPurchaseHistory> findByPurchasedByIdAndPurchasedByTypeAndPurchasedAtBetween(String userId,
			String userType, LocalDateTime fromDate, LocalDateTime toDate);

	MarketingToolPurchaseHistory findByPurchaseIdAndIsDisabled(Long purchaseId, boolean status);

	@Query("SELECT new com.mrmrscart.userservice.pojo.admin.AdminMarketingToolCountPojo(m.toolStatus,m.subscriptionTypeId,a.adminMarketingToolName) FROM "
			+ "MarketingToolPurchaseHistory m INNER JOIN AdminMarketingTools a ON "
			+ "m.subscriptionTypeId=a.adminMarketingToolId and m.purchasedByType=:purchasedByType")
	List<AdminMarketingToolCountPojo> fetchMarketingToolPurchaseHistoryJoin(
			@Param("purchasedByType") String purchasedByType);

	public MarketingToolPurchaseHistory findByPurchasedByTypeAndPurchasedByIdAndToolStatusAndSubscriptionTypeAndSubscriptionTypeId(
			String purchasedByType, String purchasedById, EMarketingToolStatus toolStatus, String subscriptionType,
			Long subscriptionTypeId);

	@Query("select m from MarketingToolPurchaseHistory m INNER JOIN "
			+ "AdminMarketingTools a ON m.subscriptionTypeId = a.adminMarketingToolId and "
			+ "a.adminMarketingToolName=:adminMarketingToolName and m.purchasedById=:purchasedById and "
			+ "m.purchasedByType=:purchasedByType and m.toolStatus=:toolStatus")
	public MarketingToolPurchaseHistory findPurchaseMarketingToolHistory(
			@Param("adminMarketingToolName") String adminMarketingToolName,
			@Param("purchasedById") String purchasedById, @Param("purchasedByType") String purchasedByType,
			@Param("toolStatus") EMarketingToolStatus toolStatus);

	MarketingToolPurchaseHistory findByPurchaseIdAndIsDisabledAndToolStatus(Long purchaseId, boolean b,
			EMarketingToolStatus active);

	MarketingToolPurchaseHistory findByPurchasedByTypeAndPurchasedByIdAndAdminMarketingToolNameAndIsDisabledAndToolStatus(String purchasedByType,String purchasedById,
			 String adminMarketingToolName, boolean b, EMarketingToolStatus toolStatus);
}
