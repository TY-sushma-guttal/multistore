package com.mrmrscart.notificationreportlogservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolPriceChange;

@Repository
public interface MarketingToolPriceChangeRepository extends JpaRepository<MarketingToolPriceChange, Long> {

	List<MarketingToolPriceChange> findByToolTypeAndToolIdIn(String type, List<Long> toolId);

}
