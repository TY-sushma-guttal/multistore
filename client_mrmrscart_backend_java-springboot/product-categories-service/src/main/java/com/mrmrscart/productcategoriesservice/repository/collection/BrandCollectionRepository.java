package com.mrmrscart.productcategoriesservice.repository.collection;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.collection.BrandCollection;

@Repository
public interface BrandCollectionRepository extends MongoRepository<BrandCollection, String> {
	
	@Query("{'brandName' : :#{#brandName}, 'isDisable' : :#{#isDisable}}")
	public BrandCollection findByBrandNameAndisDisable(String brandName, boolean isDisable);

	public List<BrandCollection> findByisDisable(boolean b);

}
