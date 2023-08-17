package com.mrmrscart.productcategoriesservice.pojo.product;

import com.mrmrscart.productcategoriesservice.entity.product.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTagPojo {
	
	private String tagName;
	private String createdBy;
	private EUserType createdByType;

}
