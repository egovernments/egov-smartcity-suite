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

import java.util.ArrayList;
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
	

	public List<LegalCaseSearchResult> getLegalCaseReport(LegalCaseSearchResult legalCaseSearchResultOblj, 
			final Boolean isStatusExcluded) {
		final StringBuilder queryStr = new StringBuilder();
		queryStr.append("select distinct legalObj  as  legalCase ,courtmaster.name  as  courtName ,");
		queryStr.append(" egwStatus.code  as  caseStatus ");
		queryStr.append(" from LegalCase legalObj,CourtMaster courtmaster,CaseTypeMaster casetypemaster,");
		queryStr.append(" PetitionTypeMaster petmaster,EgwStatus egwStatus");
		queryStr.append(" where legalObj.courtMaster.id=courtmaster.id and ");
		queryStr.append(" legalObj.caseTypeMaster.id=casetypemaster.id and legalObj.petitionTypeMaster.id=petmaster.id and ");
		queryStr.append(" legalObj.status.id=egwStatus.id and egwStatus.moduletype =:mdoculeType ");
		
		
		getAppendQuery(legalCaseSearchResultOblj, isStatusExcluded,
				queryStr);
		Query queryResult= getCurrentSession().createQuery(queryStr.toString());
		queryResult=setParametersToQuery(legalCaseSearchResultOblj, isStatusExcluded,queryResult);
		List<LegalCaseSearchResult>  legalcaseSearchList=queryResult.list();
		return legalcaseSearchList;

	}

	private Query setParametersToQuery(LegalCaseSearchResult legalCaseSearchResultOblj, Boolean isStatusExcluded,
			Query queryResult) {
		queryResult.setString("mdoculeType", LcmsConstants.MODULE_TYPE_LEGALCASE);
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getLcNumber()))
			queryResult.setString("lcNumber", legalCaseSearchResultOblj.getLcNumber());
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getCaseNumber()))
			queryResult.setString("caseNumber", legalCaseSearchResultOblj.getCaseNumber() + "%" );
		if (legalCaseSearchResultOblj.getCourtId()!=null )
			queryResult.setInteger("court", legalCaseSearchResultOblj.getCourtId());
		if (legalCaseSearchResultOblj.getCasecategory()!=null)
			queryResult.setInteger("casetype",legalCaseSearchResultOblj.getCasecategory());
		if (legalCaseSearchResultOblj.getCourtType()!=null)
			queryResult.setInteger("courttype", legalCaseSearchResultOblj.getCourtType());
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getStandingCouncil()))
			queryResult.setString("standingcoouncil",  legalCaseSearchResultOblj.getStandingCouncil() + "%");
		if (legalCaseSearchResultOblj.getStatusId()!=null)
			queryResult.setInteger("status",legalCaseSearchResultOblj.getStatusId());
		  
		if(legalCaseSearchResultOblj.getCaseFromDate()!=null)
		{
			queryResult.setDate("fromdate" ,legalCaseSearchResultOblj.getCaseFromDate());
		}
		if(legalCaseSearchResultOblj.getCaseToDate()!=null)
		{
			queryResult.setDate("toDate" ,legalCaseSearchResultOblj.getCaseToDate());
		}
		if(legalCaseSearchResultOblj.getPetitionTypeId()!=null)
		{
			queryResult.setInteger("petiontionType", legalCaseSearchResultOblj.getPetitionTypeId());
		}
		if(isStatusExcluded)
		{
			List<String>statusCodeList=new ArrayList<String>();
			statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_CLOSED);
			statusCodeList.add(LcmsConstants.LEGALCASE_STATUS_JUDGMENT_IMPLIMENTED);
			queryResult.setParameterList("statusCodeList",statusCodeList );
		}
		queryResult.setResultTransformer(new AliasToBeanResultTransformer(LegalCaseSearchResult.class));
		return queryResult;
	}


	private void getAppendQuery( LegalCaseSearchResult legalCaseSearchResultOblj  ,
			final Boolean isStatusExcluded, final StringBuilder queryStr) {
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getLcNumber()))
			queryStr.append(" and legalObj.lcNumber =:lcNumber");
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getCaseNumber()))
			queryStr.append(" and legalObj.caseNumber like :caseNumber ");
		if (legalCaseSearchResultOblj.getCourtId()!=null)
			queryStr.append(" and courtmaster.id =:court ");
		if (legalCaseSearchResultOblj.getCasecategory()!=null)
			queryStr.append(" and casetypemaster.id =:casetype");
		if (legalCaseSearchResultOblj.getCourtType()!=null)
			queryStr.append(" and courtmaster.id =:courttype ");
		if (StringUtils.isNotBlank(legalCaseSearchResultOblj.getStandingCouncil()))
			queryStr.append(" and legalObj.oppPartyAdvocate like :standingcoouncil ");
		if (legalCaseSearchResultOblj.getStatusId()!=null)
			queryStr.append(" and egwStatus.id =:status ");
		if(legalCaseSearchResultOblj.getCaseFromDate()!=null)
		{
			queryStr.append(" and legalObj.caseDate >=:fromdate " );
		}
		if(legalCaseSearchResultOblj.getCaseToDate()!=null)
		{
			queryStr.append(" and legalObj.caseDate <=:toDate " );
		}
		if(legalCaseSearchResultOblj.getPetitionTypeId()!=null)
		{
			queryStr.append(" and petmaster.id =:petiontionType " );
		}
		
		if(isStatusExcluded)
		{
			queryStr.append(" and egwStatus.code not in (:statusCodeList ) ");
		}
	}

}
