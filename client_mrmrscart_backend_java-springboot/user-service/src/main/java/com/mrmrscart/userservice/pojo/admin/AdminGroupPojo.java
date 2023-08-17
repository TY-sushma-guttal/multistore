package com.mrmrscart.userservice.pojo.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGroupPojo {

	private String groupName;
	private String description;
	private List<String> adminId;
}
