package com.mrmrscart.productcategoriesservice.controller.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.SupplierVariationOption;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SupplierVariationOptionPojo;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.category.SupplierVariationOptionService;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*")
public class SupplierVariationOptionController {

	@Autowired
	private SupplierVariationOptionService service;

	@PostMapping("/variation/option/variation-approval")
	public ResponseEntity<SuccessResponse> addSupplierVariationOption(
			@RequestBody SupplierVariationOptionPojo variationOptionPojo) {
		SupplierVariationOption addSupplierVariationOption = service.addSupplierVariationOption(variationOptionPojo);
		return new ResponseEntity<>(
				new SuccessResponse(false, "Variation added successfully", addSupplierVariationOption),
				HttpStatus.OK);
	}

	@GetMapping("/variation/option/variation-approval/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSupplierVariationOption(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize) {

		List<SupplierVariationOption> addSupplierVariationOption = service.getAllInitiatedVariationOption(pageNumber,
				pageSize);
		if (!addSupplierVariationOption.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, "Variations found successfully", addSupplierVariationOption),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "There are no variations for the approval",
					addSupplierVariationOption), HttpStatus.OK);
		}

	}
	
	@PostMapping("/variation/option/variation-approval/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSupplierVariationOption(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize, @RequestBody SupplierVariationOptionFilterPojo filterPojo) {

		List<SupplierVariationOption> addSupplierVariationOption = service.getAllInitiatedVariationOption(pageNumber,
				pageSize,filterPojo);
		if (!addSupplierVariationOption.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, "Variations found successfully", addSupplierVariationOption),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "There are no variations for the approval",
					addSupplierVariationOption), HttpStatus.OK);
		}

	}

	@PutMapping("/variation/option/variation-approval")
	public ResponseEntity<SuccessResponse> approveVariationOption(
			@RequestBody SupplierVariationOptionPojo variationOptionPojo) {
		SupplierVariationOption approveVariationOption = service.approveRejectVariation(variationOptionPojo);
		return new ResponseEntity<>(new SuccessResponse(false,
				"Variation " + approveVariationOption.getApprovalStatus() + " successfully", approveVariationOption),
				HttpStatus.OK);

	}

}
