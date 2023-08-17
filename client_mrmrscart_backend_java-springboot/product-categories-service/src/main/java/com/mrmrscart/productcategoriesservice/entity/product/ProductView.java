package com.mrmrscart.productcategoriesservice.entity.product;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_product_view")
public class ProductView {

	@Id
	private String productViewId;
	private String masterProductId;
	private List<String> profileIdList;
}
