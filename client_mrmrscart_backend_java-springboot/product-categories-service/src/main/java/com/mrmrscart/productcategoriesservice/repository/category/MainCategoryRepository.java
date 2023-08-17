package com.mrmrscart.productcategoriesservice.repository.category;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.MainCategory;

@Repository
public interface MainCategoryRepository extends MongoRepository<MainCategory, String> {

	@Query("{isDisabled:?0}")
	List<MainCategory> findEnabledForDropdown(boolean b);

	MainCategory findByMainCategoryIdAndIsDisabled(String mainCategoryId, boolean b);

	List<MainCategory> findByIsDisabled(boolean isDisabled);
	
	List<MainCategory> findByCommissionTypeAndIsDisabled(ECategory commissionMode, boolean isDisabled);
	
	List<MainCategory> findByMainCategoryIdInAndIsDisabledAndCommissionType(List<String> mainCategoryIds,boolean b, ECategory commissionMode);

}
