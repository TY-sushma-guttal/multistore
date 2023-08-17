package com.mrmrscart.userservice.pojo.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferredSupplierSubPojo {

	private String supplierId;
	private String profileImageUrl;
	private String firstName;
	private String lastName;
	private String supplierStoreName;
	private Long signupFreeOrderCount;
}
