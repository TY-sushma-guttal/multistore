package com.mrmrscart.helpandsupportservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.helpandsupportservice.entity.ETicketStatus;
import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;

@Repository
public interface HelpSupportRepository extends JpaSpecificationExecutor<HelpSupport>, JpaRepository<HelpSupport, Long> {

	public List<HelpSupport> findByUserToIdAndUserToTypeAndIssueTypeContaining(String userId, EUserType userType,
			String keyword);

	public List<HelpSupport> findByUserToIdAndUserToTypeAndTicketStatusContaining(String userId, EUserType userType,
			ETicketStatus valueOf);

	public List<HelpSupport> findByUserToIdAndUserToType(String userId, EUserType userType);

	public HelpSupport findByTicketIdAndIsDeleted(Long ticketId, boolean b);

	public List<HelpSupport> findByUserFromTypeAndIsDeleted(EUserType userType, boolean b);

	@Query("SELECT h FROM HelpSupport h WHERE  h.userFromId=?1 AND h.isDeleted=?2 AND CONCAT(h.ticketId, '') LIKE %?3%")
	public List<HelpSupport> filterUserTickets(String userId, boolean b, String keyword);

	@Query("SELECT h FROM HelpSupport h WHERE h.userToId=?1 AND h.userFromType=?2 AND h.isDeleted=?3 AND CONCAT(h.ticketId, '') LIKE %?4%")
	public List<HelpSupport> filterAdminTickets(String userId, EUserType admin, boolean b, String keyword);

	@Query("SELECT h FROM HelpSupport h WHERE h.userFromType=?1 AND h.userToId=?2 AND h.isDeleted=?3 AND CONCAT(h.ticketId, '') LIKE %?4%")
	public List<HelpSupport> filterCustomerTickets(EUserType customer, String userId, boolean b, String keyword);

	public List<HelpSupport> findByUserFromTypeAndUserToIdAndIsDeleted(EUserType customer, String userId, boolean b);

	public List<HelpSupport> findByUserFromType(EUserType userFromType);

	public List<HelpSupport> findByUserFromTypeAndTicketStatus(EUserType userFromType, String ticketStatus);

	public List<HelpSupport> findByUserFromIdAndIsDeletedAndIssueTypeIgnoreCaseContaining(String userId, boolean b,
			String keyword);

	public List<HelpSupport> findByUserToIdAndUserFromTypeAndIsDeletedAndIssueTypeIgnoreCaseContaining(String userId,
			EUserType admin, boolean b, String keyword);

	public List<HelpSupport> findByUserFromIdAndIsDeleted(String userId, boolean b);

	public List<HelpSupport> findByUserToIdAndUserFromTypeAndIsDeleted(String userId, EUserType admin, boolean b);

	public List<HelpSupport> findByUserFromIdAndIsDeletedAndTicketStatusIgnoreCaseContaining(String userId, boolean b,
			String keyword);

	public List<HelpSupport> findByUserToIdAndUserFromTypeAndIsDeletedAndTicketStatusIgnoreCaseContaining(String userId,
			EUserType admin, boolean b, String keyword);

	public List<HelpSupport> findByUserFromTypeAndUserToIdAndIsDeletedAndIssueTypeIgnoreCaseContaining(
			EUserType customer, String userId, boolean b, String keyword);

	public List<HelpSupport> findByUserFromTypeAndUserToIdAndIsDeletedAndTicketStatusIgnoreCaseContaining(
			EUserType customer, String userId, boolean b, String keyword);

}
