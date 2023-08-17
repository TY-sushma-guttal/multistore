package com.mrmrscart.helpandsupportservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageMedia;

@Repository
public interface HelpSupportMessageMediaRepository extends JpaRepository<HelpSupportMessageMedia, Long> {

}
