package com.mrmrscart.userservice.entity.supplier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_user_bank_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserBankDetails extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankId;
	@Column(length = 100)
	private String bankName;
	@Column(length = 18)
	private String accountNumber;
	@Column(length = 100)
	private String accountHolderName;
	@Column(length = 11)
	private String ifscCode;
	private boolean isPrimary;
	private boolean isDeleted;
}
