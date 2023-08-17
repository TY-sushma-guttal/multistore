package com.mrmrscart.userservice.feign.response;

import java.util.List;

import com.mrmrscart.userservice.feign.pojo.ProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductAndVariationResponse {
	private boolean error;
	private String message;
	private List<ProductPojo> data;
}
