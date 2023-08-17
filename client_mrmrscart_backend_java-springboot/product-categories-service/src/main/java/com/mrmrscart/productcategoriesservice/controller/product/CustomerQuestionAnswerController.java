package com.mrmrscart.productcategoriesservice.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Veenal
 *
 */

import com.mrmrscart.productcategoriesservice.entity.product.CustomerQuestionAnswer;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQAFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerViewPojo;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.CustomerQuestionAnswerService;

import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_FETCH_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_POST_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_DELETE_SUCCESS;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_DELETE_FAILURE;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class CustomerQuestionAnswerController {

	@Autowired
	private CustomerQuestionAnswerService customerQuestionAnswerService;

	@Operation(summary = "This method is used to post/edit a question by the customer")
	@PostMapping("/customer-question-answer")
	public ResponseEntity<SuccessResponse> postQuestion(
			@RequestBody CustomerQuestionAnswerPojo customerQuestionAnswerPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, QUESTION_POST_SUCCESS,
				customerQuestionAnswerService.postQuestion(customerQuestionAnswerPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to answer a question by admin,supplier,reseller or any customer")
	@PutMapping("/customer-question-answer")
	public ResponseEntity<SuccessResponse> answerQuestion(
			@RequestBody CustomerQuestionAnswerPojo customerQuestionAnswerPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, QUESTION_ANSWER_SUCCESS,
				customerQuestionAnswerService.answerQuestion(customerQuestionAnswerPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all questions asked for the product")
	@GetMapping("/customer-question-answer/{variationId}")
	public ResponseEntity<SuccessResponse> getAllQuestionsAnswersByVariation(@PathVariable String variationId) {
		List<CustomerQuestionAnswer> allQuestionsAnswersByVariation = customerQuestionAnswerService
				.getAllQuestionsAnswersByVariation(variationId);
		if (!allQuestionsAnswersByVariation.isEmpty())
			return new ResponseEntity<>(
					new SuccessResponse(false, QUESTION_ANSWER_FETCH_SUCCESS, allQuestionsAnswersByVariation),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SuccessResponse(true, QUESTION_ANSWER_FAILURE, allQuestionsAnswersByVariation),
					HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "This method is used to get all questions for the admin")
	@PostMapping("/customer-question-answer/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllByStatus(@PathVariable int pageNumber, @PathVariable int pageSize,
			@RequestBody CustomerQAFilterPojo customerQAFilterPojo) {
		CustomerQuestionAnswerViewPojo allQuestionsAnswers = customerQuestionAnswerService.getAllByStatus(pageNumber,
				pageSize, customerQAFilterPojo);
		if (!allQuestionsAnswers.getQuestionAnswerViewPojo().isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, QUESTION_ANSWER_FETCH_SUCCESS, allQuestionsAnswers),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, QUESTION_ANSWER_FAILURE, allQuestionsAnswers),
					HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "This method is used to get all questions asked for all the products of a supplier")
	@PostMapping("/customer-question-answer/{pageNumber}/{pageSize}/{supplierId}")
	public ResponseEntity<SuccessResponse> getAllByStatusSupplier(@PathVariable int pageNumber,
			@PathVariable int pageSize, @PathVariable String supplierId,
			@RequestBody CustomerQAFilterPojo customerQAFilterPojo) {
		CustomerQuestionAnswerViewPojo allQuestionsAnswers = customerQuestionAnswerService
				.getAllByStatusSupplier(pageNumber, pageSize, supplierId, customerQAFilterPojo);
		if (!allQuestionsAnswers.getQuestionAnswerViewPojo().isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, QUESTION_ANSWER_FETCH_SUCCESS, allQuestionsAnswers),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, QUESTION_ANSWER_FAILURE, allQuestionsAnswers),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to delete a question by the admin")
	@DeleteMapping("/customer-question-answer/{customerQuestionId}")
	public ResponseEntity<SuccessResponse> deleteQuestionAnswer(@PathVariable String customerQuestionId) {
		boolean deleteQuestionAnswer = customerQuestionAnswerService.deleteQuestionAnswer(customerQuestionId);
		if (deleteQuestionAnswer)
			return new ResponseEntity<>(
					new SuccessResponse(false, QUESTION_ANSWER_DELETE_SUCCESS, deleteQuestionAnswer), HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, QUESTION_ANSWER_DELETE_FAILURE, deleteQuestionAnswer),
					HttpStatus.NOT_FOUND);

	}
}
