package com.mrmrscart.userservice.repository.supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.userservice.entity.supplier.UserBankDetails;

public interface UserBankDetailsRepository extends JpaRepository<UserBankDetails, Long> {

	public List<UserBankDetails> findByIsDeleted(boolean b);

	public UserBankDetails findByBankIdAndIsDeleted(Long bankId, boolean b);

}
