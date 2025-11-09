package com.egram.api.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egram.api.constants.UserStatus;
import com.egram.api.dao.AdminDao;
import com.egram.api.dao.DSADao;
import com.egram.api.dao.SystemUserDao;
import com.egram.api.dto.SystemUserDto;
import com.egram.api.entity.DsaApplicationEntity;
import com.egram.api.entity.RegionsEntity;
import com.egram.api.entity.RoleEntity;
import com.egram.api.entity.SystemUserEntity;
import com.egram.api.exceptions.ResourceNotFoundException;
import com.egram.api.exceptions.UserDeactivatedException;
import com.egram.api.exceptions.UserSuspendedException;
import com.egram.api.security.CustomUserDetail;
import com.egram.api.service.SystemUserService;

/**
 * @author RAM
 *
 */
@Service
@Transactional(readOnly = true)
public class SystemUserServiceImpl implements SystemUserService {

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private SystemUserDao dao;

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private DSADao dsaDao;

	@Override
	@Transactional("tenantTransactionManager")
	public CustomUserDetail loadUserByUserId(String userId) {
		var customUserDetail = dao.loadUserByUserId(userId);

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
	public SystemUserEntity getSystemUserByUsername(String username) {
		SystemUserEntity userEntity = dao.getSystemUserByUsername(username);
		if (userEntity == null) {
			throw new ResourceNotFoundException("User not found with username = " + username);
		}
		return userEntity;
	}

	@Override
	public List<SystemUserEntity> getAllSystemUser() {
		List<SystemUserEntity> allSystemUser = dao.getAllSystemUser();
		if (allSystemUser == null) {
			throw new ResourceNotFoundException("User not found");
		}
		return allSystemUser;
	}

	@Override
	public SystemUserEntity updateSystemUser(SystemUserDto userDto) {

		List<RegionsEntity> allRegionsByIds = adminDao.getAllRegionsByIds(userDto.getRegions());
		List<RoleEntity> allRoleByIds = adminDao.getAllRoleByIds(userDto.getRoles());
		DsaApplicationEntity dsaById = dsaDao.getDSAById(userDto.getDsaApplicationId());

		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

		SystemUserEntity userEntity = mapper.map(userDto, SystemUserEntity.class);
		userEntity.setRegions(allRegionsByIds);
		userEntity.setRoles(allRoleByIds);
		userEntity.setDsaApplicationId(dsaById);

		dao.updateSystemUser(userEntity);

		return userEntity;
	}

}
