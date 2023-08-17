package com.mrmrscart.userservice.util;

import static com.mrmrscart.userservice.common.admin.MediaConstant.UPLOAD_FAILURE;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.mrmrscart.userservice.exception.admin.FailedToUploadException;

public class SSSFileUpload {

	/**
	 * This is the end point url for amazon s3 bucket
	 */
	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	/**
	 * This is the bucketName for amazon s3 bucket
	 */
	@Value("${amazonProperties.bucketName}")
	public String bucketName;

	/**
	 * This enables automatic dependency injection of AmazonS3 interface. This
	 * object is used by methods in the SimpleProductServiceImplementation to call
	 * the respective methods.
	 */
	@Autowired
	private AmazonS3 s3client;

	public String uploadFile(String filePath, MultipartFile multipartFile) {
		String fileUrl = "";
		try {
			fileUrl = uploadFileTos3bucketConfig(filePath, multipartFile);
		} catch (Exception e) {
			throw new FailedToUploadException(UPLOAD_FAILURE);
		}
		return fileUrl;
	}

	public String uploadFileTos3bucketConfig(String filePath, MultipartFile file)
			throws AmazonServiceException, SdkClientException, IOException {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(file.getContentType());
		objectMetadata.setContentLength(file.getSize());
		s3client.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		return s3client.getUrl(bucketName, filePath).toString();
	}

	public void deleteS3Folder(String bucketName, String folderPath) {
		for (S3ObjectSummary file : s3client.listObjects(bucketName, folderPath).getObjectSummaries()) {
			s3client.deleteObject(bucketName, file.getKey());
		}
	}
}
