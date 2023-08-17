package com.mrmrscart.userservice.pojo.supplier;

import lombok.Data;

@Data
public class UserAddressDetailsPojo {
	
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
	private String supplierId;

}
