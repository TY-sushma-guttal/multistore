package com.mrmrscart.productcategoriesservice.service.collection;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.collection.ColorCollection;
import com.mrmrscart.productcategoriesservice.exception.collection.ColorCollectionException;
import com.mrmrscart.productcategoriesservice.pojo.collection.ColorCollectionPojo;
import com.mrmrscart.productcategoriesservice.repository.collection.ColorCollectionRepository;
import static com.mrmrscart.productcategoriesservice.common.collection.CollectionConstant.*;

import java.util.List;
import java.util.Optional;

@Service
public class ColorCollectionServiceImpl implements ColorCollectionService {

	@Autowired
	private ColorCollectionRepository colorCollectionRepository;

	@Override
	public ColorCollection addColorCollection(ColorCollectionPojo colorCollectionPojo) {
		try {
			ColorCollection colorCollection = new ColorCollection();
			BeanUtils.copyProperties(colorCollectionPojo, colorCollection);
			return colorCollectionRepository.save(colorCollection);
		} catch (Exception e) {
			throw new ColorCollectionException(COLOR_COLLECTION_FAILURE);
		}

	}

	@Override
	public ColorCollection updateColorCollection(ColorCollectionPojo colorCollectionPojo) {
		Optional<ColorCollection> findById = colorCollectionRepository
				.findById(colorCollectionPojo.getColorCollectionId());
		if (findById.isPresent()) {
			ColorCollection colorCollection = findById.get();
			colorCollection.setColorName(colorCollectionPojo.getColorName());
			colorCollection.setColorCode(colorCollectionPojo.getColorCode());
			return colorCollectionRepository.save(colorCollection);
		} else {
			throw new ColorCollectionException(INVALID_COLOR_COLLECTION_ID);
		}
	}

	@Override
	public List<ColorCollection> getAllColorCollection() {
		List<ColorCollection> findAll = colorCollectionRepository.findAll();
		if (findAll.isEmpty()) {
			throw new ColorCollectionException(COLOR_COLLECTION_EMPTY);
		}
		return findAll;
	}

	@Override
	public String deleteColorCollection(String id) {
System.out.println("nbmnbmnb" + id);
		try {
			Optional<ColorCollection> findById = colorCollectionRepository.findById(id);
			if (findById.isPresent()) {
				colorCollectionRepository.deleteById(id);
				return COLOR_COLLECTION_DELETE_SUCCESS;
			} else {
				throw new ColorCollectionException(INVALID_COLOR_COLLECTION_ID);
			}

		} catch (ColorCollectionException e) {
			throw e;
		}

		catch (Exception e) {
			e.printStackTrace();
             throw new ColorCollectionException(SOMETHING_WENT_WRONG);
		}
		
	}

}
