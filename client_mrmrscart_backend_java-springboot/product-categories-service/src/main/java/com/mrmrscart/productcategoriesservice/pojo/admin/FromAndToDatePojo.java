package com.mrmrscart.productcategoriesservice.pojo.admin;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FromAndToDatePojo {

	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	
}
