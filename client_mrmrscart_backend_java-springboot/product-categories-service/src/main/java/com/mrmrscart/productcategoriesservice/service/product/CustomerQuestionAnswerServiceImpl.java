package com.mrmrscart.productcategoriesservice.service.product;

import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.INVALID_QUESTION_ID;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.PRODUCT_VARIATION_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.QUESTION_ANSWER_FAILURE;
import static com.mrmrscart.productcategoriesservice.common.product.CustomerQuestionAnswerConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.CustomerQuestionAnswer;
import com.mrmrscart.productcategoriesservice.entity.product.ProductVariation;
import com.mrmrscart.productcategoriesservice.exception.product.CustomerQuestionAnswerException;
import com.mrmrscart.productcategoriesservice.exception.product.ProductVariationNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.QuestionAnswerNotFound;
import com.mrmrscart.productcategoriesservice.exception.product.QuestionNotFoundException;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQAFilterPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.CustomerQuestionAnswerViewPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.QuestionAnswerViewPojo;
import com.mrmrscart.productcategoriesservice.repository.product.CustomerQuestionAnswerRepository;
import com.mrmrscart.productcategoriesservice.repository.product.MasterProductRepository;
import com.mrmrscart.productcategoriesservice.repository.product.ProductVariationRepository;

@Service
class CustomerQuestionAnswerServiceImpl implements CustomerQuestionAnswerService {

	@Autowired
	private CustomerQuestionAnswerRepository customerQuestionAnswerRepository;

	@Autowired
	private ProductVariationRepository productVariationRepository;

	@Autowired
	private MasterProductRepository masterProductRepository;

	@Override
	public CustomerQuestionAnswer postQuestion(CustomerQuestionAnswerPojo customerQuestionAnswerPojo) {
		try {
			if (customerQuestionAnswerPojo.getCustomerQuestionId() != null) {
				Optional<CustomerQuestionAnswer> optional = customerQuestionAnswerRepository
						.findById(customerQuestionAnswerPojo.getCustomerQuestionId());
				if (optional.isPresent()) {
					BeanUtils.copyProperties(customerQuestionAnswerPojo, optional.get(), "customerQuestionId");
					return customerQuestionAnswerRepository.save(optional.get());
				} else {
					throw new QuestionNotFoundException(INVALID_QUESTION_ID);
				}
			} else {
				CustomerQuestionAnswer questionAnswer = new CustomerQuestionAnswer();
				ProductVariation productVariation = productVariationRepository.findByProductVariationIdAndStatus(
						customerQuestionAnswerPojo.getVariationId(), EStatus.APPROVED);
				if (productVariation != null) {
					BeanUtils.copyProperties(customerQuestionAnswerPojo, questionAnswer, "customerQuestionId");
					return customerQuestionAnswerRepository.save(questionAnswer);
				} else {
					throw new ProductVariationNotFound(PRODUCT_VARIATION_FAILURE);
				}
			}
		} catch (ProductVariationNotFound | QuestionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomerQuestionAnswerException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public CustomerQuestionAnswer answerQuestion(CustomerQuestionAnswerPojo customerQuestionAnswerPojo) {
		Optional<CustomerQuestionAnswer> optional = customerQuestionAnswerRepository
				.findById(customerQuestionAnswerPojo.getCustomerQuestionId());
		try {
			if (optional.isPresent()) {
				optional.get().setUserAnswer(customerQuestionAnswerPojo.getUserAnswer());
				optional.get().setAnswerFromType(customerQuestionAnswerPojo.getAnswerFromType());
				optional.get().setAnswerFromTypeId(customerQuestionAnswerPojo.getAnswerFromTypeId());
				optional.get().setQuestionAnsweredAt(LocalDateTime.now());
				return customerQuestionAnswerRepository.save(optional.get());
			} else {
				throw new QuestionNotFoundException(INVALID_QUESTION_ID);
			}
		} catch (Exception e) {
			throw new QuestionNotFoundException(INVALID_QUESTION_ID);
		}
	}

	@Override
	public List<CustomerQuestionAnswer> getAllQuestionsAnswersByVariation(String variationId) {
		List<CustomerQuestionAnswer> questionAnswers = customerQuestionAnswerRepository.findByVariationId(variationId);
		try {
			if (!questionAnswers.isEmpty())
				return questionAnswers;
			else
				throw new QuestionAnswerNotFound(QUESTION_ANSWER_FAILURE);
		} catch (Exception e) {
			throw new QuestionAnswerNotFound(QUESTION_ANSWER_FAILURE);
		}
	}

	@Override
	public CustomerQuestionAnswerViewPojo getAllByStatus(int pageNumber, int pageSize,
			CustomerQAFilterPojo customerQAFilterPojo) {
		CustomerQuestionAnswerViewPojo pojo = new CustomerQuestionAnswerViewPojo();
		if (customerQAFilterPojo.isStatus()) {
			List<CustomerQuestionAnswer> answeredList;
			if (customerQAFilterPojo.getKeyword() != null || !customerQAFilterPojo.getKeyword().equals(""))
				answeredList = customerQuestionAnswerRepository
						.findByIsDeletedAndCreatedByContaining(false, customerQAFilterPojo.getKeyword()).stream()
						.filter(a -> a.getUserAnswer() != null).collect(Collectors.toList());
			else
				answeredList = customerQuestionAnswerRepository.findByIsDeleted(false).stream()
						.filter(a -> a.getUserAnswer() != null).collect(Collectors.toList());

			pojo.setCount(customerQuestionAnswerRepository.findByIsDeleted(false).stream()
					.filter(a -> a.getUserAnswer() != null).collect(Collectors.toList()).size());

			List<CustomerQuestionAnswer> paginated = getQAPaginated(customerQAFilterPojo.getDateFrom(),
					customerQAFilterPojo.getDateTo(), pageNumber, pageSize, answeredList);
			pojo.setQuestionAnswerViewPojo(setProductMedia(paginated));

		} else {
			List<CustomerQuestionAnswer> unAnsweredList;
			if (customerQAFilterPojo.getKeyword() != null || !customerQAFilterPojo.getKeyword().equals("")) {
				unAnsweredList = customerQuestionAnswerRepository
						.findByIsDeletedAndCreatedByContaining(false, customerQAFilterPojo.getKeyword()).stream()
						.filter(a -> a.getUserAnswer() == null).collect(Collectors.toList());
			} else {
				unAnsweredList = customerQuestionAnswerRepository.findByIsDeleted(false).stream()
						.filter(a -> a.getUserAnswer() == null).collect(Collectors.toList());
			}

			pojo.setCount(customerQuestionAnswerRepository.findByIsDeleted(false).stream()
					.filter(a -> a.getUserAnswer() == null).collect(Collectors.toList()).size());
			List<CustomerQuestionAnswer> paginated = getQAPaginated(customerQAFilterPojo.getDateFrom(),
					customerQAFilterPojo.getDateTo(), pageNumber, pageSize, unAnsweredList);
			pojo.setQuestionAnswerViewPojo(setProductMedia(paginated));
		}
		return pojo;

	}

	private List<CustomerQuestionAnswer> getQAPaginated(LocalDateTime dateFrom, LocalDateTime dateTo, int pageNumber,
			int pageSize, List<CustomerQuestionAnswer> find) {
		List<CustomerQuestionAnswer> list = new ArrayList<>();
		if (dateFrom != null && dateTo != null) {
			list = find.stream().filter(f -> dateFrom.isBefore(f.getCreatedAt()) && dateTo.isAfter(f.getCreatedAt()))
					.collect(Collectors.toList());
			if (list.isEmpty()) {
				return list;
			}
		}

		if (pageNumber == 0) {
			if (!list.isEmpty())
				return list.stream().limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			if (!list.isEmpty())
				return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	private List<QuestionAnswerViewPojo> setProductMedia(List<CustomerQuestionAnswer> paginated) {
		List<QuestionAnswerViewPojo> answerPojos = new ArrayList<>();
		paginated.forEach(p -> {
			QuestionAnswerViewPojo answerPojo = new QuestionAnswerViewPojo();
			BeanUtils.copyProperties(p, answerPojo);

			ProductVariation productVariation = productVariationRepository
					.findByProductVariationIdAndStatus(p.getVariationId(), EStatus.APPROVED);
			if (productVariation != null) {
				answerPojo.setProductImages(productVariation.getVariationMedia());
			} else {
				throw new ProductVariationNotFound(PRODUCT_VARIATION_FAILURE);
			}
			answerPojos.add(answerPojo);
		});
		return answerPojos;
	}

	@Override
	public CustomerQuestionAnswerViewPojo getAllByStatusSupplier(int pageNumber, int pageSize, String supplierId,
			CustomerQAFilterPojo customerQAFilterPojo) {
		CustomerQuestionAnswerViewPojo pojo = new CustomerQuestionAnswerViewPojo();
		List<String> variationIds = new ArrayList<>();
		masterProductRepository.findBySupplierIdAndIsDelete(supplierId, false)
				.forEach(master -> productVariationRepository
						.findByMasterProductIdAndStatusAndIsDelete(master.getMasterProductId(), EStatus.APPROVED, false)
						.forEach(product -> variationIds.add(product.getProductVariationId())));
		if (customerQAFilterPojo.isStatus()) {
			List<CustomerQuestionAnswer> answeredList;

			if (customerQAFilterPojo.getKeyword() != null || !customerQAFilterPojo.getKeyword().equals("")) {
				answeredList = customerQuestionAnswerRepository
						.findByIsDeletedAndVariationIdInAndCreatedByContaining(false, variationIds,
								customerQAFilterPojo.getKeyword())
						.stream().filter(a -> a.getUserAnswer() != null).collect(Collectors.toList());
			} else {
				answeredList = customerQuestionAnswerRepository.findByIsDeletedAndVariationIdIn(false, variationIds)
						.stream().filter(a -> a.getUserAnswer() != null).collect(Collectors.toList());
			}
			pojo.setCount(customerQuestionAnswerRepository.findByIsDeletedAndVariationIdIn(false, variationIds).stream()
					.filter(a -> a.getUserAnswer() != null).collect(Collectors.toList()).size());
			List<CustomerQuestionAnswer> paginated = getQAPaginated(customerQAFilterPojo.getDateFrom(),
					customerQAFilterPojo.getDateTo(), pageNumber, pageSize, answeredList);
			pojo.setQuestionAnswerViewPojo(setProductMedia(paginated));

		} else {
			List<CustomerQuestionAnswer> unAnsweredList;
			if (customerQAFilterPojo.getKeyword() != null || !customerQAFilterPojo.getKeyword().equals("")) {
				unAnsweredList = customerQuestionAnswerRepository
						.findByIsDeletedAndVariationIdInAndCreatedByContaining(false, variationIds,
								customerQAFilterPojo.getKeyword())
						.stream().filter(a -> a.getUserAnswer() == null).collect(Collectors.toList());
			} else {
				unAnsweredList = customerQuestionAnswerRepository.findByIsDeletedAndVariationIdIn(false, variationIds)
						.stream().filter(a -> a.getUserAnswer() == null).collect(Collectors.toList());
			}

			pojo.setCount(customerQuestionAnswerRepository.findByIsDeletedAndVariationIdIn(false, variationIds).stream()
					.filter(a -> a.getUserAnswer() == null).collect(Collectors.toList()).size());
			List<CustomerQuestionAnswer> paginated = getQAPaginated(customerQAFilterPojo.getDateFrom(),
					customerQAFilterPojo.getDateTo(), pageNumber, pageSize, unAnsweredList);
			pojo.setQuestionAnswerViewPojo(setProductMedia(paginated));
		}
		return pojo;
	}

	@Override
	public boolean deleteQuestionAnswer(String customerQuestionId) {
		Optional<CustomerQuestionAnswer> optional = customerQuestionAnswerRepository.findById(customerQuestionId);
		try {
			if (optional.isPresent()) {
				optional.get().setDeleted(true);
				customerQuestionAnswerRepository.save(optional.get());
				return true;
			} else {
				throw new QuestionNotFoundException(INVALID_QUESTION_ID);
			}
		} catch (Exception e) {
			throw new QuestionNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

}
