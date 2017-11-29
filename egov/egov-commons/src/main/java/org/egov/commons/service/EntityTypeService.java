/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.commons.service;

import org.egov.commons.utils.EntityType;
import org.egov.infra.validation.exception.ValidationException;

import java.util.List;

/**
 * 
 * @author mani
 * Every sevice  of model  which implements EntityType should implement this service.
 *
 */
public interface EntityTypeService {
	/**
	 * to get the list of active Entities that will used for posting.
	 * if Entity is mapped to multiple AccountDetailTypes consider the passed input parameter
	 * for eg: AccountEntity will have master list of Telephone as well as Electricity as AccountDetailTypes
	 *         
	 * @param accountDetailTypeId
	 * @return
	 */
	public List<? extends EntityType> getAllActiveEntities(Integer accountDetailTypeId);

	/**
	 * Returns the list of active entities filtered using the given filter key.
	 * This is mainly used for filtering entities to be shown in auto-complete
	 * boxes on UI. It is expected that the implementation of this method uses
	 * both the "name" and "code" to perform the filtering, and supports text
	 * searches (LIKE clause)
	 * 
	 * @param filterKey
	 *            The filter key
	 * @param maxRecords
	 *            Maximum number of records to be returned. If this is passed as
	 *            -1, the method should return all the records.
	 * @param accountDetailTypeId
	 *            The account detail type id
	 * @return List of filtered active entities
	 */
	public List<? extends EntityType> filterActiveEntities(String filterKey, int maxRecords, Integer accountDetailTypeId);

	/**
	 * Returns the list of Asset Codes linked to  accountdetailkey (projectCodeId).
	 *         
	 * @param accountdetailkey
	 * @return List of filtered Asset Codes
	 * @throws ValidationException
	 */
	public List getAssetCodesForProjectCode(Integer accountdetailkey) throws ValidationException;

	/**
    *
    * @param idsList
    * @return
    * @throws ValidationException
    * returns only those which are invalid for RTGSPayment
    */
   public List<? extends EntityType> validateEntityForRTGS(List<Long> idsList) throws ValidationException;
   /**
    *
    * @param idsList
    * @return
    * @throws ValidationException
    * will return entities for the given ids which are same as accountdetailkeyid
    */
   public List<? extends EntityType> getEntitiesById(List<Long> idsList) throws ValidationException;

}