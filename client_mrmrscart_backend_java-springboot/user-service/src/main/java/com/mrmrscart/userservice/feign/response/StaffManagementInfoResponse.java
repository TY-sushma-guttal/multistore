package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;

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
