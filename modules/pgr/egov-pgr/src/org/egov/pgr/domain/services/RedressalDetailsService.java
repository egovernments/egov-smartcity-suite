/*
 * @(#)RedressalDetailsService.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.services;

import org.egov.lib.admbndry.Boundary;
import org.egov.pgr.domain.entities.ComplaintDetails;
import org.egov.pgr.domain.entities.ComplaintTypes;
import org.egov.pgr.domain.entities.RedressalDetails;
import org.egov.pgr.services.persistence.EntityService;
import org.egov.pims.commons.Position;

public interface RedressalDetailsService extends EntityService<RedressalDetails, Long> {

	public ComplaintDetails createNewRedressal(ComplaintDetails complaint);

	public Position getRedressalOfficerPos(ComplaintTypes complaintType, Boundary boundary, Boundary topLevelbndry);

}
