package com.mrmrscart.productcategoriesservice.entity.collection;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_size_collection")
public class SizeCollection {
	@Id
	private String sizeCollectionId;
	private List<String> kidsSize;
	private List<String> sizeInRoman;
	private List<String> sizeInNumber;
 }
