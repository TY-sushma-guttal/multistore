package com.mrmrscart.orderspaymentsservice.entity.order;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_order_delivery_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderStatusId;
	
	private EOrders orderStatus;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastUpdatedAt;
}
