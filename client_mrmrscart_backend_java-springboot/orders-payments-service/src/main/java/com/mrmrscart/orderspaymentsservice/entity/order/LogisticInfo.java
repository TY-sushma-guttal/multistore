package com.mrmrscart.orderspaymentsservice.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_logistic_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticInfo {
	
	@Id
	@GeneratedValue
	private Long logisticId;
	
	@Column(length = 65)
	private String logisticPartnerName;

	@Column(length = 255)
	private String logisticUrl;
	
	@Column(length = 255)
	private String trackingId;
	
	private double logisticCharges;
	
	@Column(length = 35)
	private String orderDeliveryType;
	
	@Column(length = 35)
	private String orderType;

}
