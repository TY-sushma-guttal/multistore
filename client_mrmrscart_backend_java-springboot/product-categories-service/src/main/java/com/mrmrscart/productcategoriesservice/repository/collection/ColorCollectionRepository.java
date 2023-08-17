package com.mrmrscart.productcategoriesservice.repository.collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.collection.ColorCollection;

@Repository
public interface ColorCollectionRepository extends MongoRepository<ColorCollection, String>{

	ColorCollection findByColorNameIgnoreCase(String colorName);
}
