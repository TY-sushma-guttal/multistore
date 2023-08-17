package com.mrmrscart.userservice.service.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.Banner;
import com.mrmrscart.userservice.entity.admin.EBannerStatus;
import com.mrmrscart.userservice.exception.admin.BannerException;
import com.mrmrscart.userservice.exception.admin.BannerIdNotPresentException;
import com.mrmrscart.userservice.exception.admin.BannerListIsEmptyException;
import com.mrmrscart.userservice.pojo.admin.BannerPojo;
import com.mrmrscart.userservice.pojo.admin.BannersByDatePojo;
import com.mrmrscart.userservice.repository.admin.BannerRepository;
import com.mrmrscart.userservice.util.PaginatedResponse;
import com.mrmrscart.userservice.util.SSSFileUpload;

import static com.mrmrscart.userservice.common.admin.BannerConstant.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	private BannerRepository bannerRepository;

	@Autowired
	private PaginatedResponse paginatedResponse;

	@Autowired
	private SSSFileUpload sssObj;

	@Override
	@Transactional
	public Banner addBanner(BannerPojo data) {
		try {
			Banner newBanner = new Banner();
			BeanUtils.copyProperties(data, newBanner);
			newBanner.setStatus(EBannerStatus.ENABLED);
			return bannerRepository.save(newBanner);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Banner getBanner(Long id) {
		try {
			Banner banner = bannerRepository.findByBannerIdAndIsDelete(id, false);
			if (banner != null) {
				return banner;
			} else {
				throw new BannerIdNotPresentException(INVALID_BANNER_ID);
			}
		} catch (BannerIdNotPresentException e) {
			throw e;
		} catch (Exception e) {
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Banner deleteBanner(Long id) {
		try {
			Optional<Banner> getBanner = bannerRepository.findById(id);
			if (getBanner.isPresent()) {
				getBanner.get().setDelete(true);
				return bannerRepository.save(getBanner.get());
			} else {
				throw new BannerIdNotPresentException(INVALID_BANNER_ID);
			}
		} catch (BannerIdNotPresentException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<Banner> getAllBanners() {
		try {
			List<Banner> getAllBanners = bannerRepository.findAll();
			if (!getAllBanners.isEmpty()) {
				return getAllBanners;
			} else {
				throw new BannerListIsEmptyException(EMPTY_BANNER_LIST);
			}
		} catch (BannerListIsEmptyException e) {
			throw e;
		} catch (Exception e) {
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Banner> getAllBannersByDate(BannersByDatePojo data) {
		try {
			if (data.getFromDate() != null && data.getToDate() != null) {
				return (List<Banner>) paginatedResponse
						.getPaginatedResponse(
								bannerRepository.findByCreatedDateBetweenAndCreatedByAndIsDelete(data.getFromDate(),
										data.getToDate(), data.getCreatedById(),false),
								data.getPageNumber(), data.getPageSize());
			} else {
				return (List<Banner>) paginatedResponse.getPaginatedResponse(
						bannerRepository.findByCreatedByAndIsDelete(data.getCreatedById(), false), data.getPageNumber(),
						data.getPageSize());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<Banner> getAllSupplierBanners(String supplierId) {
		try {
			List<Banner> findByCreatedByAndIsDelete = bannerRepository.findByCreatedByAndIsDelete(supplierId, false);
			if (!findByCreatedByAndIsDelete.isEmpty()) {
				return findByCreatedByAndIsDelete;
			} else {
				throw new BannerListIsEmptyException(EMPTY_BANNER_LIST);
			}
		} catch (BannerListIsEmptyException e) {
			throw e;
		} catch (Exception e) {
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public Banner updateBanner(BannerPojo data, String userId) {
		try {
			Banner getBanner = bannerRepository.findByBannerIdAndIsDeleteAndCreatedBy(data.getBannerId(), false,
					userId);
			if (getBanner != null) {
				if (getBanner.getBannerImageUrlForWeb() != null && !getBanner.getBannerImageUrlForWeb().equals("")) {
					String path = getBanner.getBannerImageUrlForWeb()
							.replace("https://dev-mrmrscart-assets.s3.ap-south-1.amazonaws.com/", "");
					sssObj.deleteS3Folder(sssObj.bucketName, path);
				}
				if (getBanner.getBannerImageUrlForMobile() != null
						&& !getBanner.getBannerImageUrlForMobile().equals("")) {
					String path = getBanner.getBannerImageUrlForMobile()
							.replace("https://dev-mrmrscart-assets.s3.ap-south-1.amazonaws.com/", "");
					sssObj.deleteS3Folder(sssObj.bucketName, path);
				}
				BeanUtils.copyProperties(data, getBanner);
				getBanner.setStatus(EBannerStatus.UPDATED);
				return getBanner;
			} else {
				throw new BannerIdNotPresentException(INVALID_BANNER_ID);
			}
		} catch (BannerIdNotPresentException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public int disableBanner(Long id,boolean isDisable) {
		try {
			Banner fetchedBanner = bannerRepository.findByBannerIdAndIsDelete(id, false);
			if(fetchedBanner!=null) {
				if(isDisable) {
					fetchedBanner.setStatus(EBannerStatus.DISABLED);
					fetchedBanner.setDisable(isDisable);
					bannerRepository.save(fetchedBanner);
					return 0;
				}else {
					fetchedBanner.setDisable(false);
					fetchedBanner.setStatus(EBannerStatus.ENABLED);
					bannerRepository.save(fetchedBanner);
					return 1;
				}
			}else {
				throw new BannerIdNotPresentException(INVALID_BANNER_ID);
			}
		}catch(BannerIdNotPresentException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			throw new BannerException(SOMETHING_WENT_WRONG);
		}
	}
}
