package com.mrmrscart.userservice.service.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.Banner;
import com.mrmrscart.userservice.pojo.admin.BannerPojo;
import com.mrmrscart.userservice.pojo.admin.BannersByDatePojo;

public interface BannerService {
	public Banner addBanner(BannerPojo data);

	public Banner getBanner(Long id);

	public Banner deleteBanner(Long id);

	public List<Banner> getAllBanners();

	public List<Banner> getAllBannersByDate(BannersByDatePojo data);

	public List<Banner> getAllSupplierBanners(String supplierId);

	public Banner updateBanner(BannerPojo data, String userId);
	
	public int disableBanner(Long id,boolean isDisable);
}
