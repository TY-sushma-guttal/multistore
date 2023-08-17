package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_neck_style_collection")
public class NeckStyleCollection {
	@Id
	private String neckStyleCollectionId;
	private String neckStyleName;
	private boolean isDisable;
}
