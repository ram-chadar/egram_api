package com.egram.api.daoimpl;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.egram.api.constants.ApprovalStatus;
import com.egram.api.constants.UserStatus;
import com.egram.api.dao.DSAUserMetricsDao;
import com.egram.api.entity.DsaApplicationEntity;
import com.egram.api.entity.SystemUserEntity;

@Repository
@Transactional
public class DSAUserMetricsDaoImpl implements DSAUserMetricsDao {

	@Autowired
	@Qualifier("tenantSessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public long getTotalSystemUsers() {
		try (Session session = sessionFactory.openSession()) {
			Criteria criteria = session.createCriteria(SystemUserEntity.class);
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getActiveSystemUsers() {
		try (Session session = sessionFactory.openSession()) {
			Criteria criteria = session.createCriteria(SystemUserEntity.class);
			criteria.add(Restrictions.eq("status", UserStatus.ACTIVE.getValue()));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getDailyNewDsaRegistrations() {
		try (Session session = sessionFactory.openSession()) {
			LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
			Criteria criteria = session.createCriteria(DsaApplicationEntity.class);
			criteria.add(Restrictions.ge("createdAt", oneDayAgo));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getWeeklyNewDsaRegistrations() {
		try (Session session = sessionFactory.openSession()) {
			LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
			Criteria criteria = session.createCriteria(DsaApplicationEntity.class);
			criteria.add(Restrictions.ge("createdAt", sevenDaysAgo));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getMonthlyNewDsaRegistrations() {
		try (Session session = sessionFactory.openSession()) {
			LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
			Criteria criteria = session.createCriteria(DsaApplicationEntity.class);
			criteria.add(Restrictions.ge("createdAt", thirtyDaysAgo));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getPendingDsaRegistrations() {
		try (Session session = sessionFactory.openSession()) {
			Criteria criteria = session.createCriteria(DsaApplicationEntity.class);
			criteria.add(Restrictions.eq("approvalStatus", ApprovalStatus.PENDING.getValue()));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}

	@Override
	public long getDeactivedSystemUser() {
		try (Session session = sessionFactory.openSession()) {
			Criteria criteria = session.createCriteria(SystemUserEntity.class);
			criteria.add(Restrictions.eq("status", UserStatus.DEACTIVATED.getValue()));
			criteria.setProjection(Projections.rowCount());
			return (Long) criteria.uniqueResult();
		}
	}
}
