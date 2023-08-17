package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_fit_top_collection")
public class FitTopCollection {
	@Id
	private String fitTopCollectionId;
	private String fitTopName;
	private boolean isDisable;
}
