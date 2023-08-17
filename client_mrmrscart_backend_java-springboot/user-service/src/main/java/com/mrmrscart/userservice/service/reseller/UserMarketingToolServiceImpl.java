package com.mrmrscart.userservice.service.reseller;

import static com.mrmrscart.userservice.common.admin.MediaConstant.FILE_NAME_FAILURE;
import static com.mrmrscart.userservice.common.admin.MediaConstant.PATH;
import static com.mrmrscart.userservice.common.reseller.MarketingToolThemeConstant.INVALID_MARKETING_TOOL_THEME_ID;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.ACTIVE_SUBSCRIPTION_FAILED;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.INVALID_SUBSCRIPTION;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.INVALID_TOOL_TYPE;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_CAMPAIGN_NOT_FOUND;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_NOT_FOUND;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.PURCHASED_MARKETING_TOOL_HISTORY_NOT_FOUND;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.PURCHASE_HISTORY_SAVE_FAILED;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.entity.ContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;
import com.mrmrscart.userservice.entity.admin.AdminMarketingToolsCampaign;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingToolType;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.reseller.ActiveMarketingToolSubscription;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.entity.reseller.MarketingToolTheme;
import com.mrmrscart.userservice.entity.reseller.ResellerStoreDetails;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignException;
import com.mrmrscart.userservice.exception.admin.FailedToUploadException;
import com.mrmrscart.userservice.exception.admin.MarketingToolStatusException;
import com.mrmrscart.userservice.exception.reseller.ActiveMarketingToolSubscriptionSaveFailedException;
import com.mrmrscart.userservice.exception.reseller.InvalidSubscriptionException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolNotFound;
import com.mrmrscart.userservice.exception.reseller.MarketingToolThemeNotFound;
import com.mrmrscart.userservice.exception.reseller.MasterProductNotFoundException;
import com.mrmrscart.userservice.exception.reseller.PurchaseHistorySaveFailedException;
import com.mrmrscart.userservice.exception.reseller.PuschasedMarketingToolHistoryNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.feign.client.ProductCategoryClient;
import com.mrmrscart.userservice.feign.pojo.MasterProductIdAndVariationIdPojo;
import com.mrmrscart.userservice.feign.pojo.ProductPojo;
import com.mrmrscart.userservice.feign.response.MasterProductAndVariationResponse;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolPojo;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.PriceCatalogPojo;
import com.mrmrscart.userservice.pojo.reseller.PriceCatalogSubPojo;
import com.mrmrscart.userservice.pojo.reseller.PurchaseMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolMainResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolUpdatePojo;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsCampaignRepository;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsRepository;
import com.mrmrscart.userservice.repository.reseller.ActiveMarketingToolSubscriptionRepository;
import com.mrmrscart.userservice.repository.reseller.MarketingToolPurchaseHistoryRepository;
import com.mrmrscart.userservice.repository.reseller.MarketingToolThemeRepository;
import com.mrmrscart.userservice.repository.reseller.ResellerStoreDetailsRepository;
import com.mrmrscart.userservice.repository.reseller.UserMarketingToolRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.util.SSSFileUpload;
import com.mrmrscart.userservice.wrapper.reseller.MarketingToolStatusWrapper;

import feign.FeignException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class UserMarketingToolServiceImpl implements UserMarketingToolService {

	@Autowired
	private UserMarketingToolRepository userMarketingToolRepository;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private ProductCategoryClient productCategoryClientService;

	@Autowired
	private MarketingToolThemeRepository marketingToolThemeRepository;

	@Autowired
	private MarketingToolPurchaseHistoryRepository purchasedMarketingToolsRepository;

	@Autowired
	private AdminMarketingToolsRepository adminMarketingToolsRepository;

	@Autowired
	private AdminMarketingToolsCampaignRepository adminMarketingToolsCampaignRepository;

	@Autowired
	private ActiveMarketingToolSubscriptionRepository activeMarketingToolSubscriptionRepository;

	@Autowired
	private ResellerStoreDetailsRepository resellerStoreDetailsRepository;

	@Autowired
	private MarketingToolPurchaseHistoryRepository marketingToolPurchaseHistoryRepository;

	@Autowired
	private SSSFileUpload sssObj;

	private String generateCouponCode() {
		return "Generated coupon code";
	}

	@Override
	public List<UserMarketingTool> addMarketingTool(UserMarketingToolPojo data) {
		try {
			UserMarketingTool marketingTool = new UserMarketingTool();
			BeanUtils.copyProperties(data, marketingTool);
			marketingTool.setCustomerType(data.getCustomerType().name());

			productCategoryClientService.getSubCategory(data.getSubCategoryId());
			productCategoryClientService.getMainCategory(data.getMainCategoryId());

			SupplierRegistration findBySupplierIdAndStatus = supplierRegistrationRepository
					.findBySupplierIdAndStatus(data.getUserTypeId(), ESupplierStatus.APPROVED);
			if (findBySupplierIdAndStatus == null) {
				throw new SupplierIdNotFoundException(INVALID_SUPPLIER);
			}

			List<MasterProductIdAndVariationIdPojo> pojoList = new ArrayList<>();
			data.getMarketingToolProductList().forEach(e -> {
				MasterProductIdAndVariationIdPojo pojo = new MasterProductIdAndVariationIdPojo();
				pojo.setMasterProductId(e.getMasterProductId());
				pojo.setVariationId(e.getVariationId());
				pojoList.add(pojo);
			});
			ResponseEntity<MasterProductAndVariationResponse> productPojoList = productCategoryClientService
					.getProductListByProductIdAndVariationId(pojoList);

			marketingTool.setToolStatus(EMarketingToolStatus.PENDING);
			marketingTool.setMarketingToolQuestionAnswerList(data.getMarketingToolQuestionAnswerList());
			marketingTool.setSplitDiscountDetailList(data.getSplitDiscountDetailList());
			marketingTool.setMarketingToolProductList(data.getMarketingToolProductList());

			/* coupon code generation code start */

			// Coupon code format not yet confirmed
			marketingTool.setCouponCode(generateCouponCode());

			/* coupon code generation code end */

			/*
			 * Checking the subscription period of the of the user marketing tool code and
			 * save user marketing tool with the saving of the MarketinToolPurchaseHistory
			 * starts
			 */
			ActiveMarketingToolSubscription subscription = activeMarketingToolSubscriptionRepository
					.findBySubscriptionByTypeAndSubscriptionByIdAndMarketingToolNameAndIsDisabled(data.getUserType(),
							data.getUserTypeId(), data.getToolType().name(), false);
			MarketingToolPurchaseHistory savedObj = null;
			if (subscription == null) {
				throw new InvalidSubscriptionException(INVALID_SUBSCRIPTION);
			} else {
				/* Came to else block means that particular subscription is present */
				/*
				 * find by marketing tool type from the admin marketing tool table by
				 * marketinToolName
				 */
				/*
				 * FINAL STEPS:1 Fetch the tool object from the table called
				 * MarketinToolPurchaseHistory using (purchased by id, purchased by type, tool
				 * status, )
				 */

				AdminMarketingTools adminMarketingTool = adminMarketingToolsRepository
						.findByAdminMarketingToolNameAndIsDisabledAndStatusAndDays(subscription.getMarketingToolName(),
								false, subscription.getSubscriptionStatus(), subscription.getDays());
				/* I have to fetch the marketingToolPurchase history object */
				/*
				 * Create a method with fields subscriptionTypeId from admin marketing tool
				 * adminMarketinToolId and adminMarketingToolName purchasedById and
				 * purchasedByType toolStatus
				 */

				MarketingToolPurchaseHistory purchaseHistory = purchasedMarketingToolsRepository
						.findPurchaseMarketingToolHistory(adminMarketingTool.getAdminMarketingToolName(),
								subscription.getSubscriptionById(), subscription.getSubscriptionByType(),
								subscription.getSubscriptionStatus());
				List<UserMarketingTool> userMarketingTools = null;
				if (purchaseHistory != null) {
					userMarketingTools = purchaseHistory.getUserMarketingTools();
					if (!userMarketingTools.isEmpty()) {
						userMarketingTools.add(marketingTool);
					} else {
						userMarketingTools = new ArrayList<>();
						userMarketingTools.add(marketingTool);
					}
				} else {
					throw new PuschasedMarketingToolHistoryNotFoundException(
							PURCHASED_MARKETING_TOOL_HISTORY_NOT_FOUND);
				}
				userMarketingTools.forEach(e -> {
					if (data.getToolType().name().equals("SPIN_WHEEL")) {
						Optional<MarketingToolTheme> findById = marketingToolThemeRepository
								.findById(data.getMarketingToolThemeId());
						if (findById.isPresent()) {
							if (findById.get().getMarketingTools() == null) {
								List<UserMarketingTool> userMarketingToolList = new ArrayList<>();
								userMarketingToolList.add(e);
								findById.get().setMarketingTools(userMarketingToolList);
							} else {
								findById.get().getMarketingTools().add(e);
							}
							marketingToolThemeRepository.save(findById.get());
						} else {
							throw new MarketingToolThemeNotFound(INVALID_MARKETING_TOOL_THEME_ID);
						}
					}
					if (data.getToolType().name().equals("PRICE_TARGETED")) {
						e.setProductCatalogueUrl(getProductCatalogueUrl(data, productPojoList.getBody().getData()));
					}
				});
				purchaseHistory.setUserMarketingTools(userMarketingTools);
				savedObj = purchasedMarketingToolsRepository.save(purchaseHistory);
			}

			/*
			 * Checking the subscription period of the of the user marketing tool code ends
			 */

			/* User Marketing Tool Theme add code start */
			/* User marketing tool theme add code end */

			return savedObj.getUserMarketingTools();
		} catch (MasterProductNotFoundException | MarketingToolThemeNotFound | InvalidSubscriptionException
				| SupplierIdNotFoundException | PuschasedMarketingToolHistoryNotFoundException | FeignException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	private String getProductCatalogueUrl(UserMarketingToolPojo data, List<ProductPojo> productPojos) {
		try {
			PriceCatalogPojo priceCatalogPojo = new PriceCatalogPojo();
			if (data.getUserType().equals("RESELLER")) {
				ResellerStoreDetails resellerStoreDetails = resellerStoreDetailsRepository
						.findByStoreOwnerTypeAndStoreOwnerId(data.getUserType(), data.getUserTypeId());
				priceCatalogPojo.setStoreCode(resellerStoreDetails.getResellerStoreCode());
				priceCatalogPojo.setStoreName(resellerStoreDetails.getResellerStoreName());
			} else if (data.getUserType().equals("SUPPLIER")) {
				Optional<SupplierRegistration> findById = supplierRegistrationRepository.findById(data.getUserTypeId());
				SupplierStoreInfo supplierStoreInfo = findById.get().getSupplierStoreInfo();
				priceCatalogPojo.setStoreCode(supplierStoreInfo.getSupplierStoreCode());
				priceCatalogPojo.setStoreName(supplierStoreInfo.getSupplierStoreName());
			}
			priceCatalogPojo.setCampaignTitle(data.getCampaignTitle());
			priceCatalogPojo.setDescription(data.getDescription());
			priceCatalogPojo.setPrice(data.getPriceStartRange() + " - " + data.getPriceEndRange());
			priceCatalogPojo.setShoppingLink("https://mrmrscart.com?products=123456");
			List<PriceCatalogSubPojo> priceCatalogSubPojos = new ArrayList<>();
			productPojos.forEach(e -> {
				PriceCatalogSubPojo priceCatalogSubPojo = new PriceCatalogSubPojo();
				priceCatalogSubPojo.setProductName(e.getVariationData().getProductTitle());
				priceCatalogSubPojo.setShortDescription(e.getShortDescription());
				priceCatalogSubPojo.setPurchaseLink("https://mrmrscart.com?products=123456");
				priceCatalogSubPojos.add(priceCatalogSubPojo);
			});
			priceCatalogPojo.setPriceCatalogSubPojos(priceCatalogSubPojos);
			List<PriceCatalogPojo> priceCatalogPojos = new ArrayList<>();
			priceCatalogPojos.add(priceCatalogPojo);
			JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(priceCatalogPojos);
			JasperReport jasperReport = JasperCompileManager
					.compileReport(new FileInputStream("src/main/resources/reports/PriceCatalog.jrxml"));
			HashMap<String, Object> map = new HashMap<>();

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, jrBeanCollectionDataSource);
			byte[] exportReportToPdf = JasperExportManager.exportReportToPdf(jasperPrint);
//			FileUtils.writeByteArrayToFile(new File("src/main/resources/reportimg/productcatalog.pdf"),
//					exportReportToPdf);
			InputStream inputStream = new ByteArrayInputStream(exportReportToPdf);
			MultipartFile file = new MockMultipartFile("productCatalog", "priceCatalog",
					ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
			String generateFileName = generateFileName(file);
			String filePath = data.getUserType().toLowerCase() + PATH + "media" + PATH + "pricecatalog" + PATH
					+ customCategoryIdGenerator() + PATH + generateFileName;
			return sssObj.uploadFile(filePath, file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}

	}

	public String customCategoryIdGenerator() {
		DateTimeFormatter zonedFormatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmssSSS");
		return LocalDateTime.now().format(zonedFormatter);
	}

	private String generateFileName(MultipartFile multiPart) {

		String fileName = multiPart.getOriginalFilename();
		if (fileName != null) {
			return new Date().getTime() + "-" + fileName.replace(" ", "_");
		} else {
			throw new FailedToUploadException(FILE_NAME_FAILURE);
		}

	}

	@Override
	public UserMarketingTool deleteMarketingTool(Long marketingToolId) {
		Optional<UserMarketingTool> findById = userMarketingToolRepository.findById(marketingToolId);
		try {
			if (findById.isPresent()) {
				findById.get().setDelete(true);
				return userMarketingToolRepository.save(findById.get());
			} else {
				throw new MarketingToolException(MARKETING_TOOL_NOT_FOUND);
			}
		} catch (MarketingToolException e) {
			throw new MarketingToolException(MARKETING_TOOL_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PurchasedMarketingToolResponsePojo> getAllSubscriptions(
			PurchasedMarketingToolPojo purchasedMarketingToolPojo, int pageNumber, int pageSize) {

		try {
			if (purchasedMarketingToolPojo.getFromDate() != null && purchasedMarketingToolPojo.getToDate() != null) {
				List<MarketingToolPurchaseHistory> list = purchasedMarketingToolsRepository
						.findByPurchasedByIdAndPurchasedByTypeAndPurchasedAtBetween(
								purchasedMarketingToolPojo.getUserId(), purchasedMarketingToolPojo.getUserType(),
								purchasedMarketingToolPojo.getFromDate(), purchasedMarketingToolPojo.getToDate());
				if (list.isEmpty()) {
					return new ArrayList<>();
				} else {
					List<PurchasedMarketingToolResponsePojo> findAllSubscriptions = findAllSubscriptions(list);
					return (List<PurchasedMarketingToolResponsePojo>) getPaginatedResponse(findAllSubscriptions,
							pageNumber, pageSize);
				}

			} else {
				List<MarketingToolPurchaseHistory> list = purchasedMarketingToolsRepository
						.findByPurchasedByIdAndPurchasedByType(purchasedMarketingToolPojo.getUserId(),
								purchasedMarketingToolPojo.getUserType());
				if (list.isEmpty()) {
					return new ArrayList<>();
				} else {
					List<PurchasedMarketingToolResponsePojo> findAllSubscriptions = findAllSubscriptions(list);
					return (List<PurchasedMarketingToolResponsePojo>) getPaginatedResponse(findAllSubscriptions,
							pageNumber, pageSize);
				}
			}
		} catch (MarketingToolException e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	private List<PurchasedMarketingToolResponsePojo> findAllSubscriptions(List<MarketingToolPurchaseHistory> list) {

		try {
			List<MarketingToolPurchaseHistory> collect = list.stream()
					.filter(e -> e.getSubscriptionType().equals(EMarketingToolType.INDIVIDUAL_PRICING.name()))
					.collect(Collectors.toList());
			list.removeAll(collect);
			List<PurchasedMarketingToolResponsePojo> result = new ArrayList<>();
			if (!collect.isEmpty()) {
				collect.forEach(e -> {
					Optional<AdminMarketingTools> findById = adminMarketingToolsRepository
							.findById(e.getSubscriptionTypeId());
					if (findById.isPresent()) {
						PurchasedMarketingToolResponsePojo responsePojo = new PurchasedMarketingToolResponsePojo();
						BeanUtils.copyProperties(e, responsePojo);
						responsePojo.setSubscriptionTitle(findById.get().getAdminMarketingToolName());
						result.add(responsePojo);
					}
				});
			}
			if (!list.isEmpty()) {
				list.forEach(e -> {
					Optional<AdminMarketingToolsCampaign> findById = adminMarketingToolsCampaignRepository
							.findById(e.getSubscriptionTypeId());
					if (findById.isPresent()) {
						PurchasedMarketingToolResponsePojo responsePojo = new PurchasedMarketingToolResponsePojo();
						BeanUtils.copyProperties(e, responsePojo);
						responsePojo.setSubscriptionTitle(findById.get().getTitle());
						result.add(responsePojo);
					}
				});
			}
			return result;
		} catch (Exception e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	private List<?> getPaginatedResponse(List<?> storeCoupons, int pageNumber, int pageSize) {
		if (pageNumber == 0) {
			return storeCoupons.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return storeCoupons.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@Override
	public UserMarketingTool userMarketingToolUpdate(UserMarketingToolUpdatePojo data) {
		try {
			UserMarketingTool findById = userMarketingToolRepository.findByMarketingToolIdAndToolStatusAndIsDelete(
					data.getMarketingToolId(), data.getToolStatus(), data.isDelete());
			if (findById != null) {
				findById.setCampaignTitle(data.getCampaignTitle());
				findById.setDescription(data.getDescription());
				return userMarketingToolRepository.save(findById);
			} else {
				throw new MarketingToolNotFound(MARKETING_TOOL_NOT_FOUND);
			}
		} catch (MarketingToolNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserMarketingToolMainResponsePojo getMarketingTool(String userTypeId, EMarketingTools toolType,
			int pageNumber, int pageSize) {
		try {
			UserMarketingToolMainResponsePojo userMarketingToolMainResponsePojo = new UserMarketingToolMainResponsePojo();
			ActiveMarketingToolSubscription activeMarketingToolSubscription = activeMarketingToolSubscriptionRepository
					.findBySubscriptionByIdAndMarketingToolName(userTypeId, toolType.name());

			userMarketingToolMainResponsePojo
					.setSubscriptionStartDateTime(activeMarketingToolSubscription.getActivatedAt());

			userMarketingToolMainResponsePojo
					.setSubscriptionEndDateTime(activeMarketingToolSubscription.getExpirationDate());

			List<UserMarketingTool> findByUserTypeIdAndToolType = userMarketingToolRepository
					.findByUserTypeIdAndToolTypeAndIsDelete(userTypeId, toolType, false);
			List<UserMarketingToolResponsePojo> userMarketingToolResponsePojos = new ArrayList<>();
			findByUserTypeIdAndToolType.forEach(e -> {
				UserMarketingToolResponsePojo userMarketingToolResponsePojo = new UserMarketingToolResponsePojo();
				BeanUtils.copyProperties(e, userMarketingToolResponsePojo);
				userMarketingToolResponsePojo.setCategory(productCategoryClientService
						.getMainCategory(e.getMainCategoryId()).getBody().getData().getMainCategoryName());
				userMarketingToolResponsePojo.setSubCategory(productCategoryClientService
						.getSubCategory(e.getSubCategoryId()).getBody().getData().getSubCategoryName());
				userMarketingToolResponsePojo.setCustomerMarketingToolList(e.getCustomerMarketingToolList());
				userMarketingToolResponsePojo.setLeadResellerMarketingToolList(e.getLeadResellerMarketingToolList());
				userMarketingToolResponsePojo
						.setMarketingToolQuestionAnswerList(e.getMarketingToolQuestionAnswerList());
				userMarketingToolResponsePojo.setSplitDiscountDetailList(e.getSplitDiscountDetailList());
				List<MasterProductIdAndVariationIdPojo> masterProductIdAndVariationIdPojos = new ArrayList<>();
				e.getMarketingToolProductList().forEach(f -> {
					MasterProductIdAndVariationIdPojo masterProductIdAndVariationIdPojo = new MasterProductIdAndVariationIdPojo();
					BeanUtils.copyProperties(f, masterProductIdAndVariationIdPojo);
					masterProductIdAndVariationIdPojos.add(masterProductIdAndVariationIdPojo);
				});
				userMarketingToolResponsePojo.setMarketingToolProductList(productCategoryClientService
						.getProductListByProductIdAndVariationId(masterProductIdAndVariationIdPojos).getBody()
						.getData());
				userMarketingToolResponsePojos.add(userMarketingToolResponsePojo);
			});
			userMarketingToolMainResponsePojo.setMarketingToolResponsePojo(
					(List<UserMarketingToolResponsePojo>) getPaginatedResponse(userMarketingToolResponsePojos,
							pageNumber, pageSize));
			return userMarketingToolMainResponsePojo;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MarketingToolPurchaseHistory> purchaseMarketingTool(PurchaseMarketingToolPojo marketingTool) {

		try {
			if (marketingTool.getSubscriptionType() == (EMarketingToolType.INDIVIDUAL_PRICING)) {

				AdminMarketingTools adminMarketingTools = findEnabledMarketingTools(marketingTool);

				return saveSubscriptions(adminMarketingTools, marketingTool);

			} else if (marketingTool.getSubscriptionType() == (EMarketingToolType.TOOL_CAMPAIGN)) {

				AdminMarketingToolsCampaign toolsCampaign = findEnabledToolCampaign(marketingTool);

				List<MarketingToolPurchaseHistory> result = new ArrayList<>();

				toolsCampaign.getAdminMarketingTools().forEach(e -> {
					List<MarketingToolPurchaseHistory> saveSubscriptions = saveSubscriptions(e, marketingTool);
					result.addAll(saveSubscriptions);
				});
				return result;

			} else {
				throw new MarketingToolStatusException(INVALID_TOOL_TYPE);
			}
		} catch (MarketingToolStatusException | AdminMarketingToolsCampaignException
				| AdminMarketingToolNotFoundException | PurchaseHistorySaveFailedException
				| ActiveMarketingToolSubscriptionSaveFailedException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}

	}

	private List<MarketingToolPurchaseHistory> saveSubscriptions(AdminMarketingTools e,
			PurchaseMarketingToolPojo marketingTool) {

		ActiveMarketingToolSubscription marketingToolSubscription = findEnabledActiveSubscription(marketingTool,
				e.getAdminMarketingToolName());

		List<MarketingToolPurchaseHistory> result = new ArrayList<>();

		if (marketingToolSubscription != null) {
			MarketingToolPurchaseHistory history = saveMarketingToolPurchaseHistory(marketingTool, true, e);
			result.add(history);
			return result;
		} else {
			saveActiveMarketingToolSubscription(e, marketingTool);
			result.add(saveMarketingToolPurchaseHistory(marketingTool, false, e));
			return result;
		}
	}

	private ActiveMarketingToolSubscription findEnabledActiveSubscription(PurchaseMarketingToolPojo marketingTool,
			String adminMarketingToolName) {
		return activeMarketingToolSubscriptionRepository
				.findBySubscriptionByTypeAndSubscriptionByIdAndMarketingToolNameAndIsDisabled(
						marketingTool.getPurchasedByType(), marketingTool.getPurchasedById(), adminMarketingToolName,
						false);
	}

	private MarketingToolPurchaseHistory saveMarketingToolPurchaseHistory(PurchaseMarketingToolPojo marketingTool,
			boolean status, AdminMarketingTools adminMarketingTools) {
		try {
			if (status) {
				MarketingToolPurchaseHistory marketingToolPurchaseHistory = new MarketingToolPurchaseHistory();
				BeanUtils.copyProperties(marketingTool, marketingToolPurchaseHistory);
				marketingToolPurchaseHistory.setSubscriptionType(marketingTool.getSubscriptionType().name());
				marketingToolPurchaseHistory.setActivatedAt(null);
				marketingToolPurchaseHistory.setAdminMarketingToolName(adminMarketingTools.getAdminMarketingToolName());
				marketingToolPurchaseHistory.setDisabled(false);
				marketingToolPurchaseHistory.setExpirationDate(null);
				marketingToolPurchaseHistory.setOrderId(null);
				marketingToolPurchaseHistory.setDays(adminMarketingTools.getDays());
				marketingToolPurchaseHistory.setToolStatus(EMarketingToolStatus.PENDING);
				marketingToolPurchaseHistory.setPurchasedAt(LocalDateTime.now());
				marketingToolPurchaseHistory.setSubscriptionAmount(adminMarketingTools.getPrice());
				return purchasedMarketingToolsRepository.save(marketingToolPurchaseHistory);
			} else {
				MarketingToolPurchaseHistory marketingToolPurchaseHistory = new MarketingToolPurchaseHistory();
				BeanUtils.copyProperties(marketingTool, marketingToolPurchaseHistory);
				marketingToolPurchaseHistory.setSubscriptionType(marketingTool.getSubscriptionType().name());
				marketingToolPurchaseHistory.setActivatedAt(LocalDateTime.now());
				marketingToolPurchaseHistory.setAdminMarketingToolName(adminMarketingTools.getAdminMarketingToolName());
				marketingToolPurchaseHistory.setDisabled(false);
				marketingToolPurchaseHistory.setExpirationDate(
						LocalDateTime.now().plusDays(Integer.parseInt(adminMarketingTools.getDays().split(" ")[0])));
				marketingToolPurchaseHistory.setOrderId(null);
				marketingToolPurchaseHistory.setDays(adminMarketingTools.getDays());
				marketingToolPurchaseHistory.setPurchasedAt(LocalDateTime.now());
				marketingToolPurchaseHistory.setToolStatus(EMarketingToolStatus.ACTIVE);
				marketingToolPurchaseHistory.setSubscriptionAmount(adminMarketingTools.getPrice());
				return purchasedMarketingToolsRepository.save(marketingToolPurchaseHistory);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new PurchaseHistorySaveFailedException(PURCHASE_HISTORY_SAVE_FAILED);
		}
	}

	private void saveActiveMarketingToolSubscription(AdminMarketingTools adminMarketingTools,
			PurchaseMarketingToolPojo marketingTool) {
		try {
			ActiveMarketingToolSubscription activeMarketingToolSubscription = new ActiveMarketingToolSubscription();
			activeMarketingToolSubscription.setActivatedAt(LocalDateTime.now());
			activeMarketingToolSubscription.setDisabled(false);
			activeMarketingToolSubscription.setDays(adminMarketingTools.getDays());
			activeMarketingToolSubscription.setExpirationDate(
					LocalDateTime.now().plusDays(Integer.parseInt(adminMarketingTools.getDays().split(" ")[0])));
			activeMarketingToolSubscription.setMarketingToolName(adminMarketingTools.getAdminMarketingToolName());
			activeMarketingToolSubscription.setSubscriptionById(marketingTool.getPurchasedById());
			activeMarketingToolSubscription.setSubscriptionByType(marketingTool.getPurchasedByType());
			activeMarketingToolSubscription.setSubscriptionId(marketingTool.getSubscriptionTypeId());
			activeMarketingToolSubscription.setSubscriptionStatus(EMarketingToolStatus.ACTIVE);
			activeMarketingToolSubscriptionRepository.save(activeMarketingToolSubscription);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ActiveMarketingToolSubscriptionSaveFailedException(ACTIVE_SUBSCRIPTION_FAILED);
		}

	}

	private AdminMarketingToolsCampaign findEnabledToolCampaign(PurchaseMarketingToolPojo marketingTool) {
		AdminMarketingToolsCampaign toolsCampaign = adminMarketingToolsCampaignRepository
				.findByAdminMarketingToolsCampaignIdAndStoreTypeAndIsDisabled(marketingTool.getSubscriptionTypeId(),
						marketingTool.getPurchasedByType(), false);
		if (toolsCampaign != null) {
			return toolsCampaign;
		} else {
			throw new AdminMarketingToolsCampaignException(MARKETING_TOOL_CAMPAIGN_NOT_FOUND);
		}
	}

	private AdminMarketingTools findEnabledMarketingTools(PurchaseMarketingToolPojo marketingTool) {
		AdminMarketingTools adminMarketingTools = adminMarketingToolsRepository
				.findByAdminMarketingToolIdAndStoreTypeAndIsDisabled(marketingTool.getSubscriptionTypeId(),
						marketingTool.getPurchasedByType(), false);
		if (adminMarketingTools == null) {
			throw new AdminMarketingToolNotFoundException(MARKETING_TOOL_NOT_FOUND);
		} else {
			return adminMarketingTools;
		}
	}

	@Override
	public MarketingToolStatusWrapper validateMarketingToolStatus(String supplierId) {
		try {
			List<ActiveMarketingToolSubscription> list = activeMarketingToolSubscriptionRepository
					.findBySubscriptionById(supplierId);
			if (list.isEmpty()) {
				return new MarketingToolStatusWrapper();
			} else {
				List<String> collect = list.stream().map(ActiveMarketingToolSubscription::getMarketingToolName)
						.collect(Collectors.toList());
				MarketingToolStatusWrapper wrapper = new MarketingToolStatusWrapper();
				List<String> arrayList = new ArrayList<>();

				collect.forEach(e -> {
					switch (e) {
					case "DISCOUNT_COUPON":
						arrayList.add("creatediscountcoupons");
						break;
					case "TODAYS_DEAL":
						arrayList.add("createtodaysdeals");
						break;
					case "QUIZ":
						arrayList.add("createquiz");
						break;
					case "SPIN_WHEEL":
						arrayList.add("spinwheel");
						break;
					case "SCRATCH_CARD":
						arrayList.add("scratchcard");
						break;
					case "PRICE_TARGETED":
						arrayList.add("shareproductbyprice");
						break;
					case "NOTIFICATIONS":
						arrayList.add("notification");
						break;
					case "FLAGS":
						arrayList.add("flags");
						break;
					default:
						break;
					}
				});
				wrapper.setUnlockedTools(arrayList);
				return wrapper;
			}
		} catch (Exception e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}
}
