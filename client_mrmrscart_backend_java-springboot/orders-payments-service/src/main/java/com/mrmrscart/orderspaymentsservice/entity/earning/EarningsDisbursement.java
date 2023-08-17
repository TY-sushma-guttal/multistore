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
@Table(name = "mmc_earnings_disbursement")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningsDisbursement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger earningsPaymentDisbursementId;
	@Column(length = 20)
	private String userType;
	@Column(length = 45)
	private String userId;
	private double amount;
	private LocalDateTime createdAt;
	

}
