package com.mrmrscart.userservice.repository.reseller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.reseller.ActiveMarketingToolSubscription;

@Repository

public interface ActiveMarketingToolSubscriptionRepository
		extends JpaRepository<ActiveMarketingToolSubscription, Long> {
	ActiveMarketingToolSubscription findByMarketingToolNameAndSubscriptionById(String marketingTool,
			String subscriptionById);

	List<ActiveMarketingToolSubscription> findByMarketingToolName(String name);

	public ActiveMarketingToolSubscription findBySubscriptionByTypeAndSubscriptionByIdAndMarketingToolNameAndIsDisabled(
			String subscriptionByType, String subscriptionById, String marketingToolName, boolean isDisabled);

	public ActiveMarketingToolSubscription findBySubscriptionByIdAndMarketingToolName(String subscriptionById,
			String marketingToolName);

	public List<ActiveMarketingToolSubscription> findBySubscriptionById(String supplierId);
}