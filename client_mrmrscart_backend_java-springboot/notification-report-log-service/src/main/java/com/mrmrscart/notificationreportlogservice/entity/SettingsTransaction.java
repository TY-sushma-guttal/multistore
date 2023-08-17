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
@Table(name = "mmc_settings_transaction_details")

public class SettingsTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger settingsTransactionId;
	@Column(length = 45)
	private String transactionType;
	@Column(length = 255)
	private String message;
	@Column(length = 45)
	private String lastUpdatedBy;
	private LocalDateTime lastUpdatedAt;
	@Column(length = 255)
	private String mediaUrl;
	@Column(length = 45)
	private String transactionMessageFor;
}
