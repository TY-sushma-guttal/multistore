package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierSideNavigationPojo {
	private String id;
	private String title;
	private String logo;
	private String pathName;
	private boolean navigate;
	private List<Object> child;
}
