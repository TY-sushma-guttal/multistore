package com.mrmrscart.notificationreportlogservice.service;

import java.util.List;

import com.mrmrscart.notificationreportlogservice.entity.PushNotificationSuggestion;
import com.mrmrscart.notificationreportlogservice.pojo.PushNotificationSuggestionPojo;

public interface PushNotificationSuggestionService {

	List<PushNotificationSuggestion> createPushNotificationSuggestion(List<String> titles);

	List<PushNotificationSuggestion> getPushNotificationSuggestion(int pageNumber, int pageSize);

	PushNotificationSuggestion updatePushNotificationSuggestion(
			PushNotificationSuggestionPojo pushNotificationSuggestionPojo);

	PushNotificationSuggestion deletePushNotificationSuggestion(Long id);

	List<PushNotificationSuggestion> getAllPushNotificationSuggestion();

}
