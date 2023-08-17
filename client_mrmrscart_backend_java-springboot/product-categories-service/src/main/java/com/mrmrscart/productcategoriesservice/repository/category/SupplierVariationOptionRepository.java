package com.mrmrscart.productcategoriesservice.repository.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.category.SupplierVariationOption;

public interface SupplierVariationOptionRepository extends MongoRepository<SupplierVariationOption, String> {

	Page<SupplierVariationOption> findByApprovalStatus(EStatus status, PageRequest of);
	
	@Query("{'createdAt':{$gt:?0,$lt:?1}}")
	List<SupplierVariationOption> getByDate(@DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")LocalDateTime dateFrom,@DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")LocalDateTime dateTo);

} 
