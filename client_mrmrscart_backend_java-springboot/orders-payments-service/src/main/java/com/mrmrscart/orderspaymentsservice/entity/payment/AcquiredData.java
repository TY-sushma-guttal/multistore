package com.mrmrscart.orderspaymentsservice.entity.payment;

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

@Entity
@Table(name = "mmc_acquired_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquiredData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger acquireId;
	@Column(length = 255)
	private String rrn;
	@Column(length = 255)
	private String upiTransactionId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
