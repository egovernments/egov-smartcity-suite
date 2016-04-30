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
package org.egov.bpa.services.extd.inspection;

import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.InspectMeasurementDtlsExtn;
import org.egov.bpa.models.extd.InspectionChecklistExtn;
import org.egov.bpa.models.extd.InspectionExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ConstructionStagesExtn;
import org.egov.bpa.models.extd.masters.InspectionSourceExtn;
import org.egov.bpa.models.extd.masters.LandBuildingTypesExtn;
import org.egov.bpa.models.extd.masters.LayoutMasterExtn;
import org.egov.bpa.models.extd.masters.RoadWidthMasterExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly=true)
public class InspectionExtnService extends PersistenceService<InspectionExtn, Long>{

	private PersistenceService persistenceService;
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public RegistrationExtn getRegistrationObjectbyId(Long registrationId) {

		return (RegistrationExtn) persistenceService.find("from RegistrationExtn where id=?",registrationId);
	}
	
	public List<InspectionExtn> getInspectionListforRegistrationObject(RegistrationExtn registrationObj) {
		if(isUserMappedToAeOrAeeRole())
			return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? and inspectedBy.id=? order by id desc,inspectionDate desc",registrationObj,Boolean.FALSE,(EgovThreadLocals.getUserId()));
		else
			return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,Boolean.FALSE);
	}
		
	public Boolean isUserMappedToAeOrAeeRole() {
		if(EgovThreadLocals.getUserId()!=null){
			User user = getUserbyId((EgovThreadLocals.getUserId()));
			for(Role role : user.getRoles())
				if(role.getName()!=null && (role.getName().equalsIgnoreCase(BpaConstants.BPAAEROLE) || 
						role.getName().equalsIgnoreCase(BpaConstants.BPAAEEROLE)))
				{
					return true;
				}
				
		}
		return false;
	}
	
	
	public ServiceTypeExtn getServiceTypeById(Long serviceTypeId) {

		return (ServiceTypeExtn) persistenceService.find(" from ServiceTypeExtn where id=?",serviceTypeId);
	}
	
	public List<InspectionExtn> getAllSiteInspectionListforRegistrationObject(RegistrationExtn registrationObj) {
		
		return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,Boolean.TRUE);
}

	@Transactional
	public InspectionExtn save(InspectionExtn inspection,User loginuser,String postponeReason,Boolean isInspected,EgwStatus  status) {
		//inspection.getInspectionDetails().setId(inspection.getId());
		
		if(inspection!=null){
			inspection.setIsInspected(isInspected);
			if(loginuser!=null)
				inspection.setInspectedBy(loginuser);
			if(postponeReason!=null && !"".equals(postponeReason))
				inspection.setPostponementReason(postponeReason);
			if(status!=null)
				inspection.getRegistration().setEgwStatus(status);
		}
		
		if(inspection.getId()!=null){
			if(inspection.getModifiedDate()==null)
				inspection.setModifiedDate(new Date());
				inspection= (InspectionExtn) merge(inspection);
		}else
			inspection= (InspectionExtn) persist(inspection);
		
		return inspection;
	}

	public User getUserbyId(Long userId) {
		
		return (User)persistenceService.find("from User where id=?",userId);
	}
	

public List<CheckListDetailsExtn> getCheckListByServiceAndType(Long serviceTypeId,String checkListTypeVal) {
		
		Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeId));
		checkListDet.add(Restrictions.eq("checkList.checklistType",checkListTypeVal)); 
		return checkListDet.list();
		
	}

	public List<CheckListDetailsExtn> getCheckListforService(Long serviceTypeId) {
		
		Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeId));
		checkListDet.add(Restrictions.eq("checkList.checklistType",BpaConstants.INSPECTION));
		return checkListDet.list();
	
		
	}

	public List<InspectMeasurementDtlsExtn> getInspectionMeasureMentDtls(
			Long inspectionId) {
		
		Criteria inspectionmeasureDet =getSession().createCriteria(InspectMeasurementDtlsExtn.class,"inspectionmeasurement");
		inspectionmeasureDet.createAlias("inspectionmeasurement.inspectionDetails", "inspectiondetails");
		inspectionmeasureDet.add(Restrictions.eq("inspectiondetails.id", inspectionId));
		return inspectionmeasureDet.list();
	}
	


	public InspectionSourceExtn getInspectionSource(
			String code) {
		
		return (InspectionSourceExtn)persistenceService.find("from InspectionSourceExtn where code=?",code);
	}

	public List<InspectionChecklistExtn> getInspectionCheckDtls(Long inspectionId) {
		
		Criteria checkListDet =getSession().createCriteria(InspectionChecklistExtn.class,"inschecklist");
		checkListDet.createAlias("inschecklist.inspectionDetails", "inspectiondetails");
		checkListDet.createAlias("inschecklist.checklistDetails", "chklistmstr");
		
		checkListDet.add(Restrictions.eq("inspectiondetails.id", inspectionId));
		checkListDet.addOrder(Order.asc("chklistmstr.id"));
		return checkListDet.list();
	
		
	}
	
	public InspectionExtn getInspectionbyId(Long inspectionId){
		return find("from InspectionExtn where id=?", inspectionId);
	}
	
	

	public List<ConstructionStagesExtn> getAllConstructionStages() {
		
		 return persistenceService.findAllBy("from ConstructionStagesExtn order by description asc");
		
	}
	
	public List<InspectionExtn> getSiteInspectionListforRegistrationObject(RegistrationExtn registrationObj) {
		if(isUserMappedToAeOrAeeRole())
			return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? and inspectedBy.id=? order by id desc,inspectionDate desc",registrationObj,Boolean.TRUE,(EgovThreadLocals.getUserId()));
		else
			return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,Boolean.TRUE);
	}
	
	public List<InspectionExtn> getSiteInspectionListforRegBySurveyorAndOfficial(RegistrationExtn registrationObj) {
			return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,Boolean.TRUE);
	}
	
	public List<InspectionExtn> getSiteInspectionListforRegistrationObjectWhereInsDetailsEnetered(RegistrationExtn registrationObj,User OfficialUser) {
   if(OfficialUser!=null){
		return (List<InspectionExtn>) findAllBy("from InspectionExtn where registration=? and inspectionDate is not null and inspectedBy=? and isInspected=? order by id desc,inspectionDate desc",registrationObj,OfficialUser,Boolean.TRUE);
   }
   else 
	   return null;
   }

	public ConstructionStagesExtn getConstructionStage(Long constructionstage) {
		return (ConstructionStagesExtn) persistenceService.find("from ConstructionStagesExtn where id=?",constructionstage);
		
	}

	public List<ConstructionStagesExtn> getAllSurroundedBuildingDetails() {
		
		 return persistenceService.findAllBy("from SurroundedBldgDtlsExtn where isActive=1 order by id asc");
		
	}
	
	public List<LayoutMasterExtn> getAllLayoutTypes() {
		
		 return persistenceService.findAllBy("from LayoutMasterExtn order by id asc");
		
	}
	
	public List<LandBuildingTypesExtn> getAllLandBuildingTypes(String type) {
		List<LandBuildingTypesExtn> landBuildingTypesExtnList = new ArrayList<LandBuildingTypesExtn>();
		if(type!=null && type!=""){
			if(type.equalsIgnoreCase("Land"))
				landBuildingTypesExtnList= persistenceService.findAllBy("from LandBuildingTypesExtn where isactive=1 and type!='Building' order by id asc");
			else if(type.equalsIgnoreCase("Building"))
				landBuildingTypesExtnList= persistenceService.findAllBy("from LandBuildingTypesExtn where isactive=1 and type!='Land' order by id asc");
		}
		else{
			landBuildingTypesExtnList= persistenceService.findAllBy("from LandBuildingTypesExtn where isactive=1 order by id asc");
		}
		return landBuildingTypesExtnList;
	}
	
	public List<RoadWidthMasterExtn> getAllRoadWidth() {
		
		 return persistenceService.findAllBy("from RoadWidthMasterExtn order by id asc");
		
	}
}
