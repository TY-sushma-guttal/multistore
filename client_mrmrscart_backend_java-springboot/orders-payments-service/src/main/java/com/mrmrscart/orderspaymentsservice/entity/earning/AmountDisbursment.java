package com.mrmrscart.orderspaymentsservice.entity.earning;

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
@Table(name = "mmc_amount_disbursment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountDisbursment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger amountDisbursmentId;
	@Column(length = 20)
	private String userType;
	@Column(length = 45)
	private String userTypeId;
	private double totalAmount;
	private double amountDisbursed;
	private double amountRemaining;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
