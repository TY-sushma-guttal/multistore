package com.mrmrscart.productcategoriesservice.repository.category;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;

@Repository
public interface StandardOptionRepository extends MongoRepository<StandardOption, String> {
	StandardOption findByStandardOptionId(String variationId);

	public StandardOption findByStandardOptionIdAndStandardVariationId(String standardOptionId,
			String standardVariationId);
}
