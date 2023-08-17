package com.mrmrscart.userservice.controller.supplier;

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

import com.mrmrscart.userservice.constant.supplier.UserBankDetailsConstant;
import com.mrmrscart.userservice.entity.supplier.UserBankDetails;
import com.mrmrscart.userservice.pojo.supplier.UserBankDetailsPojo;
import com.mrmrscart.userservice.response.supplier.SupplierResponse;
import com.mrmrscart.userservice.service.supplier.UserBankDetailsService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/users/")
@CrossOrigin(origins = "*")
public class UserBankDetailsController {

	@Autowired
	private UserBankDetailsService userBankDetailsService;

	@Operation(summary = "This method is used to add new bank details ")
	@PostMapping("/supplier-bank-details")
	public ResponseEntity<SupplierResponse> addBankDetails(@RequestBody UserBankDetailsPojo bankDetailsPojo) {
		return new ResponseEntity<>(new SupplierResponse(false, UserBankDetailsConstant.BANK_SAVE_SUCCESS,
				userBankDetailsService.addBankDetails(bankDetailsPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to update Bank details")
	@PutMapping("/supplier-bank-details")
	public ResponseEntity<SupplierResponse> updateBankDetails(@RequestBody UserBankDetailsPojo bankDetailsPojo) {
		return new ResponseEntity<>(new SupplierResponse(false, UserBankDetailsConstant.BANK_UPDATED_SUCCESS,
				userBankDetailsService.updateBankDetails(bankDetailsPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the Bank details")
	@GetMapping("/supplier-bank-details/{supplierId}")
	public ResponseEntity<SupplierResponse> getAllBankes(@PathVariable String supplierId) {
		List<UserBankDetails> banks = userBankDetailsService.getAllBanks(supplierId);
		if (!banks.isEmpty())
			return new ResponseEntity<>(
					new SupplierResponse(false, UserBankDetailsConstant.BANK_GET_SUCCESS_MESSAGE, banks),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SupplierResponse(true, UserBankDetailsConstant.BANK_GET_FAIL_MESSAGE, banks),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to change primary Bank details")
	@GetMapping("/supplier-primary-bank/{supplierId}/{bankId}")
	public ResponseEntity<SupplierResponse> changePrimaryBank(@PathVariable String supplierId,
			@PathVariable Long bankId) {

		return new ResponseEntity<>(new SupplierResponse(false, UserBankDetailsConstant.BANK_GET_SUCCESS_MESSAGE,
				userBankDetailsService.changePrimaryBank(supplierId, bankId)), HttpStatus.OK);

	}

	@Operation(summary = "This method is used to delete bank details")
	@DeleteMapping("/supplier-Bank/{supplierId}/{bankId}")
	public ResponseEntity<SupplierResponse> deleteBank(@PathVariable String supplierId, @PathVariable Long bankId) {

		return new ResponseEntity<>(new SupplierResponse(false, UserBankDetailsConstant.BANK_DELETE_SUCCESS_MESSAGE,
				userBankDetailsService.deleteBank(supplierId, bankId)), HttpStatus.OK);

	}

	@Operation(summary = "This method is used to get Bank details by Id")
	@GetMapping("/supplier-Bank/{supplierId}/{bankId}")
	public ResponseEntity<SupplierResponse> getBankById(@PathVariable String supplierId, @PathVariable Long bankId) {
		UserBankDetails bank = userBankDetailsService.getBankById(supplierId, bankId);
		if (bank != null)
			return new ResponseEntity<>(
					new SupplierResponse(false, UserBankDetailsConstant.BANK_GET_SUCCESS_MESSAGE, bank), HttpStatus.OK);
		else
			return new ResponseEntity<>(new SupplierResponse(true, UserBankDetailsConstant.BANK_GET_FAIL_MESSAGE, bank),
					HttpStatus.NOT_FOUND);

	}

}
