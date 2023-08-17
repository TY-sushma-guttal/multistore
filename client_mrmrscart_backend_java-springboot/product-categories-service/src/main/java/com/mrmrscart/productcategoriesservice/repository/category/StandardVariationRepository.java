package com.mrmrscart.productcategoriesservice.repository.category;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;

public interface StandardVariationRepository extends MongoRepository<StandardVariation, String>{

	StandardVariation findByVariationName(String variationName);
}
