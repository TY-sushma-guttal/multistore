package com.mrmrscart.userservice.service.supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.supplier.InactiveSupplierStoreException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreInfoRepository;

@ExtendWith(MockitoExtension.class)
public class SupplierStoreServiceImplTest {

	@InjectMocks
	private SupplierStoreServiceImpl serviceImpl;

	@Mock
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	private SupplierRegistration registration;

	private SupplierStoreInfo storeInfo;

	@BeforeEach
	public void setUp() {

		registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		storeInfo = new SupplierStoreInfo();
		storeInfo.setSupplierStoreInfoId(1l);
		storeInfo.setSupplierStoreCode("SUPSTRCODE");
		storeInfo.setStoreActive(true);
		storeInfo.setActiveProductCount(50);

		registration.setSupplierStoreInfo(storeInfo);

	}// End of setUp method

	@Test
	@DisplayName("getSupplierStoreConfiguration_With_Exception")
	public void getSupplierStoreConfiguration() {

		when(supplierStoreInfoRepository.findBySupplierStoreCodeAndIsStoreActive("SUPSTRCODE", true))
				.thenReturn(storeInfo);

		SupplierStoreInfo supplierStoreConfiguration = serviceImpl.getSupplierStoreConfiguration("SUPSTRCODE");

		assertThat(supplierStoreConfiguration.getSupplierStoreCode()).isEqualTo(storeInfo.getSupplierStoreCode());

		SupplierStoreInfo storeInfo1 = null;

		when(supplierStoreInfoRepository.findBySupplierStoreCodeAndIsStoreActive("SUPSTRCODE", true))
				.thenReturn(storeInfo1);

		InactiveSupplierStoreException assertThrows = Assertions.assertThrows(InactiveSupplierStoreException.class,
				() -> serviceImpl.getSupplierStoreConfiguration("SUPSTRCODE"));

		Assertions.assertEquals("Supplier Store Is Inactive", assertThrows.getMessage());

		SupplierStoreInfo storeInfo2 = null;

		when(supplierStoreInfoRepository.findBySupplierStoreCodeAndIsStoreActive("SUPSTRCODE", true))
				.thenReturn(storeInfo2);

		Exception assertThrowsI = Assertions.assertThrows(Exception.class,
				() -> serviceImpl.getSupplierStoreConfiguration(null));

		Assertions.assertEquals("Something Went Wrong. ", assertThrowsI.getMessage());

	}// End of getSupplierStoreConfiguration method

	@Test
	public void updateProductCountI() {

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(Optional.of(registration));

		storeInfo.setStoreActive(true);
		storeInfo.setActiveProductCount(50);

		registration.setSupplierStoreInfo(storeInfo);

		when(supplierRegistrationRepository.save(registration)).thenReturn(registration);

		SupplierStoreInfo updateProductCount = serviceImpl.updateProductCount("SUP0001", 50);

		assertThat(updateProductCount.getSupplierStoreCode())
				.isEqualTo(registration.getSupplierStoreInfo().getSupplierStoreCode());

	}// End of updateProductCountI method

	@Test
	public void updateProductCountII() {

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(Optional.of(registration));

		storeInfo.setStoreActive(false);
		storeInfo.setActiveProductCount(49);

		registration.setSupplierStoreInfo(storeInfo);

		when(supplierRegistrationRepository.save(registration)).thenReturn(registration);

		SupplierStoreInfo updateProductCount = serviceImpl.updateProductCount("SUP0001", 49);

		assertThat(updateProductCount.getSupplierStoreCode())
				.isEqualTo(registration.getSupplierStoreInfo().getSupplierStoreCode());

		SupplierIdNotFoundException assertThrows = Assertions.assertThrows(SupplierIdNotFoundException.class,
				() -> serviceImpl.updateProductCount("SUP001", 0));

		Assertions.assertEquals("Supplier Not Found!", assertThrows.getMessage());

	}// End of updateProductCountII method

}// End of SupplierStoreServiceImplTest class
