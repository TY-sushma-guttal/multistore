package com.mrmrscart.userservice.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mrmrscart.userservice.entity.admin.Banner;
import com.mrmrscart.userservice.entity.admin.EBannerStatus;
import com.mrmrscart.userservice.entity.supplier.CouponInfo;
import com.mrmrscart.userservice.entity.supplier.EProductCoupon;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreCoupon;
import com.mrmrscart.userservice.repository.admin.BannerRepository;
import com.mrmrscart.userservice.repository.supplier.CouponInfoRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreCouponRepository;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	@Autowired
	CouponInfoRepository couponInfoRepository;
	@Autowired
	private SupplierStoreCouponRepository supplierStoreCouponRepository;
	@Autowired
	private BannerRepository bannerRepository;

	@Scheduled(cron = "0 0 0 * * *")
	public void checkExpiry() {
		checkCouponExpiry();
		checkSupplierStoreCouponExpiry();
	}

	@Scheduled(cron = "0 0 * * * *")
	public void hourlyScheduler() {
		publishExpireBanner();

	}

	private void checkSupplierStoreCouponExpiry() {
		List<EProductCoupon> arrayList = new ArrayList<>();
		arrayList.add(EProductCoupon.PUBLISHED);
		arrayList.add(EProductCoupon.DRAFT);

		List<SupplierStoreCoupon> supplierStoreCoupons = supplierStoreCouponRepository.findByCouponStatusIn(arrayList);

		List<SupplierStoreCoupon> list = supplierStoreCoupons.stream()
				.filter(e -> LocalDate.now().compareTo(e.getExpirationDate()) > 0).collect(Collectors.toList());
		if (!list.isEmpty()) {
			list.forEach(f -> {
				f.setCouponStatus(EProductCoupon.EXPIRED.name());
				supplierStoreCouponRepository.save(f);
			});
		}
	}

	private void checkCouponExpiry() {

		List<CouponInfo> findAll = couponInfoRepository.findAll();
		List<CouponInfo> list = findAll.stream().filter(e -> LocalDate.now().compareTo(e.getCouponExpiryDate()) > 0)
				.collect(Collectors.toList());
		if (!list.isEmpty()) {
			list.forEach(f -> {
				f.setCouponStatus(EProductCoupon.EXPIRED.name());
				couponInfoRepository.save(f);
			});
		}
	}

	private void publishExpireBanner() {
		List<EBannerStatus> arrayList = new ArrayList<>();
		arrayList.add(EBannerStatus.DRAFT);
		arrayList.add(EBannerStatus.PUBLISHED);

		List<Banner> banners = bannerRepository.findByIsDeleteAndStatusIn(false, arrayList);

		List<Banner> list = banners.stream().filter(e -> LocalDateTime.now().compareTo(e.getStartDateTime()) > 0)
				.collect(Collectors.toList());
		if (!list.isEmpty()) {
			list.forEach(e -> {
				e.setStatus(EBannerStatus.PUBLISHED);
				bannerRepository.save(e);
			});
		}
		banners.removeAll(list);

		List<Banner> collect = banners.stream().filter(f -> LocalDateTime.now().compareTo(f.getEndDateTime()) > 0)
				.collect(Collectors.toList());

		if (!collect.isEmpty()) {
			collect.forEach(e -> {
				e.setStatus(EBannerStatus.EXPIRED);
				bannerRepository.save(e);
			});
		}
	}

}
