package com.mrmrscart.userservice.pojo.admin;

import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierApprovalPojo {
	private String supplierId;
	private ESupplierStatus status;
}
