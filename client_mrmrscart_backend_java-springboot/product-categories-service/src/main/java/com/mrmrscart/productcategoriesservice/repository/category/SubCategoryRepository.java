package com.mrmrscart.productcategoriesservice.repository.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mrmrscart.productcategoriesservice.entity.category.SubCategory;

public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {
	@Query("{'setId' : :#{#setId}, 'isDisable' : :#{#isDisable}}")
	public List<SubCategory> findBySetIdAndisDisable(String setId, boolean isDisable);

	public List<SubCategory> findBySetId(String categorySetId);

	public SubCategory findBySubCategoryNameAndSubCategoryId(String subCategoryName, String subCategoryId);

	public List<SubCategory> findByIsDisable(boolean isDesable);

	public SubCategory findBySubCategoryIdAndIsDisable(String subCategoryId, boolean isDisable);

	public SubCategory findBySubCategoryIdAndIsDisableAndCreatedAtBetween(String subCategoryId, boolean isDisable,
			LocalDateTime fromDate, LocalDateTime toDate);

	public List<SubCategory> findBySetIdAndIsDisableAndCreatedAtBetween(String categorySetId, Boolean isDisable,
			LocalDateTime fromDate, LocalDateTime toDate);

	public List<SubCategory> findBySetIdIn(List<String> list);

	public List<SubCategory> findByIsDisableAndSetIdIn(boolean b, List<String> list);

}
