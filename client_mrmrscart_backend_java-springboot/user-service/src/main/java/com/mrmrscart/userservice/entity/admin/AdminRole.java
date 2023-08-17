package com.mrmrscart.userservice.entity.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "mmc_admin_role")
@AllArgsConstructor
@NoArgsConstructor
public class AdminRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long adminRoleId;

	private String adminRoleTitle;
}
