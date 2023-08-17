package com.mrmrscart.userservice.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;

@Component
public class StaffManagementInfoSpecification {
	
	private StaffManagementInfoSpecification() {
		
	}

	public static Specification<StaffManagementInfo> containsFirstName(String firstName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");

	}

	public static Specification<StaffManagementInfo> containsLastName(String lastName) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");

	}

	public static Specification<StaffManagementInfo> containsMobileNumber(String mobileNumber) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("mobileNumber"),
				"%" + mobileNumber + "%");

	}

	public static Specification<StaffManagementInfo> containsEmailId(String emailId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("emailId"), "%" + emailId + "%");

	}
	
	public static Specification<StaffManagementInfo> findBySupplierId(String supplierId){
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("supplierId"), supplierId);
	}
	
	public static Specification<StaffManagementInfo> findByIsDelete(boolean isDelete){
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDelete"), isDelete);
	}
}
