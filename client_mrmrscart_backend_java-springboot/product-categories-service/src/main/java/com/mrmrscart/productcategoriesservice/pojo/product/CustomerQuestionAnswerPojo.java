package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQuestionAnswerPojo {
	
	private String customerQuestionId;
	private String customerQuestion;
	private String profileId;
	private String userAnswer;
	private String answerFromType;
	private String answerFromTypeId;
	private String variationId;
	private List<String> productImages;
	private LocalDateTime questionAnsweredAt;

}
