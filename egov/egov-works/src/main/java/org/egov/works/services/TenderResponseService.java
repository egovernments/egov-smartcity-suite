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
