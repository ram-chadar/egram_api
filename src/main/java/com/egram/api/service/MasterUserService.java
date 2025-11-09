package com.egram.api.service;

import com.egram.api.entity.master.MasterUserEntity;
import com.egram.api.security.CustomUserDetail;

public interface MasterUserService {
    CustomUserDetail loadUserByUserId(String userId);
    MasterUserEntity getMasterUserByUsername(String username);
    // Add other methods if needed, e.g., MasterUserEntity updateMasterUser(MasterUserDto dto);
}