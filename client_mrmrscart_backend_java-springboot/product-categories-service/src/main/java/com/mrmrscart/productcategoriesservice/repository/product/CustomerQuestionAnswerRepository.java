package com.mrmrscart.productcategoriesservice.repository.product;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mrmrscart.productcategoriesservice.entity.product.CustomerQuestionAnswer;

public interface CustomerQuestionAnswerRepository extends MongoRepository<CustomerQuestionAnswer, String> {

	List<CustomerQuestionAnswer> findByVariationId(String variationId);

	List<CustomerQuestionAnswer> findByIsDeletedAndVariationIdIn(boolean b,List<String> variationIds);

	List<CustomerQuestionAnswer> findByIsDeletedAndVariationIdInAndCreatedByContaining(boolean b,List<String> variationIds, String keyword);

	List<CustomerQuestionAnswer> findByIsDeletedAndCreatedByContaining(boolean b,String keyword);

	List<CustomerQuestionAnswer> findByIsDeleted(boolean b);
	
}
