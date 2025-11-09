package com.egram.api.service;

import java.util.List;

import com.egram.api.dto.ContactUsDTO;
import com.egram.api.dto.CustomerDTO;
import com.egram.api.dto.DocumentDTO;
import com.egram.api.dto.LoanApplicationDTO;
import com.egram.api.entity.CustomerEntity;

public interface CustomerService {
	public abstract String contactUs(ContactUsDTO contactUs);

	public abstract String createCustomer(CustomerDTO customerDTO);

	public abstract String checkLoanEligibility(String customerId);

	public abstract String customerLoanApplication(LoanApplicationDTO loanApplicationDTO);

	public abstract List<CustomerEntity> getAllCustomers();
	
	public abstract List<CustomerEntity> getCustomersByDsaAgentId(String dsaAgentId);


	public abstract CustomerEntity getCustomerById(String id);

	public abstract CustomerDTO updateCustomer(CustomerDTO customerDTO);

	public abstract void cancelCustomerLoanApplication(String id);

	public abstract void uploadDocument(String customerId, DocumentDTO documentDTO);

	public abstract List<DocumentDTO> getDocumentsByCustomerId(String customerId);

	public abstract DocumentDTO getDocumentById(String customerId, String documentId);

	public abstract void deleteDocument(String customerId, String documentId);

}
