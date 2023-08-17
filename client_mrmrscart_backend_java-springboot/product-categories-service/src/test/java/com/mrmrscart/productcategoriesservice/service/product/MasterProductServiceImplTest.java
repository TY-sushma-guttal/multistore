package com.mrmrscart.productcategoriesservice.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.pojo.SupplierRegistration;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierRegistrationGetResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;

@ExtendWith(MockitoExtension.class)
public class MasterProductServiceImplTest {
	
	@InjectMocks
	private MasterProductServiceImpl masterProductServiceImpl;
	
	@Mock
	private UserServiceClient userServiceClient;
	
	
	@Test
	void addSimpleAndVariableProduct() {
		MasterProductPojo masterProductPojo = new MasterProductPojo();
//		masterProductPojo.set
		
		SupplierRegistrationGetResponse supplierRegistrationGetResponse = new SupplierRegistrationGetResponse();
		SupplierRegistration supplierRegistration = new SupplierRegistration();
		ResponseEntity responseEntity = new ResponseEntity<SupplierRegistrationGetResponse>(
				new SupplierRegistrationGetResponse(false, "message", supplierRegistration), HttpStatus.OK);
	
		when(userServiceClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(responseEntity);
		MasterProduct result = masterProductServiceImpl.addSimpleAndVariableProduct(masterProductPojo);
		assertThat(result.getBrand()).isEqualTo("DocName");
	
	}

}
