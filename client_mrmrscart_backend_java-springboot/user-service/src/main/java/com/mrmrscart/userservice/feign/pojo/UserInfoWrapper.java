package com.mrmrscart.userservice.feign.pojo;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoWrapper {
	private List<String> userId;
	private EUserType userType;
}
