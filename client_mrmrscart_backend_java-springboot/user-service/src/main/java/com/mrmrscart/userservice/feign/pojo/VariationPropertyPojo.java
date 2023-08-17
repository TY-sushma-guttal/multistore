package com.mrmrscart.userservice.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariationPropertyPojo {
	private String standardOptionId;
	private String optionName;
	private String standardVariationId;
	private String standardVariationName;
	private String variationType;
}
