package com.mrmrscart.userservice.entity.reseller;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_marketing_tool_theme")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MarketingToolTheme extends Audit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long themeId;
	
	@Column(length = 255)
	private String imageUrl;
	
	@Convert(converter = ListToStringConverter.class)
	private List<String> colorCode;
	
	private String themeType;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "theme_id")
	private List<UserMarketingTool> marketingTools;
}
