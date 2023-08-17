package com.mrmrscart.userservice.wrapper.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSupplierCountWrapper {

	private int count;
	private List<SupplierRegistration> supplierRegistrations;
}
