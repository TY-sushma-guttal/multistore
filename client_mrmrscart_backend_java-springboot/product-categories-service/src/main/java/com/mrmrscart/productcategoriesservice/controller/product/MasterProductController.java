package com.mrmrscart.productcategoriesservice.controller.product;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.EMPTY_MASTER_PRODUCT_LIST;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_ADD_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_FETCH_FAIL_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_OUTOFSTOCK_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MASTER_PRODUCT_UPDATE_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MERGE_PRODUCT_ADD_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.MERGE_PRODUCT_FETCH_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_COUNT_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_DELETE_FAILURE_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_DELETE_SUCCESS_MESSAGE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.PRODUCT_VARIATION_GET_SUCCESS_MESSAGE;

import java.util.List;

import javax.ws.rs.QueryParam;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EFilterStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EMasterProduct;
import com.mrmrscart.productcategoriesservice.pojo.product.CrossSellsPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductIdAndSkuIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductTitlePojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductVariationIdAndStatusPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPriceRangePojo;
import com.mrmrscart.productcategoriesservice.response.product.MasterProductAndVariationResponse;
import com.mrmrscart.productcategoriesservice.response.product.MasterProductGetResponse;
import com.mrmrscart.productcategoriesservice.response.product.ProductVariationGetResponse;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.MasterProductService;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductVariationWrapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/products")
public class MasterProductController {

	@Autowired
	private MasterProductService masterProductService;

	@Operation(summary = "This method will add the simple product or variable product. ")
	@PostMapping("/master-product")
	public ResponseEntity<SuccessResponse> addSimpleProductAndVariableProduct(@RequestBody MasterProductPojo data) {

		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_ADD_SUCCESS_MESSAGE,
				masterProductService.addSimpleAndVariableProduct(data)), HttpStatus.OK);
	}

	@Operation(summary = "This method help us to get the simple product or variable product based on product id. ")
	@GetMapping("/master-product/{id}")
	public ResponseEntity<MasterProductGetResponse> getSimpleOrVariableProduct(@PathVariable String id) {
		return new ResponseEntity<>(new MasterProductGetResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getSimpleOrVariableProduct(id)), HttpStatus.OK);
	}

	@Operation(summary = "This method will approve or reject a product. ")
	@PutMapping("/master-product-approval")
	public ResponseEntity<SuccessResponse> approveOrRejectMasterProduct(
			@RequestBody ProductVariationIdAndStatusPojo data, @RequestHeader("userId") String userId) {
		return new ResponseEntity<>(new SuccessResponse(false, "Master product " + data.getStatus() + ". ",
				masterProductService.approveOrRejectMasterProduct(data, userId)), HttpStatus.OK);
	}

	@Operation(summary = "This method will fetch all master product based on supplier id. ")
	@GetMapping("/master-product")
	public ResponseEntity<SuccessResponse> getAllMasterProductBySupplierId(@RequestParam String supplierId) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getAllProductBySupplierId(supplierId)), HttpStatus.OK);
	}

//
	@Operation(summary = "This method will fetch all product based on supplier id and status. ")
	@GetMapping("/master-product/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllMasterProductBySupplierIdAndStatus(
			@RequestParam("supplierId") String supplierId, @RequestParam("status") EStatus status,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		return new ResponseEntity<>(
				new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
						masterProductService.getProductBySupIdAndStatus(supplierId, status, pageNumber, pageSize)),
				HttpStatus.OK);
	}

//
	@Operation(summary = "This method will fetch all the product erespective of everything. ")
	@GetMapping("/master-products/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllMasterProduct(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getAllMasterProduct(pageNumber, pageSize)), HttpStatus.OK);
	}

	@Operation(summary = "This method will help us to make a list of product as out of stock. ")
	@PutMapping("/master-product/out-of-stock")
	public ResponseEntity<SuccessResponse> setStatusOutOfStock(@RequestBody List<ProductIdAndSkuIdPojo> data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, MASTER_PRODUCT_OUTOFSTOCK_MESSAGE, masterProductService.setOutOfStock(data)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method will make a product as duplicate. ")
	@PostMapping("/master-product/duplicate-product")
	public ResponseEntity<SuccessResponse> duplicateProduct(@RequestBody MasterProductPojo data,
			@QueryParam("oldSupplierId") String oldSupplierId, @QueryParam("oldVariationId") String oldVariationId) {
		return new ResponseEntity<>(new SuccessResponse(false, "Master product added successfully. ",
				masterProductService.duplicateProducts(data, oldSupplierId, oldVariationId)), HttpStatus.OK);
	}

	@Operation(summary = "This method will upload the multiple product at a time. ")
	@PostMapping("/master-product/bulk-upload")
	public ResponseEntity<SuccessResponse> bulkUpload(@RequestBody List<MasterProductPojo> masterProductPojos) {
		return new ResponseEntity<>(new SuccessResponse(false, "Master products added successfully. ",
				masterProductService.bulkUpload(masterProductPojos)), HttpStatus.OK);
	}

	@Operation(summary = "This method will delete simple product or variable product. ")
	@DeleteMapping("/variation/{variationId}")
	public ResponseEntity<SuccessResponse> deleteVariation(@PathVariable String variationId) {
		if (masterProductService.deleteSimpleOrVariableProduct(variationId)) {
			return new ResponseEntity<>(new SuccessResponse(false, PRODUCT_DELETE_SUCCESS_MESSAGE, true),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, PRODUCT_DELETE_FAILURE_MESSAGE, false), HttpStatus.OK);
	}

	@Operation(summary = "This method will fetch a simple product or variable product based on product id and status")
	@GetMapping("/master-product-status/{id}/{status}")
	public ResponseEntity<SuccessResponse> getSimpleOrVariableProductByIdAndStatus(@PathVariable String id,
			@PathVariable EStatus status) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getMasterProductByIdAndStatus(id, status)), HttpStatus.OK);
	}

//	 get all product by status
	@Operation(summary = "This method will help us to get all master product by status. ")
	@GetMapping("/approved-master-product/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllMasterProductByStatus(@RequestParam("status") EStatus status,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getAllProductsByStatus(status, pageNumber, pageSize)), HttpStatus.OK);
	}

	// all products with filter
	@Operation(summary = "This method will help us to get all master product by status. ")
	@GetMapping("/filter-master-product/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllProductsByStatusAndFilter(@RequestParam("status") EStatus status,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize,
			@RequestParam(name = "filterStatus") EFilterStatus filterStatus,
			@RequestParam(name = "keyword", required = false) String keyword) {
		List<MasterProductPojo> list = masterProductService.getAllProductsByStatusAndFilter(status, pageNumber,
				pageSize, filterStatus, keyword);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, "There are no products to display", list),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "This method is used to merge the same type of product. ")
	@PutMapping("/merge-product")
	public ResponseEntity<SuccessResponse> mergeProduct(@RequestBody ProductPojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, MERGE_PRODUCT_ADD_SUCCESS_MESSAGE, masterProductService.mergeProduct(data)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method will fetch all the merged product based on id. ")
	@GetMapping("/merge-product/variation/{id}")
	public ResponseEntity<SuccessResponse> getProductByVariationId(@PathVariable String id) {
		return new ResponseEntity<>(new SuccessResponse(false, MERGE_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getMergeProducts(id)), HttpStatus.OK);
	}

	@GetMapping("/merge-product/variation")
	public ResponseEntity<SuccessResponse> getProductByProductTitle(@RequestBody ProductTitlePojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, data),
				HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * 
	 * @param status
	 * @param pageNumber
	 * @param pageSize
	 * @param keyword
	 * @param supplierId
	 * @param filterStatus
	 * @return
	 */
	@Operation(summary = "This Filter method will fetch Products based on status (Supplier Product-Inventory My-Products)")
	@GetMapping("/master-product-filter")
	public ResponseEntity<SuccessResponse> getAllProductsByStatusAndFilterAndSupplierId(
			@RequestParam("status") EStatus status, @RequestParam("pageNumber") int pageNumber,
			@RequestParam("pageSize") int pageSize, @RequestParam("keyword") String keyword,
			@RequestParam("supplierId") String supplierId, @RequestParam("filterStatus") EMasterProduct filterStatus) {
		List<MasterProductPojo> list = masterProductService.getAllProductsByStatusAndFilterAndSupplierId(status,
				pageNumber, pageSize, filterStatus, keyword, supplierId);
		if (list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, PRODUCT_FAILURE, list), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, list),
				HttpStatus.OK);
	}

	@GetMapping("/master-product/my-collection/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> myCollectionFilter(@RequestParam String supplierId,
			@RequestParam EStatus status, @RequestParam EMasterProduct columnName, @RequestParam String keyword,
			@PathVariable int pageNumber, @PathVariable int pageSize) {
		List<MasterProductPojo> list = masterProductService.getAllProductsByColumnName(supplierId, status, columnName,
				keyword, pageNumber, pageSize);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, MASTER_PRODUCT_FETCH_FAIL_MESSAGE, list),
					HttpStatus.OK);
	}

	@Operation(summary = "This api find one product variation based on id. ")
	@GetMapping("/product-variation/{id}")
	public ResponseEntity<ProductVariationGetResponse> getProductVariationWithId(@PathVariable String id) {
		return new ResponseEntity<>(new ProductVariationGetResponse(false, PRODUCT_VARIATION_GET_SUCCESS_MESSAGE,
				masterProductService.getProductVariationById(id)), HttpStatus.OK);
	}

	@Operation(summary = "This api will take list of master product id and variation id and it return list of product")
	@PostMapping("/master-product/product-variation")
	public ResponseEntity<MasterProductAndVariationResponse> getProductListByProductIdAndVariationId(
			@RequestBody List<MasterProductIdAndVariationIdPojo> data) {
		return new ResponseEntity<>(new MasterProductAndVariationResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getProductList(data)), HttpStatus.OK);
	}

	@Operation(summary = "This will help us to update the product details. ")
	@PutMapping("/master-product")
	public ResponseEntity<SuccessResponse> updateMasterProduct(@RequestBody MasterProductPojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_UPDATE_SUCCESS_MESSAGE,
				masterProductService.updateMasterProduct(data)), HttpStatus.OK);
	}

	@Operation(summary = "This api will fetch all products based on sub category id and status")
	@GetMapping("/master-product/sub-category")
	public ResponseEntity<SuccessResponse> getProductsBySubCategoryIdAndStatus(@RequestParam String id,
			@RequestParam EStatus status) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getProductsBySubCategoryIdAndStatus(id, status)), HttpStatus.OK);
	}

	@Operation(summary = "This api will fetch all the variations based on one variation id and status")
	@GetMapping("/master-product/product-variations")
	public ResponseEntity<SuccessResponse> getAllVariationsByOneVariationId(@RequestParam String id,
			@RequestParam EStatus status) {
		return new ResponseEntity<>(new SuccessResponse(false, PRODUCT_VARIATION_GET_SUCCESS_MESSAGE,
				masterProductService.getAllVariationByVariationIdAndStatus(id, status)), HttpStatus.OK);
	}

	@Operation(summary = "This will fetch all the products based on the supplier id and sub category id")
	@GetMapping("/master-product/products")
	public ResponseEntity<SuccessResponse> getProductsBySupIdAndSubId(@RequestParam String supplierId,
			@RequestParam String subCategoryId) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getProductsBySupplierIdAndSubCategoryIdAndStatus(supplierId, subCategoryId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API will fetch the products count of a supplier based on status")
	@GetMapping("/master-product/product-variations-count")
	public ResponseEntity<SuccessResponse> getProductCountByStatus(@RequestParam String supplierId) {
		return new ResponseEntity<>(new SuccessResponse(false, PRODUCT_COUNT_SUCCESS_MESSAGE,
				masterProductService.getProductCountByStatus(supplierId)), HttpStatus.OK);
	}

	@Operation(summary = "This will fetch all the products based on the supplier id and main category id")
	@GetMapping("/master-product/upsells-product")
	public ResponseEntity<SuccessResponse> getProductsBySupIdAndMainCategoryId(@RequestParam String supplierId,
			@RequestParam String mainCatgoryId) {
		List<ProductVariationWrapper> list = masterProductService
				.getProductsBySupplierIdAndMainCategoryIdAndStatus(supplierId, mainCatgoryId);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, list),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, EMPTY_MASTER_PRODUCT_LIST, list), HttpStatus.OK);
	}

	@Operation(summary = "This will fetch all the products based on the supplier id and sub category id and price ranges")
	@PostMapping("/master-product/products/price")
	public ResponseEntity<SuccessResponse> getProductsBySupIdAndSubIdAndPriceRange(
			@RequestBody VariationPriceRangePojo pojo) {
		return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE,
				masterProductService.getProductsBySupIdAndSubIdAndPriceRange(pojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the products based on list of sub categories and supplier id")
	@PostMapping("/master-product/cross-sells")
	public ResponseEntity<SuccessResponse> getProductsBySubcategoryIdsAndSupplierId(@RequestBody CrossSellsPojo pojo) {
		List<ProductVariationWrapper> products = masterProductService.getProductsBySubcategoryIdsAndSupplierId(pojo);
		if (!products.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, products),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, MASTER_PRODUCT_FETCH_SUCCESS_MESSAGE, products),
					HttpStatus.OK);
	}
}
