package com.mrmrscart.notificationreportlogservice.service;

import java.util.List;

import com.mrmrscart.notificationreportlogservice.entity.SupplierChangesHistory;
import com.mrmrscart.notificationreportlogservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.notificationreportlogservice.pojo.SupplierChangesHistoryViewPojo;

public interface SupplierChangesHistoryService {

	public List<SupplierChangesHistory> addSupplierChangesHistory(List<SupplierChangesHistoryPojo> supplierChangesHistorypojo);
	
	public SupplierChangesHistory rejectSupplierChangesHistory(Long changeId);
	
	public SupplierChangesHistory approveSupplierChangesHistory(Long changeId);
	
	public SupplierChangesHistory getById(Long changeId);
	
	public List<SupplierChangesHistoryViewPojo> getAllSupplierChangesHistory();
	
}
