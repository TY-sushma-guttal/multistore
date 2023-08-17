package com.mrmrscart.helpandsupportservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageView;

@Repository
public interface HelpSupportMessageViewRepository extends JpaRepository<HelpSupportMessageView, Long> {

}
