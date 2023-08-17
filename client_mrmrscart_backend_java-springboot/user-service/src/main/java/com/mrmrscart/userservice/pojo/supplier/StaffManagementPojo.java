package com.mrmrscart.userservice.pojo.supplier;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffManagementPojo {
	private String staffId;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private String emailId;

	private String supplierId;

	private Object staffCapabilityList;

	private LocalDateTime createdAt;

	private LocalDateTime lastUpdatedAt;

	private LocalDateTime recentLoginDate;

	private boolean isDelete;
}
