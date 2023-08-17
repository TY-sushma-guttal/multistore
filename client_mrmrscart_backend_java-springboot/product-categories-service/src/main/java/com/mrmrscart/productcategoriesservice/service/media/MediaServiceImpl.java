package com.mrmrscart.productcategoriesservice.service.media;

import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.B2B_INVOICE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.BANNER;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.FILE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.FILE_NAME_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.FLAG;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.IMAGE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.PATH;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.PRODUCT;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.PROFILE_IMAGE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.STORE_IMAGE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.SUPPLIER;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.SUPPLIER_LOGO;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.TRADEMARK_INVOICE;
import static com.mrmrscart.productcategoriesservice.common.media.MediaConstant.UNEXPECTED_MEDIA_TYPE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.util.IOUtils;
import com.google.common.io.Files;
import com.mrmrscart.productcategoriesservice.exception.category.FailedToUploadException;
import com.mrmrscart.productcategoriesservice.exception.media.EmptyMediaException;
import com.mrmrscart.productcategoriesservice.exception.media.MediaException;
import com.mrmrscart.productcategoriesservice.exception.media.UnacceptedImageTypeException;
import com.mrmrscart.productcategoriesservice.pojo.product.ImgNameAndImgUrlPojo;
import com.mrmrscart.productcategoriesservice.util.SSSFileUpload;

@Service
public class MediaServiceImpl implements MediaService {

	@Autowired
	private SSSFileUpload sssObj;

	@Override
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
	public String uploadMedia(String moduleName, String moduleSection, String categoryType, MultipartFile media) {
		String generateFileName = generateFileName(media);
		String filePath = moduleName + PATH + moduleSection + PATH + categoryType + PATH + customCategoryIdGenerator()
				+ PATH + FILE + PATH + generateFileName;
		return sssObj.uploadFile(filePath, media);
	}

	@Override
	public List<String> uploadProductMedia(String supplierId, List<MultipartFile> medias) {
		List<String> urls = new ArrayList<>();
		medias.forEach(e -> {
			String generateFileName = generateFileName(e);
			String fileExtension = Files.getFileExtension(generateFileName);
			if (fileExtension.equals("jpeg") || fileExtension.equals("jpg") || fileExtension.equals("png")) {
				String filePath = SUPPLIER + PATH + supplierId + PATH + PRODUCT + PATH + IMAGE + PATH
						+ generateFileName;
				urls.add(sssObj.uploadFile(filePath, e));
			} else {
				String filePath = SUPPLIER + PATH + supplierId + PATH + PRODUCT + PATH + FILE + PATH + generateFileName;
				urls.add(sssObj.uploadFile(filePath, e));
			}
		});
		return urls;
	}

	@Override
	public String uploadFlagMedia(MultipartFile media, String supplierId) {
		String generateFileName = generateFileName(media);
		String filePath = SUPPLIER + PATH + supplierId + PATH + FLAG + PATH + IMAGE + PATH + generateFileName;
		return sssObj.uploadFile(filePath, media);
	}

	@Override
	public String sendUrlGetBaseUrl(String data) throws IOException {
		try {
			URL url = new URL(data);
			String fileNameWithExtension = url.getFile();
			String fileExtension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf("."));
			String[] ar = fileExtension.split("\\.");
			String extension = ar[1];

			if (!extension.equalsIgnoreCase("jpg") && !extension.equalsIgnoreCase("png")
					&& !extension.equalsIgnoreCase("jpeg")) {
				throw new UnacceptedImageTypeException(UNEXPECTED_MEDIA_TYPE);
			}
			InputStream is = url.openStream();
			byte[] bytes = IOUtils.toByteArray(is);
			return org.apache.tomcat.util.codec.binary.Base64.encodeBase64String(bytes);
		} catch (UnacceptedImageTypeException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MediaException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<String> uploadInvoiceAndTrademarkMedia(String supplierId, List<MultipartFile> medias,
			String documentType) {

		List<String> urls = new ArrayList<>();

		medias.forEach(e -> {
			String generateFileName = generateFileName(e);
			String fileExtension = Files.getFileExtension(generateFileName);

			if (documentType.equalsIgnoreCase("B2B_INVOICE")) {
				if (fileExtension.equals("jpeg") || fileExtension.equals("jpg") || fileExtension.equals("png")) {
					String filePath = SUPPLIER + PATH + supplierId + PATH + B2B_INVOICE + PATH + IMAGE + PATH
							+ generateFileName;
					urls.add(sssObj.uploadFile(filePath, e));
				} else {
					String filePath = SUPPLIER + PATH + supplierId + PATH + B2B_INVOICE + PATH + FILE + PATH
							+ generateFileName;
					urls.add(sssObj.uploadFile(filePath, e));
				}
			} else if (documentType.equalsIgnoreCase("TRADEMARK_LETTER")) {
				if (fileExtension.equals("jpeg") || fileExtension.equals("jpg") || fileExtension.equals("png")) {
					String filePath = SUPPLIER + PATH + supplierId + PATH + TRADEMARK_INVOICE + PATH + IMAGE + PATH
							+ generateFileName;
					urls.add(sssObj.uploadFile(filePath, e));
				} else {
					String filePath = SUPPLIER + PATH + supplierId + PATH + TRADEMARK_INVOICE + PATH + FILE + PATH
							+ generateFileName;
					urls.add(sssObj.uploadFile(filePath, e));
				}
			} else {
				throw new MediaException("Provide A Valid Document Type");
			}
		});
		return urls;
	}

	@Override
	public String deleteImageObjectByUrl(String url) {
		String[] getActualUrl = url.split(".com/");
		sssObj.deleteS3Folder(sssObj.bucketName, getActualUrl[1]);
		return "Image deleted successfully";
	}

	@Override
	public List<ImgNameAndImgUrlPojo> uploadAsste(MultipartFile[] imageList) {
		try {
			List<ImgNameAndImgUrlPojo> imageUrlList = new ArrayList<>();
			for (MultipartFile media : imageList) {
				String fileName = media.getOriginalFilename();
				String filePath = "asset" + PATH + fileName;
				ImgNameAndImgUrlPojo imageObj = new ImgNameAndImgUrlPojo();
				imageObj.setFileName(fileName);
				imageObj.setFileUrl(sssObj.uploadFile(filePath, media));
				imageUrlList.add(imageObj);
			}
			return imageUrlList;
		} catch (Exception e) {
			throw new MediaException(SOMETHING_WENT_WRONG);
		}
	}

	public List<String> uploadStoreConfigurationImages(MultipartFile supplierLogo, MultipartFile storeImage,
			String supplierId) {

		String uploadFile = null;
		String file = null;
		if (supplierLogo != null) {
			String generateFileName = generateFileName(supplierLogo);
			String filePath = SUPPLIER + PATH + supplierId + PATH + SUPPLIER_LOGO + PATH + generateFileName;
			uploadFile = sssObj.uploadFile(filePath, supplierLogo);
		}
		if (storeImage != null) {
			String fileName = generateFileName(storeImage);
			String path = SUPPLIER + PATH + supplierId + PATH + STORE_IMAGE + PATH + fileName;
			file = sssObj.uploadFile(path, storeImage);
		}
		return Arrays.asList(uploadFile, file);
	}

	@Override
	public String uploadSupplierProfileImage(MultipartFile profileImage, String supplierId) {
		String generateFileName = generateFileName(profileImage);
		String filePath = SUPPLIER + PATH + supplierId + PATH + PROFILE_IMAGE + PATH + generateFileName;
		return sssObj.uploadFile(filePath, profileImage);
	}

	@Override
	public String uploadBannerImage(MultipartFile media, String userId) {
		String generateFileName = generateFileName(media);
		String filePath = SUPPLIER + PATH + userId + PATH + BANNER + PATH + generateFileName;
		return sssObj.uploadFile(filePath, media);
	}

	@Override
	public List<String> getImageUrlFromBaseUrl(List<String> baseUrlList, String supplierId) {
		List<String> imageUrlList = new ArrayList<>();
		for (String baseUrl : baseUrlList) {
			String[] imageData = baseUrl.split(";");
			String[] imageInfo = imageData[0].split(":");
			String filePath = SUPPLIER + PATH + supplierId + PATH + PRODUCT + PATH + IMAGE + PATH
					+ customCategoryIdGenerator()+PATH+imageInfo[1].split("/")[0]+"."+imageInfo[1].split("/")[1];
			String imageExtension = imageInfo[1].split("/")[1];
			byte[] imageByteStream = org.apache.commons.codec.binary.Base64
					.decodeBase64((baseUrl.substring(baseUrl.indexOf(",") + 1)).getBytes());
			InputStream inputStream = new ByteArrayInputStream(imageByteStream);
			imageUrlList.add(sssObj.uploadBase64ToS3Bucket(imageByteStream, inputStream, filePath, imageExtension));
		}
		return imageUrlList;
	}

	@Override
	public List<String> uploadHelpSupportUrl(MultipartFile[] imageList, String ticketCreatedByType) {
		try {
			if (imageList.length != 0) {
				List<String> urls = new ArrayList<>();
				for (MultipartFile media : imageList) {
					String filePath = ticketCreatedByType + PATH + "help_support" + PATH + IMAGE + PATH
							+ customCategoryIdGenerator() + PATH + generateFileName(media);
					urls.add(sssObj.uploadFile(filePath, media));
				}
				return urls;
			} else {
				throw new EmptyMediaException("Media List Can't Be Empty");
			}
		} catch (EmptyMediaException e) {
			throw e;
		} catch (Exception e) {
			throw new MediaException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<String> uploadNotificationMedia(MultipartFile[] file, String supplierId) {

		try {
			if (file.length != 0) {
				List<String> urls = new ArrayList<>();
				for (MultipartFile media : file) {
					String filePath = supplierId + PATH + "notification" + PATH + FILE + PATH + generateFileName(media);
					urls.add(sssObj.uploadFile(filePath, media));
				}
				return urls;
			} else {
				throw new EmptyMediaException("Media List Can't Be Empty");
			}
		} catch (EmptyMediaException e) {
			throw e;
		} catch (Exception e) {
			throw new MediaException(SOMETHING_WENT_WRONG);
		}
	}
}
