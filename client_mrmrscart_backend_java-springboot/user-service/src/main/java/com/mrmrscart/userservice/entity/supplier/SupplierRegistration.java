package com.mrmrscart.userservice.entity.supplier;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.mrmrscart.userservice.util.ListToStringConverter;
import com.mrmrscart.userservice.util.SupplierCustomIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_supplier_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Staff_id")
	@GenericGenerator(name = "Staff_id", strategy = "com.mrmrscart.userservice.util.SupplierCustomIdGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SupplierCustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "SP"),
			@Parameter(name = SupplierCustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	private String supplierId;
	@Column(length = 60)
	private String businessName;
	@Column(length = 255)
	private String emailId;
	@Column(length = 13)
	private String mobileNumber;
	@Column(length = 15)
	private String gstin;
	@Column(length = 10)
	private String avgStockCount;
	@Column(length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> mainCategories;
	@Column(length = 50)
	private String websiteName;
	@Column(length = 255)
	private String profileImageUrl;
	@Column(length = 255)
	private String websiteLink;
	private ESupplierStatus status;
	@Column(length = 50)
	private String city;
	@Column(length = 50)
	private String firstName;
	@Column(length = 50)
	private String lastName;
	@CreatedDate
	private LocalDateTime registeredAt;
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	private LocalDateTime approvedAt;
	private LocalDateTime recentLoginDate;
	private boolean isAccountVerified;

	private String supplierReferralCode;
	private String referredById;
	private Long totalFreeOrderCount;
	private Long remainingFreeOrderCount;
	private Long signupFreeOrderCount;
	
	private String supplierCode;

	@OneToOne(cascade = CascadeType.ALL)
	private SupplierStoreInfo supplierStoreInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplierId")
	private List<UserAddressDetails> userAddressDetails;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_id")
	private List<UserBankDetails> userBankDetails;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplierId")
	private List<StaffManagementInfo> staffManagementInfos;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_id")
	private List<SellerReviews> sellerReviews;
}
