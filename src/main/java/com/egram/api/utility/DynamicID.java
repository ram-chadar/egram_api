package com.egram.api.utility;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DynamicID {

	private static final SecureRandom random = new SecureRandom();

	public static String generateUniqueId(String type, String firstName, String lastName) {

		
		int currentYear = LocalDate.now().getYear();
		String initials = (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();

		int uniqueNumber = 100000 + random.nextInt(900000);

		return type + "-" + currentYear + "-" + initials + uniqueNumber;
	}

	public static String getGeneratedId() {
		// 20250101123456789
		String id = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
		return id;

	}

	public static String getGeneratedLoanId() {
		// LN-2025-00001
		int uniqueNumber = 1 + random.nextInt(99999);
		int currentYear = LocalDate.now().getYear();
		return "LN-" + currentYear + "-" + String.format("%05d", uniqueNumber);
	}

	public static String getGeneratedDocumentId() {
		// DOC-20250101123456789
		return "DOC-" + getGeneratedId();
	}

	public static String getGeneratedCustomerId(String firstName, String lastName) {
		// CUST-2025-AS000001
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		String initials = (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();

		return "CUST-" + currentYear + "-" + initials + String.format("%06d", uniqueNumber);
	}

	public static String getGeneratedRoleId() {
		// ROLE-00001
		int uniqueNumber = 1 + random.nextInt(99999);
		return "ROLE-" + String.format("%05d", uniqueNumber);

	}

	public static String getGeneratedContactUsId() {
		// CONTACTUS-20250101123445789
		return "CONTACTUS-" + getGeneratedId();
	}

	public static String getGeneratedRegionId() {
		// REG-20250101123445734
		return "REG-" + getGeneratedId();
	}

	public static String getGeneratedDisbursementId() {
		// DISB-2025-000001
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		return "DISB-" + currentYear + "-" + String.format("%05d", uniqueNumber);
	}

	public static String getGeneratedLoanDisbursementId() {
		// LD-2025-000001
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		return "LD-" + currentYear + "-" + String.format("%05d", uniqueNumber);
	}

	public static String getGeneratedLoanApplicationId() {
		// LA-2025-000001
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		return "LA-" + currentYear + "-" + String.format("%05d", uniqueNumber);
	}

// Tranche
	public static String getGeneratedTrancheId() {
		// TR-20250101127845789
		return "TR-" + getGeneratedId();
	}

	public static String getGeneratedLoanTrancheId() {
		// LT-20250101156845789
		return "LT-" + getGeneratedId();
	}

	public static String getGeneratedTrancheAuditId() {
		// TA-20250101156765789
		return "TA-" + getGeneratedId();
	}

	public static String getGeneratedRepaymentId() {
		// REP-20250101345845789
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		return "REP-" + currentYear + "-" + String.format("%05d", uniqueNumber);
	}

	//
	public static String getGeneratedReconciliationId() {
		// REC-20250101345845789
		return "REC-" + getGeneratedId();
	}

	public static String getGeneratedSubscriptionPlanId(String type) {
		// BASIC-001
		type = type.trim().toUpperCase();
		int uniqueNumber = 1 + random.nextInt(999);
		return type + "-" + uniqueNumber;
	}
	
	// Tenant ID
	public static String getGeneratedTenantId(String tenantName) {
		 if (tenantName == null || tenantName.trim().isEmpty()) {
	            throw new IllegalArgumentException("tenantName must not be blank");
	        }

	        String[] words = tenantName.trim().split("\\s+");
	        int currentYear = LocalDate.now().getYear();

	        // Step 1: Take all words EXCEPT the last 2
	        String[] remaining = words.length > 2
	                ? Arrays.copyOfRange(words, 0, words.length - 2)
	                : new String[0];

	        // Step 2: Use FULL words (lowercase), join with "_"
	        String part = Arrays.stream(remaining)
	                .filter(word -> !word.isEmpty())
	                .map(String::toLowerCase)
	                .collect(Collectors.joining("_"));

	        // Step 3: Fallback if no words left (e.g., only 1 or 2 words)
	        if (part.isEmpty()) {
	            part = tenantName.trim().split("\\s+")[0].toLowerCase(); // first word
	            if (part.isEmpty()) part = "x";
	        }

	        // Step 4: Random 6-digit number
	        int uniqueNumber = 100000 + random.nextInt(900000);

	        // Step 5: Final ID → TEN-2025-UPPERCASE_PART + NUMBER
	        return String.format("ten_%d_%s%d", currentYear, part.toLowerCase()+"_", uniqueNumber);
	}
	
	// Subscription ID
	public static String getGeneratedSubscriptionId() {
		// SUB-2025-000001
		int uniqueNumber = 1 + random.nextInt(999999);
		int currentYear = LocalDate.now().getYear();
		return "SUB-" + currentYear + "-" + String.format("%05d", uniqueNumber);
		
	}
	
}
