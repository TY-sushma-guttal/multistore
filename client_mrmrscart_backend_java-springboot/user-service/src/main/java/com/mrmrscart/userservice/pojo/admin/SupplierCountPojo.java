package com.mrmrscart.userservice.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCountPojo {

	private long newSuppliers;
	private long oldSuppliers;
}
