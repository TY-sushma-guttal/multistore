package com.mrmrscart.notificationreportlogservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.notificationreportlogservice.entity.EStatus;
import com.mrmrscart.notificationreportlogservice.entity.SupplierChangesHistory;
import com.mrmrscart.notificationreportlogservice.exception.NoRecordFoundException;
import com.mrmrscart.notificationreportlogservice.feign.UserService;
import com.mrmrscart.notificationreportlogservice.feign.entity.ESupplierStatus;
import com.mrmrscart.notificationreportlogservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.notificationreportlogservice.pojo.SupplierChangesHistoryViewPojo;
import com.mrmrscart.notificationreportlogservice.repository.SupplierChangesHistoryRepository;
import com.mrmrscart.notificationreportlogservice.response.SupplierResponse;

@Service
public class SupplierChangesHistoryServiceImpl implements SupplierChangesHistoryService{
	
	@Autowired
	private SupplierChangesHistoryRepository changesHistoryRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public List<SupplierChangesHistory> addSupplierChangesHistory(
			List<SupplierChangesHistoryPojo> supplierChangesHistorypojo) {
		List<SupplierChangesHistory> histories=new ArrayList<>();
		supplierChangesHistorypojo.forEach(change->{
			SupplierChangesHistory changesHistory=new SupplierChangesHistory();
			BeanUtils.copyProperties(change, changesHistory);
			changesHistory.setStatus(EStatus.CREATED);
			histories.add(changesHistory);
		});
		return changesHistoryRepository.saveAll(histories);
	}

	@Override
	public SupplierChangesHistory rejectSupplierChangesHistory(Long changeId) {
		Optional<SupplierChangesHistory> optional = changesHistoryRepository.findById(changeId);
		SupplierChangesHistoryPojo history=new SupplierChangesHistoryPojo();
		SupplierChangesHistory supplierChangesHistory=null ;
		if(optional.isPresent()) {
			BeanUtils.copyProperties(optional.get(), history);
			optional.get().setApprovedAt(LocalDateTime.now());
			optional.get().setStatus(EStatus.REJECTED);
			supplierChangesHistory=changesHistoryRepository.save(optional.get());
			if(changesHistoryRepository.findBySupplierIdAndApprovedAt(optional.get().getSupplierId(),null).isEmpty()) {
				history.setAccountVerified(true);
			}
		}
		
		userService.updateSupplierProfile(history);
		return supplierChangesHistory;
	}

	@Override
	public SupplierChangesHistory approveSupplierChangesHistory(Long changeId) {
		SupplierChangesHistoryPojo history=new SupplierChangesHistoryPojo();
		Optional<SupplierChangesHistory> optional = changesHistoryRepository.findById(changeId);
		SupplierChangesHistory supplierChangesHistory =new SupplierChangesHistory();
		if(optional.isPresent()) {
			optional.get().setApprovedAt(LocalDateTime.now());
			optional.get().setStatus(EStatus.APPROVED);
			optional.get().setApproved(true);
			supplierChangesHistory= changesHistoryRepository.save(optional.get());
			if(changesHistoryRepository.findBySupplierIdAndApprovedAt(optional.get().getSupplierId(),null).isEmpty()) {
				history.setAccountVerified(true);
			}
		}
		BeanUtils.copyProperties(supplierChangesHistory, history);
		
		userService.approveSupplierProfile(history);
		return supplierChangesHistory;

	}

	@Override
	public List<SupplierChangesHistoryViewPojo> getAllSupplierChangesHistory() {
		List<SupplierChangesHistory> status = changesHistoryRepository.findByStatus(EStatus.CREATED);
		List<SupplierChangesHistoryViewPojo> changesHistoryViewPojos=new ArrayList<>();
		status.forEach(s->{
			SupplierChangesHistoryViewPojo pojo=new SupplierChangesHistoryViewPojo();
			ResponseEntity<SupplierResponse> entity = userService.getSupplierById(s.getSupplierId(), ESupplierStatus.APPROVED);
			SupplierResponse body = entity.getBody();
			if(body!=null && body.getData()!=null) {
					pojo.setChangeHistoryId(s.getChangeHistoryId());
					pojo.setSupplierId(body.getData().getSupplierId());
					pojo.setChangedField("Updated "+ s.getChangedField());
					pojo.setOldValue(s.getOldValue());
					pojo.setChangedValue(s.getChangedValue());
					pojo.setEmailId(body.getData().getEmailId());
					pojo.setMobileNumber(body.getData().getMobileNumber());
					pojo.setUpdatedAt(s.getUpdatedAt());
					pojo.setCreatedDate(s.getCreatedDate());
					pojo.setQueries(s.getQueries());
					pojo.setAnswers(s.getAnswers());
					changesHistoryViewPojos.add(pojo);
			}
		});
		return changesHistoryViewPojos;
	}

	@Override
	public SupplierChangesHistory getById(Long changeId) {
		Optional<SupplierChangesHistory> optional = changesHistoryRepository.findById(changeId);
		if(optional.isPresent()) {
			return optional.get();
		}else {
			throw new NoRecordFoundException("No changes found");
		}
	}

}
