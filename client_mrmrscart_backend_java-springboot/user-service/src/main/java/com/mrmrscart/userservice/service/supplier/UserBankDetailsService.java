package com.mrmrscart.userservice.service.supplier;

import java.util.List;

import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.pojo.supplier.UserBankDetailsPojo;

public interface UserBankDetailsService {

	public UserBankDetails addBankDetails(UserBankDetailsPojo bankDetailsPojo);

	public UserBankDetails updateBankDetails(UserBankDetailsPojo bankDetailsPojo);

	public List<UserBankDetails> getAllBanks(String supplierID);

	public String changePrimaryBank(String supplierId, Long BankId);

	public String deleteBank(String supplierId, Long BankId);

	public UserBankDetails getBankById(String supplierId, Long BankId);

	
}
