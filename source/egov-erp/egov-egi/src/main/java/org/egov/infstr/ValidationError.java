/*
 * @(#)ValidationError.java 3.0, 17 Jun, 2013 3:09:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr;

import java.io.Serializable;

public class ValidationError implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String key;
	private final String message;
	private String[] args = null;

	public ValidationError(final String key, final String message) {
		this.key = key;
		this.message = message;
	}

	/**
	 * Gets a message based on a key using the supplied args, as defined in {@link java.text.MessageFormat}, or, if the message is not found, a supplied default value is returned.
	 * @param key the resource bundle key that is to be searched for
	 * @param message the default value which will be returned if no message is found
	 * @param args an array args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	public ValidationError(final String key, final String message, final String... args) {
		this.key = key;
		this.message = message;
		this.args = args;
	}

	public String getKey() {
		return this.key;
	}

	public String getMessage() {
		return this.message;
	}

	public String[] getArgs() {
		return this.args;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
		result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ValidationError other = (ValidationError) obj;
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
			return false;
		}
		if (this.message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!this.message.equals(other.message)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Key=" + this.key + ",Message=" + this.message;
	}

}
