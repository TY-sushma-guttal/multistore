package com.mrmrscart.helpandsupportservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessage;

@Repository
public interface HelpSupportMessageRepository extends JpaRepository<HelpSupportMessage, Long> {

}
