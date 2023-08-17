package com.mrmrscart.orderspaymentsservice.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.orderspaymentsservice.entity.order.ManifestData;

public interface ManifestRepository extends JpaRepository<ManifestData, Long>{
	
}
