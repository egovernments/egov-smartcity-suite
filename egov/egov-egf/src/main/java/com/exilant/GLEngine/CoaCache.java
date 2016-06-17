package com.exilant.GLEngine;

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

import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;
@Component
public class CoaCache {

 
	@Autowired
	@Qualifier("persistenceService")
	PersistenceService persistenceService;
	@Autowired
    @Qualifier("chartOfAccountDetailService")
    private  ChartOfAccountDetailService chartOfAccountDetailService;
	@Autowired
    private ApplicationCacheManager applicationCacheManager;
	private static final Logger LOGGER = Logger.getLogger(ChartOfAccounts.class);

    private static final String ROOTNODE = "/COA";
    private static final String GLACCCODENODE = "GlAccountCodes";
    private static final String GLACCIDNODE = "GlAccountIds";
    private static final String ACCOUNTDETAILTYPENODE = "AccountDetailType";
    private static final String EXP = "Exp=";
    private static final String EXILRPERROR = "exilRPError";
    
	 @Transactional(propagation=Propagation.REQUIRES_NEW)
     public void loadAccountData() throws TaskFailedException {
       
        
        /*
         * 1.Loads all the account codes and details of that as GLAccount objects in theGLAccountCode,theGLAccountId HashMap's
         */
        
        // Temporary place holders
        final HashMap glAccountCodes = new HashMap();
        final HashMap glAccountIds = new HashMap();
        final HashMap accountDetailType = new HashMap();

        DataExtractor.getExtractor();

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
        try
        {
            final HashMap<String, HashMap> hm = new HashMap<String, HashMap>();
            hm.put(ACCOUNTDETAILTYPENODE, accountDetailType);
            hm.put(GLACCCODENODE, glAccountCodes);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Loading size:" + glAccountCodes.size());
            hm.put(GLACCIDNODE, glAccountIds);
            // cache.put(ROOTNODE+"/"+FilterName.get(),ACCOUNTDETAILTYPENODE,accountDetailType);
            // cache.put(ROOTNODE+"/"+FilterName.get(),GLACCCODENODE,glAccountCodes);
            // cache.put(ROOTNODE+"/"+FilterName.get(),GLACCIDNODE,glAccountIds);
            applicationCacheManager.put(ROOTNODE , hm);
        } catch (final Exception e)
        {
            LOGGER.error(EXP + e.getMessage(), e);
            throw new TaskFailedException();

        }
        }
    
	 
	 private  synchronized void loadParameters(final HashMap glAccountCodes, final HashMap glAccountIds)
	            throws TaskFailedException {
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
}
