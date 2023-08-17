package com.mrmrscart.notificationreportlogservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.notificationreportlogservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_push_notification_suggestion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PushNotificationSuggestion extends Audit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pushNotificationSuggestionId;
	private String title;
	private boolean isDelete;
}
