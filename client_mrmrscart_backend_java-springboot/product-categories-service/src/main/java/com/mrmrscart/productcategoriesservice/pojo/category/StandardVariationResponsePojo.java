package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardVariationResponsePojo {

	
	private String standardVariationId;
	private String variationName;
	private boolean isDisable;
	private List<StandardOptionResponsePojo> optionList;
	private boolean isDelete;
	private String variationType;
}
