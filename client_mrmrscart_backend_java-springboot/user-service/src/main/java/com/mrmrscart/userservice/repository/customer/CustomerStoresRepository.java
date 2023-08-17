package com.mrmrscart.userservice.repository.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.customer.CustomerStores;

@Repository
public interface CustomerStoresRepository extends JpaRepository<CustomerStores, Long> {

	public List<CustomerStores> findByStoreCode(String supplierStoreCode);
	
	long countByStoreCode(String supplierStoreCode);
	
	@Query("Select c from CustomerStores c where c.storeCode=?1 and  EXTRACT (year FROM c.joinedAt) =?2 and EXTRACT (month FROM c.joinedAt) =?3 ")
	public List<CustomerStores> findByYearMonth(String storeCode,int year,int month);

}
