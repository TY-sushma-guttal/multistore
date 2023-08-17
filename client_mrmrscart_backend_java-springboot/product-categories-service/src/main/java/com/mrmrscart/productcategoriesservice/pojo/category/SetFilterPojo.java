package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetFilterPojo {

	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private String keyword;
}
