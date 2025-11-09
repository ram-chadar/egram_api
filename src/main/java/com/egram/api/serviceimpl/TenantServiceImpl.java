package com.egram.api.serviceimpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egram.api.config.TenantContext;
import com.egram.api.dao.TenantDao;
import com.egram.api.entity.master.TenantEntity;
import com.egram.api.exceptions.SomethingWentWrongException;
import com.egram.api.service.TenantService;
import com.egram.api.utility.DynamicID;
import com.egram.api.utility.MailAsyncServices;

@Service
public class TenantServiceImpl implements TenantService {

	private static final Logger log = LogManager.getLogger(TenantServiceImpl.class);

	@Autowired
	private TenantDao tenantDao;
	
	@Autowired
	MailAsyncServices mailAsyncServices;

	
	@Value("${tenant.dbUsername}")
	private String dbUsername;
	@Value("${tenant.dbPassword}")
	private String dbPassword;

	

	@Override
	@Transactional("masterTransactionManager")
	public String createTenant(String tenantName,String email) {
		String tenantId = DynamicID.getGeneratedTenantId(tenantName);
		

		try {
			// Save tenant metadata in master database
			TenantEntity tenant = new TenantEntity();
			tenant.setTenantId(tenantId);
			tenant.setTenantName(tenantName);
			tenant.setEmail(email);
			tenant.setDbUrl("jdbc:postgresql://localhost:5432/" + tenantId);
			tenant.setDbUsername(dbUsername);
			tenant.setDbPassword(dbPassword);
			tenant.setSubscriptionStatus("INACTIVE");
			log.info("Attempting to save TenantEntity: tenantId={}, tenantName={}", tenantId, tenantName);

			tenantDao.save(tenant);
			log.info("TenantEntity saved successfully in master_egram.tenants: tenantId={}", tenantId);
			
			// mail
			mailAsyncServices.tenantCreationConfirmationMail(email, tenantName, tenantId);
			
			log.info("Tenant registered successfully: {}", tenantId);
			return tenantId;
		} catch (Exception e) {
			log.error("Failed to register tenant: {}", tenantId, e);
			throw new SomethingWentWrongException("Failed to register tenant: " + tenantId, e);
		} finally {
			TenantContext.clear();
		}
	}

}