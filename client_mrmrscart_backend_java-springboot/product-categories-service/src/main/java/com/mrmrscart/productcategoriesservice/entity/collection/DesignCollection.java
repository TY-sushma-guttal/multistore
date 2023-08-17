package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_design_collection")
public class DesignCollection {
	@Id
	private String designCollectionId;
	private String designName;
	private boolean isDisable;
}
	