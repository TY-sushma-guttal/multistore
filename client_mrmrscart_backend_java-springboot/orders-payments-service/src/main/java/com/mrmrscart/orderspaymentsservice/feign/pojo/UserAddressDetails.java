package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDetails {
	private Long addressId;
	private String name;
	private String mobileNumber;
	private String pinCode;
	private String location;
	private String address;
	private String cityDistrictTown;
	private String state;
	private String landmark;
	private String latitudeValue;
	private String longitudeValue;
	private String alternativeMobileNumber;
	private boolean isPrimary;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isDeleted;
	private String supplierId;
}
