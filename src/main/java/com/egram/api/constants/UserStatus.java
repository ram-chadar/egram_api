package com.egram.api.constants;

public enum UserStatus {
	DEACTIVATED("Deactivated"), 
	SUSPENDED("Suspended"), 
	ACTIVE("Active");

	private final String value;

	UserStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
