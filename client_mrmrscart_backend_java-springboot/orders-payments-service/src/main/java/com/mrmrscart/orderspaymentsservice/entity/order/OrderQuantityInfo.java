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
@Table(name = "mmc_order_quantity_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderQuantityInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderQuantityInfoId;
	private String orderStatus;
	private String serialNumber;
}
