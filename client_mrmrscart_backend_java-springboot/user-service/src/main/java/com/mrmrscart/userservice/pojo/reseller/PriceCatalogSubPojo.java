package com.mrmrscart.userservice.pojo.reseller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCatalogSubPojo {

	private String productName;
	private String shortDescription;
	private String purchaseLink;
}
