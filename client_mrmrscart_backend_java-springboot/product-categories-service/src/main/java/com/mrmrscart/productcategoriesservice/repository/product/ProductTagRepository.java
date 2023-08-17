package com.mrmrscart.productcategoriesservice.repository.product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.product.ProductTag;

public interface ProductTagRepository extends MongoRepository<ProductTag, String> {

	List<ProductTag> findByisDeleted(boolean b);

	List<ProductTag> findByisDeletedAndIsApproved(boolean b, boolean c);

}
