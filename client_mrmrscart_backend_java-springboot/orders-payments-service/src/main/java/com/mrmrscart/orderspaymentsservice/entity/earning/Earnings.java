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
@Table(name = "mmc_earnings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Earnings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger earningId;
	private double supplierAmount;
	private double resellerAmount;
	@Column(length = 45)
	private String adminId;
	private double adminAmount;
	@Column(length = 45)
	private String supplierId;
	@Column(length = 45)
	private String resellerId;
	@Column(length = 45)
	private String referenceResellerId;
	private double referenceResellerAmount;
	private boolean isDispatchable;
	private LocalDateTime disbursedAt;
}
