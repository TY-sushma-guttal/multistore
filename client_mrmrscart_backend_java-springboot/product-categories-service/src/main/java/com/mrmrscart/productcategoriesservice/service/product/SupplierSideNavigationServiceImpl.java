package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.product.MasterProductConstant.SOMETHING_WENT_WRONG;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmrscart.productcategoriesservice.entity.product.SupplierSideNavigation;
import com.mrmrscart.productcategoriesservice.exception.product.MasterProductException;
import com.mrmrscart.productcategoriesservice.repository.product.SupplierSideNavigationRepository;

@Service
public class SupplierSideNavigationServiceImpl implements SupplierSideNavigationService {

	@Autowired
	private SupplierSideNavigationRepository supplierSideNavigationRepository;

	@Transactional
	@Override
	public List<SupplierSideNavigation> saveSideNavigationInfo(List<SupplierSideNavigation> navigationPojo) {
		try {
			return supplierSideNavigationRepository.saveAll(navigationPojo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SupplierSideNavigation> getSideNavigationInfo(String userType) {
		try {
			return supplierSideNavigationRepository.findAll();
		} catch (Exception e) {
			throw new MasterProductException(SOMETHING_WENT_WRONG);
		}
	}

}
