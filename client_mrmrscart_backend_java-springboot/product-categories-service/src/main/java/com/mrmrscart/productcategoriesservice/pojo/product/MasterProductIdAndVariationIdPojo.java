package com.mrmrscart.productcategoriesservice.pojo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductIdAndVariationIdPojo {
	private String masterProductId;
	private String variationId;
}
