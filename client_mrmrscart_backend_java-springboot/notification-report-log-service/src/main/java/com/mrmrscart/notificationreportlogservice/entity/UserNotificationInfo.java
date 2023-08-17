package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_user_notification_details")
public class UserNotificationInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger userNotificationId;
	@Column(length = 20)
	private String userType;
	@Column(length = 45)
	private String userId;
	private boolean mailSentStatus;
	private boolean messageSentStatus;
	private boolean pushNotificationSentStatus;
	private boolean isViewed;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
