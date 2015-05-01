/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.security.spring.filter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SSOPrincipal implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Integer sessionTimeOut; // minutes
	private String userName;
	private String sessionId;
	private long timestamp;
	private final Map<String, String> credentials = new HashMap<String, String>();
	private PasswordEncoder passwordEncoder;

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public static SSOPrincipal valueOf(final String principalString) {
		final SSOPrincipal principal = new SSOPrincipal();
		final String decrypted = principalString;
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
		return passwordEncoder.encode(builder.toString());
	}

	public static void setSessionTimeout(final Integer sessionTimeout) {
		sessionTimeOut = sessionTimeout;
	}
}
