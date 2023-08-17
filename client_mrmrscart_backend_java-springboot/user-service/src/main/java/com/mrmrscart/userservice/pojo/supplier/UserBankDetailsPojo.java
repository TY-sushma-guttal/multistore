package com.mrmrscart.userservice.pojo.supplier;

import lombok.Data;

@Data
public class UserBankDetailsPojo {
	
	private Long bankId;
	private String bankName;
	private String accountNumber;
	private String accountHolderName;
	private String ifscCode;
	private String supplierId;

}
