package com.egram.api.dao;

import java.util.List;
import java.util.Optional;

import com.egram.api.entity.master.SubscriptionEntity;

public interface SubscriptionDAO {
    void save(SubscriptionEntity entity);
    List<SubscriptionEntity> findByTenantId(String tenantId);
    Optional<SubscriptionEntity> findById(String subscriptionId);
    void update(SubscriptionEntity entity);
    void delete(String subscriptionId, String tenantId);
}