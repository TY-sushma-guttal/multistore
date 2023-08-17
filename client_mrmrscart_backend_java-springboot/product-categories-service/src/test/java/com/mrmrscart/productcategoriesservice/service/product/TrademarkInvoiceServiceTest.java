package com.mrmrscart.productcategoriesservice.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.TrademarkInvoiceNotFoundException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.pojo.SupplierRegistration;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierRegistrationGetResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.TrademarkInvoiceInfoPojo;
import com.mrmrscart.productcategoriesservice.repository.product.InvoiceTrademarkRepository;

@ExtendWith(MockitoExtension.class)
class TrademarkInvoiceServiceTest {

	@InjectMocks
	private TrademarkInvoiceServiceImpl trademarkInvoiceServiceImpl;

	@Mock
	private InvoiceTrademarkRepository invoiceTrademarkRepository;

	@Mock
	private UserServiceClient userClient;

	@Mock
	private MongoTemplate mongoTemplate;
	
	private TrademarkInvoiceInfoPojo trademarkInvoiceInfoPojo;
	
	private TrademarkInvoiceInfo trademarkInvoiceInfo;
	
	private List<TrademarkInvoiceInfo> trademarkInvoiceInfos = new ArrayList<>();
	
	private ResponseEntity<SupplierRegistrationGetResponse> supplierById;
	
	private SupplierRegistration supplierRegistration;
	
	@BeforeEach
	void testValueSetup() {
		trademarkInvoiceInfoPojo = new TrademarkInvoiceInfoPojo("InvoiceId", "SupplierId",
				"DocName", "DocType", "Description", new ArrayList<>());
		trademarkInvoiceInfo = new TrademarkInvoiceInfo("InvoiceId", "SupplierId", "DocType",
				"DocName", "Description", new ArrayList<>(), false, LocalDateTime.now(), LocalDateTime.now(),
				"LastUpdatedBy"); 
		supplierRegistration =  new SupplierRegistration();
		supplierById = new ResponseEntity<SupplierRegistrationGetResponse>(
				new SupplierRegistrationGetResponse(false, "message", supplierRegistration), HttpStatus.OK);
	}

	@Test
	void saveTrademarkInvoiceInfoWithSupplierRegistrationGetResponse() {
		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);
		when(invoiceTrademarkRepository.save(Mockito.any())).thenReturn(trademarkInvoiceInfo);

		TrademarkInvoiceInfo result = trademarkInvoiceServiceImpl.saveTrademarkInvoiceInfo(trademarkInvoiceInfoPojo);
		assertThat(result.getDocumentName()).isEqualTo("DocName");
	}

	@Test
	void saveTrademarkInvoiceInfoWithoutSupplierRegistrationGetResponse() {
		SupplierRegistrationGetResponse supplierRegistrationGetResponse = null;
		ResponseEntity<SupplierRegistrationGetResponse> supplierById = new ResponseEntity<SupplierRegistrationGetResponse>(
				supplierRegistrationGetResponse, HttpStatus.OK);

		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.saveTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(SupplierNotFoundException.class);
	}

	@Test
	void saveTrademarkInvoiceInfoWithoutSupplierRegistration() {
		ResponseEntity<SupplierRegistrationGetResponse> supplierById = new ResponseEntity<SupplierRegistrationGetResponse>(
				new SupplierRegistrationGetResponse(false, "message", null), HttpStatus.OK);

		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.saveTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(SupplierNotFoundException.class);
	}

	@Test
	void saveTrademarkInvoiceInfoWithMasterProductException() {
		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenThrow(MasterProductException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.saveTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(MasterProductException.class);
	}

	@Test
	void updateTrademarkInvoiceInfoWithSupplierRegistrationGetResponse() {
		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);
		when(invoiceTrademarkRepository.findByIsDeletedAndTrademarkInvoiceId(Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(trademarkInvoiceInfo);
		when(invoiceTrademarkRepository.save(Mockito.any())).thenReturn(trademarkInvoiceInfo);

		TrademarkInvoiceInfo result = trademarkInvoiceServiceImpl.updateTrademarkInvoiceInfo(trademarkInvoiceInfoPojo);
		assertThat(result.getDocumentName()).isEqualTo("DocName");
	}

	@Test
	void updateTrademarkInvoiceInfoWithoutTrademarkInvoiceInfo() {
		TrademarkInvoiceInfo trademarkInvoiceInfo = null;

		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);
		when(invoiceTrademarkRepository.findByIsDeletedAndTrademarkInvoiceId(Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(trademarkInvoiceInfo);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.updateTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(TrademarkInvoiceNotFoundException.class);
	}

	@Test
	void updateTrademarkInvoiceInfoWithoutSupplierRegistrationGetResponse() {
		ResponseEntity<SupplierRegistrationGetResponse> supplierById = new ResponseEntity<SupplierRegistrationGetResponse>(
				new SupplierRegistrationGetResponse(false, "message", null), HttpStatus.OK);

		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenReturn(supplierById);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.updateTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(TrademarkInvoiceNotFoundException.class);
	}

	@Test
	void updateTrademarkInvoiceInfoWithSupplierNotFoundException() {
		when(userClient.getSupplierById(Mockito.anyString(), Mockito.any())).thenThrow(SupplierNotFoundException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.updateTrademarkInvoiceInfo(trademarkInvoiceInfoPojo))
				.isInstanceOf(SupplierNotFoundException.class);
	}

	@Test
	void getTrademarkInvoiceForDropdown() {
		trademarkInvoiceInfos.add(trademarkInvoiceInfo);

		when(invoiceTrademarkRepository.findByIsDeletedAndSupplierIdAndDocumentType(Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(trademarkInvoiceInfos);
		List<TrademarkInvoiceInfo> result = trademarkInvoiceServiceImpl.getTrademarkInvoiceForDropdown("DocType",
				"SupplierId");
		assertThat(result.get(0).getDescription()).isEqualTo("Description");
	}

	@Test
	void getTrademarkInvoiceForDropdownWithTrademarkInvoiceNotFoundException() {
		when(invoiceTrademarkRepository.findByIsDeletedAndSupplierIdAndDocumentType(Mockito.anyBoolean(),
				Mockito.anyString(), Mockito.anyString())).thenThrow(TrademarkInvoiceNotFoundException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.getTrademarkInvoiceForDropdown("DocType", "SupplierId"))
				.isInstanceOf(TrademarkInvoiceNotFoundException.class);
	}

	@Test
	void deleteTrademarkInvoiceInfo() {
		when(invoiceTrademarkRepository.findByIsDeletedAndTrademarkInvoiceId(Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(trademarkInvoiceInfo);
		when(invoiceTrademarkRepository.save(Mockito.any())).thenReturn(trademarkInvoiceInfo);

		TrademarkInvoiceInfo result = trademarkInvoiceServiceImpl.deleteTrademarkInvoiceInfo("InvoiceId");
		assertThat(result.getDocumentName()).isEqualTo("DocName");
	}

	@Test
	void deleteTrademarkInvoiceInfoWithTrademarkInvoiceNotFoundException() {
		TrademarkInvoiceInfo trademarkInvoiceInfo = null;

		when(invoiceTrademarkRepository.findByIsDeletedAndTrademarkInvoiceId(Mockito.anyBoolean(), Mockito.anyString()))
				.thenReturn(trademarkInvoiceInfo);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.deleteTrademarkInvoiceInfo("InvoiceId"))
				.isInstanceOf(TrademarkInvoiceNotFoundException.class);
	}

	@Test
	void deleteTrademarkInvoiceInfoWithSupplierNotFoundException() {
		when(invoiceTrademarkRepository.findByIsDeletedAndTrademarkInvoiceId(Mockito.anyBoolean(), Mockito.anyString()))
				.thenThrow(SupplierNotFoundException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.deleteTrademarkInvoiceInfo("InvoiceId"))
				.isInstanceOf(SupplierNotFoundException.class);
	}

	@Test
	void getTrademarkInfo() {
		trademarkInvoiceInfos.add(trademarkInvoiceInfo);
		trademarkInvoiceInfos.add(trademarkInvoiceInfo);
		when(mongoTemplate.find(Mockito.any(), ArgumentMatchers.<Class<TrademarkInvoiceInfo>>any()))
				.thenReturn(trademarkInvoiceInfos);
		when(invoiceTrademarkRepository.findByIsDeletedAndSupplierId(Mockito.anyBoolean(), Mockito.anyString(),
				Mockito.any())).thenReturn(trademarkInvoiceInfos);

		List<TrademarkInvoiceInfo> resultWithSkipCount = trademarkInvoiceServiceImpl.getTrademarkInfo(1, 1, "keyword",
				"SupplierId");
		assertThat(resultWithSkipCount.get(0).getDescription()).isEqualTo("Description");

		List<TrademarkInvoiceInfo> resultWithoutSkipCount = trademarkInvoiceServiceImpl.getTrademarkInfo(0, 1,
				"keyword", "SupplierId");
		assertThat(resultWithoutSkipCount.get(0).getDescription()).isEqualTo("Description");

		List<TrademarkInvoiceInfo> resultWithoutKeyword = trademarkInvoiceServiceImpl.getTrademarkInfo(1, 1, null,
				"SupplierId");
		assertThat(resultWithoutKeyword.get(0).getDescription()).isEqualTo("Description");
	}

	@Test
	void getTrademarkInfoWithTrademarkInvoiceNotFoundException() {
		when(mongoTemplate.find(Mockito.any(), ArgumentMatchers.<Class<TrademarkInvoiceInfo>>any()))
				.thenThrow(TrademarkInvoiceNotFoundException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.getTrademarkInfo(1, 1, "keyword", "SupplierId"))
				.isInstanceOf(TrademarkInvoiceNotFoundException.class);
	}
	
	@Test
	void getTrademarkInfoWithSupplierNotFoundException() {
		when(mongoTemplate.find(Mockito.any(), ArgumentMatchers.<Class<TrademarkInvoiceInfo>>any()))
				.thenThrow(SupplierNotFoundException.class);

		assertThatThrownBy(() -> trademarkInvoiceServiceImpl.getTrademarkInfo(1, 1, "keyword", "SupplierId"))
				.isInstanceOf(SupplierNotFoundException.class);
	}
}
