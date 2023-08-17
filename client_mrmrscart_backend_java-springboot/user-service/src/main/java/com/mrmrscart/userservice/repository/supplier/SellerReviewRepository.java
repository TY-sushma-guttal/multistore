package com.mrmrscart.userservice.repository.supplier;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.SellerReviews;

@Repository
public interface SellerReviewRepository extends JpaRepository<SellerReviews, Long> {

	@Query("SELECT s FROM SellerReviews s WHERE s.sellerReviewsId IN (?1) AND CONCAT(s.sellerRatings, '') LIKE %?2%")
	List<SellerReviews> filterReviewByRatings(List<Long> reviewId, String keyword);

}
