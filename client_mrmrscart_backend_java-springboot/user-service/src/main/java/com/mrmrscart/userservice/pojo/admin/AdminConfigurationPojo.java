package com.mrmrscart.userservice.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminConfigurationPojo {

	private String adminConfigurationName;
	private String adminConfigurationValue;
}
