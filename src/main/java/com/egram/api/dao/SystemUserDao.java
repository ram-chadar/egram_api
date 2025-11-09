package com.egram.api.dao;

import java.util.List;

import com.egram.api.entity.RoleEntity;
import com.egram.api.entity.SystemUserEntity;
import com.egram.api.security.CustomUserDetail;

public interface SystemUserDao {
	public CustomUserDetail loadUserByUserId(String userId);
    public SystemUserEntity getSystemUserByUsername(String username);
    public List<SystemUserEntity> getAllSystemUser();
    public void updateSystemUser(SystemUserEntity userEntity);
    public void saveRole(RoleEntity role);
    public void save(SystemUserEntity user);

}
