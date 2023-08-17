package com.mrmrscart.userservice.entity.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.mrmrscart.userservice.entity.reseller.ResellerStoreDetails;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.util.SupplierCustomIdGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_customer_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_id")
	@GenericGenerator(name = "Customer_id", strategy = "com.mrmrscart.userservice.util.CustomerCustomIdGenerator", parameters = {
			@Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = SupplierCustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "CST"),
			@Parameter(name = SupplierCustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	private String customerId;
	@Column(length = 50)
	private String customerName;
	@Column(length = 13)
	private String mobileNumber;
	@Column(length = 255)
	private String emailId;
	@Column(length = 10)
	private String gender;
	private LocalDate dob;
	private boolean isDisabled;
	private LocalDateTime registeredAt;
	private LocalDateTime lastUpdatedAt;
	private LocalDateTime recentLoginDate;
	private String customerCode;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private List<CustomerCoupons> customerCoupons;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private List<UserAddressDetails> userAddressDetails;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private List<UserBankDetails> userBankDetails;

	@OneToMany(mappedBy = "customerRegistration")
	private List<CustomerMarketingTools> customerMarketingTools;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<ResellerStoreDetails> resellerStoreDetails;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private List<CustomerStores> customerStores;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "customer_id")
	private List<UserProfile> profiles;
}
