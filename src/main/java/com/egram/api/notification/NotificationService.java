package com.egram.api.notification;

import java.util.List;

import com.egram.api.dto.DSAApplicationDTO;
import com.egram.api.entity.RegionsEntity;
import com.egram.api.entity.RoleEntity;

/**
 * @author RAM
 *
 */
public interface NotificationService {

	public void dsaRegistrationConfirmationMail(String to, String dsaName, String dsaId, String registeredName,
			String contactInfo);

	public void dsaReviewMail(String to, String dsaName, String reviewStatus, String type,String dsaId);

	public void dsaKycConfirmationMail(String to, String kycId, String dsaId, String dsaName, String contact,
			String address, List<String> docs);

	public void userProfileCreatedConfirmationMail(DSAApplicationDTO dsaById, String username, String password,
			List<RoleEntity> roleEntity, List<RegionsEntity> regionsEntity);

	public void emailVerificationRequestMail(String dsaId, String dsaName, String emailTo, String token);
	
	public void tenantCreationConfirmationMail(String tenantName, String tenantId, String email);
}
