package com.mrmrscart.productcategoriesservice.service.media;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mrmrscart.productcategoriesservice.pojo.product.ImgNameAndImgUrlPojo;

public interface MediaService {
	public String customCategoryIdGenerator();

	public String uploadMedia(String moduleName, String moduleSection, String categoryType, MultipartFile media);

	public List<String> uploadProductMedia(String supplierId, List<MultipartFile> medias);

	public String uploadFlagMedia(MultipartFile media, String supplierId);

	public String sendUrlGetBaseUrl(String url) throws IOException;

	public List<String> uploadInvoiceAndTrademarkMedia(String supplierId, List<MultipartFile> medias,
			String documentType);

	public String deleteImageObjectByUrl(String url);

	public List<ImgNameAndImgUrlPojo> uploadAsste(MultipartFile[] imageList);

	public List<String> uploadStoreConfigurationImages(MultipartFile supplierLogo, MultipartFile storeImage,
			String supplierId);

	public String uploadSupplierProfileImage(MultipartFile profileImage, String supplierId);

	public String uploadBannerImage(MultipartFile media, String userId);

	public List<String> getImageUrlFromBaseUrl(List<String> baseUrlList, String supplierId);

	public List<String> uploadHelpSupportUrl(MultipartFile[] imageList, String ticketCreatedByType);

	public List<String> uploadNotificationMedia(MultipartFile[] file, String supplierId);
}
