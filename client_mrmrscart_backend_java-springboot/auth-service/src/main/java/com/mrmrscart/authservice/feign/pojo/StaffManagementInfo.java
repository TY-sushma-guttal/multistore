package com.mrmrscart.authservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffManagementInfo {

	private String staffId;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private String emailId;

	private String createdById;

	private String supplierId;

	private Object staffCapabilityList;

	private LocalDateTime createdAt;

	private LocalDateTime lastUpdatedAt;

	private LocalDateTime recentLoginDate;

	private boolean isDelete;
}
