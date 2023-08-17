package com.mrmrscart.orderspaymentsservice.entity.earning;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_payment_cycle_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCycleDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger PaymentCycleDetailId;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Earnings> earnings;

}
