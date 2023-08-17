package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.feign.pojo.MasterProductPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterProductGetResponse {
	private boolean error;
	private String message;
	private MasterProductPojo data;
}
