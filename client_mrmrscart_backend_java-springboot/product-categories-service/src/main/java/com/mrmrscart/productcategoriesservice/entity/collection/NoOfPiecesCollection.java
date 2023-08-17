package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_no_of_pieces_collection")
public class NoOfPiecesCollection {
	@Id
	private String noOfPiecesCollectionId;
	private String noOfPiecesName;
	private boolean isDisable;
}
