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
package org.egov.bpa.services.extd.autoDcr;

import org.apache.log4j.Logger;
import org.egov.bpa.constants.BpaConstants;
import org.egov.bpa.models.extd.AutoDcrExtn;
import org.egov.bpa.models.extd.LetterToPartyExtn;
import org.egov.bpa.models.extd.RegistrationExtn;
import org.egov.bpa.models.extd.RegnAutoDcrExtn;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class AutoDcrExtnService extends PersistenceService<RegnAutoDcrExtn, Long> {

	private Logger LOGGER = Logger.getLogger(getClass());
	private PersistenceService persistenceService;
	private AutoDcrExtn searchAutoDcr=new AutoDcrExtn();
	private static final String STATUSCODE    		    =    "statusObj.code";
	private static final String STATUSMODULE            =    "statusObj.moduletype";
	public RegnAutoDcrExtn saveAutoDcr(RegnAutoDcrExtn autoDcr) {
		autoDcr = (RegnAutoDcrExtn) persist(autoDcr);
		return autoDcr;
	}
	
	public RegnAutoDcrExtn getAutoDcrByLpIdAndRegId(RegistrationExtn reg, LetterToPartyExtn letterToParty) {
		return (RegnAutoDcrExtn) persistenceService.find(" from RegnAutoDcrExtn where letterToParty.id=? and registration.id=?", 
				letterToParty.getId(), reg.getId());
	}
	
	public RegnAutoDcrExtn getAutoDcrByRegId(Long registrationId) {
		List autoDcrList=persistenceService.findAllBy(" from RegnAutoDcrExtn where letterToParty is null and registration.id=?", registrationId);
		if(autoDcrList.size()>0)
			return (RegnAutoDcrExtn) autoDcrList.get(0);
		else
			return null;
	}
	
	public RegnAutoDcrExtn createAutoDcr(RegnAutoDcrExtn autoDcr, RegistrationExtn registration) {
		autoDcr.setIsActive(true);
		autoDcr.setRegistration(registration);
		
		saveAutoDcr(autoDcr);
		
		return autoDcr;
	}
	
	
	/**
	 * @param autonum
	 * @param regnId
	 * @return
	 */
	public boolean checkAutodcrAlreadyExistByLpId(String autonum,Long regnId,Long lpId)
 {
		if (autonum != null && !"".equals(autonum)) {
			Criteria autoDcrCriteria = buildAutoDcrCheckCriteria(autonum,
					regnId,lpId);
			List<RegnAutoDcrExtn> regnAutoDcrList = autoDcrCriteria.list();
			if (regnAutoDcrList.size() > 0)
				return true;
		}
		return false;
	}
	/**
	 * @param autonum
	 * @param regnId
	 * @return
	 */
	public boolean checkAutodcrAlreadyExist(String autonum,Long regnId)
 {
		if (autonum != null && !"".equals(autonum)) {
			Criteria autoDcrCriteria = buildAutoDcrCheckCriteria(autonum,
					regnId,null);
			List<RegnAutoDcrExtn> regnAutoDcrList = autoDcrCriteria.list();
			if (regnAutoDcrList.size() > 0)
				return true;
		}
		return false;
	}

	private Criteria buildAutoDcrCheckCriteria(String autonum, Long regnId,Long lpId) {
		Criteria autoDcrCriteria = persistenceService.getSession()
				.createCriteria(RegnAutoDcrExtn.class, "autoDcr")
				.createAlias("autoDcr.registration", "registration")
				.createAlias("registration.egwStatus", "statusObj");

		autoDcrCriteria.add(Restrictions.eq(STATUSMODULE, "NEWBPAREGISTRATION"));
		autoDcrCriteria.add(Restrictions.ne(STATUSCODE, BpaConstants.CANCELLED));
		autoDcrCriteria.add(Restrictions.ne(STATUSCODE,BpaConstants.REJECTIONAPPROVED));
		

		if (regnId != null)
			autoDcrCriteria.add(Restrictions.ne("registration.id", regnId));

		if (lpId != null)
			autoDcrCriteria.add(Restrictions.ne("letterToParty.id", lpId));

		
		autoDcrCriteria.add(Restrictions.eq("isActive", true));
		autoDcrCriteria.add(Restrictions.eq("autoDcrNum", autonum));
		return autoDcrCriteria;
	}
	public RegnAutoDcrExtn getLatestRegnAutoDcrForRegn(RegistrationExtn registration) {
		RegnAutoDcrExtn regnAutoDcr=null;
		Criteria autoDcrCriteria= persistenceService.getSession().createCriteria(RegnAutoDcrExtn.class,"autoDcr");
		autoDcrCriteria.add(Restrictions.eq("registration.id",registration.getId()));
		autoDcrCriteria.add(Restrictions.eq("isActive", true));
		autoDcrCriteria.addOrder(Order.desc("id"));
		List<RegnAutoDcrExtn> regnAutoDcrList= autoDcrCriteria.list();
		if(regnAutoDcrList.size()!=0){
			regnAutoDcr = regnAutoDcrList.get(0);
		}
		return regnAutoDcr;
	}

	public List<AutoDcrExtn> getAutoDcrByMobileNumber(Long mobileNumber) {
		Criteria autoDcrCriteria= persistenceService.getSession().createCriteria(AutoDcrExtn.class,"autoDcr");
		autoDcrCriteria.add(Restrictions.eq("autoDcr.mobileno",mobileNumber));
		return autoDcrCriteria.list();
	}
	
	public List<RegnAutoDcrExtn> getRegnAutoDcrByPassingAutoDcrNumber(String autoDcrNumber) {
		Criteria autoDcrCriteria= persistenceService.getSession().createCriteria(RegnAutoDcrExtn.class,"autoDcr");
		//autoDcrCriteria.setProjection(Projections.distinct(Projections.property("id")));
		autoDcrCriteria.add(Restrictions.ilike("autoDcrNum",autoDcrNumber));
		autoDcrCriteria.add(Restrictions.eq("isActive", true));
		return autoDcrCriteria.list();
		
	}
	
	public AutoDcrExtn getAutoDcrByAutoDcrNum(String autoDcrNum) {
		AutoDcrExtn autoDcr = (AutoDcrExtn) persistenceService.find("from AutoDcrExtn where autoDcrNum=?",autoDcrNum);
		
		return autoDcr;
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public AutoDcrExtn getSearchAutoDcr() {
		return searchAutoDcr;
	}

	public void setSearchAutoDcr(AutoDcrExtn searchAutoDcr) {
		this.searchAutoDcr = searchAutoDcr;
	}
	
}
