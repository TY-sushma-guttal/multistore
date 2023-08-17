package com.mrmrscart.notificationreportlogservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolNotification;

@Repository
public interface MarketingToolNotificationRespository extends JpaRepository<MarketingToolNotification, Long> {

	List<MarketingToolNotification> findBySupplierIdAndIsDelete(String supplierId, boolean b);
	
	MarketingToolNotification findByMarketingToolNotificationIdAndIsDelete(long marketingToolNotificationId, boolean b);

	List<MarketingToolNotification> findBySupplierIdAndIsDeleteAndNotificationTitleContaining(String supplierId, boolean b, String keyword);

}
