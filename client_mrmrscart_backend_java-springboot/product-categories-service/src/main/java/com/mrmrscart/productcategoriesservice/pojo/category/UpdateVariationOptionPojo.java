package com.mrmrscart.productcategoriesservice.pojo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVariationOptionPojo {

	private String optionId;
	private String optionName;
	private boolean isDisable;
	private boolean isDelete;
}
