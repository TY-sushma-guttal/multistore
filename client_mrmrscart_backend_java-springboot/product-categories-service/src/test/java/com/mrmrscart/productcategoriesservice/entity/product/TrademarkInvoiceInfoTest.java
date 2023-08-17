package com.mrmrscart.productcategoriesservice.entity.product;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class TrademarkInvoiceInfoTest {
	private ObjectMapper mapper = new ObjectMapper();

	String json = "{\"createdBy\":null,\"lastModifiedBy\":null,\"trademarkInvoiceId\":\"InvoiceId\",\"supplierId\":\"SupplierId\",\"documentType\":\"DocType\",\"documentName\":\"DocName\",\"description\":\"Description\",\"documentUrl\":[],\"createdAt\":null,\"lastModifiedAt\":null,\"lastupdatedBy\":\"LastUpdatedBy\",\"deleted\":false}\r\n"
			+ "";

	@Test
	void serializationTest() throws Exception {
		TrademarkInvoiceInfo trademarkInvoiceInfo = new TrademarkInvoiceInfo("InvoiceId", "SupplierId", "DocType",
				"DocName", "Description", new ArrayList<>(), false, null, null, "LastUpdatedBy");
		String expected = mapper.writeValueAsString(mapper.readValue(json, TrademarkInvoiceInfo.class));
		assertEquals(mapper.writeValueAsString(trademarkInvoiceInfo), expected);
	}// end of serializationTest method

	@Test
	void deserializationTest() throws JsonMappingException, JsonProcessingException {
		TrademarkInvoiceInfo trademarkInvoiceInfo = mapper.readValue(json, TrademarkInvoiceInfo.class);
		assertEquals("DocName", trademarkInvoiceInfo.getDocumentName());
	}// end of deserializationTest method

}
