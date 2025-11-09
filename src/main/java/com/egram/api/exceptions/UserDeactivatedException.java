package com.egram.api.exceptions;

import com.egram.api.constants.UserStatus;

public class UserDeactivatedException extends RuntimeException {
	
	/**
	 * @author RAM
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UserDeactivatedException(String value) {
		super(value);
	}
	
	public UserDeactivatedException(UserStatus status) {
		super(status.getValue());
	}

}
