package com.mrmrscart.userservice.wrapper.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminIdAndNameWrapper {
	private int adminRegistrationId;
	private String adminManagerName;
}
