package com.mrmrscart.productcategoriesservice.controller.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.SupplierFlag;
import com.mrmrscart.productcategoriesservice.pojo.product.DropDownPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.SupplierFlagPojo;
import com.mrmrscart.productcategoriesservice.response.product.ProductFlagResponse;
import com.mrmrscart.productcategoriesservice.service.product.MarketingToolFlagService;
import com.mrmrscart.productcategoriesservice.wrapper.product.SupplierFlagWrapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class MarketingToolFlagControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private MarketingToolFlagService marketingToolFlagService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void addProductsToFlagTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		SupplierFlag supplierFlag = new SupplierFlag();
		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo();
		when(marketingToolFlagService.addProductsToFlag(Mockito.any())).thenReturn(supplierFlag);
		String contentAsString = mockMvc
				.perform(post("/api/v1/products/supplier-flag").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(supplierFlagPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Flag saved/updated successfully", readValue.getMessage());
	}

	@Test
	void updateFlagTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		SupplierFlag supplierFlag = new SupplierFlag();
		SupplierFlagPojo supplierFlagPojo = new SupplierFlagPojo();
		when(marketingToolFlagService.addProductsToFlag(Mockito.any())).thenReturn(supplierFlag);
		String contentAsString = mockMvc
				.perform(put("/api/v1/products/supplier-flag").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(supplierFlagPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Flag saved/updated successfully", readValue.getMessage());
	}

	@Test
	void disableFlag() throws UnsupportedEncodingException, Exception {
		SupplierFlag supplierFlag = new SupplierFlag();
		when(marketingToolFlagService.disableFlag(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(supplierFlag);
		String contentAsString = mockMvc
				.perform(put("/api/v1/products/supplier-flag/qwerty@123/true").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Flag saved/updated successfully", readValue.getMessage());
	}

	@Test
	void getFlagByIdTest() throws UnsupportedEncodingException, Exception {
		SupplierFlag supplierFlag = new SupplierFlag();
		when(marketingToolFlagService.getFlagById(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString()))
				.thenReturn(supplierFlag);
		String contentAsString = mockMvc
				.perform(get("/api/v1/products/supplier-flag/test123/123456789/qwerty123")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Flags fetched successfully", readValue.getMessage());
	}

	@Test
	void getAllFlagByUserTypeIfBlockTest() throws UnsupportedEncodingException, Exception {
		DropDownPojo downPojo = new DropDownPojo("qwerty123", "Name");
		List<DropDownPojo> downPojos = new ArrayList<>();
		downPojos.add(downPojo);
		when(marketingToolFlagService.getAllFlagByUserType(Mockito.any(), Mockito.anyString())).thenReturn(downPojos);
		String contentAsString = mockMvc
				.perform(get("/api/v1/products/supplier-flag/ADMIN/qwerty123").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Product flag found successfully. ", readValue.getMessage());
	}

	@Test
	void getAllFlagByUserTypeElseBlockTest() throws UnsupportedEncodingException, Exception {
		List<DropDownPojo> downPojos = new ArrayList<>();
		when(marketingToolFlagService.getAllFlagByUserType(Mockito.any(), Mockito.anyString())).thenReturn(downPojos);
		String contentAsString = mockMvc
				.perform(get("/api/v1/products/supplier-flag/ADMIN/qwerty123").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Failed to save the Product flag ", readValue.getMessage());
	}

	@Test
	void getAllSupplierFlagIfBlockTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		LocalDate d2 = LocalDate.of(2002, 05, 01);
		LocalTime d1 = LocalTime.of(01, 12, 34);
		LocalDateTime dateTime = LocalDateTime.of(d2, d1);
		DropDownPojo downPojo = new DropDownPojo("qwerty123", "Name");
		List<DropDownPojo> downPojos = new ArrayList<>();
		downPojos.add(downPojo);
		EStatus active = EStatus.ACTIVE;
		SupplierFlagWrapper flagWrapper = new SupplierFlagWrapper("supplierflagid", "flagtitle", "imageurl", dateTime,
				dateTime, 10.00, active, 123456678L, "supplierstoreid", "flagId", dateTime, dateTime, "createdby",
				"modifiedby", downPojos);
		List<SupplierFlagWrapper> flagWrappers = new ArrayList<>();
		flagWrappers.add(flagWrapper);
		when(marketingToolFlagService.getAllSupplierFlag(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(flagWrappers);
		SupplierFlagFilterPojo filterPojo = new SupplierFlagFilterPojo();
		String contentAsString = mockMvc
				.perform(post("/api/v1/products/supplier-flag/1/10").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(filterPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Flags fetched successfully", readValue.getMessage());
	}

	@Test
	void getAllSupplierFlagElseBlockTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<SupplierFlagWrapper> flagWrappers = new ArrayList<>();
		when(marketingToolFlagService.getAllSupplierFlag(Mockito.anyInt(), Mockito.anyInt(), Mockito.any()))
				.thenReturn(flagWrappers);
		SupplierFlagFilterPojo filterPojo = new SupplierFlagFilterPojo();
		String contentAsString = mockMvc
				.perform(post("/api/v1/products/supplier-flag/1/10").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(filterPojo)))
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		ProductFlagResponse readValue = mapper.readValue(contentAsString, ProductFlagResponse.class);
		assertEquals("Unable to fetch flags", readValue.getMessage());
	}
}
