package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_push_notification_details")
public class PushNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger pushNotificationId;
	@Column(length = 60)
	private String pushNotificationMessage;
	@Column(length = 20)
	private String pushNotificationType;
	@Column(length = 20)
	private String pushNotificationTitle;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@OneToMany
	private List<NotificationMedia> notificationMediaList;
}
