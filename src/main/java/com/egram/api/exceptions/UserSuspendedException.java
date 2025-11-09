package com.egram.api.exceptions;

import com.egram.api.constants.UserStatus;

public class UserSuspendedException extends RuntimeException {
	
	/**
	 * @author RAM
	 *
	 */
	private static final long serialVersionUID = 1L;

	public UserSuspendedException(String value) {
		super(value);
	}
	
	public UserSuspendedException(UserStatus status) {
		super(status.getValue());
	}

}
