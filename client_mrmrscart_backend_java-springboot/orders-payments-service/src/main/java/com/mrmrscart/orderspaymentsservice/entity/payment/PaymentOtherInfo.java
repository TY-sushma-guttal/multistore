package com.mrmrscart.orderspaymentsservice.entity.payment;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_payment_other_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOtherInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger paymentOtherInfoId;
	private double amount;
	private double amountRefunded;
	@Column(length = 255)
	private String contact;
	@Column(length = 255)
	private String currency;
	@Column(length = 255)
	private String email;
	@Column(length = 255)
	private String entity;
	@Column(length = 60)
	private String invoiceId;
	@Column(length = 60)
	private String paymentMethod;
	@Column(length = 60)
	private String orderId;
	@Column(length = 60)
	private String paymentCustomerId;
	@Column(length = 60)
	private String paymentId;
	@Column(length = 35)
	private String refundStatus;
	@Column(length = 35)
	private String paymentStatus;
	@Column(length = 255)
	private String vpa;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@OneToOne(cascade = CascadeType.ALL)
	private AcquiredData acquiredData;
	@OneToMany(cascade = CascadeType.ALL)
	private List<PaymentInfo> paymentInfos;
}
