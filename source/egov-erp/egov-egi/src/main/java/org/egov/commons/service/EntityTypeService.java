/*
 * @(#)EntityTypeService.java 3.0, 14 Jun, 2013 12:08:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons.service;

import java.util.List;

import org.egov.commons.utils.EntityType;

public interface EntityTypeService {
	/**
	 * to get the list of active Entities that will used for posting. if Entity is mapped to multiple AccountDetailTypes 
	 * consider the passed input parameter for eg: AccountEntity will have master list of Telephone as well as 
	 * Electricity as AccountDetailTypes
	 * @param accountDetailTypeId
	 * @return
	 */
	public List<? extends EntityType> getAllActiveEntities(Integer accountDetailTypeId);

	/**
	 * Returns the list of active entities filtered using the given filter key. This is mainly used for filtering 
	 * entities to be shown in auto-complete boxes on UI. It is expected that the implementation of this 
	 * method uses both the "name" and "code" to perform the filtering, and supports text
	 * searches (LIKE clause)
	 * @param filterKey The filter key
	 * @param maxRecords Maximum number of records to be returned. If this is passed as -1, 
	 * the method should return all the records.
	 * @param accountDetailTypeId The account detail type id
	 * @return List of filtered active entities
	 */
	public List<? extends EntityType> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId);
}
