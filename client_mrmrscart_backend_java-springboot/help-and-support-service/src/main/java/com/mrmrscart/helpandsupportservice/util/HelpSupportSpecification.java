package com.mrmrscart.helpandsupportservice.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;

@Component
public class HelpSupportSpecification {

	private HelpSupportSpecification() {
	}

	public static Specification<HelpSupport> containsTicketStatus(List<String> ticketStatus) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("ticketStatus")).value(ticketStatus);
	}

	public static Specification<HelpSupport> containsIssueType(List<String> issueType) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("issueType")).value(issueType);
	}

	public static Specification<HelpSupport> containsTicketId(List<Long> ticketId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("ticketId")).value(ticketId);
	}

	public static Specification<HelpSupport> findByUserFromType(EUserType userType) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userFromType"), userType);
	}

	public static Specification<HelpSupport> findByIsDeleted(boolean b) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), b);
	}

	public static Specification<HelpSupport> findByUserToType(EUserType userType) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("userToType"), userType);
	}

	public static Specification<HelpSupport> findByFromDateAndToDate(LocalDateTime fromDate, LocalDateTime toDate) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("lastModifiedDate"), fromDate,
				toDate);
	}

}
