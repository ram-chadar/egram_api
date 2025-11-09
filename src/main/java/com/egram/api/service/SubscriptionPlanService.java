package com.egram.api.service;

import java.util.List;

import com.egram.api.dto.SubscriptionPlanDTO;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createPlan(SubscriptionPlanDTO planDTO);
    List<SubscriptionPlanDTO> getAllPlans();
    SubscriptionPlanDTO getPlan(String planId);
    SubscriptionPlanDTO updatePlan(SubscriptionPlanDTO planDTO);
    void deletePlan(String planId);
}