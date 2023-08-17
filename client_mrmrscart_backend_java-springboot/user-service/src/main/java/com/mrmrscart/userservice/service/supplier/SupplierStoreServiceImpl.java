package com.mrmrscart.userservice.service.supplier;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;

import java.util.Optional;

import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.supplier.InactiveSupplierStoreException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.exception.supplier.UserIdNotFoundException;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreInfoPojo;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreInfoRepository;
import com.mrmrscart.userservice.util.SSSFileUpload;

@Service
public class SupplierStoreServiceImpl implements SupplierStoreService {

	@Autowired
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private SSSFileUpload sssFileUpload;

	@Transactional
	@Override
	public SupplierStoreInfo updateSupplierStoreConfiguration(SupplierStoreInfoPojo storeInfoPojo) {

		try {
			SupplierStoreInfo storeInfo = supplierStoreInfoRepository
					.findBySupplierStoreInfoIdAndIsStoreActive(storeInfoPojo.getSupplierStoreInfoId(), true);

			if (storeInfo != null) {

//				if (!storeInfo.getShopDescriptionImageUrl().equals(storeInfoPojo.getShopDescriptionImageUrl())) {
//					sssFileUpload.deleteS3Folder(sssFileUpload.bucketName, storeInfo.getShopDescriptionImageUrl());
//				}
//
//				if (!storeInfo.getSupplierStoreLogo().equals(storeInfoPojo.getSupplierStoreLogo())) {
//					sssFileUpload.deleteS3Folder(sssFileUpload.bucketName, storeInfo.getSupplierStoreLogo());
//				}

				BeanUtils.copyProperties(storeInfoPojo, storeInfo);
				storeInfo.setStoreThemes(storeInfoPojo.getStoreThemes());
				return supplierStoreInfoRepository.save(storeInfo);

			} else {
				throw new InactiveSupplierStoreException("Supplier Store Is Inactive");
			}
		} catch (InactiveSupplierStoreException | UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierStoreInfo getSupplierStoreConfiguration(String storeCode) {
		try {
			SupplierStoreInfo storeInfo = supplierStoreInfoRepository.findBySupplierStoreCodeAndIsStoreActive(storeCode,
					true);
			if (storeInfo != null) {
				return storeInfo;
			} else {
				throw new InactiveSupplierStoreException("Supplier Store Is Inactive");
			}
		} catch (InactiveSupplierStoreException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierStoreInfo updateProductCount(String supplierId, int productCount) {

		try {
			Optional<SupplierRegistration> findById = supplierRegistrationRepository.findById(supplierId);
			if (findById.isPresent()) {
				SupplierRegistration supplierRegistration = findById.get();

				SupplierStoreInfo supplierStoreInfo = findById.get().getSupplierStoreInfo();
				if (productCount == 50) {
					supplierStoreInfo.setStoreActive(true);
				} else if (productCount < 50) {
					supplierStoreInfo.setStoreActive(false);
				}
				supplierStoreInfo.setActiveProductCount(productCount);
				supplierRegistration.setSupplierStoreInfo(supplierStoreInfo);
				SupplierRegistration save = supplierRegistrationRepository.save(supplierRegistration);

				return save.getSupplierStoreInfo();
			} else {

				throw new SupplierIdNotFoundException(INVALID_SUPPLIER);
			}
		} catch (Exception e) {

			throw new SupplierIdNotFoundException(INVALID_SUPPLIER);
		}
	}
}
