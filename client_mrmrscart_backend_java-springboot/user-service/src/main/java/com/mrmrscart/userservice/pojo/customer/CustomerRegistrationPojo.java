package com.mrmrscart.userservice.pojo.customer;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationPojo {

	private String customerName;
	private String mobileNumber;
	private String emailId;
	private LocalDate dob;
	private String storeCode;
	private String password;
	private boolean isWished;
}
