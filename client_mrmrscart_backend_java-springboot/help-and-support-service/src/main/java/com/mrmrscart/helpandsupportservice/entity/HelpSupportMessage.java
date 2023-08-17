package com.mrmrscart.helpandsupportservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_help_support_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class HelpSupportMessage implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long messageId;
	
	@Column(length = 45)
	private String messageFromId;
	
	@Column(length = 20)
	private EUserType messageFromType;
	
	@Column(length = 255)
	private String message;
	
	private LocalDateTime messagedAt;
	
	private LocalDateTime updatedAt;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "message_id")
	private List<HelpSupportMessageMedia> helpSupportMessageMedias;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "message_id")
	private List<HelpSupportMessageView> helpSupportMessageViews;
}

