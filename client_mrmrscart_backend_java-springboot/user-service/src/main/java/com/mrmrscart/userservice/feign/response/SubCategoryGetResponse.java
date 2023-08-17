package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.feign.pojo.SubCategoryResponsePojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryGetResponse {
	private boolean error;
	private String message;
	private SubCategoryResponsePojo data;
}
