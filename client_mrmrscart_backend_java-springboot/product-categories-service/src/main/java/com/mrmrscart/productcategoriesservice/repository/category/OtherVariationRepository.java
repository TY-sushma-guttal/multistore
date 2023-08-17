package com.mrmrscart.productcategoriesservice.repository.category;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.OtherVariation;

@Repository
public interface OtherVariationRepository  extends MongoRepository<OtherVariation, String>{

	List<OtherVariation> findBySubCategoryId(String subCategoryId);
	OtherVariation findBySubCategoryIdAndOtherVariationId(String subCategoryId, String variationId);
}
