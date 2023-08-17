package com.mrmrscart.productcategoriesservice.controller.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardOptionPojo;
import com.mrmrscart.productcategoriesservice.response.category.StandardOptionResponse;
import com.mrmrscart.productcategoriesservice.service.category.StandardOptionService;

import io.swagger.v3.oas.annotations.Operation;

import static com.mrmrscart.productcategoriesservice.common.category.StandardOptionConstant.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class StandardOptionController {

	@Autowired
	private StandardOptionService standardOptionService;

	@Operation(summary = "This API is used to add multiple standard options for a variation")
	@PostMapping("/standard-option")
	public ResponseEntity<StandardOptionResponse> addStandardOption(
			@RequestBody StandardOptionPojo standardOptionPojo) {
		List<StandardOption> addStandardOption = standardOptionService.addStandardOption(standardOptionPojo);
		if (addStandardOption != null) {
			return new ResponseEntity<>(new StandardOptionResponse(false, STANDARD_OPTION_SUCCESS, addStandardOption),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new StandardOptionResponse(false, STANDARD_OPTION_FAILURE, addStandardOption),
					HttpStatus.BAD_REQUEST);
		}
	}
}
