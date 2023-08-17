package com.mrmrscart.productcategoriesservice.service.category;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardOptionPojo;

public interface StandardOptionService {

	public List<StandardOption> addStandardOption(StandardOptionPojo standardOptionPojo);
}
