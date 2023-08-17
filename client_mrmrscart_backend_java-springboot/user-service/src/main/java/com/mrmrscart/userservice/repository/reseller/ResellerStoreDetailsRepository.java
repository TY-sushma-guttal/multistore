package com.mrmrscart.userservice.repository.reseller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.reseller.ResellerStoreDetails;

@Repository
public interface ResellerStoreDetailsRepository extends JpaRepository<ResellerStoreDetails, Long>{

	ResellerStoreDetails findByStoreOwnerTypeAndStoreOwnerId(String storeOwnerType,String storeOwnerId);
}
