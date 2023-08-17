package com.mrmrscart.notificationreportlogservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.notificationreportlogservice.entity.PushNotificationSuggestion;

@Repository
public interface PushNotificationSuggestionRepository extends JpaRepository<PushNotificationSuggestion, Long>{

	List<PushNotificationSuggestion> findByIsDelete(boolean isDelete);
}
