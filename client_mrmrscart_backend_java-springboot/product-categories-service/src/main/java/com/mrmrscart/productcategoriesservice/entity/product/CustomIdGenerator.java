package com.mrmrscart.productcategoriesservice.entity.product;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "custom_id_collection")
public class CustomIdGenerator {

	@Id
	private String customId;
	private EProductCoupon idType;
	private BigInteger idValue;
}
