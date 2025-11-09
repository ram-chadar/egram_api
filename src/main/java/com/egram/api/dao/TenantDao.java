package com.egram.api.dao;

import java.util.List;

import com.egram.api.entity.RegionsEntity;
import com.egram.api.entity.RoleEntity;
import com.egram.api.entity.SystemUserEntity;
import com.egram.api.entity.master.TenantEntity;

public interface TenantDao {
	TenantEntity findById(String tenantId);

	List<TenantEntity> findAll();

	void save(TenantEntity tenant);

	void delete(String tenantId);
	
	void update(TenantEntity tenant);
	
	void saveTenantEntities(String tenantId, RoleEntity adminRole, RegionsEntity defaultRegion, SystemUserEntity adminUser);
}