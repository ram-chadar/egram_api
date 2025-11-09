package com.egram.api.serviceimpl;

import com.egram.api.config.TenantContext;
import com.egram.api.config.TenantRoutingDataSource;
import com.egram.api.constants.ApprovalStatus;
import com.egram.api.dao.SubscriptionDAO;
import com.egram.api.dao.TenantDao;
import com.egram.api.dto.DSAApplicationDTO;
import com.egram.api.dto.SubscriptionDTO;
import com.egram.api.entity.*;
import com.egram.api.entity.loan.*;
import com.egram.api.entity.master.SubscriptionEntity;
import com.egram.api.entity.master.SubscriptionPlanEntity;
import com.egram.api.entity.master.TenantEntity;
import com.egram.api.exceptions.SomethingWentWrongException;
import com.egram.api.service.SubscriptionService;
import com.egram.api.utility.DynamicID;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger log = LogManager.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private DSAServiceImpl dsaServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("routingDataSource")
    private TenantRoutingDataSource routingDataSource;

    @Override
    @Transactional("masterTransactionManager")
    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        String subscriptionId = DynamicID.getGeneratedSubscriptionId();
        subscriptionDTO.setSubscriptionId(subscriptionId);

        subscriptionDTO.setStartDate(LocalDateTime.now());
        subscriptionDTO.setEndDate(subscriptionDTO.getStartDate().plusMonths(1));
        subscriptionDTO.setBillingStatus("DONE");
        subscriptionDTO.setPaymentGatewayId(UUID.randomUUID().toString());

        SubscriptionEntity entity = modelMapper.map(subscriptionDTO, SubscriptionEntity.class);
        SubscriptionPlanEntity plan = new SubscriptionPlanEntity();
        plan.setPlanId(subscriptionDTO.getPlanId());

        TenantEntity tenant = tenantDao.findById(subscriptionDTO.getTenantId());
        entity.setPlan(plan);
        entity.setTenant(tenant);

        subscriptionDAO.save(entity);

        // Activate tenant
        tenant.setSubscriptionStatus("ACTIVE");
        tenantDao.update(tenant);

        createTenantDatabaseIfNotExists(tenant);

        // Create tenant DataSource
        DataSource tenantDataSource = createTenantDataSource(tenant);
        routingDataSource.addTenantDataSource(subscriptionDTO.getTenantId(), tenantDataSource);
        log.info("Tenant DataSource added for tenantId={}", subscriptionDTO.getTenantId());

        // Test and initialize
        testDatabaseConnection(subscriptionDTO.getTenantId(), tenantDataSource);
        initializeTenantSchema(subscriptionDTO.getTenantId(), tenantDataSource, 3, 1000);
        verifyTenantSchema(subscriptionDTO.getTenantId(), tenantDataSource);

        // Insert default entities
        saveTenantEntities(subscriptionDTO.getTenantId(), tenant.getTenantName());

        return modelMapper.map(entity, SubscriptionDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> getSubscriptionsByTenant(String tenantId) {
        return subscriptionDAO.findByTenantId(tenantId)
                .stream()
                .map(e -> modelMapper.map(e, SubscriptionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionDTO getSubscription(String subscriptionId) {
        SubscriptionEntity entity = subscriptionDAO.findById(subscriptionId)
                .orElseThrow(() -> new SomethingWentWrongException("Subscription not found: " + subscriptionId));
        return modelMapper.map(entity, SubscriptionDTO.class);
    }

    @Override
    @Transactional("masterTransactionManager")
    public SubscriptionDTO changeSubscriptionPlan(String subscriptionId, String newPlanId) {
        SubscriptionEntity entity = subscriptionDAO.findById(subscriptionId)
                .orElseThrow(() -> new SomethingWentWrongException("Subscription not found: " + subscriptionId));

        SubscriptionPlanEntity newPlan = new SubscriptionPlanEntity();
        newPlan.setPlanId(newPlanId);
        entity.setPlan(newPlan);

        subscriptionDAO.update(entity);
        return modelMapper.map(entity, SubscriptionDTO.class);
    }

    @Override
    @Transactional
    public void deleteSubscription(String subscriptionId, String tenantId) {
        subscriptionDAO.delete(subscriptionId, tenantId);
    }

    @Transactional("tenantTransactionManager")
    private void saveTenantEntities(String tenantId, String tenantName) {
        TenantContext.setCurrentTenant(tenantId);

        RoleEntity adminRole = new RoleEntity();
        adminRole.setId(DynamicID.getGeneratedRoleId());
        adminRole.setName("ROLE_ADMIN");
        adminRole.setCreatedAt(LocalDateTime.now());

        RegionsEntity defaultRegion = new RegionsEntity();
        defaultRegion.setId(DynamicID.getGeneratedRegionId());
        defaultRegion.setRegionName("Default Region");
        defaultRegion.setRegionCode("DR001");

        DSAApplicationDTO applicationDTO = new DSAApplicationDTO();
        applicationDTO.setFirstName("TenantAdmin");
        applicationDTO.setMiddleName("TenantAdmin");
        applicationDTO.setLastName("TenantAdmin");
        applicationDTO.setGender("Female");
        applicationDTO.setDateOfBirth("2025-09-25");
        applicationDTO.setNationality("Indian");
        applicationDTO.setContactNumber("98257245");
        applicationDTO.setEmailAddress("thekiranacademyojtdev@gmail.com");
        applicationDTO.setStreetAddress("14 Powai Road");
        applicationDTO.setCity("Mumbai");
        applicationDTO.setState("Maharashtra");
        applicationDTO.setPostalCode("400076");
        applicationDTO.setCountry("India");
        applicationDTO.setPreferredLanguage("Hindi");
        applicationDTO.setEducationalQualifications("Master of Business Administration");
        applicationDTO.setExperience("7 years in finance and accounting");
        applicationDTO.setIsAssociatedWithOtherDSA("NO");
        applicationDTO.setAssociatedInstitutionName("NA");
        applicationDTO.setReferralSource("Job portal");
        applicationDTO.setEmailVerified(true);
        applicationDTO.setApprovalStatus(ApprovalStatus.APPROVED.getValue());

        DSAApplicationDTO dsaApplication = dsaServiceImpl.dsaApplication(applicationDTO);

        SystemUserEntity adminUser = new SystemUserEntity();
        adminUser.setUsername("tenant_admin_" + tenantName);
        adminUser.setPassword(passwordEncoder.encode("Temp@123"));
        adminUser.setQuestion("Default question");
        adminUser.setAnswer("Default answer");
        adminUser.setStatus("ACTIVE");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setRoles(Collections.singletonList(adminRole));
        adminUser.setRegions(Collections.singletonList(defaultRegion));

        if (dsaApplication != null) {
            DsaApplicationEntity applicationEntity = mapper.map(dsaApplication, DsaApplicationEntity.class);
            adminUser.setDsaApplicationId(applicationEntity);
        }

        tenantDao.saveTenantEntities(tenantId, adminRole, defaultRegion, adminUser);
        log.info("Default entities prepared for tenantId={}", tenantId);
    }

    private DataSource createTenantDataSource(TenantEntity tenant) {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setUrl(tenant.getDbUrl());
        properties.setUsername(tenant.getDbUsername());
        properties.setPassword(tenant.getDbPassword());
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    private void testDatabaseConnection(String tenantId, DataSource tenantDataSource) {
        TenantContext.setCurrentTenant(tenantId);
        try {
            log.info("Testing database connection for tenantId={}", tenantId);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(tenantDataSource);
            jdbcTemplate.execute("SELECT 1");
            log.info("Database connection successful for tenantId={}", tenantId);
        } catch (Exception e) {
            log.error("Failed to connect to tenant database for tenantId: {}", tenantId, e);
            throw new SomethingWentWrongException("Failed to connect to tenant database: " + tenantId, e);
        } finally {
            TenantContext.clear();
        }
    }
    
    
    private void createTenantDatabaseIfNotExists(TenantEntity tenant) {
        try {
            // Extract database name from URL
            String dbUrl = tenant.getDbUrl();
            String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);

            // Connect to default postgres database
            String adminUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/") + 1) + "postgres";
            log.info("Checking or creating tenant database: {}", dbName);

            DataSourceProperties adminProps = new DataSourceProperties();
            adminProps.setUrl(adminUrl);
            adminProps.setUsername(tenant.getDbUsername());
            adminProps.setPassword(tenant.getDbPassword());

            JdbcTemplate adminJdbc = new JdbcTemplate(
                    adminProps.initializeDataSourceBuilder().type(HikariDataSource.class).build()
            );

            // Check if database exists
            Integer count = adminJdbc.queryForObject(
                    "SELECT COUNT(*) FROM pg_database WHERE datname = ?", Integer.class, dbName);

            if (count == 0) {
                adminJdbc.execute("CREATE DATABASE \"" + dbName + "\"");
                log.info("Tenant database '{}' created successfully.", dbName);
            } else {
                log.info("Tenant database '{}' already exists.", dbName);
            }

        } catch (Exception e) {
            log.error("Failed to create tenant database", e);
            throw new SomethingWentWrongException("Failed to create tenant database: " + tenant.getDbUrl(), e);
        }
    }


    private void initializeTenantSchema(String tenantId, DataSource tenantDataSource, int maxRetries, long delayMs) {
        TenantContext.setCurrentTenant(tenantId);
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            try {
                log.info("Initializing tenant schema for tenantId={}, attempt={}", tenantId, attempt + 1);

                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.connection.datasource", tenantDataSource)
                        .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                        .applySetting("hibernate.hbm2ddl.auto", "update")
                        .applySetting("hibernate.show_sql", "true")
                        .applySetting("hibernate.format_sql", "true")
                        .applySetting("hibernate.cache.use_second_level_cache", "false")
                        .applySetting("hibernate.cache.use_query_cache", "false")
                        .build();

                MetadataSources sources = new MetadataSources(registry);
                sources.addAnnotatedClass(AuditLog.class);
                sources.addAnnotatedClass(ContactUsEntity.class);
                sources.addAnnotatedClass(CustomerEntity.class);
                sources.addAnnotatedClass(DocumentEntity.class);
                sources.addAnnotatedClass(DsaKycEntity.class);
                sources.addAnnotatedClass(DisbursementEntity.class);
                sources.addAnnotatedClass(LoanApplicationEntity.class);
                sources.addAnnotatedClass(LoanConditioEntity.class);
                sources.addAnnotatedClass(LoanDisbursementEntity.class);
                sources.addAnnotatedClass(LoanTrancheEntity.class);
                sources.addAnnotatedClass(ReconciliationEntity.class);
                sources.addAnnotatedClass(RepaymentEntity.class);
                sources.addAnnotatedClass(TrancheAuditEntity.class);
                sources.addAnnotatedClass(TrancheEntity.class);
                sources.addAnnotatedClass(RoleEntity.class);
                sources.addAnnotatedClass(SystemUserEntity.class);
                sources.addAnnotatedClass(DsaApplicationEntity.class);
                sources.addAnnotatedClass(RegionsEntity.class);
                sources.addPackage("com.egram.api.entity");
                sources.addPackage("com.egram.api.entity.loan");

                SessionFactory sessionFactory = sources.buildMetadata().buildSessionFactory();
                try (var session = sessionFactory.openSession()) {
                    session.beginTransaction().commit();
                }
                sessionFactory.close();
                log.info("Tenant schema initialized successfully for tenantId={}", tenantId);
                return;
            } catch (Exception e) {
                lastException = e;
                log.warn("Failed to initialize tenant schema for tenantId={}, attempt={}: {}", tenantId, attempt + 1, e.getMessage());
                attempt++;
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                TenantContext.clear();
            }
        }

        log.error("Failed to initialize tenant schema for tenantId={} after {} attempts", tenantId, maxRetries, lastException);
        throw new SomethingWentWrongException("Failed to initialize tenant schema for tenant: " + tenantId, lastException);
    }

    private void verifyTenantSchema(String tenantId, DataSource tenantDataSource) {
        TenantContext.setCurrentTenant(tenantId);
        try {
            log.info("Verifying tenant schema for tenantId={}", tenantId);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(tenantDataSource);

            List<String> tables = jdbcTemplate.queryForList(
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'",
                    String.class
            );
            List<String> lowerTables = tables.stream().map(String::toLowerCase).collect(Collectors.toList());

            if (!lowerTables.contains("role"))
                throw new SomethingWentWrongException("Role table not found in tenant database: " + tenantId);
            if (!lowerTables.contains("system_users"))
                throw new SomethingWentWrongException("System_users table not found in tenant database: " + tenantId);
            if (!lowerTables.contains("dsa_application"))
                throw new SomethingWentWrongException("Dsa_application table not found in tenant database: " + tenantId);

            log.info("Tenant schema verification successful for tenantId={}", tenantId);
        } catch (Exception e) {
            log.error("Failed to verify tenant schema for tenantId: {}", tenantId, e);
            throw new SomethingWentWrongException("Failed to verify tenant schema for tenant: " + tenantId, e);
        } finally {
            TenantContext.clear();
        }
    }
}
