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

package com.exilant.GLEngine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.service.ChartOfAccountDetailService;
import org.egov.infra.cache.impl.ApplicationCacheManager;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

 

@Component
public class CoaCache implements Serializable {

    @Autowired
    @Qualifier("persistenceService")
    PersistenceService persistenceService;
    @Autowired
    @Qualifier("chartOfAccountDetailService")
    private ChartOfAccountDetailService chartOfAccountDetailService;
    @Autowired
    private ApplicationCacheManager applicationCacheManager;
    private static final Logger LOGGER = Logger.getLogger(ChartOfAccounts.class);

    private static final String ROOTNODE = "/COA";
    private static final String GLACCCODENODE = "GlAccountCodes";
    private static final String GLACCIDNODE = "GlAccountIds";
    private static final String ACCOUNTDETAILTYPENODE = "AccountDetailType";
    private static final String EXP = "Exp=";
    private static final String EXILRPERROR = "exilRPError";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadAccountData()   {

        /*
         * 1.Loads all the account codes and details of that as GLAccount objects in theGLAccountCode,theGLAccountId HashMap's
         */

        // Temporary place holders
        final HashMap glAccountCodes = new HashMap();
        final HashMap glAccountIds = new HashMap();
        final HashMap accountDetailType = new HashMap();

        String sql = "select id as \"id\",name as \"name\",tableName as \"tableName\"," +
                "description as \"description\",columnName as \"columnName\",attributeName as \"attributeName\"" +
                ",nbrOfLevels as  \"nbrOfLevels\" from AccountDetailType";

        final Session currentSession = persistenceService.getSession();
        SQLQuery createSQLQuery = currentSession.createSQLQuery(sql);
        createSQLQuery
                .addScalar("id", IntegerType.INSTANCE)
                .addScalar("name")
                .addScalar("tableName")
                .addScalar("description")
                .addScalar("columnName")
                .addScalar("attributeName")
                .setResultTransformer(Transformers.aliasToBean(AccountDetailType.class));
        List<AccountDetailType> accountDetailTypeList = new ArrayList<AccountDetailType>();
        List<GLAccount> glAccountCodesList = new ArrayList<GLAccount>();
        new ArrayList<GLAccount>();

        accountDetailTypeList = createSQLQuery.list();
        for (final AccountDetailType type : accountDetailTypeList)
            accountDetailType.put(type.getAttributeName(), type);
        sql = "select ID as \"ID\", glCode as \"glCode\" ,name as \"name\" ," +
                "isActiveForPosting as \"isActiveForPosting\" ,classification as \"classification\", functionReqd as \"functionRequired\" from chartofaccounts ";
        createSQLQuery = currentSession.createSQLQuery(sql);
        createSQLQuery
                .addScalar("ID", IntegerType.INSTANCE)
                .addScalar("glCode")
                .addScalar("name")
                .addScalar("isActiveForPosting", BooleanType.INSTANCE)
                .addScalar("classification", LongType.INSTANCE)
                .addScalar("functionRequired", BooleanType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(GLAccount.class));

        glAccountCodesList = createSQLQuery.list();
        for (final GLAccount type : glAccountCodesList)
            glAccountCodes.put(type.getCode(), type);
        for (final GLAccount type : glAccountCodesList)
            glAccountIds.put(type.getId(), type);
        loadParameters(glAccountCodes, glAccountIds);
        try {
            final HashMap<String, HashMap> hm = new HashMap<String, HashMap>();
            hm.put(ACCOUNTDETAILTYPENODE, accountDetailType);
            hm.put(GLACCCODENODE, glAccountCodes);
            if (LOGGER.isDebugEnabled()) 
            	LOGGER.debug("Loading size:" + glAccountCodes.size());
            hm.put(GLACCIDNODE, glAccountIds);
            applicationCacheManager.put(ROOTNODE, hm);
        } catch (final Exception e) {
            throw e;
        }
        
        
    }

    private synchronized void loadParameters(final HashMap glAccountCodes, final HashMap glAccountIds)
             {
        final List<CChartOfAccountDetail> chList = chartOfAccountDetailService.findAllBy("from CChartOfAccountDetail");
        for (final CChartOfAccountDetail chartOfAccountDetail : chList) {
            final GLParameter parameter = new GLParameter();
            parameter.setDetailId(chartOfAccountDetail.getDetailTypeId().getId());
            parameter.setDetailName(chartOfAccountDetail.getDetailTypeId().getAttributename());
            final GLAccount glAccCode = getGlAccCode(chartOfAccountDetail.getGlCodeId(), glAccountCodes);
            final GLAccount glAccId = getGlAccId(chartOfAccountDetail.getGlCodeId(), glAccountIds);
            if (glAccCode != null && glAccCode.getGLParameters() != null)
                glAccCode.getGLParameters().add(parameter);
            if (glAccId != null && glAccId.getGLParameters() != null)
                glAccId.getGLParameters().add(parameter);
        }
    }

    private static GLAccount getGlAccCode(final CChartOfAccounts glCodeId, final Map glAccountCodes) {
        for (final Object key : glAccountCodes.keySet())
            if (((String) key).equalsIgnoreCase(glCodeId.getGlcode()))
                return (GLAccount) glAccountCodes.get(key);
        return null;
    }

    private static GLAccount getGlAccId(final CChartOfAccounts glCodeId, final Map glAccountIds) {
        for (final Object key : glAccountIds.keySet())
            if (key.toString().equalsIgnoreCase(glCodeId.getId().toString()))
                return (GLAccount) glAccountIds.get(key);
        return null;
    }
    /**
     * CLeans the cache
     */
    private void clear()
    {
    	applicationCacheManager.remove(ROOTNODE);
    }
    /**
     * reloads the cache
     */
    public void reLoad()
    {
    	applicationCacheManager.remove(ROOTNODE);
        loadAccountData();
    }
}
