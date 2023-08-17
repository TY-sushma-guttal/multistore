package com.mrmrscart.notificationreportlogservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.notificationreportlogservice.entity.EStatus;
import com.mrmrscart.notificationreportlogservice.entity.SupplierChangesHistory;

@Repository
public interface SupplierChangesHistoryRepository extends JpaRepository<SupplierChangesHistory, Long> {

	List<SupplierChangesHistory> findBySupplierIdAndApprovedAt(String supplierId, Object object);

	List<SupplierChangesHistory> findByStatus(EStatus created);

}
