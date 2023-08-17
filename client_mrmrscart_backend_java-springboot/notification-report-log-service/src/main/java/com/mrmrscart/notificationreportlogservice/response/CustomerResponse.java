package com.mrmrscart.notificationreportlogservice.response;

import java.util.List;

import com.mrmrscart.notificationreportlogservice.feign.pojo.DropDownPojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerResponse {
	
	private boolean error;
	private String message;
	private List<DropDownPojo> data;

}
