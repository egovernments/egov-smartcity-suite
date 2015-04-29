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
package org.egov.works.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.service.PersonalInformationService;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.TenderResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class TenderResponseService extends PersistenceService<TenderResponse, Long> {
	private static final Logger logger = Logger.getLogger(TenderResponseService.class);
	@Autowired
        private EmployeeService employeeService; 
	private PersonalInformationService personalInformationService;

	
	public List getApprovedByList(Integer deptId) {
		List approvedByList = null;
		try {
			//approvedByList = eisManager.searchEmployee(deptId, 0, null, null, 0);
			HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
			criteriaParams.put("departmentId", deptId);
			criteriaParams.put("isPrimary", "Y");
			approvedByList=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
		} catch (Exception e) {
			throw new EGOVRuntimeException("user.find.error", e);
		}
		return approvedByList;
	}

	public List populateNegotiationPreparedByList(AbstractEstimate abstractEstimate) {
		List negotiationPreparedByList = null;
		if (abstractEstimate != null && abstractEstimate.getExecutingDepartment() != null) {
			try {
				HashMap<String,Object> criteriaParams =new HashMap<String,Object>();
				criteriaParams.put("departmentId", abstractEstimate.getExecutingDepartment().getId());
				criteriaParams.put("isPrimary", "Y");
				//negotiationPreparedByList = eisManager.searchEmployee(abstractEstimate.getExecutingDepartment().getId(), 0,null, null, 0);
				negotiationPreparedByList=personalInformationService.getListOfEmployeeViewBasedOnCriteria(criteriaParams, -1, -1);
			} catch (Exception e) {
				logger.info("-----inside tenderResponseservice---------Exception");
				negotiationPreparedByList = Collections.emptyList();
			}
		} 
		if(negotiationPreparedByList==null)
			negotiationPreparedByList = Collections.emptyList();
		
		return negotiationPreparedByList;
	}

	/*
	 * returns employee name and designation
	 * 
	 * @ return String
	 * 
	 * @ abstractEstimate, eisManager
	 */

	/*public PersonalInformation getPersonalInformation(Position position) {
		PersonalInformation personalInformation = null;
		try {
			personalInformation = employeeService.getEmpForPosition(position.getEfferctiveDate(), position.getId());
		} catch (Exception e) {
			logger.debug("exception " + e);
		}
		return personalInformation;
	}*/


	public void setPersonalInformationService(
			PersonalInformationService personalInformationService) {
		this.personalInformationService = personalInformationService;
	}

}
