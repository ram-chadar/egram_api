package com.egram.api.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egram.api.dao.SubscriptionPlanDAO;
import com.egram.api.dto.SubscriptionPlanDTO;
import com.egram.api.entity.master.SubscriptionPlanEntity;
import com.egram.api.exceptions.ResourceNotFoundException;
import com.egram.api.exceptions.SomethingWentWrongException;
import com.egram.api.service.SubscriptionPlanService;
import com.egram.api.utility.DynamicID;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

	@Autowired
	private SubscriptionPlanDAO subscriptionPlanDAO;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Transactional("masterTransactionManager")
	public SubscriptionPlanDTO createPlan(SubscriptionPlanDTO planDTO) {
		planDTO.setPlanId(DynamicID.getGeneratedSubscriptionPlanId(planDTO.getPlanType()));
		SubscriptionPlanEntity entity = modelMapper.map(planDTO, SubscriptionPlanEntity.class);
		subscriptionPlanDAO.save(entity);
		return modelMapper.map(entity, SubscriptionPlanDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SubscriptionPlanDTO> getAllPlans() {
		List<SubscriptionPlanEntity> entities = subscriptionPlanDAO.findAll();
		return entities.stream().map(entity -> modelMapper.map(entity, SubscriptionPlanDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public SubscriptionPlanDTO getPlan(String planId) {
		SubscriptionPlanEntity entity = subscriptionPlanDAO.findById(planId)
				.orElseThrow(() -> new ResourceNotFoundException("Plan not found: " + planId));
		return modelMapper.map(entity, SubscriptionPlanDTO.class);
	}

	@Override
	@Transactional
	public SubscriptionPlanDTO updatePlan(SubscriptionPlanDTO planDTO) {
		SubscriptionPlanEntity entity = subscriptionPlanDAO.findById(planDTO.getPlanId())
				.orElseThrow(() -> new SomethingWentWrongException("Plan not found: " + planDTO.getPlanId()));
		modelMapper.map(planDTO, entity);
		subscriptionPlanDAO.update(entity);
		return modelMapper.map(entity, SubscriptionPlanDTO.class);
	}

	@Override
	@Transactional
	public void deletePlan(String planId) {
		subscriptionPlanDAO.delete(planId);
	}
}