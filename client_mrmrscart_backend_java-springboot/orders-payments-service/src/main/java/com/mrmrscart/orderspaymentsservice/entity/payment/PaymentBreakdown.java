package com.mrmrscart.orderspaymentsservice.entity.payment;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_payment_breakdown")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBreakdown {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger PaymentBreakdownId;
	private double paymentAmount;
	private double logisticCharges;
	private double transactionCharges;
	private double remainingAmount;
	

}
