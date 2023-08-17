package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_quality_collection")
public class QualityCollection {
	@Id
	private String qualityCollectionId;
	private String qualityName;
	private boolean isDisable;
}
