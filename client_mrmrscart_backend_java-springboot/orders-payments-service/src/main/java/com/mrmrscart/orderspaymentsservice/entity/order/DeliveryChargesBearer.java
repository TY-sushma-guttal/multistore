package com.mrmrscart.orderspaymentsservice.entity.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_delivery_charges_bearer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryChargesBearer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deliveryChargesBearerId;
	
	private boolean isForwardOrder;
	
	private boolean isReturnOrder;

}
