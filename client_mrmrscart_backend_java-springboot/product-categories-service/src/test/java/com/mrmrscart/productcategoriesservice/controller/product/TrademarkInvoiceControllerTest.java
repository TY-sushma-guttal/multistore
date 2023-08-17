package com.mrmrscart.productcategoriesservice.controller.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;
import com.mrmrscart.productcategoriesservice.pojo.product.TrademarkInvoiceInfoPojo;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.TrademarkInvoiceService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TrademarkInvoiceControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private TrademarkInvoiceInfoPojo trademarkInvoiceInfoPojo;

	private TrademarkInvoiceInfo trademarkInvoiceInfo;

	@MockBean
	private TrademarkInvoiceService trademarkInvoiceService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		trademarkInvoiceInfoPojo = new TrademarkInvoiceInfoPojo("InvoiceId", "SupplierId", "DocName", "DocType",
				"Description", new ArrayList<>());
		trademarkInvoiceInfo = new TrademarkInvoiceInfo("InvoiceId", "SupplierId", "DocType", "DocName", "Description",
				new ArrayList<>(), false, LocalDateTime.now(), LocalDateTime.now(), "LastUpdatedBy");
	}

	@Test
	void saveTrademarkInvoiceInfo() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(trademarkInvoiceService.saveTrademarkInvoiceInfo(Mockito.any())).thenReturn(trademarkInvoiceInfo);

		String result = mockMvc
				.perform(post("/api/v1/products/supplier/product/trademark-invoice")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(trademarkInvoiceInfoPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Trademark Invoice Details Saved Successfully", successResponse.getMessage());

	}

	@Test
	void updateTrademarkInvoiceInfo() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(trademarkInvoiceService.updateTrademarkInvoiceInfo(Mockito.any())).thenReturn(trademarkInvoiceInfo);

		String result = mockMvc
				.perform(put("/api/v1/products/supplier/product/trademark-invoice")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(trademarkInvoiceInfoPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Trademark invoice details updated successfully", successResponse.getMessage());

	}

	@Test
	void getTrademarkInvoiceInfoWhenSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<TrademarkInvoiceInfo> trademarkInfos = new ArrayList<>();
		trademarkInfos.add(trademarkInvoiceInfo);

		when(trademarkInvoiceService.getTrademarkInfo(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(trademarkInfos);

		String result = mockMvc
				.perform(get("/api/v1/products/supplier/product/trademark-invoice/2/2").param("keyword", "keyword")
						.param("supplierId", "supplierId").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Trademark invoice details found successfully", successResponse.getMessage());

	}

	@Test
	void getTrademarkInvoiceInfoWhenFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<TrademarkInvoiceInfo> trademarkInfos = new ArrayList<>();

		when(trademarkInvoiceService.getTrademarkInfo(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(trademarkInfos);

		String result = mockMvc
				.perform(get("/api/v1/products/supplier/product/trademark-invoice/2/2").param("keyword", "keyword")
						.param("supplierId", "supplierId").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("There are no trademark invoice details for the supplier", successResponse.getMessage());

	}

	@Test
	void getTrademarkInvoiceForDropdownWhenSuccess()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<TrademarkInvoiceInfo> trademarkInfos = new ArrayList<>();
		trademarkInfos.add(trademarkInvoiceInfo);

		when(trademarkInvoiceService.getTrademarkInvoiceForDropdown(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(trademarkInfos);

		String result = mockMvc
				.perform(get("/api/v1/products/supplier/product/trademark-invoice-dropdown")
						.param("documentType", "documentType").param("supplierId", "supplierId")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Trademark invoice details found successfully", successResponse.getMessage());

	}

	@Test
	void getTrademarkInvoiceForDropdownWhenFailure()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<TrademarkInvoiceInfo> trademarkInfos = new ArrayList<>();

		when(trademarkInvoiceService.getTrademarkInvoiceForDropdown(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(trademarkInfos);

		String result = mockMvc
				.perform(get("/api/v1/products/supplier/product/trademark-invoice-dropdown")
						.param("documentType", "documentType").param("supplierId", "supplierId")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("There are no trademark invoice details for the supplier", successResponse.getMessage());

	}

	@Test
	void deleteTrademarkInvoiceInfo() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(trademarkInvoiceService.deleteTrademarkInvoiceInfo(Mockito.anyString()))
				.thenReturn(trademarkInvoiceInfo);

		String result = mockMvc
				.perform(delete("/api/v1/products/supplier/product/trademark-invoice")
						.param("trademarkInvoiceId", "trademarkInvoiceId").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Trademark invoice deleted successfully", successResponse.getMessage());

	}
}
