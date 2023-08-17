package com.mrmrscart.productcategoriesservice.controller.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EFilterStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EMasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.MasterProduct;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.pojo.product.CrossSellsPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.MasterProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductIdAndSkuIdPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.VariationPriceRangePojo;
import com.mrmrscart.productcategoriesservice.response.category.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.product.MasterProductService;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductCountWrapper;
import com.mrmrscart.productcategoriesservice.wrapper.product.ProductVariationWrapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MasterProductControllerTest {
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private MasterProductService masterProductService;

	private MasterProductPojo masterProductPojo;

	private MasterProduct masterProduct;

	private ProductPojo productPojo;

	private ProductVariationWrapper productVariationWrapper;

	private CrossSellsPojo crossSellsPojo;

	private List<MasterProductPojo> masterProductPojos = new ArrayList<>();

	private List<MasterProduct> masterProducts = new ArrayList<>();

	private List<ProductPojo> productPojos = new ArrayList<>();

	private List<ProductVariationWrapper> productVariationWrappers = new ArrayList<>();

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		masterProductPojo = new MasterProductPojo();
		masterProduct = new MasterProduct();
		productPojo = new ProductPojo();
		productVariationWrapper = new ProductVariationWrapper();
		crossSellsPojo = new CrossSellsPojo();
	}

	@Test
	void addSimpleProductAndVariableProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.addSimpleAndVariableProduct(Mockito.any())).thenReturn(masterProduct);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Added Successfully. ", successResponse.getMessage());

	}

	@Test
	void getSimpleOrVariableProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getSimpleOrVariableProduct(Mockito.anyString())).thenReturn(masterProductPojo);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void approveOrRejectMasterProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.approveOrRejectMasterProduct(Mockito.any(), Mockito.anyString()))
				.thenReturn(masterProductPojo);

		Map<String, String> map = new HashMap<>();
		map.put("userId", "userId");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAll(map);

		String result = mockMvc
				.perform(put("/api/v1/products/master-product-approval").accept(MediaType.APPLICATION_JSON_VALUE)
						.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals(false, successResponse.isError());

	}

	@Test
	void getAllMasterProductBySupplierId() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getAllProductBySupplierId(Mockito.anyString())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("supplierId", "supplierId").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllMasterProductBySupplierIdAndStatus()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getProductBySupIdAndStatus(Mockito.anyString(), Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/1/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("supplierId", "supplierId").param("status", EStatus.APPROVED.toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllMasterProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getAllMasterProduct(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-products/1/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void setStatusOutOfStock() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<ProductVariation> productVariations = new ArrayList<>();
		ProductVariation productVariation = new ProductVariation();
		productVariations.add(productVariation);

		List<ProductIdAndSkuIdPojo> productIdAndSkuIdPojos = new ArrayList<>();
		ProductIdAndSkuIdPojo productIdAndSkuIdPojo = new ProductIdAndSkuIdPojo();
		productIdAndSkuIdPojos.add(productIdAndSkuIdPojo);

		when(masterProductService.setOutOfStock(Mockito.any())).thenReturn(productVariations);

		String result = mockMvc
				.perform(put("/api/v1/products/master-product/out-of-stock").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(productIdAndSkuIdPojos)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Master product out of stock. ", successResponse.getMessage());

	}

	@Test
	void duplicateProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.duplicateProducts(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(masterProduct);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/duplicate-product")
						.accept(MediaType.APPLICATION_JSON_VALUE).param("oldSupplierId", "oldSupplierId")
						.param("oldVariationId", "oldVariationId").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Master product added successfully. ", successResponse.getMessage());

	}

	@Test
	void bulkUpload() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProducts.add(masterProduct);
		when(masterProductService.bulkUpload(Mockito.anyList())).thenReturn(masterProducts);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/bulk-upload").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductPojos)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Master products added successfully. ", successResponse.getMessage());

	}

	@Test
	void deleteVariationWhenSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.deleteSimpleOrVariableProduct(Mockito.anyString())).thenReturn(true);

		String result = mockMvc
				.perform(delete("/api/v1/products/variation/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Deleted Successfully", successResponse.getMessage());

	}

	@Test
	void deleteVariationWhenFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.deleteSimpleOrVariableProduct(Mockito.anyString())).thenReturn(false);

		String result = mockMvc
				.perform(delete("/api/v1/products/variation/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Not Deleted", successResponse.getMessage());

	}

	/** Enum Issue **/
//	@Test
	void getSimpleOrVariableProductByIdAndStatus()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getMasterProductByIdAndStatus(Mockito.anyString(), Mockito.any()))
				.thenReturn(masterProductPojo);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product-status/1/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllMasterProductByStatus() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getAllProductsByStatus(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/approved-master-product/1/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("status", EStatus.APPROVED.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void mergeProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.mergeProduct(Mockito.any())).thenReturn(productPojo);

		String result = mockMvc
				.perform(put("/api/v1/products/merge-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(productPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product marged successfully. ", successResponse.getMessage());

	}

	@Test
	void getProductByVariationId() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		productPojos.add(productPojo);
		when(masterProductService.getMergeProducts(Mockito.anyString())).thenReturn(productPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/merge-product/variation/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Merged product fetched successfully. ", successResponse.getMessage());

	}

	/** Waiting for code changes, service is not called **/
//	@Test
//	void getProductByProductTitle() throws JsonProcessingException, UnsupportedEncodingException, Exception {
//		productPojos.add(productPojo);
//		when(masterProductService.getMergeProducts(Mockito.anyString())).thenReturn(productPojos);
//
//		String result = mockMvc
//				.perform(get("/api/v1/products/merge-product/variation").accept(MediaType.APPLICATION_JSON_VALUE)
//						.contentType(MediaType.APPLICATION_JSON_VALUE))
//				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//
//		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
//		assertEquals("Merged product fetched successfully. ", successResponse.getMessage());
//
//	}

	@Test
	void getProductVariationWithId() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		ProductVariation productVariation = new ProductVariation();

		when(masterProductService.getProductVariationById(Mockito.anyString())).thenReturn(productVariation);

		String result = mockMvc
				.perform(get("/api/v1/products/product-variation/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Variation Fetched Successfully. ", successResponse.getMessage());

	}

	@Test
	void getProductListByProductIdAndVariationId()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		List<MasterProductIdAndVariationIdPojo> masterProductIdAndVariationIdPojos = new ArrayList<>();
		MasterProductIdAndVariationIdPojo masterProductIdAndVariationIdPojo = new MasterProductIdAndVariationIdPojo();
		masterProductIdAndVariationIdPojos.add(masterProductIdAndVariationIdPojo);

		when(masterProductService.getProductList(Mockito.anyList())).thenReturn(productPojos);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/product-variation")
						.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductIdAndVariationIdPojos)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void updateMasterProduct() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.updateMasterProduct(Mockito.any())).thenReturn(masterProduct);

		String result = mockMvc
				.perform(put("/api/v1/products/master-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(masterProductPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Master Product Updated Successfully", successResponse.getMessage());

	}

	@Test
	void getProductsBySupIdAndSubId() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		productVariationWrappers.add(productVariationWrapper);
		when(masterProductService.getProductsBySupplierIdAndSubCategoryIdAndStatus(Mockito.anyString(),
				Mockito.anyString())).thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/products").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("supplierId", "supplierId").param("subCategoryId", "subCategoryId")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getProductCountByStatus() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		ProductCountWrapper productCountWrapper = new ProductCountWrapper();
		when(masterProductService.getProductCountByStatus(Mockito.anyString())).thenReturn(productCountWrapper);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/product-variations-count")
						.accept(MediaType.APPLICATION_JSON_VALUE).param("supplierId", "supplierId")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Count Fetched Successfully", successResponse.getMessage());

	}

	@Test
	void getProductsBySupIdAndMainCategoryIdWhenSuccess()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		productVariationWrappers.add(productVariationWrapper);
		when(masterProductService.getProductsBySupplierIdAndMainCategoryIdAndStatus(Mockito.anyString(),
				Mockito.anyString())).thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/upsells-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("supplierId", "supplierId").param("mainCatgoryId", "mainCatgoryId")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getProductsBySupIdAndMainCategoryIdWhenFailure()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getProductsBySupplierIdAndMainCategoryIdAndStatus(Mockito.anyString(),
				Mockito.anyString())).thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/upsells-product").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("supplierId", "supplierId").param("mainCatgoryId", "mainCatgoryId")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product list is empty. ", successResponse.getMessage());

	}

	@Test
	void getProductsBySupIdAndSubIdAndPriceRange()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		VariationPriceRangePojo variationPriceRangePojo = new VariationPriceRangePojo();
		when(masterProductService.getProductsBySupIdAndSubIdAndPriceRange(Mockito.any()))
				.thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/products/price").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(variationPriceRangePojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getProductsBySubcategoryIdsAndSupplierIdWhenSuccess()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		productVariationWrappers.add(productVariationWrapper);
		when(masterProductService.getProductsBySubcategoryIdsAndSupplierId(Mockito.any()))
				.thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/cross-sells").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(crossSellsPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals(false, successResponse.isError());

	}

	@Test
	void getProductsBySubcategoryIdsAndSupplierIdWhenFailure()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getProductsBySubcategoryIdsAndSupplierId(Mockito.any()))
				.thenReturn(productVariationWrappers);

		String result = mockMvc
				.perform(post("/api/v1/products/master-product/cross-sells").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(crossSellsPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals(true, successResponse.isError());

	}

	@Test
	void getAllProductsByStatusAndFilterWithSuccess()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojo.setProductStatus(EStatus.ACTIVE);
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getAllProductsByStatusAndFilter(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.any(), Mockito.anyString())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/filter-master-product/0/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("status", EStatus.ACTIVE.toString())
						.param("filterStatus", EFilterStatus.PRODUCT_TITLE.toString()).param("keyword", "keyword")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllProductsByStatusAndFilterWithFailure()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getAllProductsByStatusAndFilter(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(),
				Mockito.any(), Mockito.anyString())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/filter-master-product/1/1").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("status", EStatus.ACTIVE.toString())
						.param("filterStatus", EFilterStatus.PRODUCT_TITLE.toString()).param("keyword", "keyword")
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("There are no products to display", successResponse.getMessage());

	}

	@Test
	void getAllProductsByStatusAndFilterAndSupplierIdWhenSuccess()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojo.setProductStatus(EStatus.ACTIVE);
		masterProductPojos.add(masterProductPojo);
		when(masterProductService.getAllProductsByStatusAndFilterAndSupplierId(Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt(), Mockito.any(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product-filter").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("status", EStatus.ACTIVE.toString()).param("pageNumber", "1").param("pageSize", "1")
						.param("keyword", "keyword").param("supplierId", "supplierId")
						.param("filterStatus", EMasterProduct.BRAND.toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllProductsByStatusAndFilterAndSupplierIdWhenFailure()
			throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getAllProductsByStatusAndFilterAndSupplierId(Mockito.any(), Mockito.anyInt(),
				Mockito.anyInt(), Mockito.any(), Mockito.anyString(), Mockito.anyString()))
						.thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product-filter").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("status", EStatus.ACTIVE.toString()).param("pageNumber", "1").param("pageSize", "1")
						.param("keyword", "keyword").param("supplierId", "supplierId")
						.param("filterStatus", EMasterProduct.BRAND.toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("There are no products to display. ", successResponse.getMessage());

	}

	@Test
	void getProductsBySubCategoryIdAndStatus() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getProductsBySubCategoryIdAndStatus(Mockito.anyString(), Mockito.any()))
				.thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/sub-category").accept(MediaType.APPLICATION_JSON_VALUE)
						.param("id", "id").param("status", EStatus.ACTIVE.toString())
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void getAllVariationsByOneVariationId() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getAllVariationByVariationIdAndStatus(Mockito.anyString(), Mockito.any()))
				.thenReturn(masterProductPojo);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/product-variations")
						.accept(MediaType.APPLICATION_JSON_VALUE).param("id", "id")
						.param("status", EStatus.ACTIVE.toString()).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Product Variation Fetched Successfully. ", successResponse.getMessage());

	}

	@Test
	void myCollectionFilterWhenSuccess() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		masterProductPojos.add(masterProductPojo);

		when(masterProductService.getAllProductsByColumnName(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/my-collection/1/1")
						.accept(MediaType.APPLICATION_JSON_VALUE).param("supplierId", "supplierId")
						.param("status", EStatus.ACTIVE.toString()).param("columnName", EMasterProduct.ALL.toString())
						.param("keyword", "keyword").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("Products fetched successfully. ", successResponse.getMessage());

	}

	@Test
	void myCollectionFilterWhenFailure() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		when(masterProductService.getAllProductsByColumnName(Mockito.anyString(), Mockito.any(), Mockito.any(),
				Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(masterProductPojos);

		String result = mockMvc
				.perform(get("/api/v1/products/master-product/my-collection/1/0")
						.accept(MediaType.APPLICATION_JSON_VALUE).param("supplierId", "supplierId")
						.param("status", EStatus.ACTIVE.toString()).param("columnName", EMasterProduct.ALL.toString())
						.param("keyword", "keyword").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		SuccessResponse successResponse = mapper.readValue(result, SuccessResponse.class);
		assertEquals("There Are No Product Collection", successResponse.getMessage());

	}
}
