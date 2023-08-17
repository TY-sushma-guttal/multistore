package com.mrmrscart.productcategoriesservice.handler.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.productcategoriesservice.exception.product.CustomerQuestionAnswerException;
import com.mrmrscart.productcategoriesservice.exception.product.DuplicateProductException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyMasterProductListException;
import com.mrmrscart.productcategoriesservice.exception.product.EmptyProductVariationListException;
import com.mrmrscart.productcategoriesservice.exception.product.GroupedProductException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidCommissionMode;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidCouponStatusException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidKeywordException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidSkuException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidTrademarkLetterException;
import com.mrmrscart.productcategoriesservice.exception.product.InvalidWarrantyException;
import com.mrmrscart.productcategoriesservice.exception.product.MarketingToolSubscriptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.MergeProductException;
import com.mrmrscart.productcategoriesservice.exception.product.MergeProductIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductFlagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductTagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.QuestionAnswerNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.QuestionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.SalePriceWithOrWithoutFDRException;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierFlagNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.ToolSubscriptionExpiredException;
import com.mrmrscart.productcategoriesservice.exception.product.TrademarkInvoiceNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.UserIdNotFoundException;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;
import com.mrmrscart.productcategoriesservice.response.product.ProductFlagResponse;
import com.mrmrscart.productcategoriesservice.response.product.ProductTagResponse;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;

@RestControllerAdvice
public class ProductExceptionHandler {

	@ExceptionHandler(value = ProductFlagNotFoundException.class)
	public ResponseEntity<ProductFlagResponse> productFlagNotFoundException(
			ProductFlagNotFoundException productFlagNotFoundException) {
		return new ResponseEntity<>(new ProductFlagResponse(true, productFlagNotFoundException.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MasterProductNotFound.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(MasterProductNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MasterProductException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(MasterProductException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmptyMasterProductListException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(EmptyMasterProductListException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidSkuException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(InvalidSkuException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidCouponStatusException.class)
	public ResponseEntity<SuccessResponse> handleException(InvalidCouponStatusException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = GroupedProductException.class)
	public ResponseEntity<ExceptionResponse> groupedProductExceptionHandler(GroupedProductException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateProductException.class)
	public ResponseEntity<ExceptionResponse> duplicateProductExceptionHandler(DuplicateProductException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmptyProductVariationListException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(EmptyProductVariationListException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ProductTagNotFoundException.class)
	public ResponseEntity<ProductTagResponse> productTagNotFoundException(
			ProductTagNotFoundException productTagNotFoundException) {
		return new ResponseEntity<>(new ProductTagResponse(true, productTagNotFoundException.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ProductVariationNotFound.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(ProductVariationNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidKeywordException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(InvalidKeywordException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MergeProductIdNotFoundException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(MergeProductIdNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MergeProductException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(MergeProductException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SalePriceWithOrWithoutFDRException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(SalePriceWithOrWithoutFDRException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = TrademarkInvoiceNotFoundException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(TrademarkInvoiceNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = SupplierNotFoundException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(SupplierNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidCommissionMode.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(InvalidCommissionMode exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = UserIdNotFoundException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(UserIdNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidWarrantyException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(InvalidWarrantyException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidTrademarkLetterException.class)
	public ResponseEntity<ExceptionResponse> productExceptionHandler(InvalidTrademarkLetterException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = QuestionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> questionNotFoundException(QuestionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = QuestionAnswerNotFound.class)
	public ResponseEntity<ExceptionResponse> questionAnswerNotFound(QuestionAnswerNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CustomerQuestionAnswerException.class)
	public ResponseEntity<ExceptionResponse> customerQuestionAnswerException(
			CustomerQuestionAnswerException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = SupplierFlagNotFoundException.class)
	public ResponseEntity<ExceptionResponse> supplierFlagNotFoundException(
			SupplierFlagNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = ToolSubscriptionExpiredException.class)
	public ResponseEntity<ExceptionResponse> toolSubscriptionExpiredException(
			ToolSubscriptionExpiredException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MarketingToolSubscriptionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> marketingToolSubscriptionNotFoundException(
			MarketingToolSubscriptionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
