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
package org.egov.bpa.services.extd.masters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.services.PersistenceService;
/*import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.portal.surveyor.model.Surveyor;
import org.egov.portal.surveyor.model.SurveyorDetail;*/
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class SurveyorMasterService {
	private PersistenceService persistenceService;

	//@SuppressWarnings("unchecked")
	/*public Surveyor save(Surveyor surveyor, List<SurveyorDetail> surveyorList, String mode)
	{
		surveyor=buildSurveyorDetails(surveyor,surveyorList);
	if(surveyor.getId()==null){
		persistenceService.persist(surveyor);
	}
	else
	{
		persistenceService.merge(surveyor);
	}
		return surveyor;
	}
	private Surveyor buildSurveyorDetails(Surveyor surveyor,List<SurveyorDetail> surveyorList)
	{
		Set<SurveyorDetail> unitSet=new HashSet<SurveyorDetail>();
    	
			for(SurveyorDetail unitdetail:surveyorList)
	    	{
				
	    		unitdetail.setSurveyor(surveyor);
	    		unitSet.add(unitdetail);
	    	}
	    		surveyor.getSurveyorDetails().clear();
   	
	    		surveyor.getSurveyorDetails().addAll(unitSet);
   	
    	return surveyor;
    	
	}*/
	/*public boolean checkSurveyorNameAlreadyExist(String name,Long survId)
	 {
			if (name != null && !"".equals(name)) {
				Criteria SurveyorCodeCriteria = getCriteriaForSurveyorUserObject(survId);
				SurveyorCodeCriteria.add(Restrictions.eq("surveyor.name", name));
				
				List<Surveyor> surveyourList = SurveyorCodeCriteria.list();
				if (surveyourList.size() > 0)
					return true;
			}
			return false;
		}*/
	@SuppressWarnings("unchecked")
	public List<Boundary> getZoneList()
	{
		List<Boundary> zoneList=getPersistenceService().findAllBy("from BoundaryImpl bndry where bndry.boundaryType.name=? and bndry.parent!=1 order by bndry.name", "Zone");
		return zoneList;
	}
	public boolean checkSurveyorMobileNumberAlreadyExist(String MobileNumber,Long survId)
	 {
			if (MobileNumber != null && !"".equals(MobileNumber)) {
				/*	Criteria surveyourCriteria = getCriteriaForSurveyorUserObject(survId);
				surveyourCriteria.add(Restrictions.eq("userDetail.mobileNumber", MobileNumber));
				
				List<Surveyor> surveyourList = surveyourCriteria.list();
				if (surveyourList.size() > 0)
					return true;*/
			}
			return false;
		}
	
	public boolean checkSurveyorEmailAlreadyExist(String email,Long survId)
	 {
			if (email != null && !"".equals(email)) {
				/*	Criteria surveyourCriteria = getCriteriaForSurveyorUserObject(survId);
				surveyourCriteria.add(Restrictions.eq("userDetail.email", email));
				
				List<Surveyor> surveyourList = surveyourCriteria.list();
				if (surveyourList.size() > 0)
					return true;*/
			}
			return false;
		}

	/*private Criteria getCriteriaForSurveyorUserObject(Long survId) {
		Criteria surveyourCriteria = persistenceService.getSession()
				//.createCriteria(Surveyor.class, "surveyor")
				//.createAlias("surveyor.userDetail", "userDetail");
		if (survId != null)
			//surveyourCriteria.add(Restrictions.ne("surveyor.id", survId));
		surveyourCriteria.add(Restrictions.eq("isEnabled", true));
		return surveyourCriteria; 
	}
	public Surveyor getSurveyorObjectById(Long Id)
	{
		
		Surveyor surveObj=(Surveyor)persistenceService.find("from Surveyor where id=?",Id);
		return surveObj;
	}
	public List<SurveyorDetail> getSurveyDetailListBySurveyorId(Long Id)
	{
		
		List<SurveyorDetail> surveyorDetailList=persistenceService.findAllBy("from SurveyorDetail where surveyor.id=?",Id);
		return surveyorDetailList;
	}
	public List<Surveyor> getSurveyorObjList()
	{
		
		List<Surveyor> surveObjList=persistenceService.findAllBy("from Surveyor order by name");
		return surveObjList;
	}*/
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
