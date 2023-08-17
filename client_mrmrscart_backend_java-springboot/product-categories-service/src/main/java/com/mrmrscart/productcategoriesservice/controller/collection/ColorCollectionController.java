package com.mrmrscart.productcategoriesservice.controller.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.productcategoriesservice.pojo.collection.ColorCollectionPojo;
import com.mrmrscart.productcategoriesservice.response.product.SuccessResponse;
import com.mrmrscart.productcategoriesservice.service.collection.ColorCollectionService;
import static com.mrmrscart.productcategoriesservice.common.collection.CollectionConstant.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/products")
public class ColorCollectionController {

	@Autowired
	private ColorCollectionService colorCollectionService;
	
	@PostMapping("/color-collection")
	public ResponseEntity<SuccessResponse> addColorCollection(@RequestBody ColorCollectionPojo colorCollectionPojo){
		return new ResponseEntity<>(new SuccessResponse(false, COLOR_COLLECTION_SUCCESS, colorCollectionService.addColorCollection(colorCollectionPojo)),HttpStatus.OK);
	}
	
	@PutMapping("/color-collection")
	public ResponseEntity<SuccessResponse> updateColorCollection(@RequestBody ColorCollectionPojo colorCollectionPojo){
		return new ResponseEntity<>(new SuccessResponse(false, COLOR_COLLECTION_UPDATE_SUCCESS, colorCollectionService.updateColorCollection(colorCollectionPojo)),HttpStatus.OK);
	}
	
	@GetMapping("/color-collection")
	public ResponseEntity<SuccessResponse> getAllColorCollection(){
		return new ResponseEntity<>(new SuccessResponse(false, COLOR_COLLECTION_UPDATE_SUCCESS, colorCollectionService.getAllColorCollection()),HttpStatus.OK);
	}
	
	@DeleteMapping("/color-collection/{id}")
	public ResponseEntity<SuccessResponse> deleteColorCollection(@PathVariable String id){
		return new ResponseEntity<>(new SuccessResponse(false, colorCollectionService.deleteColorCollection(id), null),HttpStatus.OK);
	}
	
	
}
