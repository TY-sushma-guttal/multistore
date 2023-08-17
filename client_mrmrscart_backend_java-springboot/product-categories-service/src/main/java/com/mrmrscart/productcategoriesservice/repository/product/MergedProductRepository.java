package com.mrmrscart.productcategoriesservice.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.product.MergedProduct;

public interface MergedProductRepository extends MongoRepository<MergedProduct, String>{

}
