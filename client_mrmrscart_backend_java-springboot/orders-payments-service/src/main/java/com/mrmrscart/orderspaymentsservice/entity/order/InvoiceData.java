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
@Table(name = "mmc_invoice_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long invoiceId;
	
	@Column(length = 50)
	private String invoiceName;
	
	@Column(length = 255)
	private String invoiceFileUrl;
	
	private LocalDateTime invoiceDate;
	
	private LocalDateTime updatedAt;
}
