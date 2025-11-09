package com.egram.api.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egram.api.dao.LoanConditionDao;
import com.egram.api.dto.LoanConditionDto;
import com.egram.api.entity.loan.LoanConditioEntity;
import com.egram.api.exceptions.ResourceAlreadyExistsException;
import com.egram.api.service.LoanConditionService;
import com.egram.api.utility.DynamicID;

/**
 * @author RAM
 *
 */
@Service
@Transactional(readOnly = true)
public class LoanConditionServiceImpl implements LoanConditionService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private LoanConditionDao dao;

	@Override
	public LoanConditionDto createLoanCondition(LoanConditionDto loanConditionDto) {
		LoanConditionDto loan = getLoanConditionByBank_Loantype(loanConditionDto.getBankName(), loanConditionDto.getLoanType());
		if(loan==null) {
			String id = DynamicID.getGeneratedId();
			loanConditionDto.setId(id);
			LoanConditioEntity entity = dao.createLoanCondition(mapper.map(loanConditionDto, LoanConditioEntity.class));
			return mapper.map(entity, LoanConditionDto.class);
		}else {
			return null;
		}
		
	}

	@Override
	public LoanConditionDto getLoanConditionById(String id) {
		LoanConditioEntity entity = dao.getLoanConditionById(id);

		return mapper.map(entity, LoanConditionDto.class);
	}

	@Override
	public List<LoanConditionDto> getAllLoanConditions() {

		return null;
	}

	@Override
	public LoanConditionDto updateLoanCondition(LoanConditionDto loanConditionDto) {

		return null;
	}

	@Override
	public void deleteLoanCondition(String id) {

	}

	@Override
	public LoanConditionDto getLoanConditionByBank_Loantype(String bankName, String loanType) {

		LoanConditioEntity entity = dao.getLoanConditionByBank_Loantype(bankName, loanType);
		if (entity == null) {
			return mapper.map(dao.createLoanCondition(entity), LoanConditionDto.class);
			
		} else {
			throw new ResourceAlreadyExistsException("Loan Condition Already Exists for : "+bankName);
		}

	}

	@Override
	public List<String> getAllBanksNames() {
		
		return null;
	}

	@Override
	public double getIntrestRate(String bankName) {
		
		return 0;
	}

}
