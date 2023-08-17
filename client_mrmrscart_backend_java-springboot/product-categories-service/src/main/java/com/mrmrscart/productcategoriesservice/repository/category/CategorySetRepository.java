package com.mrmrscart.productcategoriesservice.repository.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.CategorySet;

@Repository
public interface CategorySetRepository extends MongoRepository<CategorySet, String> {

	public List<CategorySet> findByMainCategoryId(String mainCategoryId);

	public List<CategorySet> findByMainCategoryIdAndIsDisabled(String mainCategoryId, boolean isDisabled);

	public List<CategorySet> findByMainCategoryIdAndIsDisabledAndCreatedAtBetween(String mainCategoryId, boolean b,
			LocalDateTime fromDate, LocalDateTime toDate);

	public List<CategorySet> findByIsDisabled(boolean b);
	
	public CategorySet findByCategorySetIdAndIsDisabled(String categorySetId,boolean isDisable);
}
