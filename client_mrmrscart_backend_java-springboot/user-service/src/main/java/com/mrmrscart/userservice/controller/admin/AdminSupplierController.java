package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminSupplierConstant.SUPPLIER_COUNT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVITATION_SENT;

import java.util.List;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.pojo.admin.FromAndToDatePojo;
import com.mrmrscart.userservice.pojo.admin.SupplierApprovalPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminSupplierService;
import com.mrmrscart.userservice.wrapper.admin.AdminSupplierCountWrapper;

import io.swagger.v3.oas.annotations.Operation;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/")
public class AdminSupplierController {

	@Autowired
	private AdminSupplierService adminSupplierService;

	@PutMapping("/admin/supplier-approval")
	public ResponseEntity<SuccessResponse> approveSupplier(@RequestBody SupplierApprovalPojo data)
			throws SendFailedException {
		SupplierRegistration approval = adminSupplierService.supplierApproval(data);
		if (data.getStatus() == ESupplierStatus.APPROVED) {
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier Approved! ", approval), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier Rejected! ", approval), HttpStatus.OK);
	}

	@GetMapping("/admin/supplier/supplier-status/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSupplier(@RequestParam("status") ESupplierStatus status,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		AdminSupplierCountWrapper wrapper = adminSupplierService.getAllSupplier(status, pageNumber, pageSize);
		if (wrapper != null) {
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier details found successfully", wrapper),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "There Are No Suppliers", wrapper), HttpStatus.OK);
		}
	}

	@Operation(summary = "This api not done yet")
	@PostMapping("/admin/supplier-status/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSupplier(
			@And({ @Spec(path = "businessName", params = "businessName", spec = Like.class),
					@Spec(path = "emailId", params = "emailId", spec = Like.class),
					@Spec(path = "mobileNumber", params = "mobileNumber", spec = Like.class),
					@Spec(path = "gstin", params = "gstin", spec = Like.class),
					@Spec(path = "mainCategories", params = "mainCategories", spec = Like.class),
					@Spec(path = "city", params = "city", spec = Like.class),
					@Spec(path = "createDate", params = "createDate", spec = Equal.class),
					@Spec(path = "status", params = "status", spec = Equal.class),
					@Spec(path = "createDate", params = { "createDateGt",
							"createDateLt" }, spec = Between.class) }) Specification<SupplierRegistration> spec,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		List<SupplierRegistration> list = adminSupplierService.getAllSupplierInfo(spec, pageNumber, pageSize);

		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier details found successfully", list),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, "Failed to fetch the supplier details", list),
					HttpStatus.OK);
		}
	}

	@Operation(description = "This API is used to fetch the supplier count (Admin Suppliers Supplier-dashboard)")
	@PostMapping("/admin/supplier-count")
	public ResponseEntity<SuccessResponse> getSupplierCount(@RequestBody FromAndToDatePojo fromAndToDatePojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, SUPPLIER_COUNT, adminSupplierService.getSupplierCount(fromAndToDatePojo)),
				HttpStatus.OK);
	}

	@GetMapping("/admin/supplier")
	public ResponseEntity<SuccessResponse> getActiveSupplierByFilter(@RequestParam("status") EAdminStatus status,
			@RequestParam("category") String category, @RequestParam("formDate") String fromDate,
			@RequestParam("toDate") String toDate, @RequestParam("keyWord") String keyWord) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Data fetched successfully. ",
						adminSupplierService.getSupplierInfoBasedOnFilter(status, category, fromDate, toDate, keyWord)),
				HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param userName
	 * @return
	 * @throws SendFailedException
	 */
	@Operation(summary = "This API is used to invite the supplier (Admin Suppliers Supplier-approval)")
	@PostMapping("/admin/invite-supplier")
	public ResponseEntity<SuccessResponse> inviteSupplier(@RequestParam("userName") String userName)
			throws SendFailedException {
		adminSupplierService.inviteSupplier(userName);
		return new ResponseEntity<>(new SuccessResponse(false, INVITATION_SENT, null), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for enabling or disabling a supplier by ADMIN")
	@PutMapping("/admin/supplier-status")
	public ResponseEntity<SuccessResponse> enableDisableSupplier(@RequestParam("supplierId") String supplierId,
			@RequestParam("isDisabled") boolean isDisabled) {
		if (isDisabled) {
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier Disabled Successfully",
					adminSupplierService.enableDisableSupplier(supplierId, isDisabled)), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, "Supplier Enabled Successfully",
					adminSupplierService.enableDisableSupplier(supplierId, isDisabled)), HttpStatus.OK);
		}
	}
}
