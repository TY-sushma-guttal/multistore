package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerViewPojo {

	private String customerQuestionId;
	private String customerQuestion;
	private String profileId;
	private String userAnswer;
	private String answerFromType;
	private String answerFromTypeId;
	private String variationId;
	private List<String> productImages;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime questionAnsweredAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastModifiedAt;
	private String createdBy;
	private String lastModifiedBy;

}
