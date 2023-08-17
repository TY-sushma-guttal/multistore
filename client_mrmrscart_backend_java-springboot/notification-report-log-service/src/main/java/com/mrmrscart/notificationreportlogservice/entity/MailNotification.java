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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_mail_notification_details")
public class MailNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger mailNotificationId;
	@Column(length = 20)
	private String mailNotificationType;
	@Column(length = 50)
	private String mailSubject;
	@Column(length = 255)
	private String mailMessage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<NotificationMedia> notificationMediaList;
}
