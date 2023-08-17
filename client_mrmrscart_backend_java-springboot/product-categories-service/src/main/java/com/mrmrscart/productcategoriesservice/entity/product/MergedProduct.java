package com.mrmrscart.productcategoriesservice.entity.product;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_merged_product")
public class MergedProduct {
	
	@Id
	private String mergedProductId;
	private List<String> productIdList;

}
