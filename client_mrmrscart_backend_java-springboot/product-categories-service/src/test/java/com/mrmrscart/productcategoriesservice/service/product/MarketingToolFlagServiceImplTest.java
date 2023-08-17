package com.mrmrscart.productcategoriesservice.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.entity.product.SupplierFlag;
import com.mrmrscart.productcategoriesservice.exception.product.MarketingToolSubscriptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.ToolSubscriptionExpiredException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.enums.EMarketingToolStatus;
import com.mrmrscart.productcategoriesservice.feign.pojo.MarketingToolPurchaseHistoryPojo;
import com.mrmrscart.productcategoriesservice.feign.response.MarketingToolResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagPojo;
import com.mrmrscart.productcategoriesservice.repository.product.ProductFlagRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;
import com.mrmrscart.productcategoriesservice.repository.product.SupplierFlagRepository;

@ExtendWith(MockitoExtension.class)
class MarketingToolFlagServiceImplTest {

	@InjectMocks
	private MarketingToolFlagServiceImpl marketingToolFlagServiceImpl;

	@Mock
	private ProductFlagRepository productFlagRepository;

	@Mock
	private SupplierFlagRepository supplierFlagRepository;

	@Mock
	private ProductVariationRepository productVariationRepository;

	@Mock
	private UserServiceClient userServiceClient;

	@Test
	void getAllFlagByUserType() {

		List<ProductFlag> productFlags = new ArrayList<>();
		ProductFlag productFlag = new ProductFlag("1", "title", "cloth", "cloth", "url", null, "Theme details", "Blue",
				null, null, EStatus.APPROVED, "url", null, null, null, false, false, EUserType.SUPPLIER);
		productFlags.add(productFlag);

		MarketingToolPurchaseHistoryPojo marketingToolPurchaseHistoryPojo = new MarketingToolPurchaseHistoryPojo(1l,
				"cloth", null, null, EMarketingToolStatus.ACTIVE, new BigDecimal(5.500), null, null, "type", null,
				false, null, null, null, null);

		MarketingToolResponse marketingToolResponse = new MarketingToolResponse(false, "Message",
				marketingToolPurchaseHistoryPojo);
		ResponseEntity<MarketingToolResponse> responseEntity = new ResponseEntity<MarketingToolResponse>(
				marketingToolResponse, HttpStatus.OK);

		when(productFlagRepository.findByUserTypeAndIsDeletedAndIsEnabled(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenReturn(productFlags);
		when(userServiceClient.getSubscription(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseEntity);

		List<DropDownPojo> result = marketingToolFlagServiceImpl.getAllFlagByUserType(EUserType.SUPPLIER, "1");

		assertThat(result.get(0).getImageUrl()).isEqualTo("url");
	}

	@Test
	void getAllFlagByUserTypeWithException() {

		when(productFlagRepository.findByUserTypeAndIsDeletedAndIsEnabled(Mockito.any(), Mockito.anyBoolean(),
				Mockito.anyBoolean())).thenThrow();

		assertThatThrownBy(() -> marketingToolFlagServiceImpl.getAllFlagByUserType(EUserType.SUPPLIER, "1"))
				.isInstanceOf(Exception.class);

	}

	@Test
	void updateProductsToFlag() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", null, null, variationList, 25,
				EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);
		SupplierFlag supplierFlag = new SupplierFlag("1", "Title", "url", null, null, variationList, 25, EStatus.ACTIVE,
				1l, "1", "1", false, null, null);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(supplierFlag);
		when(productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean())).thenReturn(productVariation);
		when(supplierFlagRepository.save(Mockito.any())).thenReturn(supplierFlag);
		when(productVariationRepository.saveAll(Mockito.anyList())).thenReturn(productVariations);

		SupplierFlag result = marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo);
		assertThat(result.getPurchaseId()).isEqualTo(1l);

	}

	@Test
	void addProductsToFlag() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String str = "04-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", localDateTime, localDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);
		SupplierFlag supplierFlag = new SupplierFlag("1", "Title", "url", null, null, variationList, 25, EStatus.ACTIVE,
				1l, "1", "1", false, null, null);

		MarketingToolPurchaseHistoryPojo marketingToolPurchaseHistoryPojo = new MarketingToolPurchaseHistoryPojo(1l,
				"cloth", null, null, EMarketingToolStatus.ACTIVE, new BigDecimal(5.500), null, localDateTime, "type",
				null, false, null, null, null, null);

		MarketingToolResponse marketingToolResponse = new MarketingToolResponse(false, "Message",
				marketingToolPurchaseHistoryPojo);
		ResponseEntity<MarketingToolResponse> responseEntity = new ResponseEntity<MarketingToolResponse>(
				marketingToolResponse, HttpStatus.OK);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(null);
		lenient().when(userServiceClient.getSubscription(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseEntity);
		when(productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean())).thenReturn(productVariation);
		when(supplierFlagRepository.save(Mockito.any())).thenReturn(supplierFlag);
		when(productVariationRepository.saveAll(Mockito.anyList())).thenReturn(productVariations);

		SupplierFlag result = marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo);
		assertThat(result.getPurchaseId()).isEqualTo(1l);

	}

	@Test
	void addProductsToFlagWithException() {
		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String str = "04-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", localDateTime, localDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenThrow();
		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
				.isInstanceOf(Exception.class);
	}

	@Test
	void addProductsToFlagWithToolSubscriptionExpiredException() {
		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String str = "04-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", localDateTime, localDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenThrow(ToolSubscriptionExpiredException.class);
		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
				.isInstanceOf(ToolSubscriptionExpiredException.class);
	}

	@Test
	void updateProductsToFlagWithProductVariationNotFound() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", null, null, variationList, 25,
				EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);
		SupplierFlag supplierFlag = new SupplierFlag("1", "Title", "url", null, null, variationList, 25, EStatus.ACTIVE,
				1l, "1", "1", false, null, null);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(supplierFlag);
		when(productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean())).thenReturn(null);

		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
				.isInstanceOf(ProductVariationNotFound.class);

	}

	@Test
	void updateProductsToFlagWithNewVariation() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		List<String> supplierPojoVariations = new ArrayList<>();
		supplierPojoVariations.add("1");
		supplierPojoVariations.add("2");
		supplierPojoVariations.add("3");

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", null, null,
				supplierPojoVariations, 25, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);
		SupplierFlag supplierFlag = new SupplierFlag("1", "Title", "url", null, null, variationList, 25, EStatus.ACTIVE,
				1l, "1", "1", false, null, null);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(supplierFlag);
		when(productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean())).thenReturn(productVariation);
		when(supplierFlagRepository.save(Mockito.any())).thenReturn(supplierFlag);
		when(productVariationRepository.saveAll(Mockito.anyList())).thenReturn(productVariations);

		SupplierFlag result = marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo);
		assertThat(result.getPurchaseId()).isEqualTo(1l);

	}

	@Test
	void saveProductsToFlagWithProductVariationNotFound() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String str = "04-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", localDateTime, localDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);

		MarketingToolPurchaseHistoryPojo marketingToolPurchaseHistoryPojo = new MarketingToolPurchaseHistoryPojo(1l,
				"cloth", null, null, EMarketingToolStatus.ACTIVE, new BigDecimal(5.500), null, localDateTime, "type",
				null, false, null, null, null, null);

		MarketingToolResponse marketingToolResponse = new MarketingToolResponse(false, "Message",
				marketingToolPurchaseHistoryPojo);
		ResponseEntity<MarketingToolResponse> responseEntity = new ResponseEntity<MarketingToolResponse>(
				marketingToolResponse, HttpStatus.OK);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(null);
		lenient().when(userServiceClient.getSubscription(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseEntity);
		when(productVariationRepository.findByProductVariationIdAndStatusAndIsDelete(Mockito.anyString(), Mockito.any(),
				Mockito.anyBoolean())).thenReturn(null);

		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
				.isInstanceOf(ProductVariationNotFound.class);

	}

	@Test
	void saveProductsToFlagWhenSubscriptionNotFound() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String str = "04-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", localDateTime, localDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);

		MarketingToolResponse marketingToolResponse = null;
		ResponseEntity<MarketingToolResponse> responseEntity = new ResponseEntity<MarketingToolResponse>(
				marketingToolResponse, HttpStatus.BAD_REQUEST);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(null);
		lenient().when(userServiceClient.getSubscription(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseEntity);

		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
				.isInstanceOf(MarketingToolSubscriptionNotFoundException.class);

	}
	
	@Test
	void saveProductsToFlagWhenSubscriptionExpired() {

		List<String> variationList = new ArrayList<>();
		variationList.add("1");
		variationList.add("2");

		String supplierFlagStr = "10-08-2022 12:30:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
		LocalDateTime supplierFlagLocalDateTime = LocalDateTime.parse(supplierFlagStr, formatter);
		
		String marketingToolStr = "04-08-2022 12:30:00";
		LocalDateTime marketingToolLocalDateTime = LocalDateTime.parse(marketingToolStr, formatter);

		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo("1", "Title", "url", supplierFlagLocalDateTime, supplierFlagLocalDateTime,
				variationList, 25.5, EStatus.ACTIVE, "1", "1", "1", EUserType.SUPPLIER, 1l);

		MarketingToolPurchaseHistoryPojo marketingToolPurchaseHistoryPojo = new MarketingToolPurchaseHistoryPojo(1l,
				"cloth", null, null, EMarketingToolStatus.ACTIVE, new BigDecimal(5.500), null, marketingToolLocalDateTime, "type",
				null, false, null, null, null, null);

		MarketingToolResponse marketingToolResponse = new MarketingToolResponse(false, "Message",
				marketingToolPurchaseHistoryPojo);
		ResponseEntity<MarketingToolResponse> responseEntity = new ResponseEntity<MarketingToolResponse>(
				marketingToolResponse, HttpStatus.OK);

		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation("1", "1", "1", "title", "shipping", "1 day", null,
				"meta description", "meta keyword", false, null, false, 0, false, false, null, null, null, null, null,
				null, null, null, null, null, 0, null, false, null, null, false, 0, false, null, null, null,
				EStatus.APPROVED, null, null, null, null, null, false, false, null, null);
		productVariations.add(productVariation);

		when(supplierFlagRepository.findByFlagIdAndPurchaseIdAndIsDisabledAndStatusAndSupplierStoreId(
				Mockito.anyString(), Mockito.anyLong(), Mockito.anyBoolean(), Mockito.any(), Mockito.any()))
						.thenReturn(null);
		lenient().when(userServiceClient.getSubscription(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseEntity);
		assertThatThrownBy(() -> marketingToolFlagServiceImpl.addProductsToFlag(supplierFlagPojo))
		.isInstanceOf(ToolSubscriptionExpiredException.class);
		
	}


}
