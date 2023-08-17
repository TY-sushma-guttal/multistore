package com.mrmrscart.productcategoriesservice.repository.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import com.mrmrscart.productcategoriesservice.entity.product.EUserType;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;

public interface ProductFlagRepository extends MongoRepository<ProductFlag, String> {

	List<ProductFlag> findByisDeleted(boolean b);

	@Query("{'createdAt':{$gt:?0,$lt:?1}}")
    public List<ProductFlag> findAllFlags(@DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")LocalDateTime dateFrom,@DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")LocalDateTime dateTo);

	public List<ProductFlag> findByUserTypeAndIsDeletedAndIsEnabled(EUserType userType, boolean b, boolean c);

	public ProductFlag findByFlagIdAndIsDeletedAndIsEnabledAndUserType(String id, boolean b, boolean c, EUserType supplier);

	ProductFlag findByFlagIdAndIsDeleted(String flagId, boolean b);

}
