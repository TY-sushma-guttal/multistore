package com.mrmrscart.orderspaymentsservice.entity.payment;

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

import com.mrmrscart.orderspaymentsservice.entity.earning.Earnings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_payment_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long payId;
	private double amount;
	private double amountPaid;
	@Column(length = 13)
	private String mobileNumber;
	@Column(length = 255)
	private String paymentGatewayId;
	@Column(length = 255)
	private String paymentId;
	@Column(length = 255)
	private String paymentSignature;
	@Column(length = 45)
	private String orderId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String paymentMethod;

	@OneToMany(cascade = CascadeType.ALL)
	private List<PaymentBreakdown> paymentBreakdowns;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Earnings> earnings;
}
