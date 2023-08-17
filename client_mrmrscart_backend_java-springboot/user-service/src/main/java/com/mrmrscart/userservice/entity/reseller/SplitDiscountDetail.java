package com.mrmrscart.userservice.entity.reseller;

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
@Table(name = "mmc_split_discount_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SplitDiscountDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long splitDiscountId;
	
	private double splitAmount;
	
	private boolean isExpired;
	
	private boolean isIssued;
	
	@Column(length = 45)
	private String issuedToId;

}
