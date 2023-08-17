package com.mrmrscart.userservice.entity.admin;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.JpaConverterJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_admin_capability")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class AdminCapability extends Audit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long adminCapabilityId;
	
	@Convert(converter = JpaConverterJson.class)
	@Column(columnDefinition = "TEXT")
	private Object adminCapabilityList;
}
