package com.mrmrscart.orderspaymentsservice.entity.order;

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
@Table(name = "mmc_delivery_partner_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartnerDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deliveryPartnerId;
	
	@Column(length = 45)
	private String deliveryPartnerName;
	
	@Column(length = 13)
	private String mobileNumber;
	
	@Column(length = 100)
	private String address;
	
	@Column(length = 255)
	private String website;
	
	@Column(length = 45)
	private String createdBy;
	
	@Column(length = 45)
	private String lastUpdatedBy;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastUpdatedAt;
}
