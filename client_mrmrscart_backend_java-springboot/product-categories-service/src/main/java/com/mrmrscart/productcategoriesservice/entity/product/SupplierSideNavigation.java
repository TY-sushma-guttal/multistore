package com.mrmrscart.productcategoriesservice.entity.product;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mrmrscart.productcategoriesservice.audit.Audit;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document(collection = "mmc_supplier_side_navigation")
@Data
@EqualsAndHashCode(callSuper = false)
public class SupplierSideNavigation extends Audit {

	@Id
	private String id;
	private String title;
	private String logo;
	private String pathName;
	private boolean navigate;
	private boolean disabled;
	private List<Object> child;

}
