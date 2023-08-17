package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierVariationOptionFilterPojo {

	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;

}
