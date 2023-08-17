package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistration {
	private String customerId;
	private String customerName;
	private String mobileNumber;
	private String emailId;
	private String gender;
	private LocalDate dob;
	private boolean isDisabled;
	private LocalDateTime registeredAt;
	private LocalDateTime lastUpdatedAt;
	private LocalDateTime recentLoginDate;
	private String customerCode;
	private List<UserAddressDetails> userAddressDetails;
	private List<UserProfile> profiles;
	private List<ResellerStoreDetails> resellerStoreDetails;
	private List<CustomerStores> customerStores;

}