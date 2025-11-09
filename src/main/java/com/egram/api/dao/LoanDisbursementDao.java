package com.egram.api.dao;

import java.util.List;

import com.egram.api.entity.loan.DisbursementEntity;
import com.egram.api.entity.loan.LoanDisbursementEntity;
import com.egram.api.entity.loan.ReconciliationEntity;
import com.egram.api.entity.loan.RepaymentEntity;
import com.egram.api.entity.loan.TrancheAuditEntity;
import com.egram.api.entity.loan.TrancheEntity;

public interface LoanDisbursementDao {
    DisbursementEntity saveDisbursement(DisbursementEntity disbursement);
    DisbursementEntity findDisbursementById(String id);
    List<DisbursementEntity> findDisbursementsByLoanId(String loanApplicationId);

    TrancheEntity saveTranche(TrancheEntity tranche);
    TrancheEntity findTrancheById(String id);
    TrancheEntity findByDisbursementIdAndIdempotencyKey(String disbursementId, String idempotencyKey);
    List<TrancheEntity> findTranchesByDisbursementId(String disbursementId);

    RepaymentEntity saveRepayment(RepaymentEntity repayment);
    List<RepaymentEntity> findRepaymentsByLoanId(String loanApplicationId);

    LoanDisbursementEntity findLoanDisbursementByLoanId(String loanApplicationId);
    LoanDisbursementEntity saveLoanDisbursement(LoanDisbursementEntity entity);

    void saveTrancheAudit(TrancheAuditEntity audit);
    void saveReconciliation(ReconciliationEntity recon);
}
