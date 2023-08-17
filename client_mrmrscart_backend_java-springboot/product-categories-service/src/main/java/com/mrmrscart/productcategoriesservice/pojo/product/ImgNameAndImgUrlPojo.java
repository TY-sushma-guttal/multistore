package com.mrmrscart.productcategoriesservice.pojo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgNameAndImgUrlPojo {
	private String fileName;
	private String fileUrl;
}
