package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.util.List;

import lombok.Data;

@Data
public class LinkedProductPojo {
	private List<String> upSells;
	private List<String> crossSells;
}
