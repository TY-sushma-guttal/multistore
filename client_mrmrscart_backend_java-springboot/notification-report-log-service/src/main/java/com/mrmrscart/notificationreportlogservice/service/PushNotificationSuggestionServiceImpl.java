package com.mrmrscart.notificationreportlogservice.service;

import static com.mrmrscart.notificationreportlogservice.common.PushNotificationSuggestionConstant.INVALID_ID;
import static com.mrmrscart.notificationreportlogservice.common.PushNotificationSuggestionConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.notificationreportlogservice.entity.PushNotificationSuggestion;
import com.mrmrscart.notificationreportlogservice.exception.PushNotificationSuggestionException;
import com.mrmrscart.notificationreportlogservice.exception.UserIdNotFoundException;
import com.mrmrscart.notificationreportlogservice.pojo.PushNotificationSuggestionPojo;
import com.mrmrscart.notificationreportlogservice.repository.PushNotificationSuggestionRepository;
import com.mrmrscart.notificationreportlogservice.util.PaginatedResponse;

@Service
public class PushNotificationSuggestionServiceImpl implements PushNotificationSuggestionService {

	@Autowired
	private PushNotificationSuggestionRepository pushNotificationSuggestionRepository;

	@Override
	public List<PushNotificationSuggestion> createPushNotificationSuggestion(List<String> titles) {
		try {
			List<PushNotificationSuggestion> pushNotificationSuggestions = new ArrayList<>();
			titles.forEach(e -> {
				PushNotificationSuggestion pushNotificationSuggestion = new PushNotificationSuggestion();
				pushNotificationSuggestion.setTitle(e);
				pushNotificationSuggestions.add(pushNotificationSuggestion);
			});

			return pushNotificationSuggestionRepository.saveAll(pushNotificationSuggestions);
		} catch (UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new PushNotificationSuggestionException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PushNotificationSuggestion> getPushNotificationSuggestion(int pageNumber, int pageSize) {
		try {
			return (List<PushNotificationSuggestion>) PaginatedResponse.getPaginatedResponse(
					pushNotificationSuggestionRepository.findByIsDelete(false), pageNumber, pageSize);
		} catch (Exception e) {
			throw new PushNotificationSuggestionException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public PushNotificationSuggestion updatePushNotificationSuggestion(
			PushNotificationSuggestionPojo pushNotificationSuggestionPojo) {
		try {
			Optional<PushNotificationSuggestion> findById = pushNotificationSuggestionRepository
					.findById(pushNotificationSuggestionPojo.getPushNotificationSuggestionId());
			if (findById.isPresent()) {
				findById.get().setTitle(pushNotificationSuggestionPojo.getTitle());
				return pushNotificationSuggestionRepository.save(findById.get());
			} else {
				throw new PushNotificationSuggestionException(INVALID_ID);
			}
		} catch (UserIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new PushNotificationSuggestionException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public PushNotificationSuggestion deletePushNotificationSuggestion(Long id) {
		try {
			Optional<PushNotificationSuggestion> findById = pushNotificationSuggestionRepository.findById(id);
			if (findById.isPresent()) {
				findById.get().setDelete(true);
				;
				return pushNotificationSuggestionRepository.save(findById.get());
			} else {
				throw new PushNotificationSuggestionException(INVALID_ID);
			}
		} catch (Exception e) {
			throw new PushNotificationSuggestionException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<PushNotificationSuggestion> getAllPushNotificationSuggestion() {

		try {
			return pushNotificationSuggestionRepository.findByIsDelete(false);
		} catch (Exception e) {
			throw new PushNotificationSuggestionException(SOMETHING_WENT_WRONG);
		}
	}

}
