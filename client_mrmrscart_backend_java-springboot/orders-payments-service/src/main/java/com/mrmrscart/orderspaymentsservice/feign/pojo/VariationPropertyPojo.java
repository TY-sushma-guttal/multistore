package com.mrmrscart.orderspaymentsservice.feign.pojo;

import lombok.Data;

@Data
public class VariationPropertyPojo {
	private String optionId;
	private String optionName;
	private String variationId;
	private String variationName;
	private String variationType;
}
