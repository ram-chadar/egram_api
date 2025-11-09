package com.egram.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egram.api.dao.DSAUserMetricsDao;
import com.egram.api.service.DSAUserMetricsService;

@Service
public class DSAUserMetricsServiceImpl implements DSAUserMetricsService {

    @Autowired
    private DSAUserMetricsDao dsaUserMetricsDao;

    @Override
    public long getTotalSystemUsers() {
        return dsaUserMetricsDao.getTotalSystemUsers();
    }

    @Override
    public long getActiveSystemUsers() {
        return dsaUserMetricsDao.getActiveSystemUsers();
    }

    @Override
    public long getDailyNewDsaRegistrations() {
        return dsaUserMetricsDao.getDailyNewDsaRegistrations();
    }

    @Override
    public long getWeeklyNewDsaRegistrations() {
        return dsaUserMetricsDao.getWeeklyNewDsaRegistrations();
    }

    @Override
    public long getMonthlyNewDsaRegistrations() {
        return dsaUserMetricsDao.getMonthlyNewDsaRegistrations();
    }

    @Override
    public long getPendingDsaRegistrations() {
        return dsaUserMetricsDao.getPendingDsaRegistrations();
    }

    @Override
    public long getDeactivedSystemUser() {
        return dsaUserMetricsDao.getDeactivedSystemUser();
    }
}
