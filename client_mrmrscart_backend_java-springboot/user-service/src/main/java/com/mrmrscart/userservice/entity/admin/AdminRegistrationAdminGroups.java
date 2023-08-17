package com.mrmrscart.userservice.entity.admin;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_admin_registration_admin_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegistrationAdminGroups {

	@EmbeddedId
	private AdminRegistrationAdminGroupsKey id;
	
	@ManyToOne
	@MapsId("adminRegistrationId")
	@JoinColumn(name = "adminRegistrationId")
	private AdminRegistration adminRegistration;
	
	@ManyToOne
	@MapsId("adminGroupId")
	@JoinColumn(name = "adminGroupId")
	private AdminGroups adminGroups;
	
	private boolean isDisabled;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
}
