package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTagViewPojo {

	private String productName;
	private String productId;
	private String supplierId;
	private String createdBy;
	private LocalDateTime lastUpdatedAt;
}
