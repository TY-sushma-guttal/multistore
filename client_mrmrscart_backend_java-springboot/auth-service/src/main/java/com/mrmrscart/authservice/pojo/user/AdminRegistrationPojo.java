package com.mrmrscart.authservice.pojo.user;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.authservice.entity.user.EAdminStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationPojo {

	private String adminRegistrationId;

	private String emailId;

	private String firstName;

	private String lastName;

	private String userName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate dob;

	private String mobileNumber;

	private String designation;

	private EAdminStatus status;

	private boolean isGrouped;

	private AdminCapabilityPojo adminCapabilities;

	private List<AdminRolePojo> adminRoles;
}
