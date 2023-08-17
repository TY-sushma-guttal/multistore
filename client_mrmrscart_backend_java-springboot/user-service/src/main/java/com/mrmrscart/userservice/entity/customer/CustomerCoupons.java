package com.mrmrscart.userservice.entity.customer;

import java.math.BigInteger;

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
@Table(name = "mmc_customer_coupons")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCoupons {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger customerCouponsId;
	@Column(length = 45)
	private String couponId;
}
