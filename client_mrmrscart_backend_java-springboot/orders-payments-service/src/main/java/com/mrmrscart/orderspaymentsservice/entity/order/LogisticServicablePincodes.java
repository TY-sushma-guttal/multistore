package com.mrmrscart.orderspaymentsservice.entity.order;

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
@Table(name = "mmc_logistic_servicable_pincodes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticServicablePincodes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger servicablePincodesId;
	
	@Column(length = 6)
	private String pincode;
	
	@Column(length = 45)
	private String createdBy;
	
	@Column(length = 45)
	private String lastUpdatedBy;
	
	private boolean servicableByDelhivery;
	
	private boolean servicableByPostalDepartment;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastUpdatedAt;

}
