package com.mrmrscart.notificationreportlogservice.service;

import java.util.List;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolNotification;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationFilterPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationViewPojo;

public interface MarketingToolNotificationService {

	public MarketingToolNotification addMarketingToolNotification(
			MarketingToolNotificationPojo marketingToolNotificationPojo);

	public boolean deleteMarketingToolNotification(Long marketingToolNotificationId);

	public List<MarketingToolNotificationViewPojo> getAllMarketingToolNotifications(String supplierId);

	public List<MarketingToolNotificationViewPojo> getPaginatedMarketingToolNotifications(int pageNumber, int pageSize,
			String supplierId, MarketingToolNotificationFilterPojo filterPojo);

	public MarketingToolNotification sendNotification(Long marketingToolNotificationId);

	public MarketingToolNotification getNotificationById(Long marketingToolNotificationId);

}
