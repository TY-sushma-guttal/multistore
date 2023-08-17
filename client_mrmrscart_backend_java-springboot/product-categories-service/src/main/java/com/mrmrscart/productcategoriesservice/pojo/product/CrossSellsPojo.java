package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrossSellsPojo {

	private List<String> subCategoryId;
	private String supplierId;
}
