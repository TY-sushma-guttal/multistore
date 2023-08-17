package com.mrmrscart.userservice.service.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.exception.supplier.InvalidBankDetailsException;
import com.mrmrscart.userservice.exception.supplier.NoBankDetailsFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.pojo.supplier.UserBankDetailsPojo;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.UserBankDetailsRepository;

@ExtendWith(MockitoExtension.class)
public class UserBankDetailsServiceImplTest {

	@InjectMocks
	private UserBankDetailsServiceImpl serviceImpl;

	@Mock
	private UserBankDetailsRepository bankDetailsRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	private UserBankDetails userBankDetails;

	private SupplierRegistration registration;

	@BeforeEach
	public void setUp() {

		userBankDetails = new UserBankDetails();
		userBankDetails.setBankId(1l);
		userBankDetails.setAccountHolderName("MRMRSCART");
		userBankDetails.setAccountNumber("1234567890");
		userBankDetails.setBankName("Indian Bank");
		userBankDetails.setIfscCode("IFSC123456");
		userBankDetails.setDeleted(false);

		registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

	}// End of setUp method

	@Test
	public void addBankDetails() {

		UserBankDetailsPojo bankDetailsPojo = new UserBankDetailsPojo();
		bankDetailsPojo.setSupplierId("SUP0001");

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setPrimary(true);
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus(bankDetailsPojo.getSupplierId(),
				ESupplierStatus.APPROVED)).thenReturn(registration);

		when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		UserBankDetails addBankDetails = serviceImpl.addBankDetails(bankDetailsPojo);

		assertThat(addBankDetails.getBankId()).isEqualTo(userBankDetails.getBankId());

	}// End of addBankDetails method

	@Test
	public void addBankDetailsII() {

		UserBankDetailsPojo bankDetailsPojo = new UserBankDetailsPojo();
		bankDetailsPojo.setSupplierId("SUP0001");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus(bankDetailsPojo.getSupplierId(),
				ESupplierStatus.APPROVED)).thenReturn(registration);

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setPrimary(true);
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(new ArrayList<>());
		when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		when(supplierRegistrationRepository.save(registration)).thenReturn(registration);

		UserBankDetails addBankDetails = serviceImpl.addBankDetails(bankDetailsPojo);

		Assertions.assertEquals(addBankDetails, userBankDetails);

	}// End of addBankDetailsII method

	@Test
	public void updateBankDetails() {

		UserBankDetailsPojo bankDetailsPojo = new UserBankDetailsPojo();
		bankDetailsPojo.setSupplierId("SUP0001");

		when(bankDetailsRepository.findByBankIdAndIsDeleted(bankDetailsPojo.getBankId(), false))
				.thenReturn(userBankDetails);

		when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		UserBankDetails updateBankDetails = serviceImpl.updateBankDetails(bankDetailsPojo);

		assertThat(updateBankDetails.getBankName()).isEqualTo(userBankDetails.getBankName());

		UserBankDetailsPojo bankDetailsPojo1 = new UserBankDetailsPojo();
		bankDetailsPojo1.setBankId(0l);

		InvalidBankDetailsException assertThrows = Assertions.assertThrows(InvalidBankDetailsException.class,
				() -> serviceImpl.updateBankDetails(bankDetailsPojo1));

		Assertions.assertEquals("Invalid Bank Id", assertThrows.getMessage());

	}// End of updateBankDetails method

	@Test
	public void getAllBanks() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		List<UserBankDetails> allBanks = serviceImpl.getAllBanks("SUP0001");

		assertThat(allBanks).isEqualTo(bankDetails);

		registration.setUserBankDetails(new ArrayList<>());

		NoBankDetailsFoundException assertThrows = Assertions.assertThrows(NoBankDetailsFoundException.class,
				() -> serviceImpl.getAllBanks("SUP0001"));

		Assertions.assertEquals("No Bank details found", assertThrows.getMessage());

	}// End of getAllBanks method

	@Test
	public void changePrimaryBank() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(bankDetailsRepository.saveAll(Mockito.anyList())).thenReturn(bankDetails);

		String changePrimaryBank = serviceImpl.changePrimaryBank("SUP0001", 1l);

		assertThat(changePrimaryBank).isEqualTo("Primary Bank details Changed Successfully");

		registration.setUserBankDetails(new ArrayList<>());

		NoBankDetailsFoundException assertThrows = Assertions.assertThrows(NoBankDetailsFoundException.class,
				() -> serviceImpl.changePrimaryBank("SUP0001", 1l));

		Assertions.assertEquals("No Bank Details Found", assertThrows.getMessage());

	}// End of changePrimaryBank method

	@Test
	public void changePrimaryBankWithSupplierException() {

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(null);

		SupplierException assertThrows = Assertions.assertThrows(SupplierException.class,
				() -> serviceImpl.changePrimaryBank("SUP0001", 1l));

		Assertions.assertEquals("Invalid Supplier Id", assertThrows.getMessage());

	}// End of changePrimaryBankWithSupplierException method

	@Test
	public void changePrimaryBankWithNoBankDetailsFoundException() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(bankDetailsRepository.findByBankIdAndIsDeleted(2l, false)).thenReturn(null);

		NoBankDetailsFoundException assertThrows = Assertions.assertThrows(NoBankDetailsFoundException.class,
				() -> serviceImpl.changePrimaryBank("SUP0001", 2l));

		Assertions.assertEquals("Invalid Bank Id", assertThrows.getMessage());

	}// End of changePrimaryBankWithNoBankDetailsFoundException method

	@Test
	public void changePrimaryBankII() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setPrimary(true);
		userBankDetails.setBankId(2l);
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(bankDetailsRepository.findByBankIdAndIsDeleted(1l, false)).thenReturn(userBankDetails);

		when(bankDetailsRepository.saveAll(bankDetails)).thenReturn(bankDetails);

		String changePrimaryBank = serviceImpl.changePrimaryBank("SUP0001", 1l);

		assertThat(changePrimaryBank).isEqualTo("Primary Bank details Changed Successfully");

	}// End of changePrimaryBankII method

	@Test
	public void deleteBank() {

		List<UserBankDetails> bankDetails = new ArrayList<>();

		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		String deleteBank = serviceImpl.deleteBank("SUP0001", 1l);

		lenient().when(bankDetailsRepository.findByIsDeleted(false)).thenReturn(bankDetails);

		lenient().when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		assertThat(deleteBank).isEqualTo("Bank deleted successfully");

		SupplierException exception = Assertions.assertThrows(SupplierException.class,
				() -> serviceImpl.deleteBank(null, 1l));

		Assertions.assertEquals("Invalid Supplier Id", exception.getMessage());

	}// End of deleteBank method

	@Test
	public void deleteBankII() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setDeleted(true);
		userBankDetails.setPrimary(false);

		bankDetails.add(userBankDetails);
		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(bankDetailsRepository.save(Mockito.any())).thenReturn(userBankDetails);

		lenient().when(bankDetailsRepository.findByIsDeleted(false)).thenReturn(bankDetails);

		String deleteBank = serviceImpl.deleteBank("SUP0001", 1l);

		Assertions.assertEquals("Bank deleted successfully", deleteBank);

	}// End of deleteBankII method

	@Test
	public void deleteBankIII() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setDeleted(true);
		userBankDetails.setPrimary(true);

		bankDetails.add(userBankDetails);
		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		InvalidBankDetailsException assertThrows = Assertions.assertThrows(InvalidBankDetailsException.class,
				() -> serviceImpl.deleteBank("SUP0001", 1l));

		Assertions.assertEquals("Primary Bank Cannot be deleted!!!", assertThrows.getMessage());

	}// End of deleteBankIII method

	@Test
	public void deleteBankWithNoBankDetailsFoundException() {

		registration.setUserBankDetails(new ArrayList<>());

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		NoBankDetailsFoundException assertThrows = Assertions.assertThrows(NoBankDetailsFoundException.class,
				() -> serviceImpl.deleteBank("SUP0001", 1l));

		Assertions.assertEquals("No Bank Details Found", assertThrows.getMessage());

	}// End of deleteBankWithNoBankDetailsFoundException method

	@Test
	public void deleteBankWithSupplierException() {

		List<UserBankDetails> bankDetails = new ArrayList<>();
		userBankDetails.setPrimary(true);
		bankDetails.add(userBankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		lenient().when(bankDetailsRepository.findByIsDeleted(false)).thenReturn(bankDetails);

		lenient().when(bankDetailsRepository.save(userBankDetails)).thenReturn(userBankDetails);

		SupplierException assertThrows = Assertions.assertThrows(SupplierException.class,
				() -> serviceImpl.deleteBank("SUP0001", 1l));

		Assertions.assertEquals("Something Went Wrong", assertThrows.getMessage());

	}// End of deleteBankWithSupplierException method

	@Test
	public void getBankById() {
		List<UserBankDetails> bankDetails = new ArrayList<>();
		bankDetails.add(userBankDetails);

		registration.setUserBankDetails(bankDetails);

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		UserBankDetails bankById = serviceImpl.getBankById("SUP0001", 1l);

		assertThat(bankById).isEqualTo(userBankDetails);

	}// End of getBankById method

	@Test
	public void getBankByIdException() {

		registration.setUserBankDetails(new ArrayList<>());

		NoBankDetailsFoundException assertThrows = Assertions.assertThrows(NoBankDetailsFoundException.class,
				() -> serviceImpl.getBankById("SUP0001", 1l));

		Assertions.assertEquals("No Bank details found", assertThrows.getMessage());

	}// End of getBankByIdException method

}// End of UserBankDetailsServiceImplTest class
