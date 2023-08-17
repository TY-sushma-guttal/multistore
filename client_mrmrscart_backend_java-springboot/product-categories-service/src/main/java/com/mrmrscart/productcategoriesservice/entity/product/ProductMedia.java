package com.mrmrscart.productcategoriesservice.entity.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_product_media")
public class ProductMedia {

	@Id
	private String productMediaId;
	private String mediaUrl;
}
