package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQuestionAnswerViewPojo {

	private int count;

	private List<QuestionAnswerViewPojo> questionAnswerViewPojo;

}
