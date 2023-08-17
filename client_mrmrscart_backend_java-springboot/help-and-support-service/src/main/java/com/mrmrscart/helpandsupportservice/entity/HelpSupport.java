package com.mrmrscart.helpandsupportservice.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mrmrscart.helpandsupportservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Table(name ="mmc_help_support" )
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class HelpSupport extends Audit implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ticketId;
	
	@Column(length = 45)
	private String issueType;
	
	@Column(length = 45)
	private String orderId;
	
	@Column(length = 50)
	private String issueSubject;
	
	@Column(length = 20)
	private EUserType userFromType;
	
	@Column(length = 45)
	private String userFromId;
	
	@Column(length = 20)
	private EUserType userToType;
	
	@Column(length = 45)
	private String userToId;
	
	@Column
	private String ticketStatus;
	
	@Column(length = 45)
	private String forwardedToId;
	
	@Column(length = 20)
	private String forwardedToType;
	
	private boolean isDeleted;
	
	private String productVariationId;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ticket_id")
	private List<HelpSupportMessage> helpSupportMessages;
}



