package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.*;

import java.util.List;
import java.util.stream.Collectors;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.INVALID_SUPPLIER;
import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.SOMETHING_WENT_WRONG;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.exception.product.SupplierNotFoundException;
import com.mrmrscart.productcategoriesservice.exception.product.TrademarkInvoiceNotFoundException;
import com.mrmrscart.productcategoriesservice.feign.client.UserServiceClient;
import com.mrmrscart.productcategoriesservice.feign.enums.ESupplierStatus;
import com.mrmrscart.productcategoriesservice.feign.response.SupplierRegistrationGetResponse;
import com.mrmrscart.productcategoriesservice.pojo.product.TrademarkInvoiceInfoPojo;
import com.mrmrscart.productcategoriesservice.repository.product.InvoiceTrademarkRepository;

@Service
public class TrademarkInvoiceServiceImpl implements TrademarkInvoiceService {
	@Autowired
	private InvoiceTrademarkRepository invoiceTrademarkRepository;

	@Autowired
	private UserServiceClient userClient;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Transactional
	@Override
	public TrademarkInvoiceInfo saveTrademarkInvoiceInfo(TrademarkInvoiceInfoPojo pojo) {

		try {
			SupplierRegistrationGetResponse validateSupplier = validateSupplier(pojo.getSupplierId());
			if (validateSupplier.getData() != null) {
				TrademarkInvoiceInfo trademarkInvoiceInfo = new TrademarkInvoiceInfo();
				BeanUtils.copyProperties(pojo, trademarkInvoiceInfo);
				return invoiceTrademarkRepository.save(trademarkInvoiceInfo);

			} else {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
		} catch (SupplierNotFoundException e) {
			throw e;
		} catch (Exception exception) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<TrademarkInvoiceInfo> getTrademarkInfo(int pageNumber, int pageSize, String keyword,
			String supplierId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		try {
			if (keyword != null) {
				query.addCriteria(criteria
						.orOperator(Criteria.where("documentName").regex(keyword, I),
								Criteria.where("description").regex(keyword, I),
								Criteria.where("documentType").regex(keyword, I))
						.andOperator(Criteria.where("isDeleted").is(false),
								Criteria.where("supplierId").is(supplierId)));
				List<TrademarkInvoiceInfo> list = mongoTemplate.find(query, TrademarkInvoiceInfo.class);
				return getPaginatedResponse(pageNumber, pageSize, list);
			} else {
				return invoiceTrademarkRepository.findByIsDeletedAndSupplierId(false, supplierId,
						PageRequest.of(pageNumber, pageSize));
			}
		} catch (TrademarkInvoiceNotFoundException e) {
			throw e;
		} catch (Exception exception) {
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	private List<TrademarkInvoiceInfo> getPaginatedResponse(int pageNumber, int pageSize,
			List<TrademarkInvoiceInfo> result) {
		if (pageNumber == 0) {
			return result.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return result.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@Transactional
	@Override
	public TrademarkInvoiceInfo deleteTrademarkInvoiceInfo(String trademarkInvoiceId) {
		try {
			TrademarkInvoiceInfo trademarkInvoiceInfo = invoiceTrademarkRepository
					.findByIsDeletedAndTrademarkInvoiceId(false, trademarkInvoiceId);
			if (trademarkInvoiceInfo != null) {
				trademarkInvoiceInfo.setDeleted(true);
				return invoiceTrademarkRepository.save(trademarkInvoiceInfo);
			} else {
				throw new TrademarkInvoiceNotFoundException(TRADEMARK_INVOICE_DELETE_FAIL_MESSAGE);
			}
		} catch (TrademarkInvoiceNotFoundException e) {
			throw e;
		} catch (Exception exception) {
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistrationGetResponse validateSupplier(String supplierId) {
		ResponseEntity<SupplierRegistrationGetResponse> supplierById = userClient.getSupplierById(supplierId,
				ESupplierStatus.APPROVED);
		SupplierRegistrationGetResponse body = supplierById.getBody();
		if (body != null) {
			return body;
		} else {
			throw new SupplierNotFoundException(INVALID_SUPPLIER);
		}
	}

	@Transactional
	@Override
	public TrademarkInvoiceInfo updateTrademarkInvoiceInfo(TrademarkInvoiceInfoPojo pojo) {
		try {
			SupplierRegistrationGetResponse validateSupplier = validateSupplier(pojo.getSupplierId());
			if (validateSupplier.getData() != null) {
				TrademarkInvoiceInfo trademarkInvoiceInfo = findTrademarkInvoiceInfo(pojo.getTrademarkInvoiceId());
				BeanUtils.copyProperties(pojo, trademarkInvoiceInfo);
				return invoiceTrademarkRepository.save(trademarkInvoiceInfo);
			} else {
				throw new TrademarkInvoiceNotFoundException(TRADEMARK_INVOICE_GET_FAIL_MESSAGE);
			}
		} catch (TrademarkInvoiceNotFoundException e) {
			throw e;
		} catch (Exception exception) {
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	private TrademarkInvoiceInfo findTrademarkInvoiceInfo(String trademarkInvoiceId) {
		TrademarkInvoiceInfo trademarkInvoiceInfo = invoiceTrademarkRepository
				.findByIsDeletedAndTrademarkInvoiceId(false, trademarkInvoiceId);
		if (trademarkInvoiceInfo != null) {
			return trademarkInvoiceInfo;
		} else {
			throw new TrademarkInvoiceNotFoundException(TRADEMARK_INVOICE_GET_FAIL_MESSAGE);
		}
	}

	@Override
	public List<TrademarkInvoiceInfo> getTrademarkInvoiceForDropdown(String documentType, String supplierId) {
		try {
			return invoiceTrademarkRepository.findByIsDeletedAndSupplierIdAndDocumentType(false, supplierId,
					documentType);
		} catch (Exception e) {
			throw new TrademarkInvoiceNotFoundException(TRADEMARK_INVOICE_GET_FAIL_MESSAGE);
		}
	}
}
