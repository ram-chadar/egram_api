package com.egram.api.dao;

import java.util.List;

import com.egram.api.entity.AuditLog;

public interface AuditLogDao {

	public void save(AuditLog auditLog);

	public List<AuditLog> findAll();
	

}
