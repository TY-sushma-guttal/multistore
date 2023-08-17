package com.mrmrscart.notificationreportlogservice.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierChangesHistoryViewPojo {
	
	private long changeHistoryId;
	private String supplierId;
	private LocalDateTime updatedAt;
	private String changedField;
	private String changedValue;
	private String oldValue;
	private boolean isAccountVerified;
	private String emailId;
	private String mobileNumber;
	private String queries;
	private String answers;
	private LocalDateTime createdDate;

}
