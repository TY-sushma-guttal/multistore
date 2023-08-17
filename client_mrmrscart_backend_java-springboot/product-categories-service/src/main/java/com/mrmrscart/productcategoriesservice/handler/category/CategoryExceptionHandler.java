package com.mrmrscart.productcategoriesservice.handler.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mrmrscart.productcategoriesservice.exception.category.CategorySetEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetException;
import com.mrmrscart.productcategoriesservice.exception.category.CategorySetNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.EnableSetException;
import com.mrmrscart.productcategoriesservice.exception.category.EnableSubCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.EqualPriceAndMaxPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.EqualPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.FailedToUploadException;
import com.mrmrscart.productcategoriesservice.exception.category.FixedOrZeroCommissionException;
import com.mrmrscart.productcategoriesservice.exception.category.InvalidVariationTypeException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategoryOrSetDetailsNotPresentException;
import com.mrmrscart.productcategoriesservice.exception.category.MainCategorySaveException;
import com.mrmrscart.productcategoriesservice.exception.category.MinPriceAndEqualPriceException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.OtherVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.PriceRangeIdNotFound;
import com.mrmrscart.productcategoriesservice.exception.category.PriceRangeListNotFound;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryEmptyListException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryException;
import com.mrmrscart.productcategoriesservice.exception.category.SubCategoryIdNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.SupplierVariationOptionNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.category.VariationNameExistsException;
import com.mrmrscart.productcategoriesservice.response.category.CategorySetResponse;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;
import com.mrmrscart.productcategoriesservice.response.category.MainCategoryResponse;
import com.mrmrscart.productcategoriesservice.response.category.SubCategoryExceptionResponse;

@RestControllerAdvice
public class CategoryExceptionHandler {
	@ExceptionHandler(value = CategorySetNotFoundException.class)
	public ResponseEntity<CategorySetResponse> categorySetNotFoundExceptionHandler(
			CategorySetNotFoundException categorySetNotFoundException) {
		return new ResponseEntity<>(new CategorySetResponse(true, categorySetNotFoundException.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SubCategoryIdNotFoundException.class)
	public ResponseEntity<SubCategoryExceptionResponse> subCategoryIdNotFoundExceptionHandler(
			SubCategoryIdNotFoundException exception) {
		return new ResponseEntity<>(new SubCategoryExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SubCategoryException.class)
	public ResponseEntity<SubCategoryExceptionResponse> subCategoryExceptionHandler(SubCategoryException exception) {
		return new ResponseEntity<>(new SubCategoryExceptionResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MainCategoryNotFoundException.class)
	public ResponseEntity<MainCategoryResponse> categorySetNotFoundExceptionHandler(
			MainCategoryNotFoundException exception) {
		return new ResponseEntity<>(new MainCategoryResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MainCategorySaveException.class)
	public ResponseEntity<MainCategoryResponse> saveMainCategoryException(MainCategorySaveException exception) {
		return new ResponseEntity<>(new MainCategoryResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EnableSetException.class)
	public ResponseEntity<MainCategoryResponse> enableSetException(EnableSetException exception) {
		return new ResponseEntity<>(new MainCategoryResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EnableSubCategoryException.class)
	public ResponseEntity<MainCategoryResponse> enableSubcategoryException(EnableSubCategoryException exception) {
		return new ResponseEntity<>(new MainCategoryResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StandardVariationException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(StandardVariationException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StandardVariationIdNotFoundException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(StandardVariationIdNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FixedOrZeroCommissionException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(FixedOrZeroCommissionException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PriceRangeListNotFound.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(PriceRangeListNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EqualPriceAndMaxPriceException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(EqualPriceAndMaxPriceException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EqualPriceException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(EqualPriceException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MinPriceAndEqualPriceException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(MinPriceAndEqualPriceException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PriceRangeIdNotFound.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(PriceRangeIdNotFound exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(HttpMessageNotReadableException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, "Provide a valid status", null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = InvalidVariationTypeException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(InvalidVariationTypeException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OtherVariationNotFoundException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(OtherVariationNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = OtherVariationOptionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(OtherVariationOptionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StandardVariationOptionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(
			StandardVariationOptionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SupplierVariationOptionNotFoundException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(
			SupplierVariationOptionNotFoundException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = VariationNameExistsException.class)
	public ResponseEntity<ExceptionResponse> categoryExceptionHandler(VariationNameExistsException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = FailedToUploadException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(FailedToUploadException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = SubCategoryEmptyListException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(SubCategoryEmptyListException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MainCategoryEmptyListException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(MainCategoryEmptyListException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MainCategoryException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(MainCategoryException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = CategorySetEmptyListException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(CategorySetEmptyListException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = CategorySetException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(CategorySetException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = MainCategoryOrSetDetailsNotPresentException.class)
	public ResponseEntity<ExceptionResponse> failedToUploadExceptionHandler(MainCategoryOrSetDetailsNotPresentException exception) {
		return new ResponseEntity<>(new ExceptionResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}
}
