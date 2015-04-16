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
package org.egov.infstr.security.utils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionCache.class);

	/** Default to 30 mins **/
	private final Integer sessionTimeOutinMins = 30;

	private static final int MAX_EXPECTED_NUMBER_OF_CONCURRENT_LOGINS_AND_LOGOUTS = 10;
	private final ConcurrentMap<String, EgovSession> sessionMap = new ConcurrentHashMap<String, EgovSession>(375, 0.75f, MAX_EXPECTED_NUMBER_OF_CONCURRENT_LOGINS_AND_LOGOUTS);

	public void addSession(final HttpSession session) {
		final EgovSession egSession = new EgovSession(session.getId(), session.getLastAccessedTime(), true, this.sessionTimeOutinMins);
		this.sessionMap.put(session.getId(), egSession);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("addSession : Added to sessionMap session id " + session.getId());
			LOGGER.debug("addSession : sessionMap = " + this.sessionMap.toString());
		}
	}

	public boolean isSessionValid(final String sessionId) {
		final EgovSession egSession = this.sessionMap.get(sessionId);

		if (egSession != null) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("isSessionValid : Session with session id - " + sessionId + " 's valid state is : " + egSession.isValid());
			}
			return egSession.isValid();
		} else {
			return true;
		}
	}

	public void invalidateSession(final String sessionId) {
		this.sessionMap.remove(sessionId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("invalidateSession: removed from cache session id -" + sessionId + "; sessionMap is now = " + this.sessionMap);
		}
	}
}

class EgovSession implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sessionId;
	private long timestamp;
	private boolean valid;
	private Integer sessionTimeOut;

	public EgovSession(final String sessionId, final long timestamp, final boolean valid, final Integer sessionTimeOut) {
		super();
		this.sessionId = sessionId;
		this.timestamp = timestamp;
		this.valid = valid;
		this.sessionTimeOut = sessionTimeOut;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void setValid(final boolean valid) {
		this.valid = valid;
	}

	public Integer getSessionTimeOut() {
		return this.sessionTimeOut;
	}

	public void setSessionTimeOut(final Integer sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}

	public boolean hasExpired() {
		final double diff = System.currentTimeMillis() - this.timestamp;
		return diff / (1000 * 60) > this.sessionTimeOut;
	}

	@Override
	public String toString() {
		return this.sessionId;
	}
}
