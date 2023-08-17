package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductFlagingPojo {

	private String flagId;
	private List<String> variationId;
}
