package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.CustomerQuestionAnswer;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQAFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerViewPojo;

public interface CustomerQuestionAnswerService {
	
	public CustomerQuestionAnswer postQuestion(CustomerQuestionAnswerPojo customerQuestionAnswerPojo);
	
	public CustomerQuestionAnswer answerQuestion(CustomerQuestionAnswerPojo customerQuestionAnswerPojo);
	
	public List<CustomerQuestionAnswer> getAllQuestionsAnswersByVariation(String variationId);
	
	public CustomerQuestionAnswerViewPojo getAllByStatus(int pageNumber, int pageSize,CustomerQAFilterPojo customerQAFilterPojo);
	
	public CustomerQuestionAnswerViewPojo getAllByStatusSupplier(int pageNumber, int pageSize, String supplierId,CustomerQAFilterPojo customerQAFilterPojo);
	
	public boolean deleteQuestionAnswer(String customerQuestionId);

}
