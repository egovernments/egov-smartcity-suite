/*
 * @(#)ResponseHandler.java 3.0, 17 Jun, 2013 11:59:45 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.events.processing;

import org.egov.infstr.events.domain.entity.schema.Response;

public interface ResponseHandler {
	public void respond(Response r);
}
