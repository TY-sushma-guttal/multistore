package com.mrmrscart.userservice.pojo.admin;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.AdminCapability;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.admin.EDesignation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminManagerPojo {
	
	private String adminRegistrationId;
	
	private String emailId;
	
	private String firstName;
	
	private String lastName;

	private String userName;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate dob;
	
	private String mobileNumber;
	
	private EDesignation designation;
	
	private EAdminStatus status;
	
	private boolean isGrouped;
	
	private AdminCapability adminCapabilities;
}
