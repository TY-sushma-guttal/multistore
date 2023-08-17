package com.mrmrscart.authservice.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminCapabilityPojo {

	private long adminCapabilityId;

	private Object adminCapabilityList;
}
