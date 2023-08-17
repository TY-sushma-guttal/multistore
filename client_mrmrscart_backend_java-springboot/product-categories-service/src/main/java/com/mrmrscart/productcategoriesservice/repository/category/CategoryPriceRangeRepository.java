package com.mrmrscart.productcategoriesservice.repository.category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.CategoryPriceRange;

@Repository
public interface CategoryPriceRangeRepository extends MongoRepository<CategoryPriceRange, String> {

}
