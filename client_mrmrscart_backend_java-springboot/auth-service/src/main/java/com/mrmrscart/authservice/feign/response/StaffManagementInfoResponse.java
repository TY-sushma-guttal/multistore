package com.mrmrscart.authservice.feign.response;

import com.mrmrscart.authservice.feign.pojo.StaffManagementInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffManagementInfoResponse {

	private boolean error;
	private String message;
	private StaffManagementInfo staffManagementInfo;
}