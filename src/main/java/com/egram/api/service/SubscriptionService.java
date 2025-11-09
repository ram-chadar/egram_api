package com.egram.api.service;

import java.util.List;

import com.egram.api.dto.SubscriptionDTO;

public interface SubscriptionService {
    SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    List<SubscriptionDTO> getSubscriptionsByTenant(String tenantId);
    SubscriptionDTO getSubscription(String subscriptionId);
    SubscriptionDTO changeSubscriptionPlan(String subscriptionId, String newPlanId);
    void deleteSubscription(String subscriptionId, String tenantId);
}