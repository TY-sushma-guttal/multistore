package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_shape_collection")
public class ShapeCollection {

	@Id
	private String shapeCollectionId;
	private String shapeType;
	private boolean isDisable;
}
