package com.mrmrscart.userservice.wrapper.supplier;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInfoWrapper {
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
	private ESupplierStatus status;
	private String city;
	private String firstName;
	private String lastName;
	private LocalDateTime registeredAt;
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
	private String supplierStoreCode;
	private boolean isAddressPresent;
}
