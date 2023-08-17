package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigInteger;

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
@Table(name = "mmc_notification_media")
public class NotificationMedia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger notificationMediaId;
	@Column(length = 255)
	private String mediaUrl;
}
