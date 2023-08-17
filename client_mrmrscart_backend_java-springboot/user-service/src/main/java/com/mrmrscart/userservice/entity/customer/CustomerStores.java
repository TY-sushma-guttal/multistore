package com.mrmrscart.userservice.entity.customer;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_customer_store")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStores  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long customerStoreId;
	
	private EUserRole storeType;
	
	@Column(length = 45)
	private String storeCode;
	
	private boolean isFavourite;
	
	private LocalDateTime joinedAt;
	
	@ManyToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name="customer_id")
	private CustomerRegistration customerRegistration;
	

}
