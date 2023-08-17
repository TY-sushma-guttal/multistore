package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductsPojo {
	private List<String> productId;
}
