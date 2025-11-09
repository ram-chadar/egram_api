package com.egram.api.service;

import java.util.List;

import com.egram.api.dto.SystemUserDto;
import com.egram.api.entity.SystemUserEntity;
import com.egram.api.security.CustomUserDetail;

public interface SystemUserService {
	public abstract CustomUserDetail loadUserByUserId(String userId);

	public abstract SystemUserEntity getSystemUserByUsername(String username);

	public abstract List<SystemUserEntity> getAllSystemUser();

	public abstract SystemUserEntity updateSystemUser(SystemUserDto userDto);

}
