package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrademarkInvoiceInfoPojo {

	private String trademarkInvoiceId;
	private String supplierId;
	private String documentName;
	private String documentType;
	private String description;
	private List<String> documentUrl;

}
