
package com.mrmrscart.userservice.entity.reseller;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mrmrscart.userservice.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_marketing_tool_question_answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingToolQuestionAnswer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;

	@Column(length = 90)
	private String question;

	@Convert(converter = ListToStringConverter.class)
	private List<String> questionOptions;

	@Column(length = 35)
	private String answer;
}
