package com.mrmrscart.userservice.entity.admin;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_admin_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminGroups extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminGroupId;

	@Column(length = 20)
	private String groupName;

	@Column(length = 90)
	private String description;

	@Column(length = 20)
	private String createdByType;

	private boolean isEnabled;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "admin_group_id")
	private List<AdminRegistration> adminRegistration;
}
