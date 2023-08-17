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
@Table(name = "mmc_admin_log_activity")
public class AdminLogActivity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger logActivityId;
	@Column(length = 45)
	private String menu;
	@Column(length = 45)
	private String activity;
	@Column(length = 20)
	private String userType;
	@Column(length = 45)
	private String userId;
	private LocalDateTime createdAt;
}
