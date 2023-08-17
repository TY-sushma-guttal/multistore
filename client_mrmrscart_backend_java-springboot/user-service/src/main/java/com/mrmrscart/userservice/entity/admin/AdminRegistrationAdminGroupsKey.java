package com.mrmrscart.userservice.entity.admin;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationAdminGroupsKey implements Serializable {

	private String adminRegistrationId;
	private Long adminGroupId;
}
