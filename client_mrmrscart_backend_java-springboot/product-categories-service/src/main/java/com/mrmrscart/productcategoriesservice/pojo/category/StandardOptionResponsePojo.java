package com.mrmrscart.productcategoriesservice.pojo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardOptionResponsePojo {

	
	private String standardOptionId;
	private String optionName;
	private boolean isDisable;
	private boolean isDelete;
}
