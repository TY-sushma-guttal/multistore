package com.mrmrscart.notificationreportlogservice.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.notificationreportlogservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_supplier_changes_history")
@EqualsAndHashCode(callSuper = false)
public class SupplierChangesHistory extends Audit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long changeHistoryId;
	@Column(length = 45)
	private String supplierId;
	private LocalDateTime updatedAt;
	@Column(length = 45)
	private String changedField;
	@Column(length = 100)
	private String changedValue;
	@Column(length = 100)
	private String oldValue;
	private boolean isApproved;
	private LocalDateTime approvedAt;
	private EStatus status;
	private long ticketId;
	private String queries;
	private String answers;
}
