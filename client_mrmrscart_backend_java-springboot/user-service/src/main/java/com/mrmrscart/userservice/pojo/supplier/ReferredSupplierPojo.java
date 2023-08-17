package com.mrmrscart.userservice.pojo.supplier;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferredSupplierPojo {

	private String supplierId;
	private String supplierReferralCode;
	private Long totalCommissionSaved;
	private Long totalFreeOrderCount;
	
	private List<ReferredSupplierSubPojo> list;
}
