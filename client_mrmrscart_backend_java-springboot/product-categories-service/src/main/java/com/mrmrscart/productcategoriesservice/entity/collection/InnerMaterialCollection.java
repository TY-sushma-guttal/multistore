package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_inner_material_collection")
public class InnerMaterialCollection {
	@Id
	private String innerMaterialCollectionId;
	private String innerMaterialName;
	private boolean isDisable;
}
