package com.mrmrscart.productcategoriesservice.repository.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.product.SupplierSideNavigation;

@Repository
public interface SupplierSideNavigationRepository extends MongoRepository<SupplierSideNavigation, String> {

}
