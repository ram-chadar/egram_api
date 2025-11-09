package com.egram.api.dao;

import com.egram.api.dto.LoanDisbursementSummaryResponseDto;
import com.egram.api.dto.report.CustomerReportDto;
import com.egram.api.dto.report.DsaReportDto;
import com.egram.api.dto.report.FinancialReportDto;
import com.egram.api.dto.report.LoanReportDto;
import com.egram.api.dto.report.PerformanceReportDto;

public interface ReportDao {
	// 👥 Customer Reports
    CustomerReportDto getCustomerReport();

    // 💰 Loan Reports
    LoanReportDto getLoanReport();

    // 📈 Financial Reports
    FinancialReportDto getFinancialReport();

    // 🏦 DSA Reports
    DsaReportDto getDsaReport();

    // 📊 Performance Reports
    PerformanceReportDto getPerformanceReport();

    // 📊 Loan Disbursement Trend
    LoanDisbursementSummaryResponseDto getLoanDisbursementSummary();
}
