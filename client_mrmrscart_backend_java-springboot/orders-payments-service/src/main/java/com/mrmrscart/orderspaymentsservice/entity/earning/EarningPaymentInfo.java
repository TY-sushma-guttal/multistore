package com.mrmrscart.orderspaymentsservice.entity.earning;

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

@Entity
@Table(name = "mmc_earning_payment_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningPaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger earningPaymentInfoId;
	private double amount;
	@Column(length = 45)
	private String paymentGatewayId;
	@Column(length = 255)
	private String paymentSignature;
	@Column(length = 45)
	private String paymentStatus;
	@Column(length = 45)
	private String modeOfPayment;
	@Column(length = 45)
	private String paidBy;
	private LocalDateTime createdAt;
	@OneToMany(cascade = CascadeType.ALL)
	private List<EarningsDisbursement> earningsDisbursements;
}
