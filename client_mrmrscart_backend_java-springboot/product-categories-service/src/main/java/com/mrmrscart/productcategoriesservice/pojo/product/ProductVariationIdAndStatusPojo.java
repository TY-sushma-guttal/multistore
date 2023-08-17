package com.mrmrscart.productcategoriesservice.pojo.product;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariationIdAndStatusPojo {
	private String productVariationId;
	private EStatus status;
}
