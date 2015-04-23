/*
 * SearchPropertyDAO.java Created on Nov 25 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.dao.property;

import java.util.Date;
import java.util.List;

import org.egov.infstr.ValidationException;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.SearchResult;
import org.egov.ptis.exceptions.PropertyNotFoundException;

/**
 * @author Gayathri
 * @version 2.00
 */
public interface SearchPropertyDAO {
	public SearchResult getBasicPropertyByRegNum(String regNum) throws PropertyNotFoundException;

	public List getPropertyByBoundry(Integer zoneID, Integer wardID, Integer colonyID)
			throws PropertyNotFoundException;

	public List getPropertyIDByBoundryForWardBlockStreet(Integer wardID, Integer blockID,
			Integer streetID) throws PropertyNotFoundException;

	public List getInActivePropertyByBoundary(List boundryIDs) throws PropertyNotFoundException;

	public SearchResult getPropertyByBoundryAndMunNo(Integer zoneID, Integer wardID,
			Integer colonyID, Integer munNo) throws PropertyNotFoundException;

	public SearchResult getPropertyByPropertyId(String propertyId) throws PropertyNotFoundException;

	public List getPropertyByBoundryAndOwnerName(Integer boundryID, String ownerFullName,
			String phNumber) throws PropertyNotFoundException;

	public List getPropertyByOldMuncipalNo(String oldMuncipalNo) throws PropertyNotFoundException;

	public List getPropertiesById(List streetIds, String ownerName, String phoneNumber)
			throws PropertyNotFoundException;

	public SearchResult getPropertyByKhataNumber(String khataNumber)
			throws PropertyNotFoundException;

	public List getPropertyByRvAmout(Integer boundaryID, Character RvSel, String lowVal,
			String HighVal) throws PropertyNotFoundException;

	public List getPropertyByDmdAmout(Integer boundaryID, Character DmdSel, Character DmdChoice,
			String lowVal, String HighVal) throws PropertyNotFoundException;

	public List getPropertyByMobileNumber(String mobileNum) throws PropertyNotFoundException;

	public List getPropertyByBillNumber(String billNumber) throws PropertyNotFoundException;

	public List getPropertyByBoundryAndOwnerNameAndHouseNo(Integer boundryID, String ownerName,
			String houseNo, String oldHouseNo) throws PropertyNotFoundException;
	
	public List<Property> getPropertyByObjectionDetails(Long propertyTypeMasterId,String objectionNumber, Date fromObjection,
			Date toObjection) throws ValidationException ;
}
