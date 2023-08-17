package com.mrmrscart.productcategoriesservice.entity.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mrmrscart.productcategoriesservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mmc_trademark_invoice_info")
@EqualsAndHashCode(callSuper=false)
public class TrademarkInvoiceInfo extends Audit{
	@Id
	private String trademarkInvoiceId;
	
	private String supplierId;
	private String documentType;
	private String documentName;
	
	private String description;
	
	private List<String> documentUrl;
	
	private boolean isDeleted;
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;
	
	private String lastupdatedBy;
}
