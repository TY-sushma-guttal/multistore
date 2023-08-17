package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_format_collection")
public class FormatCollection {
	@Id
	private String formatCollectionId;
	private String formatType;
	private boolean isDisable;
}
