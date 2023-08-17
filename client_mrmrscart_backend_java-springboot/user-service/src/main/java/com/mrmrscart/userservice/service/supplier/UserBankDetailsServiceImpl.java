package com.mrmrscart.userservice.service.supplier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.userservice.constant.supplier.UserBankDetailsConstant;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.exception.supplier.InvalidBankDetailsException;
import com.mrmrscart.userservice.exception.supplier.NoBankDetailsFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.pojo.supplier.UserBankDetailsPojo;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.UserBankDetailsRepository;

@Service
public class UserBankDetailsServiceImpl implements UserBankDetailsService {

	@Autowired
	private UserBankDetailsRepository bankDetailsRepository;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Override
	public UserBankDetails addBankDetails(UserBankDetailsPojo bankDetailsPojo) {
		UserBankDetails userBankDetails = new UserBankDetails();
		BeanUtils.copyProperties(bankDetailsPojo, userBankDetails);
		SupplierRegistration supplier = supplierRegistrationRepository
				.findBySupplierIdAndStatus(bankDetailsPojo.getSupplierId(), ESupplierStatus.APPROVED);
		if (supplier != null) {
			if (supplier.getUserBankDetails().isEmpty())
				userBankDetails.setPrimary(true);
			else {
				if (supplier.getUserBankDetails().stream().allMatch(bank -> !bank.isDeleted()))
					userBankDetails.setPrimary(true);
			}

		}
		UserBankDetails bankDetails = bankDetailsRepository.save(userBankDetails);
		if (supplier != null) {
			supplier.getUserBankDetails().add(bankDetails);
			supplierRegistrationRepository.save(supplier);
		}
		return bankDetails;
	}

	@Override
	public UserBankDetails updateBankDetails(UserBankDetailsPojo bankDetailsPojo) {
		UserBankDetails bankDetail = bankDetailsRepository.findByBankIdAndIsDeleted(bankDetailsPojo.getBankId(), false);
		if (bankDetail != null) {
			BeanUtils.copyProperties(bankDetailsPojo, bankDetail);
		} else {
			throw new InvalidBankDetailsException("Invalid Bank Id");
		}
		return bankDetailsRepository.save(bankDetail);
	}

	@Override
	public List<UserBankDetails> getAllBanks(String supplierID) {
		SupplierRegistration supplier = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierID,
				ESupplierStatus.APPROVED);

		if (supplier != null && !supplier.getUserBankDetails().isEmpty()) {
			return supplier.getUserBankDetails().stream().filter(user -> !user.isDeleted())
					.collect(Collectors.toList());
		} else
			throw new NoBankDetailsFoundException(UserBankDetailsConstant.BANK_GET_FAIL_MESSAGE);
	}

	@Override
	public String changePrimaryBank(String supplierId, Long bankId) {
		SupplierRegistration supplier = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierId,
				ESupplierStatus.APPROVED);
		if (supplier != null) {
			return changePrimaryBank(bankId, supplier);
		} else {
			throw new SupplierException("Invalid Supplier Id");
		}

	}

	private String changePrimaryBank(Long bankId, SupplierRegistration supplier) {

		if (!supplier.getUserBankDetails().isEmpty()) {
			for (UserBankDetails user : supplier.getUserBankDetails()) {
				if (user.getBankId().equals(bankId))
					user.setPrimary(true);
				else {

					if (bankDetailsRepository.findByBankIdAndIsDeleted(bankId, false) != null) {
						user.setPrimary(false);
					} else
						throw new NoBankDetailsFoundException("Invalid Bank Id");
				}

			}
			bankDetailsRepository.saveAll(supplier.getUserBankDetails());
			return "Primary Bank details Changed Successfully";
		} else {
			throw new NoBankDetailsFoundException("No Bank Details Found");
		}
	}

	@Transactional
	@Override
	public String deleteBank(String supplierId, Long bankId) {
		try {
			SupplierRegistration supplier = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierId,
					ESupplierStatus.APPROVED);
			if (supplier != null) {
				if (!supplier.getUserBankDetails().isEmpty()) {
					for (UserBankDetails user : supplier.getUserBankDetails()) {
						if (user.getBankId().equals(bankId)) {
							if (user.isPrimary())
								throw new InvalidBankDetailsException("Primary Bank Cannot be deleted!!!");
							user.setDeleted(true);
							user.setPrimary(false);
							bankDetailsRepository.save(user);
						}
					}
				} else {
					throw new NoBankDetailsFoundException("No Bank Details Found");
				}
			} else {
				throw new SupplierException("Invalid Supplier Id");
			}
			return "Bank deleted successfully";
		} catch (NoBankDetailsFoundException | SupplierException | InvalidBankDetailsException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException("Something Went Wrong");
		}
	}

	@Override
	public UserBankDetails getBankById(String supplierId, Long bankId) {
		UserBankDetails bankDetails = null;
		SupplierRegistration supplier = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierId,
				ESupplierStatus.APPROVED);
		if (supplier != null && !supplier.getUserBankDetails().isEmpty()) {
			for (UserBankDetails user : supplier.getUserBankDetails()) {
				if (user.getBankId().equals(bankId) && !user.isDeleted()) {
					bankDetails = user;
					break;
				}
			}
		} else {
			throw new NoBankDetailsFoundException(UserBankDetailsConstant.BANK_GET_FAIL_MESSAGE);
		}
		return bankDetails;

	}
}
