package com.mrmrscart.productcategoriesservice.repository.category;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.OtherVariationOption;


@Repository
public interface OtherVariationOptionRepository extends MongoRepository<OtherVariationOption, String>{

	List<OtherVariationOption> findByOtherVariationId(String otherVariationId);
	
	OtherVariationOption findByOtherVariationOptionIdAndOtherVariationId(String otherVariationOptionId, String otherVariationId);
}
