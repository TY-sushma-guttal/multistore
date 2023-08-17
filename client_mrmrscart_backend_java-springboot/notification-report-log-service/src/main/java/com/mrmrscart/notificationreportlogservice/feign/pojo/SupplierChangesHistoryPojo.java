package com.mrmrscart.notificationreportlogservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SupplierChangesHistoryPojo {

	private String supplierId;
	private LocalDateTime updatedAt;
	private String changedField;
	private String changedValue;
	private String oldValue;
	private boolean isAccountVerified;
}
