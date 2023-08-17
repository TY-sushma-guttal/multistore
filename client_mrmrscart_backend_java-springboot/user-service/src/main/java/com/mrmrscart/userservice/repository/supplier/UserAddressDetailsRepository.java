package com.mrmrscart.userservice.repository.supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;

public interface UserAddressDetailsRepository extends JpaRepository<UserAddressDetails, Long> {

	public List<UserAddressDetails> findBySupplierId(String supplierId);

	public List<UserAddressDetails> findBySupplierIdAndIsDeleted(String supplierID, boolean b);

	public UserAddressDetails findByAddressIdAndSupplierIdAndIsDeleted(Long addressId, String supplierId,boolean b);

	public UserAddressDetails findByAddressIdAndIsDeleted(Long addressId, boolean b);
}
