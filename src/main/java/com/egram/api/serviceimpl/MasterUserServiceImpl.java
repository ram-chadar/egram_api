package com.egram.api.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egram.api.constants.UserStatus;
import com.egram.api.dao.MasterUserDao;
import com.egram.api.entity.master.MasterUserEntity;
import com.egram.api.exceptions.ResourceNotFoundException;
import com.egram.api.exceptions.UserDeactivatedException;
import com.egram.api.exceptions.UserSuspendedException;
import com.egram.api.security.CustomUserDetail;
import com.egram.api.service.MasterUserService;

@Service
@Transactional(readOnly = true)
public class MasterUserServiceImpl implements MasterUserService {

    @Autowired
    private MasterUserDao dao;

    @Override
    @Transactional("masterTransactionManager")
    public CustomUserDetail loadUserByUserId(String userId) {
        CustomUserDetail customUserDetail = dao.loadUserByUserId(userId);

        if (customUserDetail != null) {
            String status = customUserDetail.getStatus();

            if (UserStatus.DEACTIVATED.getValue().equalsIgnoreCase(status)) {
                throw new UserDeactivatedException(UserStatus.DEACTIVATED);
            }
            if (UserStatus.SUSPENDED.getValue().equalsIgnoreCase(status)) {
                throw new UserSuspendedException(UserStatus.SUSPENDED);
            }
        }

        return customUserDetail;
    }

    @Override
    public MasterUserEntity getMasterUserByUsername(String username) {
        MasterUserEntity userEntity = dao.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Master user not found with username = " + username));
        return userEntity;
    }

    // Add update/create methods if needed, similar to SystemUserServiceImpl
}