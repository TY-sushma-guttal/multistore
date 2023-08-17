package com.mrmrscart.userservice.entity.admin;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.StaffCustomIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_admin_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminRegistration extends Audit {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Admin_id")
	@GenericGenerator(name = "Admin_id", strategy = "com.mrmrscart.userservice.util.AdminCustomIdGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StaffCustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "ADM"),
			@Parameter(name = StaffCustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	@Column(length = 45)
	private String adminRegistrationId;

	@Column(length = 255)
	private String emailId;

	@Column(length = 50)
	private String firstName;

	@Column(length = 50)
	private String lastName;

	@Column(length = 50)
	private String userName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	private LocalDate dob;

	@Column(length = 13)
	private String mobileNumber;

	@Column(length = 20)
	private String designation;

	private EAdminStatus status;

	private boolean isGrouped;

	@OneToOne(cascade = CascadeType.ALL)
	private AdminCapability adminCapabilities;

//	@ManyToOne(cascade = CascadeType.ALL)
//	private AdminGroups adminGroup;

	private boolean isDelete;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "mmc_admin_registration_admin_role")
	private List<AdminRole> adminRoles;


}
