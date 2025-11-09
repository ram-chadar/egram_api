package com.egram.api.dao;

import java.util.List;
import java.util.Optional;

import com.egram.api.entity.master.SubscriptionPlanEntity;

public interface SubscriptionPlanDAO {
    void save(SubscriptionPlanEntity entity);
    List<SubscriptionPlanEntity> findAll();
    Optional<SubscriptionPlanEntity> findById(String planId);
    void update(SubscriptionPlanEntity entity);
    void delete(String planId);
}