package com.mrmrscart.userservice.pojo.reseller;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCatalogPojo {

	private String storeCode;
	private String storeName;
	private String campaignTitle;
	private String description;
	private String price;
	private String shoppingLink;
	
	private List<PriceCatalogSubPojo> priceCatalogSubPojos;
	
}
