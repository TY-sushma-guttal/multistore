package com.mrmrscart.userservice.entity.admin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mmc_admin_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminConfiguration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminConfigurationId;
	
	private String adminConfigurationName;
	
	private String adminConfigurationValue;
}
