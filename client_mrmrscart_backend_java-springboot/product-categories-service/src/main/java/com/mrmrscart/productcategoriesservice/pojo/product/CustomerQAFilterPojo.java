package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQAFilterPojo {
	
	private boolean status;
	
	private String keyword;
	
	private LocalDateTime dateFrom;
	
	private LocalDateTime dateTo;

}
