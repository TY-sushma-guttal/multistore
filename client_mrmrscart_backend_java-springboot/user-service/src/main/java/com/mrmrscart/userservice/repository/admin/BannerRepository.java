package com.mrmrscart.userservice.repository.admin;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.userservice.entity.admin.Banner;
import com.mrmrscart.userservice.entity.admin.EBannerStatus;

public interface BannerRepository extends JpaRepository<Banner, Long> {

	public Banner findByBannerIdAndIsDelete(Long bannerId, boolean isDelete);
	
	public Banner findByBannerIdAndIsDeleteAndIsDisable(Long bannerId, boolean isDelete,boolean isDisable);

	public Banner findByBannerIdAndIsDeleteAndCreatedBy(Long bannerId, boolean idDelete, String createdById);

	public List<Banner> findByIsDelete(boolean isDelete);

	public List<Banner> findByCreatedDateBetweenAndCreatedByAndIsDelete(LocalDateTime fromDate, LocalDateTime toDate,
			String createdById, boolean isDelete);

	public List<Banner> findByIsDeleteAndStatusIn(boolean b, List<EBannerStatus> arrayList);

	public List<Banner> findByCreatedByAndIsDelete(String supplierId, boolean isDelete);
}
