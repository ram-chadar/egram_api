package com.egram.api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerReportDto {
	private int totalRegisteredCustomers;
	private int newCustomersToday;
	private int newCustomersThisWeek;
	private int newCustomersThisMonth;
	private int activeCustomers;
	private int inactiveCustomers;
}
