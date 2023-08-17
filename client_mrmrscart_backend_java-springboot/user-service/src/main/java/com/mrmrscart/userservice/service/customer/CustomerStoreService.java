package com.mrmrscart.userservice.service.customer;

import java.util.List;

import com.mrmrscart.userservice.pojo.customer.DropDownPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreInfoResponsePojo;
import com.mrmrscart.userservice.wrapper.customer.CustomerWrapper;

public interface CustomerStoreService {

	public List<DropDownPojo> getFavouritreCustomers(String supplierId);

	public List<CustomerWrapper> getCustomers(String supplierId);

	public SupplierStoreInfoResponsePojo validateStoreCode(String storeCode);

}
