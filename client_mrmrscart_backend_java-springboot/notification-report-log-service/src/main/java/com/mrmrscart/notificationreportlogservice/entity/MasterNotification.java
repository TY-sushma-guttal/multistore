package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_notification_details")
public class MasterNotification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger masterNotificationId;
	
	private LocalDateTime notificationScheduledAt;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	@Column(length = 45)
	private String createdById;
	@Column(length = 45)
	private String lastUpdatedBy;
	@Column(length = 20)
	private String createdByType;
	@OneToOne(cascade = CascadeType.ALL)
	private MailNotification mailNotification;
	@OneToOne(cascade = CascadeType.ALL)
	private MessageNotification messageNotification;
	@OneToOne(cascade = CascadeType.ALL)
	private PushNotification pushNotification;
	@OneToMany(cascade = CascadeType.ALL)
	private List<UserNotificationInfo> userNotificationInfoList;
 }
