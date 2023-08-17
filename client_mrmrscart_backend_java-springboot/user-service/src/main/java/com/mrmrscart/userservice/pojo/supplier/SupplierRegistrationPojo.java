package com.mrmrscart.userservice.pojo.supplier;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRegistrationPojo {

	private String supplierId;
	private String businessName;
	private String emailId;
	private String mobileNumber;
	private String gstin;
	private String avgStockCount;
	private List<String> mainCategories;
	private String websiteName;
	private String profileImageUrl;
	private String websiteLink;
	private String city;
	private String firstName;
	private String lastName;
	private boolean isWished;
	private String supplierReferralCode;
}
