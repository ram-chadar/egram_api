package com.egram.api.dao;

import java.util.Optional;

import com.egram.api.entity.master.MasterUserEntity;
import com.egram.api.security.CustomUserDetail;

public interface MasterUserDao {
    Optional<MasterUserEntity> findByUsername(String username);
    CustomUserDetail loadUserByUserId(String userId);
    // Add other methods if needed, e.g., void save(MasterUserEntity entity);
}