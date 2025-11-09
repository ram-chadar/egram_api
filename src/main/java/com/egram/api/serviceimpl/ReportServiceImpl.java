package com.egram.api.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.egram.api.dao.ReportDao;
import com.egram.api.dto.LoanDisbursementSummaryResponseDto;
import com.egram.api.dto.report.CustomerReportDto;
import com.egram.api.dto.report.DsaReportDto;
import com.egram.api.dto.report.FinancialReportDto;
import com.egram.api.dto.report.LoanReportDto;
import com.egram.api.dto.report.PerformanceReportDto;
import com.egram.api.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportDao reportDao;
	
	@Autowired
	private ModelMapper mapper;


	@Override
	public CustomerReportDto getCustomerReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoanReportDto getLoanReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override // akash
	public FinancialReportDto getFinancialReport() {
	   return reportDao.getFinancialReport();

	}


	@Override
	public DsaReportDto getDsaReport() {
		// TODO Auto-generated method stub
		return reportDao.getDsaReport();
	}

	@Override
	public PerformanceReportDto getPerformanceReport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoanDisbursementSummaryResponseDto getLoanDisbursementSummary() {
		// TODO Auto-generated method stub
		return null;
	}

}
