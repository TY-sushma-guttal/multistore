package com.mrmrscart.productcategoriesservice.pojo.admin;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProductCountPojo {

	private String id;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
}
