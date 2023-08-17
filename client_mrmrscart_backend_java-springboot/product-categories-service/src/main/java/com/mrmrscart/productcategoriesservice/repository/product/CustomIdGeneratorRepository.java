package com.mrmrscart.productcategoriesservice.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.product.CustomIdGenerator;

public interface CustomIdGeneratorRepository extends MongoRepository<CustomIdGenerator, String> {

	CustomIdGenerator findByIdType(String coupon);

}
