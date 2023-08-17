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
@Table(name = "mmc_marketing_tool_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingToolProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marketingToolProductId;
	
	@Column(length = 45)
	private String masterProductId;
	
	@Column(length = 45)
	private String variationId;
}
