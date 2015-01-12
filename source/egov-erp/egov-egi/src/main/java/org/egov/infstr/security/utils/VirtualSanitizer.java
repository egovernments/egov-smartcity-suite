/*
 * @(#)VirtualSanitizer.java 3.0, 18 Jun, 2013 3:59:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.utils;

import static org.apache.commons.lang.StringUtils.isBlank;

import org.egov.exceptions.EGOVRuntimeException;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VirtualSanitizer.java This class used to sanitise user input from possible XSS attacks.
 **/
public final class VirtualSanitizer {

	private static final Logger LOG = LoggerFactory.getLogger(VirtualSanitizer.class);
	private static Policy policy;
	private static AntiSamy antiSamy;

	private static AntiSamy getAntiSamy() throws PolicyException {
		if (antiSamy == null) {
			policy = getPolicy("antisamy-myspace-1.4.3.xml");
			antiSamy = new AntiSamy();
		}
		return antiSamy;
	}

	private static Policy getPolicy(final String name) throws PolicyException {
		final Policy policy = Policy.getInstance(VirtualSanitizer.class.getResource(name));
		return policy;
	}

	public static String sanitize(final String input) {
		try {
			if (isBlank(input)) {
				return input;
			}
			final CleanResults cr = getAntiSamy().scan(input, policy);
			if (cr.getErrorMessages().size() > 0) {
				LOG.error(cr.getErrorMessages().toString());
				throw new EGOVRuntimeException("Found security threat in user input : " + cr.getErrorMessages());
			}
			return input;
		} catch (final Exception e) {
			LOG.error(e.getMessage());
			throw new EGOVRuntimeException("Error occurred while validating inputs", e);
		}

	}

}
