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
package org.egov.bpa.services.extd.lettertoparty;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.masters.CheckListDetailsExtn;
import org.egov.bpa.models.extd.masters.ServiceTypeExtn;
import org.egov.bpa.services.extd.common.BpaNumberGenerationExtnService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class LetterToPartyExtnService extends PersistenceService<LetterToPartyExtn, Long>{
	
	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService persistenceService;
	private BpaNumberGenerationExtnService bpaNumberGenerationExtnService;
	
	public RegistrationExtn getRegistrationObjectbyId(Long registrationId) {

		return (RegistrationExtn) persistenceService.find("from RegistrationExtn where id=?",registrationId);
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	public ServiceTypeExtn getServiceTypeById(Long serviceTypeId) {

		return (ServiceTypeExtn) persistenceService.find(" from ServiceTypeExtn where id=?",serviceTypeId);
	}
	
	public List<CheckListDetailsExtn> getCheckListforService(Long serviceTypeId) {
		
		Criteria checkListDet =getSession().createCriteria(CheckListDetailsExtn.class,"checklistdet");
		checkListDet.createAlias("checklistdet.checkList", "checkList");
		checkListDet.createAlias("checkList.serviceType", "servicetype");
		checkListDet.add(Restrictions.eq("servicetype.id", serviceTypeId));
		checkListDet.add(Restrictions.ilike("checkList.checklistType", "DOCUMENTATION"));
		return checkListDet.list();
	}

		
	public LetterToPartyExtn getLatestLetterToPartyForRegObj(RegistrationExtn registrationObj) {
		LetterToPartyExtn lettertoParty = null;
		
		Criteria lpCriteria = getSession().createCriteria(LetterToPartyExtn.class,"letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		lpCriteria.setMaxResults(1);
		
		lettertoParty = (LetterToPartyExtn) lpCriteria.uniqueResult();
		return lettertoParty;
	}
	/*
	 * To Check Approved letter To Party is present for Registration....
	 */
	public LetterToPartyExtn getLatestLetterToPartyForRegnTOUniqueValidate(RegistrationExtn registrationObj) {
		LetterToPartyExtn lettertoParty = null;
		
		Criteria lpCriteria = getSession().createCriteria(LetterToPartyExtn.class,"letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.createAlias("registration.egwStatus","status");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.add(Restrictions.ne("status.code", BpaConstants.CREATEDLETTERTOPARTY));
		lpCriteria.add(Restrictions.isNull("letterToParty.sentDate"));
		lpCriteria.add(Restrictions.isNull("letterToParty.isHistory"));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		lpCriteria.setMaxResults(1);
		
		lettertoParty = (LetterToPartyExtn) lpCriteria.uniqueResult();
		return lettertoParty;
	}
	
	public List<LetterToPartyExtn> getListofLetterToPartyForRegObj(RegistrationExtn registrationObj,LetterToPartyExtn letterToParty) {
		
		Criteria lpCriteria = getSession().createCriteria(LetterToPartyExtn.class,"letterToParty");
		lpCriteria.createAlias("letterToParty.registration", "registration");
		lpCriteria.add(Restrictions.eq("registration", registrationObj));
		lpCriteria.add(Restrictions.isNull("isHistory"));
		if(letterToParty!=null&&letterToParty.getId()!=null)
		lpCriteria.add(Restrictions.ne("id", letterToParty.getId()));
		lpCriteria.addOrder(Order.desc("letterToParty.id"));
		
		return lpCriteria.list();
		
	}
	 /*
	  * Get API for Comparinng LP sentDate and sysdate . sysdate-LP sentDate<=10 days allow letter to reply action and syadate-LP sentDate>10 allow reject registration
	  */
	 public List< LetterToPartyExtn> getLetterToPartyForRegnByComparingLPSentDateWithSysDateToTenDays(RegistrationExtn registrationObj,Date sentDate) {
		StringBuilder stringLp=new StringBuilder();
		stringLp.append("FROM LetterToPartyExtn lpextn where (:todaysdate)-lpextn.sentDate > :ten and lpextn.registration.id= :regnId and isHistory is null");
		Query query=HibernateUtil.getCurrentSession().createQuery(stringLp.toString());	
		query.setDate("todaysdate", new Date());
		query.setString("ten", BpaConstants.DATECOMPARETEN);
		query.setLong("regnId", registrationObj.getId());
		List<LetterToPartyExtn> queryList = (List<LetterToPartyExtn>) query.list();
	 return  queryList;
	 }

	public BpaNumberGenerationExtnService getBpaNumberGenerationExtnService() {
		return bpaNumberGenerationExtnService;
	}

	public void setBpaNumberGenerationExtnService(
			BpaNumberGenerationExtnService bpaNumberGenerationService) {
		this.bpaNumberGenerationExtnService = bpaNumberGenerationService;
	}

}
