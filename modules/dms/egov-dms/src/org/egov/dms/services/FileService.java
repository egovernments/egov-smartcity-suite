/*
 * @(#)FileService.java 3.0, 16 Jul, 2013 11:33:32 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.services;

import org.egov.dms.models.GenericFile;
import org.egov.infstr.services.PersistenceService;

public class FileService extends PersistenceService<GenericFile, Long> {

	public GenericFile getFileByNumber(String namedQuery,String fileNumber) {
		return (GenericFile)findByNamedQuery(namedQuery, fileNumber);
	}
}
