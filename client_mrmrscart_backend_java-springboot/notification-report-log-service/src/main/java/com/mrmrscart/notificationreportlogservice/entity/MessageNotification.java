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
@Table(name = "mmc_message_notification_details")
public class MessageNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger messageNotificationId;
	@Column(length = 60)
	private String messageContent;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
