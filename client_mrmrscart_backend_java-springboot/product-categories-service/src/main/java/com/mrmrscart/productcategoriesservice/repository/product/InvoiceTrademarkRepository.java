package com.mrmrscart.productcategoriesservice.repository.product;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;

@Repository
public interface InvoiceTrademarkRepository extends MongoRepository<TrademarkInvoiceInfo, String> {

	List<TrademarkInvoiceInfo> findByIsDeletedAndSupplierId(boolean b, String supplierId, PageRequest of);

	TrademarkInvoiceInfo findByIsDeletedAndTrademarkInvoiceId(boolean b, String supplierId);

	List<TrademarkInvoiceInfo> findByIsDeletedAndSupplierIdAndDocumentType(boolean b, String supplierId,
			String documentType);

}
