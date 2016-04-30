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
package org.egov.bpa.services.extd.common;

import org.egov.bpa.constants.BpaConstants;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/*import org.egov.portal.surveyor.model.Surveyor;//Phionix TODO
 import org.egov.portal.surveyor.model.SurveyorDetail;*/

public class UtilsExtnService {
	private PersistenceService persistenceService;
	@PersistenceContext
	private EntityManager entityManager;

	protected Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	/*
	 * Return the Surveyor for given BPA registration based on below rules 1.
	 * Surveyor from same zone of BPA registration 2. In case multiple Surveyors
	 * assigned to a zone then in a order of Surveyor registration in a zone and
	 * work load on Surveyor
	 */
	/*
	 * public Surveyor getSurveyorForBpa(RegistrationExtn registration) {
	 * Surveyor surveyor = null;
	 * 
	 * 
	 * SELECT surv.id, surv.code,COUNT(DISTINCT psn_num) AS totalalloted FROM
	 * EGP_SURVEYOR surv left outer join EGP_SURVEYORSERVICEREQREGISTRY service
	 * ON service.PORTALUSERID= surv.id left outer join EGBPAEXTND_REGISTRATION
	 * regn ON regn.REQUEST_NUMBER=service.REQUESTID left outer join
	 * EGP_SURVEYORDETAILS survdtl ON surv.id=survdtl.ID_SURVEYOR -- WHERE
	 * ID_BOUNDARY=18883 AND regn.statusid=966 WHERE surv.id IN ( 901,922) GROUP
	 * BY surv.code, surv.id ORDER BY COUNT(DISTINCT psn_num) ASC
	 * 
	 * Long pendingCount = Long.valueOf(0); Long tempCount = Long.valueOf(0);
	 * Boundary zone = registration.getAdminboundaryid().getParent();
	 * List<SurveyorDetail> zonalSurveyors = (List<SurveyorDetail>)
	 * persistenceService.findAllBy(
	 * "select sd from SurveyorDetail sd where sd.boundary=? order by sd.id",
	 * zone); for (SurveyorDetail zoneSurveyor : zonalSurveyors) { if
	 * (pendingCount == 0) { tempCount =
	 * getPendingWfCountForSurveyor(zoneSurveyor.getSurveyor()); surveyor =
	 * zoneSurveyor.getSurveyor(); pendingCount = tempCount; } else { tempCount
	 * = getPendingWfCountForSurveyor(zoneSurveyor.getSurveyor()); if
	 * (pendingCount > tempCount) { surveyor = zoneSurveyor.getSurveyor(); } } }
	 * return surveyor; }
	 */
	// Phionix TODO
	/*
	 * Returns boolean value for given Surveyor is having pending workflow items
	 * or not
	 */
	/*
	 * public Boolean doSurveyorHavePendingWFBefore(Surveyor surveyor) { Boolean
	 * doLSHavePending = Boolean.FALSE; Long count =
	 * getPendingWfCountForSurveyor(surveyor); if (count > 0) { doLSHavePending
	 * = Boolean.TRUE; } return doLSHavePending; }
	 */// Phionix TODO

	/*
	 * //Phionix TODO Returns count of pending workflow items for given Surveyor
	 */
	/*
	 * public Long getPendingWfCountForSurveyor(Surveyor surveyor) { Long
	 * pendingCount = Long.valueOf(0); pendingCount = (Long)
	 * persistenceService.find(
	 * "select count(srr.id) from org.egov.portal.surveyor.model.SurveyorServiceRequestRegistry srr, RegistrationExtn bpareg "
	 * +
	 * "where srr.requestID=bpareg.request_number and srr.requestStatus=? and srr.portalUser=? "
	 * , APPLICATION_FWDED_TO_LS, surveyor); return pendingCount; }
	 */

	/**
	 * @description returns Approvers having less work load
	 * @param desId
	 * @param posId
	 * @return
	 */
	public List<Object[]> getLessLoadedApproversForBpa(List<Long> desId,
			List<Long> posId) {
		List<Object[]> result = new ArrayList<Object[]>();

		String query = "SELECT empinfo.POS_ID, count(wfstate.id) "
				+ " FROM EG_EIS_EMPLOYEEINFO empinfo"
				+ " LEFT OUTER JOIN EG_WF_STATES wfstate on wfstate.owner_pos=empinfo.POS_ID and wfstate.type=:wfStateType and wfstate.value!='NEW' "
				+ " and exists (select 1 from EGBPAEXTND_REGISTRATION bpareg, EGW_STATUS status  where bpareg.STATUSID=status.id and "
				+ " status.CODE not in (:statuses) and "
				+ " status.moduletype=:moduleType and bpareg.STATEID=wfstate.id) "
				+ " where empinfo.DESIGNATIONID in (:desId) and empinfo.POS_ID in (:posId) group by empinfo.POS_ID order by count(wfstate.id) asc";
		Query sqlQuery = getCurrentSession().createSQLQuery(
				String.valueOf(query));
		List<String> status = new ArrayList<String>();
		status.add((BpaConstants.CANCELLED).toUpperCase());
		status.add((BpaConstants.ORDERISSUEDTOAPPLICANT).toUpperCase());
		status.add((BpaConstants.REJECTIONAPPROVED).toUpperCase());
		status.add((BpaConstants.REJECTORDERISSUED).toUpperCase());
		sqlQuery.setParameterList("statuses", status);
		sqlQuery.setString("moduleType", BpaConstants.NEWBPAREGISTRATIONMODULE);
		sqlQuery.setString("wfStateType",
				BpaConstants.NEWREGISTRATION_WFSTATETYPE);
		if (!desId.isEmpty()) {
			sqlQuery.setParameterList("desId", desId);
		}
		if (!posId.isEmpty()) {
			sqlQuery.setParameterList("posId", posId);
		}
		result = (List<Object[]>) sqlQuery.list();
		return result;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}