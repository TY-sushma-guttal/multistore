package com.mrmrscart.productcategoriesservice.controller.product;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.*;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;
import com.mrmrscart.productcategoriesservice.pojo.product.TrademarkInvoiceInfoPojo;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.TrademarkInvoiceService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class TrademarkInvoiceController {
	@Autowired
	private TrademarkInvoiceService service;

	@Operation(summary = "This API is used to save the trademark auth letters and invoices for the products")
	@PostMapping("/supplier/product/trademark-invoice")
	public ResponseEntity<SuccessResponse> saveTrademarkInvoiceInfo(@RequestBody TrademarkInvoiceInfoPojo pojo) {
		return new ResponseEntity<>(new SuccessResponse(false, TRADEMARK_INVOICE_SAVE_SUCCESS_MESSAGE,
				service.saveTrademarkInvoiceInfo(pojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get all the trademark auth letters and invoices of a particular supplier")
	@GetMapping("/supplier/product/trademark-invoice/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getTrademarkInvoiceInfo(
			@RequestParam(required = false, name = "keyword") String keyword,
			@RequestParam(required = true, name = "supplierId") String supplierId,
			@PathVariable(name = "pageNumber") int pageNumber, @PathVariable(name = "pageSize") int pageSize) {
		List<TrademarkInvoiceInfo> trademarkInfo = service.getTrademarkInfo(pageNumber, pageSize, keyword, supplierId);
		if (!trademarkInfo.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, TRADEMARK_INVOICE_GET_SUCCESS_MESSAGE, trademarkInfo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, TRADEMARK_INVOICE_GET_FAIL_MESSAGE, trademarkInfo),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This API is used for deleting a particular trademark document")
	@DeleteMapping("/supplier/product/trademark-invoice")
	public ResponseEntity<SuccessResponse> deleteTrademarkInvoiceInfo(
			@RequestParam(required = true, name = "trademarkInvoiceId") String trademarkInvoiceId) {
		return new ResponseEntity<>(new SuccessResponse(false, TRADEMARK_INVOICE_DELETE_SUCCESS_MESSAGE,
				service.deleteTrademarkInvoiceInfo(trademarkInvoiceId)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to update the trademark auth letters and invoices for the products")
	@PutMapping("/supplier/product/trademark-invoice")
	public ResponseEntity<SuccessResponse> updateTrademarkInvoiceInfo(@RequestBody TrademarkInvoiceInfoPojo pojo) {
		return new ResponseEntity<>(new SuccessResponse(false, TRADEMARK_INVOICE_UPDATE_SUCCESS_MESSAGE,
				service.updateTrademarkInvoiceInfo(pojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get all the documents based on document type of a particular supplier for the dropdown")
	@GetMapping("/supplier/product/trademark-invoice-dropdown")
	public ResponseEntity<SuccessResponse> getTrademarkInvoiceForDropdown(
			@RequestParam(name = "documentType") String documentType,
			@RequestParam(name = "supplierId") String supplierId) {
		List<TrademarkInvoiceInfo> trademarkInfo = service.getTrademarkInvoiceForDropdown(documentType, supplierId);
		if (!trademarkInfo.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, TRADEMARK_INVOICE_GET_SUCCESS_MESSAGE, trademarkInfo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, TRADEMARK_INVOICE_GET_FAIL_MESSAGE, trademarkInfo),
					HttpStatus.OK);
		}
	}
}
