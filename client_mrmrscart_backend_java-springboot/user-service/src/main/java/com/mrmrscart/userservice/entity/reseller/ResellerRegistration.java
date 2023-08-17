package com.mrmrscart.userservice.entity.reseller;

import java.time.LocalDateTime;
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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.util.SupplierCustomIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_reseller_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResellerRegistration extends Audit {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Staff_id")
	@GenericGenerator(name = "Staff_id", strategy = "com.mrmrscart.userservice.util.SupplierCustomIdGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SupplierCustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "SP"),
			@Parameter(name = SupplierCustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	private String resellerId;
	@Column(length = 50)
	private String resellerName;
	@Column(length = 13)
	private String mobileNumber;
	@Column(length = 255)
	private String emailId;
	@Column(length = 10)
	private String gender;
	@Column(length = 50)
	private String languageSpoken;
	@Column(length = 50)
	private String occupation;
	@Column(length = 60)
	private String businessName;
	@Column(length = 255)
	private String profileImageUrl;
	@Column(length = 13)
	private String alternateMobileNumber;
	@Column(length = 45)
	private String userReferralCode;
	private boolean isDisabled;
	private LocalDateTime recentLoginDate;
	@Column(length = 20)
	private String referralType;
	@Column(length = 45)
	private String referralTypeId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "reseller_id")
	private List<UserAddressDetails> userAddressDetails;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "reseller_id")
	private List<UserBankDetails> userBankDetails;

}
