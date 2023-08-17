package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.feign.pojo.MainCategoryPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryResponse {

	private boolean error;
	private String message;
	private MainCategoryPojo data;
}
