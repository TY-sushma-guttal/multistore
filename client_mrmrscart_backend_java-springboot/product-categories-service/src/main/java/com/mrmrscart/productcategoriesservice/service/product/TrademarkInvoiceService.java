package com.mrmrscart.productcategoriesservice.service.product;

import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.TrademarkInvoiceInfo;
import com.mrmrscart.productcategoriesservice.pojo.product.TrademarkInvoiceInfoPojo;

public interface TrademarkInvoiceService {

	public TrademarkInvoiceInfo saveTrademarkInvoiceInfo(TrademarkInvoiceInfoPojo pojo);

	public List<TrademarkInvoiceInfo> getTrademarkInfo(int pageNumber, int pageSize, String keyword, String supplierId);

	public TrademarkInvoiceInfo deleteTrademarkInvoiceInfo(String trademarkInvoiceId);

	public TrademarkInvoiceInfo updateTrademarkInvoiceInfo(TrademarkInvoiceInfoPojo pojo);

	public List<TrademarkInvoiceInfo> getTrademarkInvoiceForDropdown(String documentType, String supplierId);
}
