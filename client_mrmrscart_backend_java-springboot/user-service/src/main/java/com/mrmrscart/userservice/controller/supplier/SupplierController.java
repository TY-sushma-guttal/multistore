package com.mrmrscart.userservice.controller.supplier;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.PROFILE_UPDATE_SUCCESS;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.REFERRED_SUPPLIER_SUCCESS;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.REGISTRATION_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.RESET_PASSWORD_SUCCESS_MESSAGE;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.constant.supplier.SupplierAdressConstant;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.userservice.feign.response.AuthenticateResponse;
import com.mrmrscart.userservice.pojo.supplier.ChangePasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ForgotPasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierRegistrationPojo;
import com.mrmrscart.userservice.pojo.supplier.UserAddressDetailsPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.response.supplier.SupplierRegistartionGetResponse;
import com.mrmrscart.userservice.response.supplier.SupplierResponse;
import com.mrmrscart.userservice.service.supplier.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users/")
@CrossOrigin("*")
@Slf4j
public class SupplierController {

	@Autowired
	private SupplierService supplierService;

	@Operation(summary = "This method is used to add a new pickup address")
	@PostMapping("/supplier-address")
	public ResponseEntity<SupplierResponse> addAddressDetails(@RequestBody UserAddressDetailsPojo addressDetailsPojo) {
		return new ResponseEntity<>(new SupplierResponse(false, SupplierAdressConstant.ADDRESS_SAVE_SUCCESS,
				supplierService.addAddressDetails(addressDetailsPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to update a pickup address")
	@PutMapping("/supplier-address")
	public ResponseEntity<SupplierResponse> updateAddressDetails(
			@RequestBody UserAddressDetailsPojo addressDetailsPojo) {
		return new ResponseEntity<>(new SupplierResponse(false, SupplierAdressConstant.ADDRESS_UPDATED_SUCCESS,
				supplierService.updateAddressDetails(addressDetailsPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the pickup addresses")
	@GetMapping("/supplier-addresses/{supplierId}")
	public ResponseEntity<SupplierResponse> getAllAddresses(@PathVariable String supplierId) {
		List<UserAddressDetails> addresses = supplierService.getAllAddresses(supplierId);
		if (!addresses.isEmpty())
			return new ResponseEntity<>(
					new SupplierResponse(false, SupplierAdressConstant.ADDRESS_GET_SUCCESS_MESSAGE, addresses),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SupplierResponse(true, SupplierAdressConstant.ADDRESS_GET_FAIL_MESSAGE, addresses),
					HttpStatus.NOT_FOUND);

	}

	@Operation(summary = "This method is used to change primary pickup address")
	@GetMapping("/supplier-primary-address/{supplierId}/{addressId}")
	public ResponseEntity<SupplierResponse> changePrimaryAddress(@PathVariable String supplierId,
			@PathVariable Long addressId) {

		return new ResponseEntity<>(new SupplierResponse(false, SupplierAdressConstant.ADDRESS_GET_SUCCESS_MESSAGE,
				supplierService.changePrimaryAddress(supplierId, addressId)), HttpStatus.OK);

	}

	@Operation(summary = "This method is used to delete a pickup address")
	@DeleteMapping("/supplier-address/{supplierId}/{addressId}")
	public ResponseEntity<SupplierResponse> deleteAddress(@PathVariable String supplierId,
			@PathVariable Long addressId) {

		return new ResponseEntity<>(new SupplierResponse(false, SupplierAdressConstant.ADDRESS_DELETE_SUCCESS_MESSAGE,
				supplierService.deleteAddress(supplierId, addressId)), HttpStatus.OK);

	}

	@Operation(summary = "This method is used to get a pickup address by Id")
	@GetMapping("/supplier-address/{supplierId}/{addressId}")
	public ResponseEntity<SupplierResponse> getAddressById(@PathVariable String supplierId,
			@PathVariable Long addressId) {
		UserAddressDetails address = supplierService.getAddressById(supplierId, addressId);
		return new ResponseEntity<>(
				new SupplierResponse(false, SupplierAdressConstant.ADDRESS_GET_SUCCESS_MESSAGE, address),
				HttpStatus.OK);

	}

	/*
	 * @Author : Sudharshan , Hemadri G
	 */
	@Operation(summary = "This method is used to send otp to the user during registration for the mobile number")
	@PostMapping("/registration/send-otp")
	public ResponseEntity<SuccessResponse> sendOtpRegistration(@RequestParam("mobileNumber") String mobileNumber,
			@RequestParam("userType") EUserRole userType) throws SendFailedException {
		return new ResponseEntity<>(
				new SuccessResponse(false, supplierService.sendOtpRegistration(mobileNumber, userType), null),
				HttpStatus.OK);
	}

	@Operation(summary = "This method is used to update Supplier's Profile")
	@PutMapping("/supplier-profile")
	public ResponseEntity<SupplierResponse> updateProfile(
			@RequestBody SupplierRegistrationPojo supplierRegistrationPojo) {
		SupplierRegistration supplierRegistration = supplierService.updateProfile(supplierRegistrationPojo);
		return new ResponseEntity<>(new SupplierResponse(false, PROFILE_UPDATE_SUCCESS, supplierRegistration),
				HttpStatus.OK);

	}

	@Operation(summary = "This method is used to update Supplier's Profile after Admin's rejection")
	@PutMapping("/supplier/profile")
	public ResponseEntity<SupplierResponse> updateSupplierProfile(
			@RequestBody SupplierChangesHistoryPojo supplierChangesHistoryPojo) {
		SupplierRegistration supplierRegistration = supplierService.updateSupplierProfile(supplierChangesHistoryPojo);
		return new ResponseEntity<>(new SupplierResponse(false, PROFILE_UPDATE_SUCCESS, supplierRegistration),
				HttpStatus.OK);

	}

	@Operation(summary = "This method is used to update Supplier's Profile after Admin's approval")
	@PutMapping("/supplier-profile/approval")
	public ResponseEntity<SupplierResponse> approveSupplierProfile(
			@RequestBody SupplierChangesHistoryPojo supplierChangesHistoryPojo) {
		supplierService.approveSupplierProfile(supplierChangesHistoryPojo);
		return new ResponseEntity<>(new SupplierResponse(false, PROFILE_UPDATE_SUCCESS, null), HttpStatus.OK);

	}

	/*
	 * @Author : Sudharshan , Hemadri G
	 * 
	 */
	@Operation(summary = "This method is used to send otp to the user for the forgot password request")
	@PostMapping("/registration/forgot-password/send-otp")
	public ResponseEntity<SuccessResponse> sendOtpForForgotPassword(@RequestParam("userName") String userName,
			@RequestParam("userType") EUserRole userType) throws UnsupportedEncodingException, MessagingException {
		return new ResponseEntity<>(
				new SuccessResponse(false, supplierService.sendOtpForForgotPassword(userName, userType), null),
				HttpStatus.OK);
	}

	@Operation(summary = "This method is used  to verfiy the otp")
	@PostMapping("/registration/verify-otp")
	public ResponseEntity<SuccessResponse> verifyOtp(@RequestParam("userName") String userName,
			@RequestParam("otp") int otp) {
		return new ResponseEntity<>(new SuccessResponse(false, supplierService.verifyOtp(userName, otp), null),
				HttpStatus.OK);
	}

	/*
	 * @Author : Sudharshan , Hemadri G
	 * 
	 */
	@Operation(summary = "This method is used to reset password when user requests for forgot password")
	@PostMapping("/registration/reset-password")
	public ResponseEntity<SuccessResponse> resetPassword(@RequestBody ForgotPasswordPojo forgotPasswordPojo) {
		supplierService.forgotPassword(forgotPasswordPojo);
		return new ResponseEntity<>(new SuccessResponse(false, RESET_PASSWORD_SUCCESS_MESSAGE, null), HttpStatus.OK);
	}

	@Operation(summary = "This method is used for verifying otp and successfully login")
	@PostMapping("/registration/verify-login-otp")
	public ResponseEntity<AuthenticateResponse> verifyloginWithOtp(@RequestParam("userName") String userName,
			@RequestParam("otp") int otp, @RequestParam("userType") EUserRole userType) {
		AuthenticateResponse verifyloginWithOtp = supplierService.verifyloginWithOtp(userName, otp, userType);
		return new ResponseEntity<>(verifyloginWithOtp, HttpStatus.OK);
	}

	@Operation(summary = "This method is used for registration of a supplier")
	@PostMapping("/supplier/register-supplier")
	public ResponseEntity<SuccessResponse> addSupplier(@RequestBody SupplierRegistrationPojo supplierRegistrationPojo)
			throws UnsupportedEncodingException, MessagingException {
		log.debug(DEBUG_MESSAGE);
		return new ResponseEntity<>(new SuccessResponse(false, REGISTRATION_SUCCESS_MESSAGE,
				supplierService.addSupplier(supplierRegistrationPojo)), HttpStatus.OK);
	}

	/*
	 * @Author : Sudharshan , Hemadri G
	 * 
	 */
	@Operation(summary = "This method is used for the user to change the password")
	@PutMapping("/change-password")
	public ResponseEntity<SuccessResponse> changePassword(@RequestBody ChangePasswordPojo changePasswordPojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, supplierService.changePassword(changePasswordPojo), null), HttpStatus.OK);
	}

	@Operation(summary = "This method will fetch the supplier information based on supplierId and status. ")
	@GetMapping("/supplier-registration")
	public ResponseEntity<SupplierRegistartionGetResponse> getSupplierById(@RequestParam String id,
			@RequestParam ESupplierStatus status) {
		return new ResponseEntity<>(new SupplierRegistartionGetResponse(false, "Supplier data fetched successfully. ",
				supplierService.getSupplierById(id, status)), HttpStatus.OK);
	}

	/**
	 * 
	 * @author Veenal
	 * 
	 * 
	 */
	@Operation(summary = "This method will fetch the supplier information based on supplierId ")
	@GetMapping("/supplier-registration/{id}")
	public ResponseEntity<SupplierRegistartionGetResponse> getSupplierById(@PathVariable String id) {
		return new ResponseEntity<>(new SupplierRegistartionGetResponse(false, "Supplier data fetched successfully. ",
				supplierService.getSupplierById(id)), HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API will fetch the referred supplier details")
	@GetMapping("/referred-supplier")
	public ResponseEntity<SuccessResponse> getReferredSupplier(@RequestParam String supplierId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, REFERRED_SUPPLIER_SUCCESS, supplierService.getReferredSupplier(supplierId)),
				HttpStatus.OK);
	}

	@GetMapping("/supplier/supplier-info")
	public ResponseEntity<SuccessResponse> findSupplierAndStoreInfo(@RequestParam("supplierId") String supplierId,
			@RequestParam("status") ESupplierStatus status) {
		return new ResponseEntity<>(new SuccessResponse(false, "Supplier data fetched successfully. ",
				supplierService.findSupplierAndStoreInfo(supplierId, status)), HttpStatus.OK);

	}
}
