package com.mrmrscart.productcategoriesservice.response.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaResponse {
	private boolean error;
	private String message;
	private Object data;
}
