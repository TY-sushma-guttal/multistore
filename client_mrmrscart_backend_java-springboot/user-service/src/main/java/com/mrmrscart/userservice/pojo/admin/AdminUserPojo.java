package com.mrmrscart.userservice.pojo.admin;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.admin.EDesignation;

import lombok.Data;

@Data
public class AdminUserPojo {

	private String adminRegistrationId;

	private String emailId;

	private String firstName;

	private String lastName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate dob;

	private String mobileNumber;

	private String designation;

	private EAdminStatus status;

	private boolean isGrouped;

	private Object adminCapabilityList;

}
