package com.mrmrscart.userservice.repository.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;

@Repository
public interface SupplierStoreInfoRepository extends JpaRepository<SupplierStoreInfo, Long> {

	SupplierStoreInfo findBySupplierStoreInfoIdAndIsStoreActive(Long supplierStoreInfoId, boolean b);

	SupplierStoreInfo findBySupplierStoreCodeAndIsStoreActive(String storeCode, boolean b);

}
