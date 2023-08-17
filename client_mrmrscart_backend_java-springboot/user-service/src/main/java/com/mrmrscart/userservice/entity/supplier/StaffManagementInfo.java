package com.mrmrscart.userservice.entity.supplier;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.JpaConverterJson;
import com.mrmrscart.userservice.util.StaffCustomIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_staff_management_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StaffManagementInfo extends Audit{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Staff_id")
	@GenericGenerator(
			name = "Staff_id", 
			strategy = "com.mrmrscart.userservice.util.StaffCustomIdGenerator", 
			parameters = {
					@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
					@Parameter(name = StaffCustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "STF"),
					@Parameter(name = StaffCustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") 
			}
	)
	@Column(length = 45)
	private String staffId;
	
	@Column(length = 50)
	private String firstName;
	
	@Column(length = 50)
	private String lastName;
	
	@Column(length = 13)
	private String mobileNumber;

	@Column(length = 255)
	private String emailId;

	@Column(length = 45)
	private String supplierId;
	
	@Convert(converter = JpaConverterJson.class)
	@Column(columnDefinition = "TEXT")
	private Object staffCapabilityList;
	
	@Column
	private LocalDateTime createdAt;
	
	@Column
	private LocalDateTime lastUpdatedAt;
	
	@Column
	private LocalDateTime recentLoginDate;
	
	@Column
	private boolean isDelete;
	
	@Column
	private boolean isMobileNumberVerified;
}
