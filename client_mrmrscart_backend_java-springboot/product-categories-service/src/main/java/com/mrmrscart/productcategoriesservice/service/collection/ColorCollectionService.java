package com.mrmrscart.productcategoriesservice.service.collection;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.collection.ColorCollection;
import com.mrmrscart.productcategoriesservice.pojo.collection.ColorCollectionPojo;

public interface ColorCollectionService {

	public ColorCollection addColorCollection(ColorCollectionPojo colorCollectionPojo);
	
	public ColorCollection updateColorCollection(ColorCollectionPojo colorCollectionPojo);
	
	public List<ColorCollection> getAllColorCollection();
	
	public String deleteColorCollection(String id);
}
