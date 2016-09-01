/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.lcms.transactions.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.egov.lcms.reports.entity.LegalCaseSearchResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SearchLegalCaseService {
	@Autowired
	private EntityManager entityManager;
	
	public Session getCurrentSession()
	{
        return entityManager.unwrap(Session.class);
	}
	

	public List<LegalCaseSearchResult> getLegalCaseReport(final String caseNumber, final String lcNumber, final String court,
			final String casetype, final String standingcouncil, final String courttype, final String status,
			final Boolean isStatusExcluded) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("select distinct legalObj.casenumber as \"caseNumber\",courtmaster.name  as \"courtName\",");
		queryStr.append(" legalObj.casetitle  as \"caseTitle\",");
		queryStr.append(" legalObj.appealnum  as \"standingCouncil\",egwStatus.description  as \"caseStatus\",legalObj.lcNumber as \"lcNumber\" ");
		queryStr.append(" from EGLC_LEGALCASE legalObj,EGLC_BIPARTISANDETAILS bipart,eglc_court_master courtmaster,eglc_casetype_master casetypemaster,");
		queryStr.append(" eglc_petitiontype_master petmaster,egw_status egwStatus");
		queryStr.append(" where  bipart.legalcase=legalObj.id and legalObj.court=courtmaster.id and ");
		queryStr.append(" legalObj.casetype=casetypemaster.id and legalObj.petitiontype=petmaster.id and ");
		queryStr.append(" legalObj.status=egwStatus.id and egwStatus.moduletype =:mdoculeType ");
		
		getAppendQuery(caseNumber, lcNumber, court, casetype, standingcouncil, courttype, status, isStatusExcluded,
				queryStr);
		Query queryResult= getCurrentSession().createSQLQuery(queryStr.toString());
		queryResult=setParametersToQuery(caseNumber, lcNumber, court, casetype, standingcouncil, courttype, status, queryResult);
		List<LegalCaseSearchResult>  legalcaseSearchList=queryResult.list();
		return legalcaseSearchList;

	}

	private Query setParametersToQuery(final String caseNumber, final String lcNumber, final String court,
			final String casetype, final String standingcouncil, final String courttype, final String status,
			Query queryResult) {
		queryResult.setString("mdoculeType", LcmsConstants.MODULE_TYPE_LEGALCASE);
		if (StringUtils.isNotBlank(lcNumber))
			queryResult.setString("lcNumber", lcNumber);
		if (StringUtils.isNotBlank(caseNumber))
			queryResult.setString("caseNumber", caseNumber);
		if (StringUtils.isNotBlank(court))
			queryResult.setString("court", court);
		if (StringUtils.isNotBlank(casetype))
			queryResult.setString("casetype",casetype);
		if (StringUtils.isNotBlank(courttype))
			queryResult.setString("courttype", courttype);
		if (StringUtils.isNotBlank(standingcouncil))
			queryResult.setString("standingcoouncil", standingcouncil);
		if (status != null && !"".equals(status))
			queryResult.setString("status",status);
		if (StringUtils.isNotBlank(lcNumber) && StringUtils.isNotBlank(caseNumber) )
			queryResult.setString("caseNumber", caseNumber);
		queryResult.setResultTransformer(new AliasToBeanResultTransformer(LegalCaseSearchResult.class));
		return queryResult;
	}


	private void getAppendQuery(final String caseNumber, final String lcNumber, final String court,
			final String casetype, final String standingcouncil, final String courttype, final String status,
			final Boolean isStatusExcluded, final StringBuilder queryStr) {
		if (StringUtils.isNotBlank(lcNumber))
			queryStr.append(" and legalObj.lcNumber =:lcNumber");
		if (StringUtils.isNotBlank(caseNumber))
			queryStr.append(" and legalObj.casenumber =:caseNumber ");
		if (StringUtils.isNotBlank(court))
			queryStr.append(" and courtmaster.id =:court ");
		if (StringUtils.isNotBlank(casetype))
			queryStr.append(" and casetypemaster.id =:casetype");
		if (StringUtils.isNotBlank(courttype))
			queryStr.append(" and courtmaster.courttype =:courttype ");
		if (StringUtils.isNotBlank(standingcouncil))
			queryStr.append(" and legalObj.appealnum  =:standingcoouncil ");
		if (status != null && !"".equals(status))
			queryStr.append(" and egwStatus.id =:status ");
		if (StringUtils.isNotBlank(lcNumber) && StringUtils.isNotBlank(caseNumber) )
			queryStr.append(" and legalObj.casenumber =:caseNumber");
		if (isStatusExcluded)
			queryStr.append(" and egwStatus.code NOT IN ('CANCELLED') ");
	}

}
