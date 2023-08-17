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
@Table(name = "mmc_customer_browsing_history")
public class CustomerBrowsingHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger customerBrowsingHistoryId;
	@Column(length = 255)
	private String description;
	@Column(length = 45)
	private String customerId;
	private LocalDateTime createdAt;
}
