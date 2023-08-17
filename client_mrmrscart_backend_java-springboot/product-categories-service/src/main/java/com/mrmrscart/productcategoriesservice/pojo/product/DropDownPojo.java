package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class DropDownPojo {

	private String id;
	private String name;
	private String imageUrl;
	private Long purchaseId;
	private BigDecimal price;

	public DropDownPojo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DropDownPojo(String id, String name, Long purchaseId) {
		super();
		this.id = id;
		this.name = name;
		this.purchaseId = purchaseId;
	}

	public DropDownPojo(String id, String name, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
	}

}
