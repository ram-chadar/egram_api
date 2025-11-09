package com.egram.api.service;

import java.util.List;

import com.egram.api.dto.DSAApplicationDTO;
import com.egram.api.dto.DsaKycDto;
import com.egram.api.entity.DsaKycEntity;

/**
 * @author RAM
 *
 */
public interface DSAService {

	public abstract DSAApplicationDTO getDSAById(String dsaID); //public

	public abstract DSAApplicationDTO dsaApplication(DSAApplicationDTO dsaRegistrationDTO); //public

	public abstract List<DSAApplicationDTO> getAllDsaApplication(); //admin, subadmin
	
	public abstract List<String> getAllApprovedDsa();
	public abstract List<String> getAllApprovedDsaWithNoSystemUser();

	public abstract String notifyReview(String registrationId, String approvalStatus, String type);// subadmin

	public abstract String systemUserKyc(DsaKycDto kyc_DTO); //public
	
	public abstract DsaKycEntity getDsaKycByDsaId(String dsaRegistrationId);// subadmin
	
	public abstract void emailVerificationRequest(String dsaId);
	
	public abstract void verifyEmail(String dsaId,String token);
	
	
}
