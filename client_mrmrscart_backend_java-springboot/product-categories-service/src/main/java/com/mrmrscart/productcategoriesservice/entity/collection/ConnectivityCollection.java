package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_connectivity_collection")
public class ConnectivityCollection {

	@Id
	private String connectivityCollectionId;
	private String connectivityType; 
	private boolean isDisable;
}
