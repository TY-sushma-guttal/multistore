package com.mrmrscart.productcategoriesservice.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.product.GroupedProduct;

@Repository
public interface GroupedProductRepository extends MongoRepository<GroupedProduct, String>{
	
   GroupedProduct findByGroupedProductIdAndIsDelete(String groupedProductId,boolean isDelete);
}
