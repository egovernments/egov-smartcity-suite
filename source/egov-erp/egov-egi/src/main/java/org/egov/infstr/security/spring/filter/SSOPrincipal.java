/*
 * @(#)SSOPrincipal.java 3.0, 12 Jul, 2013 1:52:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.egov.infstr.security.utils.CryptoHelper;

public class SSOPrincipal implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Integer sessionTimeOut; // minutes
	private String userName;
	private String sessionId;
	private long timestamp;
	private final Map<String, String> credentials = new HashMap<String, String>();

	public static SSOPrincipal valueOf(final String principalString) {
		final SSOPrincipal principal = new SSOPrincipal();
		final String decrypted = CryptoHelper.decrypt(principalString);
		final String[] values = decrypted.split("@@");
		for (final String pair : values) {
			final String[] kv = pair.split("=");
			if ("js".equals(kv[0])) {
				principal.setSessionId(kv[1]);
			} else if ("u".equals(kv[0])) {
				principal.setUserName(kv[1]);
			} else if ("t".equals(kv[0])) {
				principal.setTimestamp(Long.parseLong(kv[1]));
			} else if (kv.length == 2) {
				principal.addCredential(kv[0], kv[1]);
			}
		}
		return principal;
	}

	public boolean hasExpired() {
		final double diff = System.currentTimeMillis() - this.timestamp;
		return diff / (1000 * 60) > sessionTimeOut;
	}

	public String getUserName() {
		return this.userName;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}

	public Map<String, String> getCredentials() {
		return this.credentials;
	}

	public void addCredential(final String key, final String value) {
		this.credentials.put(key, value);
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("js=").append(this.sessionId).append("@@").append("u=").append(this.userName).append("@@");
		for (final Entry<String, String> cred : this.credentials.entrySet()) {
			builder.append(cred.getKey()).append("=").append(cred.getValue()).append("@@");
		}
		builder.append("t=").append(this.timestamp);
		return CryptoHelper.encrypt(builder.toString());
	}

	public static void setSessionTimeout(final Integer sessionTimeout) {
		sessionTimeOut = sessionTimeout;
	}
}
