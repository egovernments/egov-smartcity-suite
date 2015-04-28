/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Scheme;
import org.egov.commons.SubScheme;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.SequenceGenerator;
import org.egov.model.bills.EgBillregister;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;
import org.egov.services.budget.BudgetService;
import org.egov.utils.BudgetAccountType;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetDetailsHibernateDAO extends GenericHibernateDAO implements BudgetDetailsDAO
{
	private static final String EGF = "EGF";
	private static final String BUDGETHEADID = "budgetheadid";
	private static final String GLCODEID = "glcodeid";
	private static final String BUDGETARY_CHECK_GROUPBY_VALUES = "budgetaryCheck_groupby_values";
	private Session session;
	private static final Logger LOGGER = Logger.getLogger(BudgetDetailsHibernateDAO.class);
	private static final String EMPTY_STRING="";
	private PersistenceService persistenceService;
	private PersistenceService service;
	private GenericHibernateDaoFactory genericDao;	
	private String budgetFinalStatus=null;
	protected ScriptService scriptExecutionService;
	protected SequenceGenerator sequenceGenerator;	
	private BudgetService budgetService;
	private FinancialYearDAO financialYearDAO;
	
	public void setFinancialYearDAO(FinancialYearDAO finYearDao) {
		this.financialYearDAO = finYearDao;
	}
	/**
	 * This API is to check whether the planning budget is available or not.For the amount passed if there is sufficient budget available API will return TRUE. Else it will return FALSE.
	 * At any point the budgetavailable will show the right picture of how much more we can plan for.
	 * <p>
	 * Assumptions- 1) on load of the budget there will be budgetavailable amount loaded using the multiplier factor.
	 * 2) on load of supplementary budget budget available is recalculated and updated.
	 * <p>
	 * For the sake of audit we should be updating the budgetusage object with the reference object and the moduleid and amount for any budget consumed.
	 * This will be used for reporting as to which object consumed how much and when.
	 * 
	 * @param financialyearid This is the id from the financial year object
	 * @param moduleid	This is the id of the module, say for payroll 7 and for stores 8 and for PTIS it is 2
	 * @param referencenumber	This is the module object reference number, say purchase order number or estimate number
	 * @param departmentid	This is the id of the department object
	 * @param functionid	This is the id of the function object
	 * @param functionaryid	This is the id of the functionary object
	 * @param schemeid	This is the id of the scheme object
	 * @param subschemeid	This is the id of the sub scheme object
	 * @param boundaryid	This is the id of the boundary object
	 * @param budgetheadid	This is the id of budgegroup object
	 * @param amount	This is the amount of which budget needs to be allocated
	 * @return	boolean
	 * @throws Exception
	 */
	public boolean consumeEncumbranceBudget(Map<String,Object> detailsMap)
	{
		if(detailsMap==null)
			throw new ValidationException(Arrays.asList(new ValidationError("required input is null","required input is null")));
		detailsMap.put(Constants.CONSUMEORRELEASE, true);
		BigDecimal bd = getDetails(detailsMap);
		return bd.intValue()==1;
	}
	
	public boolean consumeEncumbranceBudget(Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, double amount,String appropriationnumber)throws ValidationException {
		
		BigDecimal bd = getDetails(financialyearid, moduleid,referencenumber, departmentid, functionid,functionaryid, schemeid, subschemeid,boundaryid, budgetheadid,fundid,  amount,true,appropriationnumber);
		return bd.intValue()==1;
	}
	/**
	 * Does the same as the above API. Except this API returns BudgetUsage object if the available amount is >=0.
	 * If there is insufficient available amount then null is returned. 
	 * Note that the first parameter is a String, unlike the previous API.
	 * @param appropriationnumber
	 * @param financialyearid
	 * @param moduleid
	 * @param referencenumber
	 * @param departmentid
	 * @param functionid
	 * @param functionaryid
	 * @param schemeid
	 * @param subschemeid
	 * @param boundaryid
	 * @param budgetheadid
	 * @param fundid
	 * @param amount
	 * @return BudgetUsage
	 * @throws ValidationException
	 */
	@Deprecated
	public BudgetUsage consumeEncumbranceBudget(String appropriationnumber, Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, double amount )throws ValidationException {
		
		return getBudgetUsageDetails(financialyearid, moduleid,referencenumber, departmentid, functionid,functionaryid, schemeid, subschemeid,boundaryid, budgetheadid,fundid,  amount,true,appropriationnumber);
	}
	
	/**
	 * This API will be called for releasing the budget that was already allocated to some estimate or purchase order. 
	 * On calling this the amount that was allocated to some entity will get released. Budget available will be increased.
	 * <p>
	 * On modify of any entity they should first call the release budget and then call the consume budget.
	 * The budget usage table needs to be updated anytime this is invoked with the date and reference object number.
	 * @param detailsMap is the map containig following fields
	 * financialyearid This is the id from the financial year object
	 * moduleid	This is the id of the module, say for payroll 7 and for stores 8 and for PTIS it is 2
	 * referencenumber	This is the module object reference number, say purchase order number or estimate number
	 * departmentid	This is the id of the department object
	 * functionid	This is the id of the function object
	 * functionaryid	This is the id of the functionary object
	 * schemeid	This is the id of the scheme object
	 * subschemeid	This is the id of the sub scheme object
	 * fieldid	This is the id of the boundary object
	 * budgetheadid	This is the id of budgegroup object
	 * amount	This is the amount of which budget needs to be allocated
	 * @throws Exception
	 */
	public boolean releaseEncumbranceBudget(Map<String,Object> detailsMap)
	{
		if(detailsMap==null)
			throw new ValidationException(Arrays.asList(new ValidationError("required input is null","required input is null")));
		detailsMap.put(Constants.CONSUMEORRELEASE, false);
		BigDecimal bd = getDetails(detailsMap);
		return bd.intValue()==1;
	}
	@Deprecated
	public void releaseEncumbranceBudget(Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, double amount,String appropriationnumber)throws ValidationException {
		getDetails(financialyearid, moduleid,referencenumber, departmentid, functionid,
				functionaryid, schemeid, subschemeid,boundaryid, budgetheadid,  fundid,amount,false,appropriationnumber);
	}
	/**
	 * This does the same as the above API except this returns the BudgetUsage object that is modified.
	 * Please note that first parameter is a string.
	 * @param appropriationnumber
	 * @param financialyearid
	 * @param moduleid
	 * @param referencenumber
	 * @param departmentid
	 * @param functionid
	 * @param functionaryid
	 * @param schemeid
	 * @param subschemeid
	 * @param boundaryid
	 * @param budgetheadid
	 * @param fundid
	 * @param amount
	 * @return BudgetUsage object
	 * @throws ValidationException
	 */
	
	public BudgetUsage releaseEncumbranceBudget(String appropriationnumber,Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, double amount)throws ValidationException {
		return getBudgetUsageDetails(financialyearid, moduleid,referencenumber, departmentid, functionid,
				functionaryid, schemeid, subschemeid,boundaryid, budgetheadid,  fundid,amount,false,appropriationnumber);
	}
	
	
	private BigDecimal getDetails(Map<String,Object> detailsMap) throws ValidationException
	{
		Long financialyearid=null;
		Integer moduleid=null;
		String referencenumber=null;
		Integer departmentid=null;
		Long functionid=null;
		Integer functionaryid=null;
		Integer schemeid=null;
		Integer subschemeid=null;
		Integer boundaryid=null;
		List<Long> budgetheadid=null;
		Integer fundid=null;
		double amount=0.0d;
		String appropriationnumber=null;
		Boolean consumeOrRelease=null;
		
		if(detailsMap.containsKey(Constants.FINANCIALYEARID))
				financialyearid=(Long)detailsMap.get(Constants.FINANCIALYEARID);
		if(detailsMap.containsKey(Constants.MODULEID))
			moduleid=(Integer)detailsMap.get(Constants.MODULEID);
		if(detailsMap.containsKey(Constants.REFERENCENUMBER))
			referencenumber=(String)detailsMap.get(Constants.REFERENCENUMBER);
		if(detailsMap.containsKey(Constants.DEPARTMENTID))
			departmentid=(Integer)detailsMap.get(Constants.DEPARTMENTID);
		if(detailsMap.containsKey(Constants.FUNCTIONID))
			functionid=(Long)detailsMap.get(Constants.FUNCTIONID);
		if(detailsMap.containsKey(Constants.FUNCTIONARYID))
			functionaryid=(Integer)detailsMap.get(Constants.FUNCTIONARYID);
		if(detailsMap.containsKey(Constants.SCHEMEID))
			schemeid=(Integer)detailsMap.get(Constants.SCHEMEID);
		if(detailsMap.containsKey(Constants.SUBSCHEMEID))
			subschemeid=(Integer)detailsMap.get(Constants.SUBSCHEMEID);
		if(detailsMap.containsKey(Constants.BOUNDARYID))
			boundaryid=(Integer)detailsMap.get(Constants.BOUNDARYID);
		if(detailsMap.containsKey(Constants.BUDGETHEAD))
			budgetheadid=(List<Long>) detailsMap.get(Constants.BUDGETHEAD);
		if(detailsMap.containsKey(Constants.FUNDID))
			fundid=(Integer)detailsMap.get(Constants.FUNDID);
		if(detailsMap.containsKey(Constants.AMOUNT))
			amount=(Double)detailsMap.get(Constants.AMOUNT);
		if(detailsMap.containsKey(Constants.APPROPRIATIONNUMBER))
			appropriationnumber=(String)detailsMap.get(Constants.APPROPRIATIONNUMBER);
		if(detailsMap.containsKey(Constants.CONSUMEORRELEASE))
			consumeOrRelease = (Boolean)detailsMap.get(Constants.CONSUMEORRELEASE);
		
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("financialyearid=="+financialyearid+",moduleid=="+moduleid+",referencenumber=="+referencenumber+",departmentid=="+departmentid+",functionid=="+functionid
					+",functionaryid=="+functionaryid+",schemeid=="+schemeid+",subschemeid=="+subschemeid+",boundaryid=="+boundaryid+",budgetheadid=="+budgetheadid+",amount=="+amount);

 	        validateMandatoryParameters(moduleid,	referencenumber);
			BigDecimal amtavailable=getPlanningBudgetAvailable(financialyearid, departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);
			
			if(consumeOrRelease)
			{
				amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
			}
			else
			{
				amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("budget available after consuming/releasing="+amtavailable);  
			
			if(amtavailable!=null && amtavailable.compareTo(BigDecimal.ZERO)>=0)
			{
				// need to update budget details
				String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
				Query q =HibernateUtil.getCurrentSession().createQuery(" from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId and bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)" +query);
				if(budgetService.hasApprovedReForYear(financialyearid))
					q.setParameter("type", "RE");
				else
					q.setParameter("type", "BE");
				q.setParameter("finYearId", financialyearid);
				q.setParameterList("bgId", budgetheadid);
				List<BudgetDetail> bdList = q.list();
				if(bdList==null||bdList.size()==0)
				{	
					if(LOGGER.isDebugEnabled())     LOGGER.debug("IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
					if(LOGGER.isDebugEnabled())     LOGGER.debug("financial year id - "+financialyearid.toString()+" Budget Group -  "+budgetheadid.toString()+" Query - "+query);
					throw new ValidationException(EMPTY_STRING,"Budgetary Check Failed");
				}
				BudgetDetail bd = bdList.get(0);
				bd.setBudgetAvailable(amtavailable);  
				BudgetDetailsDAO bdDAO = new BudgetDetailsHibernateDAO(BudgetDetail.class,HibernateUtil.getCurrentSession());
				bdDAO.update(bd);
				
				BudgetUsage budgetUsage = new BudgetUsage();
				budgetUsage.setFinancialYearId(financialyearid.intValue());
				budgetUsage.setModuleId(moduleid);
				budgetUsage.setReferenceNumber(referencenumber);
				budgetUsage.setBudgetDetail(bd);
				budgetUsage.setAppropriationnumber(appropriationnumber);
				if(consumeOrRelease)
				{
					budgetUsage.setConsumedAmount(amount);
					budgetUsage.setReleasedAmount(0.0);
				}
				else
				{
					budgetUsage.setConsumedAmount(0.0);
					budgetUsage.setReleasedAmount(amount);
				}
				budgetUsage.setCreatedby(Integer.valueOf(EGOVThreadLocals.getUserId()));
				BudgetUsageDAO bu=new BudgetUsageHibernateDAO(BudgetUsage.class,HibernateUtil.getCurrentSession());
				bu.create(budgetUsage);
				return BigDecimal.ONE;
			}
			else
				return BigDecimal.ZERO;
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in consumeEncumbranceBudget API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in consumeEncumbranceBudget API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	@Deprecated
	private BigDecimal getDetails(Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, 
			double amount,boolean consumeOrRelease,String appropriationnumber) throws ValidationException
	{
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("financialyearid=="+financialyearid+",moduleid=="+moduleid+",referencenumber=="+referencenumber+",departmentid=="+departmentid+",functionid=="+functionid
					+",functionaryid=="+functionaryid+",schemeid=="+schemeid+",subschemeid=="+subschemeid+",boundaryid=="+boundaryid+",budgetheadid=="+budgetheadid+",amount=="+amount);

 	        validateMandatoryParameters(moduleid,	referencenumber);
			BigDecimal amtavailable=getPlanningBudgetAvailable(financialyearid, departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);
			
			if(consumeOrRelease)
			{
				amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
			}
			else
			{
				amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("budget available after consuming/releasing="+amtavailable);
			
			if(amtavailable!=null && amtavailable.compareTo(BigDecimal.ZERO)>=0)
			{
				// need to update budget details
				String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
				Query q =HibernateUtil.getCurrentSession().createQuery(" from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId  and  bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)" +query);
				if(budgetService.hasApprovedReForYear(financialyearid))
					q.setParameter("type", "RE");
				else
					q.setParameter("type", "BE");
				q.setParameter("finYearId", financialyearid);
				q.setParameterList("bgId", budgetheadid);
				List<BudgetDetail> bdList = q.list();
				//List<BudgetDetail> bdList = persistenceService.findAllBy(" from BudgetDetail bd where  bd.budget.financialYear.id=? and bd.budgetGroup.id=? "+query, financialyearid,budgetheadid);
				
				if(bdList==null||bdList.size()==0)
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
					if(LOGGER.isDebugEnabled())     LOGGER.debug("financial year id - "+financialyearid.toString()+" Budget Group -  "+budgetheadid.toString()+" Query - "+query);
					throw new ValidationException(EMPTY_STRING,"Budgetary Check Failed");
				}
				BudgetDetail bd = bdList.get(0);
				bd.setBudgetAvailable(amtavailable);
				BudgetDetailsDAO bdDAO = new BudgetDetailsHibernateDAO(BudgetDetail.class,HibernateUtil.getCurrentSession());
				bdDAO.update(bd);
				
				BudgetUsage budgetUsage = new BudgetUsage();
				budgetUsage.setFinancialYearId(financialyearid.intValue());
				budgetUsage.setModuleId(moduleid);
				budgetUsage.setReferenceNumber(referencenumber);
				budgetUsage.setBudgetDetail(bd);
				budgetUsage.setAppropriationnumber(appropriationnumber);
				if(consumeOrRelease)
				{
					budgetUsage.setConsumedAmount(amount);
					budgetUsage.setReleasedAmount(0.0);
				}
				else
				{
					budgetUsage.setConsumedAmount(0.0);
					budgetUsage.setReleasedAmount(amount);
				}
				budgetUsage.setCreatedby(Integer.valueOf(EGOVThreadLocals.getUserId()));
				BudgetUsageDAO bu=new BudgetUsageHibernateDAO(BudgetUsage.class,HibernateUtil.getCurrentSession());
				bu.create(budgetUsage);
				return BigDecimal.ONE;
			}
			else
				return BigDecimal.ZERO;
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in consumeEncumbranceBudget API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in consumeEncumbranceBudget API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	@Deprecated
	private BudgetUsage getBudgetUsageDetails(Long financialyearid, Integer moduleid,String referencenumber, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid, 
			double amount,boolean consumeOrRelease,String appropriationnumber) throws ValidationException
	{
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("financialyearid=="+financialyearid+",moduleid=="+moduleid+",referencenumber=="+referencenumber+",departmentid=="+departmentid+",functionid=="+functionid
					+",functionaryid=="+functionaryid+",schemeid=="+schemeid+",subschemeid=="+subschemeid+",boundaryid=="+boundaryid+",budgetheadid=="+budgetheadid+",amount=="+amount);

 	        validateMandatoryParameters(moduleid,	referencenumber);
			BigDecimal amtavailable=getPlanningBudgetAvailable(financialyearid, departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, budgetheadid, fundid);
			
			if(consumeOrRelease)
			{
				amtavailable = amtavailable.subtract(BigDecimal.valueOf(amount));
			}
			else
			{
				amtavailable = amtavailable.add(BigDecimal.valueOf(amount));
			}
			if(LOGGER.isDebugEnabled())     LOGGER.debug("budget available after consuming/releasing="+amtavailable);
			
			if(amtavailable!=null && amtavailable.compareTo(BigDecimal.ZERO)>=0)
			{
				// need to update budget details
				String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
				Query q =HibernateUtil.getCurrentSession().createQuery(" from BudgetDetail bd where  bd.budget.financialYear.id=:finYearId  and  bd.budget.isbere=:type and bd.budgetGroup.id in (:bgId)" +query);
				if(budgetService.hasApprovedReForYear(financialyearid))
					q.setParameter("type", "RE");
				else
					q.setParameter("type", "BE");
				q.setParameter("finYearId", financialyearid);
				q.setParameterList("bgId", budgetheadid);
				List<BudgetDetail> bdList = q.list();
				//List<BudgetDetail> bdList = persistenceService.findAllBy(" from BudgetDetail bd where  bd.budget.financialYear.id=? and bd.budgetGroup.id=? "+query, financialyearid,budgetheadid);
				
				if(bdList==null||bdList.size()==0)
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("IN consumeEncumbranceBudget()-getDetail() - No budget detail item defined for RE or BE for this combination!!");
					if(LOGGER.isDebugEnabled())     LOGGER.debug("financial year id - "+financialyearid.toString()+" Budget Group -  "+budgetheadid.toString()+" Query - "+query);
					throw new ValidationException(EMPTY_STRING,"Budgetary Check Failed");
				}
				BudgetDetail bd = bdList.get(0);
				bd.setBudgetAvailable(amtavailable);
				BudgetDetailsDAO bdDAO = new BudgetDetailsHibernateDAO(BudgetDetail.class,HibernateUtil.getCurrentSession());
				bdDAO.update(bd);
				
				BudgetUsage budgetUsage = new BudgetUsage();
				budgetUsage.setFinancialYearId(financialyearid.intValue());
				budgetUsage.setModuleId(moduleid);
				budgetUsage.setReferenceNumber(referencenumber);
				budgetUsage.setBudgetDetail(bd);
				budgetUsage.setAppropriationnumber(appropriationnumber);
				if(consumeOrRelease)
				{
					budgetUsage.setConsumedAmount(amount);
					budgetUsage.setReleasedAmount(0.0);
				}
				else
				{
					budgetUsage.setConsumedAmount(0.0);
					budgetUsage.setReleasedAmount(amount);
				}
				budgetUsage.setCreatedby(Integer.valueOf(EGOVThreadLocals.getUserId()));
				BudgetUsageDAO bu=new BudgetUsageHibernateDAO(BudgetUsage.class,HibernateUtil.getCurrentSession());
				bu.create(budgetUsage);
				return budgetUsage;
			}
			else
				return null;
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in consumeEncumbranceBudget API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in consumeEncumbranceBudget API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}

	private void validateParameters(Long financialyearid,Long functionid,List<Long> budgetheadid) throws ValidationException
	{
		if(financialyearid==null)
		{
			throw new ValidationException(EMPTY_STRING,"Financial year id is null or empty");
		}
	    if(functionid==null )
        	throw new ValidationException(EMPTY_STRING,"Function id is null or empty");
		if(budgetheadid==null || budgetheadid.size()==0)
			throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
		session =HibernateUtil.getCurrentSession();
	       //fetch mandatory parameters
        CFinancialYear financialyear = (CFinancialYear) findById(CFinancialYear.class,financialyearid);
        if(financialyear==null)
        	throw new ValidationException(EMPTY_STRING,"Financial year is null or empty");
        
		CFunction function = (CFunction) findById(CFunction.class, functionid);
		if(function==null )
    		throw new ValidationException(EMPTY_STRING,"Function is null or empty");
		
		for(Long bgId : budgetheadid)
		{
			BudgetGroup budgetGroup = (BudgetGroup) findById(BudgetGroup.class, bgId);
			if(budgetGroup==null)
				throw new ValidationException(EMPTY_STRING,"Budget head is null or empty");
		}
	}
	
	private String prepareAgregateQuery(Integer departmentid,Long functionid,Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid,Integer fundid)throws ValidationException
	{
		String query=EMPTY_STRING;
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside the prepareAgregateQuery... "+departmentid+" >>>"+fundid);
		if(departmentid!=null && departmentid!=0)
				query = query+getQuery(Department.class, departmentid," and bd.executingDepartment=");
		if(functionid!=null && functionid!=0)
				query = query+getQuery(CFunction.class, functionid," and bd.function=");
		if(functionaryid!=null && functionaryid!=0)
				query = query+getQuery(Functionary.class, functionaryid," and bd.functionary=");
		if(fundid!=null && fundid!=0)
				query = query+getQuery(Fund.class, fundid," and bd.fund=");
		if(schemeid!=null && schemeid!=0)
				query = query+getQuery(Scheme.class,  schemeid," and bd.scheme=");
		if(subschemeid!=null && subschemeid!=0)
				query = query+getQuery(SubScheme.class,  subschemeid," and bd.subScheme=");
		if(boundaryid!=null && boundaryid!=0)
				query = query+getQuery(Boundary.class, boundaryid," and bd.boundary=");
		
		List<AppConfigValues> appconfiglist =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budget_final_approval_status");
		if(appconfiglist.isEmpty())
			throw new ValidationException(EMPTY_STRING,"budget_final_approval_status is not defined in AppConfig");

		budgetFinalStatus = ((AppConfigValues)appconfiglist.get(0)).getValue();
		return " and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+budgetFinalStatus+"' )"+query;
	}

	
	private String prepareQuery(Integer departmentid,Long functionid,Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid,Integer fundid)throws ValidationException
	{
		String query=EMPTY_STRING;
		
		List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,BUDGETARY_CHECK_GROUPBY_VALUES);
		if(list.isEmpty())
		{
			throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not defined in AppConfig");
		}
		else
		{
			final AppConfigValues appConfigValues = list.get(0);
			String[] values = StringUtils.split(appConfigValues.getValue(),",");
			for(int i=0;i<values.length;i++)
			{
				if(values[i].equals("department"))
				{
					if(departmentid==null || departmentid==0)
						throw new ValidationException(EMPTY_STRING,"Department is required");
					else
						query = query+getQuery(Department.class, departmentid," and bd.executingDepartment=");
				}
				else if(values[i].equals("function"))
				{
					if(functionid==null || functionid==0)
						throw new ValidationException(EMPTY_STRING,"Function is required");
					else
						query = query+getQuery(CFunction.class, functionid," and bd.function=");
				}
				else if(values[i].equals("functionary"))
				{
					if(functionaryid==null || functionaryid==0)
						throw new ValidationException(EMPTY_STRING,"Functionary is required");
					else
						query = query+getQuery(Functionary.class, functionaryid," and bd.functionary=");
				}
				else if(values[i].equals("fund"))
				{
					if(fundid==null || fundid==0)
						throw new ValidationException(EMPTY_STRING,"Fund is required");
					else
						query = query+getQuery(Fund.class, fundid," and bd.fund=");
				}
				else if(values[i].equals("scheme"))
				{
					if(schemeid==null || schemeid==0)
						throw new ValidationException(EMPTY_STRING,"Scheme is required");
					else
						query = query+getQuery(Scheme.class,  schemeid," and bd.scheme=");
				}
				else if(values[i].equals("subscheme"))
				{
					if(subschemeid==null || subschemeid==0)
						throw new ValidationException(EMPTY_STRING,"Subscheme is required");
					else
						query = query+getQuery(SubScheme.class,  subschemeid," and bd.subScheme=");
				}
				else if(values[i].equals("boundary"))
				{
					if(boundaryid==null || boundaryid==0)
						throw new ValidationException(EMPTY_STRING,"Boundary is required");
					else
						query = query+getQuery(Boundary.class, boundaryid," and bd.boundary=");
				}
				else
					throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not matching="+values[i]);
			}
		}
		List<AppConfigValues> appconfiglist =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budget_final_approval_status");
		if(appconfiglist.isEmpty())
			throw new ValidationException(EMPTY_STRING,"budget_final_approval_status is not defined in AppConfig");

		budgetFinalStatus = ((AppConfigValues)appconfiglist.get(0)).getValue();
		return " and bd.budget.state in (from org.egov.infstr.models.State where type='Budget' and value='"+budgetFinalStatus+"' )"+query;
	}

	private Object findById(Class clazz,Serializable id)throws ValidationException {
		if(id == null) throw new ValidationException(EMPTY_STRING,clazz.getSimpleName()+" id is null or empty");
		
		Object object = session.get(clazz, id);
		if(object == null) throw new ValidationException(EMPTY_STRING,clazz.getSimpleName()+" is not defined for this id [ "+id.toString()+" ]");
		return object;
	}
	
	protected List<String> getFieldConfigValues() {
		List<AppConfigValues> appconfigFieldlist = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,BUDGETARY_CHECK_GROUPBY_VALUES);
		return Arrays.asList(appconfigFieldlist.get(0).getValue().split(","));
	}

	private void validateMandatoryParameters(Integer moduleid, String referencenumber) throws ValidationException {
		validateMandatoryParameter(moduleid, "Module id");
		if(StringUtils.isBlank(referencenumber))
			throw new ValidationException(EMPTY_STRING,"Reference number is null or empty");
	}
	private void validateMandatoryParameter(Object value, String description) throws ValidationException{
		if(value==null) throw new ValidationException(EMPTY_STRING,description+" is null or empty");
	}
	/**
	 * To get the planning budget available amount
	 * @param financialyearid
	 * @param departmentid
	 * @param functionid
	 * @param functionaryid
	 * @param schemeid
	 * @param subschemeid
	 * @param boundaryid
	 * @param budgetheadid
	 * @return
	 * @throws ValidationException
	 */
	public BigDecimal getPlanningBudgetAvailable(Long financialyearid, Integer departmentid, Long functionid,
			Integer functionaryid, Integer schemeid, Integer subschemeid,Integer boundaryid, List<Long> budgetheadid,Integer fundid) throws ValidationException
	{
		try
		{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("financialyearid==="+financialyearid+",departmentid==="+departmentid+",functionid==="+functionid
					+",functionaryid==="+functionaryid+",schemeid==="+schemeid+",subschemeid==="+subschemeid+",boundaryid==="+boundaryid+",budgetheadid==="+budgetheadid);
	        
	        validateParameters(financialyearid, functionid, budgetheadid);
			String query = prepareQuery(departmentid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);

			session =HibernateUtil.getCurrentSession();
	        CFinancialYear financialyear = (CFinancialYear) findById(CFinancialYear.class,financialyearid);
	        
				// check any RE is available for the passed parameters.if RE is not exist, take BE's available Amount
			String finalquery = "select sum (budgetAvailable) from BudgetDetail bd where bd.budget.isbere=:type and bd.budget.financialYear.id=:financialyearid" +
					" and bd.budgetGroup.id in (:budgetheadid) "+query;
			
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Final query="+finalquery);
			Query q =HibernateUtil.getCurrentSession().createQuery(finalquery);
			if(budgetService.hasApprovedReForYear(financialyearid))
			{
				q.setParameter("type", "RE");
			}
			else
				q.setParameter("type", "BE");
			q.setParameter("financialyearid", financialyearid);
			q.setParameterList("budgetheadid", budgetheadid);
			Object obj = q.uniqueResult();
			if(obj==null)
			{
					throw new ValidationException("no.budget.defined.for.given.parameters","No Budget is defined for the parameters for this year->"+financialyear.getFinYearRange());
			}
			else
			{
				return (BigDecimal)obj;
			}
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getPlanningBudgetAvailable API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getPlanningBudgetAvailable API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	

	/**
	 * API to load the budget consumed for the previous year/current year.
	 * it'll return the transaction amount consumed for the current year based on asOnDate or previous financial year and other given parameters from Generalledger.
	 * Transaction amount calculation for Income and Liabilities -> sum(creditamt) - sum (debitamt) 
	 * Transaction amount calculation for Expense and Assets -> sum(debitamt) - sum (creditamt)
	 * @param functionid (optional)  -id for Function object
	 * @param functionaryid (optional) - id for functionary object
	 * @param departmentid (optional) - id for department object
	 * @param schemeid (optional) - id for scheme object
	 * @param subschemeid (optional) - id for subscheme object
	 * @param boundaryid (optional) - id for boundary object
	 * @param budgetHead (mandatory) - budget head object, which having the major code/ minor code/ detailcode/ range of minor/detail codes (based on appconfig values)
	 * @param asOnDate (mandatory)- 
	 * @return transaction amount
	 */
	//Integer departmentid, Long functionid,Integer functionaryid,Integer schemeid,Integer subschemeid,Integer fieldid,Long budgetheadid
	public BigDecimal getActualBudgetUtilized(Map<String,Object> paramMap) throws ValidationException
	{
		Integer deptid=null;
		Long functionid=null;
		Integer functionaryid=null;
		Integer schemeid=null;
		Integer subschemeid=null;
		Integer boundaryid=null;
		Integer fundid=null;
		Long budgetheadid=null;
		Date fromdate = null;
		Date asondate=null;
		
        String query=EMPTY_STRING,select=EMPTY_STRING;
        BudgetGroup budgetgroup=null;
         try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)
				budgetheadid = (Long)paramMap.get(BUDGETHEADID);
			if(paramMap.get(Constants.ASONDATE)!=null)
				asondate = (java.util.Date)paramMap.get(Constants.ASONDATE);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid="+deptid+",functionid="+functionid+",functionaryid="+functionaryid+",schemeid="+schemeid+",subschemeid="+subschemeid+",boundaryid="+boundaryid+",budgetheadid="+budgetheadid+",asondate="+asondate);

			if(asondate==null)
				throw new ValidationException(EMPTY_STRING,"As On Date is null");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	        FinancialYearHibernateDAO findao = new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
	        String finid = null;//findao.getFinancialYearId(sdf.format(asondate));
	      //This fix is for Phoenix Migration.
	        CFinancialYear finyear = findao.getFinancialYearById(Long.valueOf(finid));
	        if(finyear==null)
	        	throw new ValidationException(EMPTY_STRING,"Financial year is not fefined for this date ["+sdf.format(asondate)+"]");
	        fromdate = finyear.getStartingDate();

	        query = query +getQuery(CFunction.class, functionid," and gl.functionId=");
	        query = query +getQuery(Department.class, deptid," and vmis.departmentid=");
	        query = query +getQuery(Functionary.class, functionaryid," and vmis.functionary=");
	        query = query +getQuery(Scheme.class, schemeid," and vmis.schemeid=");
	        query = query +getQuery(SubScheme.class, subschemeid," and vmis.subschemeid=");
	        query = query +getQuery(Fund.class, fundid," and vh.fundId=");
	        query = query +getQuery(Boundary.class, boundaryid," and vmis.divisionid=");

			if(budgetheadid == null || budgetheadid.equals(EMPTY_STRING))
				throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			
			persistenceService.setType(BudgetGroup.class);
			budgetgroup = (BudgetGroup)persistenceService.findById(budgetheadid, false);
			if(budgetgroup==null || budgetgroup.getId()==null)
				throw new ValidationException(EMPTY_STRING,"Budget Head is not defined for this id [ "+budgetheadid+" ]");
			
			List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"coa_majorcode_length");
			if(appList.isEmpty())
				throw new ValidationException(EMPTY_STRING,"coa_majorcode_length is not defined in AppConfig");
			int majorcodelength = Integer.valueOf(appList.get(0).getValue());
			
			if(budgetgroup.getMinCode()!=null)
			{
				query = query +" and substr(gl.glcode,1,"+budgetgroup.getMinCode().getGlcode().length()+")<='"+budgetgroup.getMinCode().getGlcode()+"' ";
				if(budgetgroup.getMaxCode()==null)
					query = query +" and substr(gl.glcode,1,"+budgetgroup.getMinCode().getGlcode().length()+")>='"+budgetgroup.getMinCode().getGlcode()+"' ";
				else
					query = query +" and substr(gl.glcode,1,"+budgetgroup.getMinCode().getGlcode().length()+")>='"+budgetgroup.getMaxCode().getGlcode()+"' ";
			}
			else if(budgetgroup.getMajorCode()!=null)
			{
				query = query +" and substr(gl.glcode,1,"+majorcodelength+")='"+budgetgroup.getMajorCode().getGlcode()+"'";
			}
				
			if(BudgetAccountType.REVENUE_RECEIPTS.equals(budgetgroup.getAccountType()) || BudgetAccountType.CAPITAL_RECEIPTS.equals(budgetgroup.getAccountType()))
			{
				select=" SELECT SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
			}
			else if(BudgetAccountType.REVENUE_EXPENDITURE.equals(budgetgroup.getAccountType()) || BudgetAccountType.CAPITAL_EXPENDITURE.equals(budgetgroup.getAccountType()))
			{
				select=" SELECT SUM(gl.debitAmount)-SUM(gl.creditAmount) ";
			}
			
			List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"exclude_status_forbudget_actual");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"exclude_status_forbudget_actual is not defined in AppConfig");
			
			String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
			
			query= select + " FROM CGeneralLedger gl,CVoucherHeader vh,Vouchermis vmis where  " +
					" vh.id = gl.voucherHeaderId.id AND vh.id=vmis.voucherheaderid and (vmis.budgetCheckReq is null or  vmis.budgetCheckReq=true) and vh.status not in ("+voucherstatusExclude+") and vh.voucherDate>=? and vh.voucherDate <=? "+query;
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("loadActualBudget query============"+query);
			Object ob= persistenceService.find(query, fromdate,asondate);
			if(ob==null)
				return BigDecimal.ZERO;
			else
				return new BigDecimal(ob.toString());
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getActualBudgetUtilized API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
        catch(Exception e)
		{
			LOGGER.error("Exp in getActualBudgetUtilized API()==="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getActualBudgetUtilized API()==="+e.getMessage());
		}
	}
	

	/**
	 * API to load the budget consumed for the previous year/current year.
	 * it'll return the transaction amount consumed for the current year based on asOnDate or previous financial year and other given parameters from Generalledger.
	 * Transaction amount calculation for Income and Liabilities -> sum(creditamt) - sum (debitamt) 
	 * Transaction amount calculation for Expense and Assets -> sum(debitamt) - sum (creditamt)
	 * @param functionid (optional)  -id for Function object
	 * @param functionaryid (optional) - id for functionary object
	 * @param departmentid (optional) - id for department object
	 * @param schemeid (optional) - id for scheme object
	 * @param subschemeid (optional) - id for subscheme object
	 * @param boundaryid (optional) - id for boundary object
	 * @param budgetHead (mandatory) - budget head object, which having the major code/ minor code/ detailcode/ range of minor/detail codes (based on appconfig values)
	 * @param asOnDate (mandatory)- 
	 * @return transaction amount
	 */
	//Integer departmentid, Long functionid,Integer functionaryid,Integer schemeid,Integer subschemeid,Integer fieldid,Long budgetheadid
	public BigDecimal getActualBudgetUtilizedForBudgetaryCheck(Map<String,Object> paramMap) throws ValidationException
	{
		Integer deptid=null;
		Long functionid=null;
		Integer functionaryid=null;
		Integer schemeid=null;
		Integer subschemeid=null;
		Integer boundaryid=null;
		Integer fundid=null;
		Long budgetheadid=null;
		Date fromdate = null;
		Date asondate=null;
		
        String query=EMPTY_STRING,select=EMPTY_STRING;
        try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			/*if(paramMap.get(BUDGETHEADID)!=null)
				budgetheadid = (Long)paramMap.get(BUDGETHEADID);*/
			if(paramMap.get(Constants.ASONDATE)!=null)
				asondate = (java.util.Date)paramMap.get(Constants.ASONDATE);
		
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid="+deptid+",functionid="+functionid+",functionaryid="+functionaryid+",schemeid="+schemeid+",subschemeid="+subschemeid+",boundaryid="+boundaryid+",budgetheadid="+budgetheadid+",asondate="+asondate);

			if(asondate==null)
				throw new ValidationException(EMPTY_STRING,"As On Date is null");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	        FinancialYearHibernateDAO findao = new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
	        String finid = null;//findao.getFinancialYearId(sdf.format(asondate));
	      //This fix is for Phoenix Migration.
	        CFinancialYear finyear = findao.getFinancialYearById(Long.valueOf(finid));
	        if(finyear==null)
	        	throw new ValidationException(EMPTY_STRING,"Financial year is not fefined for this date ["+sdf.format(asondate)+"]");
	        fromdate = finyear.getStartingDate();

	        List<AppConfigValues> budgetGrouplist =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,BUDGETARY_CHECK_GROUPBY_VALUES);
			if(budgetGrouplist.isEmpty())
			{
				throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not defined in AppConfig");
			}
			else
			{
				final AppConfigValues appConfigValues = budgetGrouplist.get(0);
				String[] values = StringUtils.split(appConfigValues.getValue(),",");
				for(int i=0;i<values.length;i++)
				{
					if(values[i].equals("department"))
						query = query +getQuery(Department.class, deptid," and vmis.departmentid=");
					else if(values[i].equals("function"))
						 query = query +getQuery(CFunction.class, functionid," and gl.functionId=");
					else if(values[i].equals("functionary"))
						 query = query +getQuery(Functionary.class, functionaryid," and vmis.functionary=");
					else if(values[i].equals("fund"))
						query = query +getQuery(Fund.class, fundid," and vh.fundId=");
					else if(values[i].equals("scheme"))
						query = query +getQuery(Scheme.class, schemeid," and vmis.schemeid=");
					else if(values[i].equals("subscheme"))
						query = query +getQuery(SubScheme.class, subschemeid," and vmis.subschemeid=");
					else if(values[i].equals("boundary"))
						query = query +getQuery(Boundary.class, boundaryid," and vmis.divisionid=");
					else
						throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not matching="+values[i]);
				}
			}

			String glcode=EMPTY_STRING;
			if(paramMap.get("glcode")!=null)
				glcode = paramMap.get("glcode").toString();
			if(EMPTY_STRING.equals(glcode))
				throw new ValidationException(EMPTY_STRING,"Glcode is null");
			
			query=query+" and gl.glcode='"+glcode+"'";
			ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO = new ChartOfAccountsHibernateDAO(CChartOfAccounts.class,HibernateUtil.getCurrentSession());
			CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glcode);
			if(coa==null)
				throw new ValidationException(EMPTY_STRING,"Chartofaccounts is null for this glcode:"+glcode);
			
			if("I".equalsIgnoreCase(coa.getType().toString()) || "L".equalsIgnoreCase(coa.getType().toString()) )
				select=" SELECT SUM(gl.creditAmount)-SUM(gl.debitAmount) ";
			else
				select=" SELECT SUM(gl.debitAmount)-SUM(gl.creditAmount) ";
			
			List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"exclude_status_forbudget_actual");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"exclude_status_forbudget_actual is not defined in AppConfig");
			
			String voucherstatusExclude = ((AppConfigValues)list.get(0)).getValue();
			
			query= select + " FROM CGeneralLedger gl,CVoucherHeader vh,Vouchermis vmis where  " +
					" vh.id = gl.voucherHeaderId.id AND vh.id=vmis.voucherheaderid and (vmis.budgetCheckReq=null or vmis.budgetCheckReq=true) and vh.status !=4 and vh.voucherDate>=? and vh.voucherDate <=? "+query;
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("loadActualBudget query============"+query);
			Object ob= persistenceService.find(query, fromdate,asondate);
			if(ob==null)
				return BigDecimal.ZERO;
			else
				return new BigDecimal(ob.toString());
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getActualBudgetUtilizedForBudgetaryCheck API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
        catch(Exception e) 
		{
			LOGGER.error("Exp in getActualBudgetUtilizedForBudgetaryCheck API()==="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getActualBudgetUtilizedForBudgetaryCheck API()==="+e.getMessage());
		}
	}
	public BigDecimal getPlanningBudgetUsage(BudgetDetail bd) {
		if(LOGGER.isDebugEnabled())
		LOGGER.debug("Budget Detail id passed="+bd.getId());  
		
	//	BudgetDetail bd=(BudgetDetail)findById(BudgetDetail.class, budgetdtlid);
		
		if(LOGGER.isDebugEnabled())
		LOGGER.debug("Budget Detail ="+bd.getUniqueNo()+" budget= "+bd.getBudget().getId() +" FinYear="+bd.getBudget().getFinancialYear().getId());
		
		List<Long> budgetDetailIds =(List<Long>) persistenceService.findAllBy("select id from BudgetDetail bd where uniqueNo=? and bd.budget.financialYear.id=? and bd.state.value='END' ",bd.getUniqueNo(),bd.getBudget().getFinancialYear().getId());
		
		if(LOGGER.isDebugEnabled())
		LOGGER.debug("ids returned if be then 1 id should return else 2 ids should return ="+budgetDetailIds);
		
		 Query sumQuery =HibernateUtil.getCurrentSession().createQuery("select sum(consumedAmount)-sum(releasedAmount) from BudgetUsage WHERE budgetDetail.id  in ( :IDS )");
		 sumQuery.setParameterList("IDS", budgetDetailIds);
		 Double	 planningbudgetusage=(Double) sumQuery.list().get(0);
		
		 
	
		 if(planningbudgetusage==null){
			 if(LOGGER.isDebugEnabled())
				 LOGGER.debug("NO Consumed Amount");
			return BigDecimal.ZERO; 
		}else{
			 if(LOGGER.isDebugEnabled())
				 LOGGER.debug("Consumed Amount ="+BigDecimal.valueOf(planningbudgetusage));
			return BigDecimal.valueOf(planningbudgetusage);  
		}
		
		
	}
	
	/**
	 * This parameter HashMap contains deptid,functionid, functionaryid,schemeid, subschemeid,boundaryid,budgetheadid,financialyearid
	 * it'll get the budgeted amount based on the parameters.
	 * @param paramMap
	 * @return budgeted amount
	 * @throws ValidationException
	 */
	public BigDecimal getBudgetedAmtForYear(Map<String,Object> paramMap) throws ValidationException{
		Integer deptid = null;
		Long functionid = null;
		Integer functionaryid = null;
		Integer schemeid = null;
		Integer subschemeid = null;
		Integer boundaryid = null;
		Integer fundid = null;
		List<BudgetGroup> budgetHeadList=null;
		Long financialyearid = null;
	
		String query=EMPTY_STRING;
 		try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)    
				budgetHeadList = (List)paramMap.get(BUDGETHEADID);
			if(paramMap.get("financialyearid")!=null)
				financialyearid = (Long)paramMap.get("financialyearid");
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid "+deptid+",functionid "+functionid+",functionaryid "+functionaryid+",schemeid "+schemeid+",subschemeid "+subschemeid+",boundaryid "+boundaryid+",budgetheadids "+budgetHeadList+",financialyearid "+financialyearid);
			
			query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
			
			//handle the list
			
			if(financialyearid==null)
				throw new ValidationException(EMPTY_STRING,"Financial Year id is null");
			query = query+getQuery(CFinancialYear.class, financialyearid," and bd.budget.financialYear=");
			if(budgetHeadList==null || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			query = query+" and bd.budgetGroup in ( :budgetHeadList )";

			// check any RE is available for the passed parameters.if RE is not exist, take BE's Approved Amount
			String finalquery;
			if(budgetService.hasApprovedReForYear(financialyearid))
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' "+query;
			}
			else
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' "+query;
			}
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Final query="+finalquery);
			 //Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 	
			 hibQuery.setParameterList("budgetHeadList", budgetHeadList);  
			 List<BudgetDetail> bdList =  (List<BudgetDetail>)hibQuery.list();
			 if(bdList==null || bdList.size()==0)
			{
				return BigDecimal.ZERO;
			}else{
					return getApprovedAmt(bdList,budgetFinalStatus);
			}
		}catch(ValidationException v){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}catch(Exception e){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getBudgetedAmtForYear=="+e.getMessage());
		}
	}
	
	/**
	 * This parameter HashMap contains deptid,functionid, functionaryid,schemeid, subschemeid,boundaryid,budgetheadid,financialyearid
	 * it'll get the budgeted amount based on the parameters.
	 * @param paramMap
	 * @return budgeted amount
	 * @throws ValidationException
	 */
	public BigDecimal getBudgetedAmtForYearAsOnDate(Map<String,Object> paramMap,Date asOnDate) throws ValidationException{
		Integer deptid = null;
		Long functionid = null;
		Integer functionaryid = null;
		Integer schemeid = null;
		Integer subschemeid = null;
		Integer boundaryid = null;
		Integer fundid = null;
		List<BudgetGroup> budgetHeadList=null;
		Long financialyearid = null;
	
		String query=EMPTY_STRING;
 		try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)    
				budgetHeadList = (List)paramMap.get(BUDGETHEADID);
			if(paramMap.get("financialyearid")!=null)
				financialyearid = (Long)paramMap.get("financialyearid");
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid "+deptid+",functionid "+functionid+",functionaryid "+functionaryid+",schemeid "+schemeid+",subschemeid "+subschemeid+",boundaryid "+boundaryid+",budgetheadids "+budgetHeadList+",financialyearid "+financialyearid);
			
			query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
			
			//handle the list
			
			if(financialyearid==null)
				throw new ValidationException(EMPTY_STRING,"Financial Year id is null");
			query = query+getQuery(CFinancialYear.class, financialyearid," and bd.budget.financialYear=");
			if(budgetHeadList==null || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			query = query+" and bd.budgetGroup in ( :budgetHeadList )";

			// check any RE is available for the passed parameters.if RE is not exist, take BE's Approved Amount
			String finalquery;
			if(budgetService.hasApprovedReForYear(financialyearid))
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' "+query;
			}
			else
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' "+query;
			}
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Final query="+finalquery);
			 //Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 	
			 hibQuery.setParameterList("budgetHeadList", budgetHeadList);  
			 List<BudgetDetail> bdList =  (List<BudgetDetail>)hibQuery.list();
			 if(bdList==null || bdList.size()==0)
			{
				return BigDecimal.ZERO;
			}else{
					return getApprovedAmtAsOnDate(bdList,budgetFinalStatus,asOnDate);
			}
		}catch(ValidationException v){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}catch(Exception e){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getBudgetedAmtForYear=="+e.getMessage());
		}
	}
	
	public BigDecimal getPlanningPercentForYear(Map<String,Object> paramMap) throws ValidationException{
		Integer deptid = null;
		Long functionid = null;
		Integer functionaryid = null;
		Integer schemeid = null;
		Integer subschemeid = null;
		Integer boundaryid = null;
		Integer fundid = null;
		List<BudgetGroup> budgetHeadList=null;
		Long financialyearid = null;
	
		String query=EMPTY_STRING;
 		try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)    
				budgetHeadList = (List)paramMap.get(BUDGETHEADID);
			if(paramMap.get("financialyearid")!=null)
				financialyearid = (Long)paramMap.get("financialyearid");
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid "+deptid+",functionid "+functionid+",functionaryid "+functionaryid+",schemeid "+schemeid+",subschemeid "+subschemeid+",boundaryid "+boundaryid+",budgetheadids "+budgetHeadList+",financialyearid "+financialyearid);
			
			query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
			
			//handle the list
			
			if(financialyearid==null)
				throw new ValidationException(EMPTY_STRING,"Financial Year id is null");
			query = query+getQuery(CFinancialYear.class, financialyearid," and bd.budget.financialYear=");
			if(budgetHeadList==null || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			query = query+" and bd.budgetGroup in ( :budgetHeadList )";

			// check any RE is available for the passed parameters.if RE is not exist, take BE's Approved Amount
			String finalquery;
			if(budgetService.hasApprovedReForYear(financialyearid))
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' "+query;
			}
			else
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' "+query;
			}
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Final query="+finalquery);
			 //Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 	
			 hibQuery.setParameterList("budgetHeadList", budgetHeadList);  
			 List<BudgetDetail> bdList =  (List<BudgetDetail>)hibQuery.list();
			 if(bdList==null || bdList.size()==0)
			{
				return BigDecimal.ZERO;
			}else if (bdList.size()>1){
				LOGGER.error("returned multiple rows");
				return BigDecimal.ZERO;
			}else
			{
			 return	bdList.get(0).getPlanningPercent();
			}
		}catch(ValidationException v){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}catch(Exception e){
			LOGGER.error("Exp in getBudgetedAmtForYear=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getBudgetedAmtForYear=="+e.getMessage());
		}
	}
	
	
	/**
	 * This parameter HashMap contains deptid,functionid, functionaryid,schemeid, subschemeid,boundaryid,budgetheadid,financialyearid
	 * it'll get the budgeted amount based on the parameters. Only financial year parameter will be considered mandatory here.
	 * @param paramMap
	 * @return budgeted amount
	 * @throws ValidationException
	 */
	public Map<String,BigDecimal> getAggregateBudgetedAmtForYear(Map<String,Object> paramMap) throws ValidationException{
		Integer deptid = null;
		Long functionid = null;
		Integer functionaryid = null;
		Integer schemeid = null;
		Integer subschemeid = null;
		Integer boundaryid = null;
		Integer fundid = null;
		Long budgetheadid = null;
		List<Long> budgetHeadList=null;
		Long financialyearid = null;
		Map<String,BigDecimal> retMap=new HashMap<String,BigDecimal>();
		String query=EMPTY_STRING;
 		try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)    
				budgetHeadList = (List)paramMap.get(BUDGETHEADID);
			if(paramMap.get("financialyearid")!=null)
				financialyearid = (Long)paramMap.get("financialyearid");
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside getAggregateBudgetedAmtForYear---> deptid "+deptid+",functionid "+functionid+",functionaryid "+functionaryid+",schemeid "+schemeid+",subschemeid "+subschemeid+",boundaryid "+boundaryid+",budgetheadids "+budgetHeadList+",financialyearid "+financialyearid);
			query = prepareAgregateQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
						
			if(financialyearid==null)
				throw new ValidationException(EMPTY_STRING,"Financial Year id is null");
			query = query+getQuery(CFinancialYear.class, financialyearid," and bd.budget.financialYear=");
			//if(budgetHeadList==null || budgetHeadList.size()==0)
				//throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			if(budgetHeadList!=null && budgetHeadList.size()!=0)
				query = query+" and bd.budgetGroup.id in ( :budgetHeadList )";

			// check any RE is available for the passed parameters.if RE is not exist, take BE's Approved Amount
			String finalquery;
			if(budgetService.hasApprovedReForYear(financialyearid))
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='RE' "+query+ " order by bd.executingDepartment ";
			}
			else
			{
				finalquery = " from BudgetDetail bd where bd.budget.isbere='BE' "+query+" order by bd.executingDepartment ";
			}
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Final query="+finalquery);
			 Query hibQuery =HibernateUtil.getCurrentSession().createQuery(finalquery);
			 if(budgetHeadList!=null && budgetHeadList.size()!=0)
				 hibQuery.setParameterList("budgetHeadList", budgetHeadList);  
			 List<BudgetDetail> bdList =  (List<BudgetDetail>)hibQuery.list();
			 if(bdList==null || bdList.size()==0)
			{
				 return retMap;
			}else{
				return getApprovedAmtDeptwise(bdList,budgetFinalStatus);				
			}
		}catch(ValidationException v){
			LOGGER.error("Exp in getAggregateBudgetedAmtForYear =="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}catch(Exception e){
			LOGGER.error("Exp in getAggregateBudgetedAmtForYear ...=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exception in getAggregateBudgetedAmtForYear=="+e.getMessage());
		}
	}

	private BigDecimal getApprovedAmt(final List<BudgetDetail> bdList,final String finalStatus) throws ValidationException{
		BigDecimal approvedAmt = BigDecimal.ZERO;
		for(BudgetDetail bd : bdList){
			if(bd.getApprovedAmount()!=null)
				approvedAmt = approvedAmt.add(bd.getApprovedAmount());
			approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotal());
		}
		return approvedAmt;
	}
	private BigDecimal getApprovedAmtAsOnDate(final List<BudgetDetail> bdList,final String finalStatus,Date asOnDate) throws ValidationException{
		BigDecimal approvedAmt = BigDecimal.ZERO;
		for(BudgetDetail bd : bdList){
			if(bd.getApprovedAmount()!=null)
				approvedAmt = approvedAmt.add(bd.getApprovedAmount());
			approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotalAsOnDate(asOnDate));
		}
		return approvedAmt;
	}
	
	private Map<String,BigDecimal> getApprovedAmtDeptwise(final List<BudgetDetail> bdList,final String finalStatus) throws ValidationException{
		BigDecimal approvedAmt = BigDecimal.ZERO;
		String deptName=null;
		Map <String,BigDecimal> deptBudget=new HashMap<String,BigDecimal>();
		
		for(BudgetDetail bd : bdList){
			approvedAmt=BigDecimal.ZERO;
			deptName=bd.getExecutingDepartment().getName();
						
			if(bd.getApprovedAmount()!=null){
				approvedAmt=bd.getApprovedAmount();
			}
			approvedAmt = approvedAmt.add(bd.getApprovedReAppropriationsTotal());
			if(null!=deptBudget && deptBudget.containsKey(deptName)){
				approvedAmt=deptBudget.get(deptName).add(approvedAmt);
				deptBudget.put(deptName, approvedAmt);
			}
			else{
				deptBudget.put(deptName, approvedAmt);
			}
			
		}
		return deptBudget;
	}
	
	/**
	 * This API is handling the budget checking
	 * @param paramMap
	 * paramMap contains
	 * 1. debitAmt (mandatory)
	 * 2. creditAmt (mandatory)
	 * 3. deptid (optional)
	 * 4. functionid (optional)
	 * 5. functionaryid (optional)
	 * 6. schemeid (optional)
	 * 7. subschemeid (optional)
	 * 8. boundaryid (optional)
	 * 9. glcode (mandatory) - based on the glcode, we can get the budgetheadid
	 * 10. asondate (manadtory) - to get the actuals, we need asondate
	 * 11. mis.budgetcheckreq-Boolean- (optional) to  skip  budget check if set to false.Default is true
	 * Budget checking will be enabled or disabled by these levels and in the order
	 *   a. Application - uses appconfig "budgetCheckRequired"
	 *   b. Voucherlevel - uses budgetcheckreq column of vouchermis table for perticular voucher
	 *   c. Debit or Credit  level - uses Budgetgroup.budgetting type for debit side only ,credit side  or both 
	 *   d. Glcode level - Uses chartofaccounts.budgetcheckreq fieled to decide budget checking .
	 * @return
	 * @throws ValidationException
	 */
	public boolean budgetaryCheck(Map<String,Object> paramMap) throws ValidationException
	{
		String cashbasedbudgetType=EMPTY_STRING,txnType=EMPTY_STRING;
		BigDecimal debitAmt=null;
		BigDecimal creditAmt=null;
		BigDecimal txnAmt=null;
		
		try
		{
			List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetCheckRequired");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"budgetCheckRequired is not defined in AppConfig");
			
			if("N".equalsIgnoreCase(((AppConfigValues)list.get(0)).getValue()))
				return true;
			if(paramMap.get("mis.budgetcheckreq")!=null && ( (Boolean) paramMap.get("mis.budgetcheckreq")).equals(false))
				return true;

			if(paramMap.get("debitAmt")!=null)
				debitAmt = (BigDecimal) paramMap.get("debitAmt");
			if(paramMap.get("creditAmt")!=null)
				creditAmt = (BigDecimal) paramMap.get("creditAmt");
			
			if(debitAmt==null && creditAmt==null) 
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is null");
			
			if(debitAmt!=null && debitAmt.compareTo(BigDecimal.ZERO)==0 && creditAmt!=null && creditAmt.compareTo(BigDecimal.ZERO)==0)
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is zero");
			
			if(debitAmt!=null && debitAmt.compareTo(BigDecimal.ZERO)>0  && creditAmt!=null && creditAmt.compareTo(BigDecimal.ZERO)>0)
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is greater than zero");
			
			// get the type of budget from appconfig .
			list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetaryCheck_budgettype_cashbased");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"budgetaryCheck_budgettype_cashbased is not defined in AppConfig");
			
			cashbasedbudgetType = ((AppConfigValues)list.get(0)).getValue();
			if(cashbasedbudgetType.equalsIgnoreCase("Y"))  // cash based budget
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("cashbasedbudgetType=="+cashbasedbudgetType);
			}
			else  // Accural based budgeting
			{
				if(debitAmt!=null  && debitAmt.compareTo(BigDecimal.ZERO)>0)
				{
					txnType = "debit";
					txnAmt = debitAmt;
				}
				else
				{
					txnType = "credit";
					txnAmt = creditAmt;
				}
				paramMap.put("txnAmt", txnAmt);
				paramMap.put("txnType", txnType);
				return checkCondition(paramMap);
			}
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in budgetary check API()="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in budgetary check API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return true;
	}
	
	/** 
	 * To check budget available for the glcode with other parameters.  
	 * if txnamt is less than the budget available, it would return true, otherwise false.
	 * @param paramMap
	 * @return
	 * @throws ValidationException
	 */
	private boolean checkCondition(Map<String,Object> paramMap) throws ValidationException
	{
		String txnType=null,glCode=null;
		BigDecimal txnAmt=null;
		java.util.Date asondate =null;
		java.util.Date fromdate=null;
		
		try
		{
			if(paramMap.get("txnAmt")!=null)
				txnAmt = (BigDecimal) paramMap.get("txnAmt");
			if(paramMap.get("txnType")!=null)
				txnType = paramMap.get("txnType").toString();
			if(paramMap.get("glcode")!=null)
				glCode = paramMap.get("glcode").toString();
			if(paramMap.get(Constants.ASONDATE)!=null)
				asondate = (Date) paramMap.get(Constants.ASONDATE);
			
			if(glCode==null)
				throw new ValidationException(EMPTY_STRING,"glcode is null");
			if(txnAmt==null)
				throw new ValidationException(EMPTY_STRING,"txnAmt is null");
			if(txnType==null)
				throw new ValidationException(EMPTY_STRING,"txnType is null");
			if(asondate==null)
				throw new ValidationException(EMPTY_STRING,"As On Date is null");
			
			// check the account code needs budget checking
			ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO = new ChartOfAccountsHibernateDAO(CChartOfAccounts.class,HibernateUtil.getCurrentSession());
			CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glCode);
			if(coa.getBudgetCheckReq()!=null && coa.getBudgetCheckReq().intValue()==1)
			{
				// get budgethead for the glcode
				//BudgetGroup bg = getBudgetHeadByGlcode(coa,paramMap);
				List<BudgetGroup> budgetHeadListByGlcode = getBudgetHeadByGlcode(coa);
				if(budgetHeadListByGlcode==null || budgetHeadListByGlcode.size()==0)
				{
					throw new ValidationException(EMPTY_STRING,"Budget Group is not defined for this glcode/No Budget details for this glcode : "+coa.getGlcode());
				}
			//get budgettinh type for first BG object
				if(!isBudgetCheckingRequiredForType(txnType,budgetHeadListByGlcode.get(0).getBudgetingType().toString()))
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("No need to check budget for :"+glCode+" as the transaction type is "+txnType);
					return true;
				}
				
				paramMap.put("glcodeid", coa.getId());
				// get the financialyear from asondate
				FinancialYearHibernateDAO finDAO = new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
				String finyearid = null;//finDAO.getFinancialYearId(new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE).format(asondate));
				//This fix is for Phoenix Migration.
				if(EMPTY_STRING.equals(finyearid))
					throw new ValidationException(EMPTY_STRING,"Financial Year is not defined for-"+asondate);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
		        CFinancialYear finyear = finDAO.getFinancialYearById(Long.valueOf(finyearid));
		        if(finyear==null)
		        	throw new ValidationException(EMPTY_STRING,"Financial year is not fefined for this date ["+sdf.format(asondate)+"]");
		        fromdate = finyear.getStartingDate();
		       
		        paramMap.put("fromdate", fromdate);
			//Here as on date is overridden by Financialyear ending date to check all budget appropriation irrespective of date	
		        paramMap.put(Constants.ASONDATE, finyear.getEndingDate());
				paramMap.put("financialyearid", Long.valueOf(finyearid));
				
				paramMap.put(BUDGETHEADID, budgetHeadListByGlcode);
				
				if(LOGGER.isDebugEnabled())     LOGGER.debug("************ BudgetCheck Details *********************");
				//pass the list of bg to getBudgetedAmtForYear 
				BigDecimal budgetedAmt =getBudgetedAmtForYear(paramMap);  // get the budgetedamount
				if(LOGGER.isDebugEnabled())     LOGGER.debug(".............Budgeted Amount For the year............"+budgetedAmt);
				if(budgetedAmt.compareTo(BigDecimal.ZERO)==0)
					return false;
				
				BigDecimal actualAmt = getActualBudgetUtilizedForBudgetaryCheck(paramMap); // get actual amount
				if(LOGGER.isDebugEnabled())     LOGGER.debug(".............Voucher Actual amount............"+actualAmt);
			
				BigDecimal billAmt = getBillAmountForBudgetCheck(paramMap); // get actual amount
				if(LOGGER.isDebugEnabled())     LOGGER.debug(".............Bill Actual amount............"+billAmt);
				EgBillregister bill=null;
				
				if(paramMap.get("bill")!=null )
				{
					bill =(EgBillregister) persistenceService.find("from EgBillregister where id=? ",(Long)paramMap.get("bill"));
				}
				if(bill!=null && bill.getEgBillregistermis().getBudgetaryAppnumber()!=null)
				{
					if(LOGGER.isDebugEnabled())     LOGGER.debug(".............Found BillId so subtracting txn amount......................"+txnAmt);
					billAmt=billAmt.subtract(txnAmt);
				}
				if(LOGGER.isDebugEnabled())     LOGGER.debug(".......Recalculated Bill Actual amount............"+billAmt);
				BigDecimal diff = budgetedAmt.subtract(actualAmt).subtract(billAmt); // get bill amt
				if(LOGGER.isDebugEnabled())     LOGGER.debug(".................diff amount..........................."+diff);
				
				if(LOGGER.isDebugEnabled())     LOGGER.debug("************ BudgetCheck Details End****************");
				//BigDecimal diff = budgetedAmt.subtract(actualAmt);
				
				if(txnAmt.compareTo(diff)<=0)
				{ 
					
					
					if(bill==null || bill.getEgBillregistermis().getBudgetaryAppnumber()==null)
					{
						if(paramMap.get("voucherHeader")!=null && ((CVoucherHeader)paramMap.get("voucherHeader")).getVouchermis().getBudgetaryAppnumber()==null )
						{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("Bill level budget app no not generated so generating voucher level");
							if(bill!=null)
							{
							if(LOGGER.isDebugEnabled())     LOGGER.debug("bill Number..........."+bill.getBillnumber());
							}else
							{
								if(LOGGER.isDebugEnabled())     LOGGER.debug("Bill not present");
							}
							((CVoucherHeader)paramMap.get("voucherHeader")).getVouchermis().setBudgetaryAppnumber(getBudgetApprNumber(paramMap));
						}
					}
					return true;
				}
				
				else
					return false;
			}
			else
				return true;
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in checkCondition API()="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in checkCondition API=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	
	/**
	 * @param paramMap
	 * @return
	 */
	public String getBudgetApprNumber(Map<String, Object> paramMap) {
		CFinancialYear  financialyear=(CFinancialYear)persistenceService.find("from CFinancialYear where id=?",(Long)paramMap.get("financialyearid"));
		ScriptContext scriptContext = ScriptService.createContext("wfItem",financialyear,"sequenceGenerator",sequenceGenerator);
		String budgetApprNumber=(String)scriptExecutionService.executeScript("egf.reappropriation.sequence.generator", scriptContext);
		return budgetApprNumber;
	}

	/**
	 * to check the budget checking is required or not 
	 * @param txnType
	 * @param budgetingType
	 * @return
	 */
	private boolean isBudgetCheckingRequiredForType(String txnType,String budgetingType)
	{
		if("debit".equalsIgnoreCase(budgetingType) && "debit".equals(txnType))
			   return true;
		else if("credit".equalsIgnoreCase(budgetingType) && "credit".equals(txnType))
			   return true;
		else if("all".equalsIgnoreCase(budgetingType))
		   return true;
		else			   
		   return false;
	}
	
	/**
	 * To get the Budgetgroup for the glcode at detailcode level or minorcode level or major code level.
	 * @param coa
	 * @param paramMap 
	 * @return
	 * @throws ValidationException
	 */
	
	//return list of budget group
	//try to make protected 
	public List<BudgetGroup> getBudgetHeadByGlcode(CChartOfAccounts coa) throws ValidationException
	{                               
		try
		{
			List<AppConfigValues> appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"coa_majorcode_length");
			if(appList.isEmpty())
				throw new ValidationException(EMPTY_STRING,"coa_majorcode_length is not defined");
			int majorcodelength = Integer.valueOf(appList.get(0).getValue());
			
			appList = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"coa_minorcode_length");
			if(appList.isEmpty())
				throw new ValidationException(EMPTY_STRING,"coa_minorcode_length is not defined");
			int minorcodelength = Integer.valueOf(appList.get(0).getValue());
			
			//  check the budget group is defined at detailcode level or detailcode within the range
			String query=" from BudgetGroup bg where bg.minCode.glcode<='"+coa.getGlcode()+"' and bg.maxCode.glcode>='"+coa.getGlcode()+"'  and bg in (select budgetGroup from BudgetDetail) and bg.isActive=1";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("getBudgetHeadByGlcode detailcode query====="+query);
			persistenceService.setType(BudgetGroup.class);
			 List bgList = persistenceService.findAllBy(query);
			if(bgList.isEmpty())
			{
				query=" from BudgetGroup bg where bg.minCode.glcode<='"+coa.getGlcode().substring(0, minorcodelength)+"' and bg.maxCode.glcode>='"+coa.getGlcode().substring(0, minorcodelength)+"' and bg in (select budgetGroup from BudgetDetail) and bg.isActive=1";
				if(LOGGER.isDebugEnabled())     LOGGER.debug("getBudgetHeadByGlcode minorcode query====="+query);
				persistenceService.setType(BudgetGroup.class);
				bgList =  persistenceService.findAllBy(query);
				if(bgList.isEmpty())
				{
					query=" from BudgetGroup bg where bg.majorCode.glcode='"+coa.getGlcode().substring(0, majorcodelength)+"' and bg in (select budgetGroup from BudgetDetail) and bg.isActive=1 ";
					persistenceService.setType(BudgetGroup.class);
					bgList = persistenceService.findAllBy(query);
					if(bgList.isEmpty())
					{
						throw new ValidationException(EMPTY_STRING,"Budget Group is not defined for this glcode/No Budget details for this glcode : "+coa.getGlcode());
					}
					else
						return bgList;
				}
				else // check the budget group is defined at majorcode level
				{
					return bgList;
				}
			}
			else // check the budget group is defined at minor level
			{
				return bgList;
			}
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getBudgetHeadByGlcode API()="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetHeadByGlcode API="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	
	/**
	 * To get the Budgetgroup for the glcode at detailcode level for a list of COA codes
	 * @param coa
	 * @param paramMap 
	 * @return
	 * @throws ValidationException
	 */
	public List<BudgetGroup> getBudgetHeadForGlcodeList(List<CChartOfAccounts> coa) throws ValidationException
	{                               
		try
		{
			String coaQry="bg.minCode.glcode in(";
			
			if(coa.isEmpty())
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("No COA is been passed");
				throw new ValidationException(EMPTY_STRING,"No Chartofaccount code is been passed for getting the budget heads");
			}
			else{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("COA list size passed "+coa.size());
				for(Integer i=0;i<coa.size();i++ )
				{
					if(i!=coa.size()-1)
						coaQry=coaQry+coa.get(i).getGlcode()+",";
					else
						coaQry=coaQry+coa.get(i).getGlcode()+")";
				}
			}
			String query=" from BudgetGroup bg where "+coaQry+" and bg in (select budgetGroup from BudgetDetail) and bg.isActive=1 order by bg.name";
			if(LOGGER.isDebugEnabled())     LOGGER.debug("getBudgetHeadForGlcodeList detailcode query====="+query);
			persistenceService.setType(BudgetGroup.class);
			List bgList = persistenceService.findAllBy(query);
			return bgList;

		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getBudgetHeadForGlcodeList API()="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetHeadForGlcodeList API="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
	}
	
	public String getQuery(Class clazz,Serializable id,String queryString) throws ValidationException{
		
		session =HibernateUtil.getCurrentSession();
		String query=EMPTY_STRING;
		if(id==null)
			return query;
		try{
			Object o = findById(clazz, id); 
			if(o != null)
				query = queryString+id;
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getQuery=="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e){
			LOGGER.equals("Exp in getQuery=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return query;
	}
	
	public PersistenceService getService(){
		service = new PersistenceService<BudgetDetail, Long>();
		//service.setSessionFactory(new SessionFactory());
		service.setType(BudgetDetail.class);
		return service;
	}
	
	/**
	 * This API is handling the budget checking
	 * @param paramMap
	 * paramMap contains
	 * 1. debitAmt (mandatory)
	 * 2. creditAmt (mandatory)
	 * 3. deptid (optional)
	 * 4. functionid (optional)
	 * 5. functionaryid (optional)
	 * 6. schemeid (optional)
	 * 7. subschemeid (optional)
	 * 8. boundaryid (optional)
	 * 9. glcode (mandatory) - based on the glcode, we can get the budgetheadid
	 * 10. asondate (manadtory) - to get the actuals, we need asondate
	 * 11.mis.budgetcheckreq-Boolean-(optional) set to false if budget check not to be done for this bill 
	 * default is True.  
	 * @return
	 * @throws ValidationException
	 */
	public boolean budgetaryCheckForBill(Map<String,Object> paramMap) throws ValidationException
	{
		String cashbasedbudgetType=EMPTY_STRING,txnType=EMPTY_STRING;
		BigDecimal debitAmt=null;
		BigDecimal creditAmt=null;
		BigDecimal txnAmt=null;
		String glCode="";
		Date asondate=null;
		Date fromdate = null;
		try
		{
			List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetCheckRequired");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"budgetCheckRequired is not defined in AppConfig");
			
			if("N".equalsIgnoreCase(((AppConfigValues)list.get(0)).getValue()))
			{
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Application Level budget check disabled skipping budget check.");
				return true;
			}
			if(paramMap.get("mis.budgetcheckreq")!=null && ((Boolean)paramMap.get("mis.budgetcheckreq")).equals(false))
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("voucher Level budget check disabled  so skipping budget check.");
				return true;
			}
			if(paramMap.get("debitAmt")!=null)
				debitAmt = (BigDecimal) paramMap.get("debitAmt");
			if(paramMap.get("creditAmt")!=null)
				creditAmt = (BigDecimal) paramMap.get("creditAmt");
			
			if(debitAmt==null && creditAmt==null) 
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is null");
			
			if(debitAmt!=null && debitAmt.compareTo(BigDecimal.ZERO)==0 && creditAmt!=null && creditAmt.compareTo(BigDecimal.ZERO)==0)
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is zero");
			
			if(debitAmt!=null && debitAmt.compareTo(BigDecimal.ZERO)>0  && creditAmt!=null && creditAmt.compareTo(BigDecimal.ZERO)>0)
				throw new ValidationException(EMPTY_STRING,"Both Debit and Credit amount is greater than zero");
			
			// get the type of budget from appconfig .
			list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,"budgetaryCheck_budgettype_cashbased");
			if(list.isEmpty())
				throw new ValidationException(EMPTY_STRING,"budgetaryCheck_budgettype_cashbased is not defined in AppConfig");
			
			cashbasedbudgetType = ((AppConfigValues)list.get(0)).getValue();
			if(cashbasedbudgetType.equalsIgnoreCase("Y"))  // cash based budget
			{
				if(LOGGER.isDebugEnabled())     LOGGER.debug("cashbasedbudgetType=="+cashbasedbudgetType);
			}
			else  // Accural based budgeting
			{
				if(debitAmt!=null  && debitAmt.compareTo(BigDecimal.ZERO)>0)
				{
					txnType = "debit";
					txnAmt = debitAmt;
				}
				else
				{
					txnType = "credit";
					txnAmt = creditAmt;
				}

				if(paramMap.get("glcode")!=null)
					glCode = paramMap.get("glcode").toString();
				if(paramMap.get(Constants.ASONDATE)!=null)
					asondate = (Date) paramMap.get(Constants.ASONDATE);
				
				if(glCode==null)
					throw new ValidationException(EMPTY_STRING,"glcode is null");
				if(txnAmt==null)
					throw new ValidationException(EMPTY_STRING,"txnAmt is null");
				if(txnType==null)
					throw new ValidationException(EMPTY_STRING,"txnType is null");
				if(asondate==null)
					throw new ValidationException(EMPTY_STRING,"As On Date is null");
				
				// check the account code needs budget checking
				ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO = new ChartOfAccountsHibernateDAO(CChartOfAccounts.class,HibernateUtil.getCurrentSession());
				CChartOfAccounts coa = chartOfAccountsHibernateDAO.getCChartOfAccountsByGlCode(glCode);
				if(coa.getBudgetCheckReq()!=null && coa.getBudgetCheckReq().intValue()==1)
				{
					// get budgethead for the glcode
					List<BudgetGroup> budgetHeadListByGlcode = getBudgetHeadByGlcode(coa);
					
					if(!isBudgetCheckingRequiredForType(txnType,budgetHeadListByGlcode.get(0).getBudgetingType().toString()))
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("No need to check budget for :"+glCode+" as the transaction type is "+txnType+"so skipping budget check");
						return true;
					}
					
					// get the financialyear from asondate
					FinancialYearHibernateDAO finDAO = new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
					String finyearid = null;//finDAO.getFinancialYearId(new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE).format(asondate));
					//This fix is for Phoenix Migration.
					if(EMPTY_STRING.equals(finyearid))
						throw new ValidationException(EMPTY_STRING,"Financial Year is not defined for-"+asondate);
					
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
			        CFinancialYear finyear = finDAO.getFinancialYearById(Long.valueOf(finyearid));
			        if(finyear==null)
			        	throw new ValidationException(EMPTY_STRING,"Financial year is not defined for this date ["+sdf.format(asondate)+"]");
			        fromdate = finyear.getStartingDate();
			        
					paramMap.put("financialyearid", Long.valueOf(finyearid));
					paramMap.put(BUDGETHEADID, budgetHeadListByGlcode);
					paramMap.put("fromdate", fromdate);
					paramMap.put(Constants.ASONDATE, finyear.getEndingDate());
					
					paramMap.put(GLCODEID, coa.getId());
					if(LOGGER.isDebugEnabled())     LOGGER.debug("************ BudgetCheck Details For bill *********************");
					BigDecimal budgetedAmt =getBudgetedAmtForYear(paramMap);  // get the budgetedamount
					
					if(LOGGER.isDebugEnabled())     LOGGER.debug(".................Budgeted amount......................"+budgetedAmt);
					if(budgetedAmt.compareTo(BigDecimal.ZERO)==0)
					{
					if(LOGGER.isDebugEnabled())     LOGGER.debug("Budget check failed Because of  Budgeted not allocated for the combination");
						return false;
					}
					
					BigDecimal actualAmt = getActualBudgetUtilizedForBudgetaryCheck(paramMap); // get actual amount only Generalledger
					if(LOGGER.isDebugEnabled())     LOGGER.debug("..................Voucher Actual amount......................."+actualAmt);
					
					BigDecimal billAmt = getBillAmountForBudgetCheck(paramMap); // get actual amount of Bill 
					EgBillregister bill=null;
					
					if(paramMap.get("bill")!=null )
					{
						bill =(EgBillregister) paramMap.get("bill");
					}
					if(bill!=null && bill.getEgBillregistermis().getBudgetaryAppnumber()!=null)
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug(".............Found BillId so subtracting txn amount......................"+txnAmt);
						billAmt=billAmt.subtract(txnAmt);
					}
					
					if(LOGGER.isDebugEnabled())     LOGGER.debug("........................Bill Actual amount ........................"+billAmt);
					
					BigDecimal diff = budgetedAmt.subtract(actualAmt).subtract(billAmt); // get diff
					if(LOGGER.isDebugEnabled())     LOGGER.debug("......................diff amount......................"+diff);
					if(LOGGER.isDebugEnabled())     LOGGER.debug("************ BudgetCheck Details For bill End *********************");
					
					if(txnAmt.compareTo(diff)<=0)
					{   if(paramMap.get("bill")!=null)
						{
						  // HibernateUtil.getCurrentSession().refresh((EgBillregister)paramMap.get("bill"));
							if(((EgBillregister)paramMap.get("bill")).getEgBillregistermis().getBudgetaryAppnumber()==null )
							{
							String budgetApprNumber = getBudgetApprNumber(paramMap);
							((EgBillregister)paramMap.get("bill")).getEgBillregistermis().setBudgetaryAppnumber(budgetApprNumber);
							}
						}
						return true;
					    
					}
					else
					{
						if(LOGGER.isDebugEnabled())     LOGGER.debug("Budget check failed Because of  non availability of enough amount");
						return false;
					}
				}
				else
					return true;
				
			}
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in budgetary check API()="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in budgetary check API()="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return true;
	}
	
	public BigDecimal getBillAmountForBudgetCheck(Map<String,Object> paramMap) throws ValidationException
	{
		Integer deptid=null;
		Long functionid=null;
		Integer functionaryid=null;
		Integer schemeid=null;
		Integer subschemeid=null;
		Integer boundaryid=null;
		Integer fundid=null;
		Long glcodeid=null;
		Date fromdate = null;
		Date asondate=null;
		BigDecimal totalBillUtilized= new BigDecimal(0);
		
        String query=EMPTY_STRING;
        String query1=EMPTY_STRING;
        try
		{
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(GLCODEID)!=null)
				glcodeid = (Long)paramMap.get(GLCODEID);
			if(paramMap.get(Constants.ASONDATE)!=null)
				asondate = (java.util.Date)paramMap.get(Constants.ASONDATE);
			if(paramMap.get("fromdate")!=null)
				fromdate = (java.util.Date)paramMap.get("fromdate");
			
			// get the financialyear from asondate
			FinancialYearHibernateDAO finDAO = new FinancialYearHibernateDAO(CFinancialYear.class,HibernateUtil.getCurrentSession());
			String finyearid = null;//finDAO.getFinancialYearId(new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE).format(asondate));
			//This fix is for Phoenix Migration.
			if(EMPTY_STRING.equals(finyearid))
				throw new ValidationException(EMPTY_STRING,"Financial Year is not defined for-"+asondate);
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
	        CFinancialYear finyear = finDAO.getFinancialYearById(Long.valueOf(finyearid));
	        if(finyear==null)
	        	throw new ValidationException(EMPTY_STRING,"Financial year is not defined for this date ["+sdf.format(asondate)+"]");
	        fromdate = finyear.getStartingDate();
	        
			paramMap.put("financialyearid", Long.valueOf(finyearid));
			paramMap.put("fromdate", fromdate);
			paramMap.put(Constants.ASONDATE, finyear.getEndingDate());
		
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid="+deptid+",functionid="+functionid+",functionaryid="+functionaryid+",schemeid="+schemeid+",subschemeid="+subschemeid+",boundaryid="+boundaryid+",glcodeid="+glcodeid+",asondate="+asondate);

			if(asondate==null)
				throw new ValidationException(EMPTY_STRING,"As On Date is null");

			List<AppConfigValues> budgetGrouplist =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,BUDGETARY_CHECK_GROUPBY_VALUES);
			if(budgetGrouplist.isEmpty())
			{
				throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not defined in AppConfig");
			}
			else
			{
				final AppConfigValues appConfigValues = budgetGrouplist.get(0);
				String[] values = StringUtils.split(appConfigValues.getValue(),",");
				for(int i=0;i<values.length;i++)
				{
					if(values[i].equals("department"))
					{
						if(deptid==null || deptid==0)
							throw new ValidationException(EMPTY_STRING,"Department is required");
						else
							query = query +getQuery(Department.class, deptid," and bmis.egDepartment.id=");
					}
					else if(values[i].equals("function"))
					{
						if(functionid==null || functionid==0)
							throw new ValidationException(EMPTY_STRING,"Function is required");
						else
							query = query +getQuery(CFunction.class, functionid," and bd.functionid=");
					}
					else if(values[i].equals("functionary"))
					{
						if(functionaryid==null || functionaryid==0)
							throw new ValidationException(EMPTY_STRING,"Functionary is required");
						else
							query = query +getQuery(Functionary.class, functionaryid," and bmis.functionaryid.id=");
					}
					else if(values[i].equals("fund"))
					{
						if(fundid==null || fundid==0)
							throw new ValidationException(EMPTY_STRING,"Fund is required");
						else
							query = query +getQuery(Fund.class, fundid," and bmis.fund.id=");
					}
					else if(values[i].equals("scheme"))
					{
						if(schemeid==null || schemeid==0)
							throw new ValidationException(EMPTY_STRING,"Scheme is required");
						else
							query = query +getQuery(Scheme.class, schemeid," and bmis.scheme.id=");
					}
					else if(values[i].equals("subscheme"))
					{
						if(subschemeid==null || subschemeid==0)
							throw new ValidationException(EMPTY_STRING,"Sub scheme is required");
						else
							query = query +getQuery(SubScheme.class, subschemeid," and bmis.subScheme.id=");
					}
					else if(values[i].equals("boundary"))
					{
						if(boundaryid==null || boundaryid==0)
							throw new ValidationException(EMPTY_STRING,"Boundary is required");
						else
							query = query +getQuery(Boundary.class, boundaryid," and bmis.fieldid.id=");
					}
					else
						throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not matching="+values[i]);
				}
			}
			if(asondate!=null)
			{
				query=query+" and br.billdate <=? ";
			}
			if(fromdate!=null)
			{
				query=query+" and  br.billdate>=? ";
			}
			
			
			
				
			query=query+" and bd.glcodeid='"+glcodeid+"'";
			query1= "select sum(decode(bd.debitamount,null,0,bd.debitamount)-decode(bd.creditamount,null,0,bd.creditamount))  " +
					" from EgBillregister br, EgBilldetails bd, EgBillregistermis bmis  " +
					" where br.id=bd.egBillregister.id and br.id=bmis.egBillregister.id and (bmis.budgetCheckReq is null or bmis.budgetCheckReq=true)  and bmis.voucherHeader is null and upper(br.status.description) not in ('CANCELLED') " +
					"    "+query;
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("getBillAmountForBudgetCheck query============"+query1);
			Object ob=null;
			if(fromdate!=null)
			{
			ob= persistenceService.find(query1, asondate,fromdate);
			}
			else
			{
				ob= persistenceService.find(query1, asondate);   
			}
			
			BigDecimal billAmountWhereCancelledVouchers=getBillAmountWhereCancelledVouchers(query,fromdate,asondate);
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Total Amount from all bills where vouchers are cancelled is : "+billAmountWhereCancelledVouchers);
			if(ob==null){
				totalBillUtilized=billAmountWhereCancelledVouchers;
			}
			else{
				totalBillUtilized=new BigDecimal(ob.toString()).add(billAmountWhereCancelledVouchers);
			}
			return totalBillUtilized;
						
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getBillAmountForBudgetCheck API()==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
        catch(Exception e)
		{
			LOGGER.error("Exp in getBillAmountForBudgetCheck API()==="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getBillAmountForBudgetCheck API()==="+e.getMessage());
		}
	}

/**
 * This API is to get the total bill amount where bill vouchers are created but cancelled later.	
 * @param query
 * @param fromdate
 * @param asondate
 * @return
 * @throws ValidationException
 */
	
	private BigDecimal getBillAmountWhereCancelledVouchers(String query,Date fromdate,Date asondate) throws ValidationException{
		
		String newQuery= "select sum(decode(bd.debitamount,null,0,bd.debitamount)-decode(bd.creditamount,null,0,bd.creditamount))  " +
				" from EgBillregister br, EgBilldetails bd, EgBillregistermis bmis,CVoucherHeader vh  " +
				" where br.id=bd.egBillregister.id and br.id=bmis.egBillregister.id and (bmis.budgetCheckReq is null or bmis.budgetCheckReq=true)  and bmis.voucherHeader=vh.id and upper(br.status.description) not in ('CANCELLED') " +
				"  and vh.status=4  "+query;
		
		if(LOGGER.isDebugEnabled())     LOGGER.debug("getBillAmountWhereCancelledVouchers query============"+newQuery);
		Object ob=null;
		if(fromdate!=null)
		{
		ob= persistenceService.find(newQuery, asondate,fromdate);
		}
		else
		{
			ob= persistenceService.find(newQuery, asondate);   
		}
		if(ob==null)
			return BigDecimal.ZERO;
		else
			return new BigDecimal(ob.toString());		
		
	}

	/**
	 * This parameter HashMap contains deptid,functionid, functionaryid,schemeid, subschemeid,boundaryid,budgetheadid,financialyearid,typeBeRe
	 * it'll get the budgeted amount based on the parameters.
	 * @param paramMap  
	 * @return budgeted amount
	 * @throws ValidationException
	 */
	public BigDecimal getBudgetedAmtForYearRegardingBEorRE(Map<String,Object> paramMap, String typeBeRe) throws ValidationException
	{
		Integer deptid = null;
		Long functionid = null;
		Integer functionaryid = null;
		Integer schemeid = null;
		Integer subschemeid = null;
		Integer boundaryid = null;
		Integer fundid = null;
		Long budgetheadid = null;
		Long financialyearid = null;
	
		String query=EMPTY_STRING;
		
 		try {
			if(paramMap.get(Constants.DEPTID)!=null)
				deptid = (Integer)paramMap.get(Constants.DEPTID);
			if(paramMap.get(Constants.FUNCTIONID)!=null)
				functionid = (Long)paramMap.get(Constants.FUNCTIONID);
			if(paramMap.get(Constants.FUNCTIONARYID)!=null)
				functionaryid = (Integer)paramMap.get(Constants.FUNCTIONARYID);
			if(paramMap.get(Constants.SCHEMEID)!=null)
				schemeid = (Integer)paramMap.get(Constants.SCHEMEID);
			if(paramMap.get(Constants.SUBSCHEMEID)!=null)
				subschemeid = (Integer)paramMap.get(Constants.SUBSCHEMEID);
			if(paramMap.get(Constants.FUNDID)!=null)
				fundid = (Integer)paramMap.get(Constants.FUNDID);
			if(paramMap.get(Constants.BOUNDARYID)!=null)
				boundaryid = (Integer)paramMap.get(Constants.BOUNDARYID);
			if(paramMap.get(BUDGETHEADID)!=null)
				budgetheadid = (Long)paramMap.get(BUDGETHEADID);
			if(paramMap.get("financialyearid")!=null)
				financialyearid = (Long)paramMap.get("financialyearid");
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("deptid "+deptid+",functionid "+functionid+",functionaryid "+functionaryid+",schemeid "+schemeid+",subschemeid "+subschemeid+",boundaryid "+boundaryid+",budgetheadid "+budgetheadid+",financialyearid "+financialyearid);
			
			query = prepareQuery(deptid, functionid, functionaryid, schemeid, subschemeid, boundaryid, fundid);
			
			if(budgetheadid==null)
				throw new ValidationException(EMPTY_STRING,"Budget head id is null or empty");
			query = query+getQuery(BudgetGroup.class, budgetheadid," and bd.budgetGroup=");

			if(financialyearid==null)
				throw new ValidationException(EMPTY_STRING,"Financial Year id is null");
			query = query+getQuery(CFinancialYear.class, financialyearid," and bd.budget.financialYear=");

			String finalquery = " from BudgetDetail bd where bd.budget.isbere= '"+typeBeRe+"' "+query;
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("finalquery  ="+finalquery);
			List<BudgetDetail> bdList =  (List<BudgetDetail>) getService().findAllBy(finalquery);
			
			if(bdList==null || bdList.size()==0)
			{
				return BigDecimal.ZERO;
			}
			else
			{
				return getApprovedAmt(bdList,budgetFinalStatus);
			}
		}catch(ValidationException v)
		{
			LOGGER.error("Exp in getBudgetedAmtForYear=="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exp in getBudgetedAmtForYear=="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,"Exp in getBudgetedAmtForYear=="+e.getMessage());
		}
	}
	
	/**
	 * @description - returns the Sanctioned Planning Budget amount based on various parameters.
	 * For an year if there is an approved RE, then the RE sanctioned planning amount will be returned else the sanctioned BE amount will be returned.
	 * This will take care of the re-appropriations also.
	 * @param parameter Map contains deptid,functionid, functionaryid,schemeid, subschemeid,boundaryid,budgetheadid,financialyearid,fundid
	 * it'll get the budgeted amount based on the parameters.
	 * @return Sanctioned Planning Budget amount
	 * @throws ValidationException
	 */
	public BigDecimal getSanctionedPlanningBudget(Map<String,Object> paramMap){
		BigDecimal multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
		BigDecimal budgetedAmtForYear = zeroOrValue(getBudgetedAmtForYear(paramMap));
		return budgetedAmtForYear.multiply(multiplicationFactor);
	}
	
	private BigDecimal zeroOrValue(BigDecimal value) {
		return value==null?BigDecimal.ZERO:value;
	}
	
	private String getAppConfigFor(String module,String key) {
		try {
			List<AppConfigValues> list = genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(module,key);
			return list.get(0).getValue().toString();
		} catch (Exception e) {
			throw new ValidationException(Arrays.asList(new ValidationError(key+" not defined in appconfig",key+" not defined in appconfig")));
		}
	}
	
	/**
	 * @description - get the list of BudgetUsage based on various parameters 
	 * @param queryParamMap - HashMap<String, Object> queryParamMap will have data required for the query 
	 *  the queryParamMap contain values :- 
	 *  the mis attribute values passed in the query map will be validated with the appconfig value of key=budgetaryCheck_groupby_values
	 *  financialyearid - optional
	 *  ExecutionDepartmentId - mandatory -if:department present in the app config value
	 *                        - optional -else
	 *  fundId 			- mandatory -if:fund present in the app config value
	 *                  - optional -else
	 *  schemeId	    - mandatory -if:Scheme present in the app config value
	 *                  - optional -else
	 *  functionId	    - mandatory -if:function present in the app config value
	 *                  - optional -else
	 *  subschemeId     - mandatory -if:Subscheme present in the app config value
	 *                  - optional -else
	 *  functionaryId   - mandatory -if:functionary present in the app config value
	 *                  - optional -else
	 *  boundaryId      - mandatory -if:boundary present in the app config value
	 *                  - optional -else
	 *  moduleId        - optional
	 *  
	 *  financialYearId -optional   
	 *  budgetgroupId   -optional
	 *  fromDate        -optional
	 *  toDate          -optional 
	 *  Order By        - optional if passed then only Budgetary appropriation number and reference number is accepted,
	 *  							 if not passed then default order by is date.                 
	 * @return
	 */
	 
	@SuppressWarnings("unchecked")
	public List<BudgetUsage> getListBudgetUsage(Map<String, Object> queryParamMap) throws ValidationException{
		
		StringBuffer query = new StringBuffer();
		Map<String, String> grpByVls = new HashMap<String, String>();
		List<BudgetUsage> listBudgetUsage = null;
		query.append("select bu from BudgetUsage bu,BudgetDetail bd where  bu.budgetDetail.id=bd.id");
		List<AppConfigValues> list =genericDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey(EGF,BUDGETARY_CHECK_GROUPBY_VALUES);
		if(list.isEmpty())
		{
			throw new ValidationException(EMPTY_STRING,"budgetaryCheck_groupby_values is not defined in AppConfig");
		}
		final AppConfigValues appConfigValues = list.get(0);
		if(appConfigValues.getValue().indexOf(",")!=1){ // if there are more than one comma separated values for key = budgetaryCheck_groupby_values
			String[] values = StringUtils.split(appConfigValues.getValue(),",");
			for (String value : values) {
				grpByVls.put(value, value);
			}
		}else{
			grpByVls.put(appConfigValues.getValue(), appConfigValues.getValue());
		}
	
		if(!isNull(grpByVls.get("fund") ))
		{
			if( isNull(queryParamMap.get("fundId"))){
				throw new ValidationException(EMPTY_STRING,"Fund is required");
			}else{
				query.append(" and bd.fund.id=").append(Integer.valueOf(queryParamMap.get("fundId").toString()));
			}
			
		}
		if(!isNull(grpByVls.get("department")))
		{
			if( isNull(queryParamMap.get("ExecutionDepartmentId"))){
				throw new ValidationException(EMPTY_STRING,"Department is required");
			}else{
				query.append(" and bd.executingDepartment.id=").append(Integer.valueOf(queryParamMap.get("ExecutionDepartmentId").toString()));
			}
			
		}
		if(!isNull(grpByVls.get("function") ))
		{
			if( isNull(queryParamMap.get("functionId"))){
				throw new ValidationException(EMPTY_STRING,"Function is required");
			}else{
				query.append(" and bd.function.id=").append(Long.valueOf(queryParamMap.get("functionId").toString()));
			}
			
		}
		if(!isNull(grpByVls.get("scheme") ))
		{
			if( isNull(queryParamMap.get("schemeId"))){
				throw new ValidationException(EMPTY_STRING,"Scheme is required");
			}else{
				query.append(" and bd.scheme.id=").append(Integer.valueOf(queryParamMap.get("schemeId").toString()));
			}
			
		}
		if(!isNull(grpByVls.get("subscheme") ))
		{
			if( isNull(queryParamMap.get("subschemeId"))){
				throw new ValidationException(EMPTY_STRING,"SubScheme is required");
			}else{
				query.append(" and bd.subScheme.id=").append(Integer.valueOf(queryParamMap.get("subschemeId").toString()));
			}
			
		}
		if(!isNull(grpByVls.get("functionary")))
		{
			if( isNull(queryParamMap.get("functionaryId"))){
				throw new ValidationException(EMPTY_STRING,"Functionary is required");
			}else{
				query.append(" and bd.functionary.id=").append(Integer.valueOf(queryParamMap.get("functionaryId").toString()));
			}
			
		}
		if(! isNull(grpByVls.get("boundary")) )
		{
			if( isNull(queryParamMap.get("boundaryId"))){
				throw new ValidationException(EMPTY_STRING,"Boundary is required");
			}else{
				query.append(" and bd.boundary.id=").append(Integer.valueOf(queryParamMap.get("boundaryId").toString()));
			}
			
		}		
			if(!isNull(queryParamMap.get("moduleId")) )
			{
				query.append(" and bu.moduleId=").append(Integer.valueOf(queryParamMap.get("moduleId").toString()));
			}
			if(!isNull(queryParamMap.get("financialYearId"))){
				
				query.append(" and bu.financialYearId=").append(Integer.valueOf(queryParamMap.get("financialYearId").toString()));
			}
			if(!isNull(queryParamMap.get("budgetgroupId"))){
				
				query.append(" and bd.budgetGroup.id=").append(Long.valueOf(queryParamMap.get("budgetgroupId").toString()));
			}
			if(!isNull(queryParamMap.get("fromDate"))){
				query.append(" and bu.updatedTime >=:from");
			}
			if(!isNull(queryParamMap.get("toDate"))){
				
				query.append(" and bu.updatedTime <=:to");
			}
			if(!isNull(queryParamMap.get("Order By"))){
				if(queryParamMap.get("Order By").toString().indexOf("appropriationnumber") == -1 && 
						queryParamMap.get("Order By").toString().indexOf("referenceNumber") == -1){
					throw new ValidationException(EMPTY_STRING,"order by value can be only Budgetary appropriation number or Reference number or both");
				}else{
					query.append(" Order By ").append(queryParamMap.get("Order By"));
				}
			}else{
				query.append(" Order By bu.updatedTime");
			}
			
			
			if(LOGGER.isDebugEnabled())     LOGGER.debug("Budget Usage Query >>>>>>>> "+ query.toString() );
			Query query1 =HibernateUtil.getCurrentSession().createQuery(query.toString());
			if(!isNull(queryParamMap.get("fromDate"))){
				query1.setTimestamp("from",(Date)queryParamMap.get("fromDate"));
			}
			if(!isNull(queryParamMap.get("toDate"))){
				Date date =(Date) queryParamMap.get("toDate");
				date.setMinutes(59);
				date.setHours(23);
				date.setSeconds(59);
				query1.setTimestamp("to",date);
			}
			
			listBudgetUsage = (List<BudgetUsage>) query1.list();
		return listBudgetUsage;
		
	}
	/**
	 * returns sum(approved BE/RE amount + appropriated (addition-deduction) amount)* mutliplicationFactor
	 * if budget and reappropriation both doesnot exist it will return zero
	 * @param fundId
	 * @param deptId 
	 * @param asOnDate
	 * @return
	 */
	public BigDecimal getPlannigBudgetBy(Integer fundId,Integer deptId,Date asOnDate)
	{
		//0.Validated
		if(fundId==null ||fundId==-1 )
			throw new IllegalArgumentException("fundId");
		if(deptId==null ||deptId==-1 )
			throw new IllegalArgumentException("deptId");
		if(asOnDate==null)
			asOnDate=new Date();

		BigDecimal amount=BigDecimal.ZERO;
		String isbere="";
		//1. get the FinancialYear for the date
		CFinancialYear finYear = financialYearDAO.getFinYearByDate(asOnDate);
		//2. check does approved RE Existis and set budgeting type
		boolean hasApprovedReForYear = budgetService.hasApprovedReAsonDate(finYear.getId(),asOnDate);
		isbere = hasApprovedReForYear?"RE":"BE";
		//3. get Budget approvedAmount
		amount = (BigDecimal) persistenceService.find("select sum(approvedAmount) from BudgetDetail bd where  bd.executingDepartment.id=? and bd.fund.id=?" +
				" and bd.budget.financialYear=? and bd.budget.isbere=? ",deptId,fundId,finYear,isbere);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Approved "+isbere+" Amount"+amount);
		amount = amount == null ? BigDecimal.ZERO : amount;
		//4. Reappropriated amounts   
		BigDecimal reappAmount =(BigDecimal) persistenceService.find("select sum(additionAmount-deductionAmount) from BudgetReAppropriation br where  br.budgetDetail.executingDepartment.id=? and br.budgetDetail.fund.id=?" +
				" and br.budgetDetail.budget.financialYear=? and br.budgetDetail.budget.isbere=? and br.status.description='Approved' and to_date(br.modifiedDate)<=? ",deptId,fundId,finYear,isbere,asOnDate);
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Approved Reappropriation Amount"+reappAmount);
		reappAmount = reappAmount == null ? BigDecimal.ZERO : reappAmount;
		//5. Multiply with mutiplication factor
		BigDecimal multiplicationFactor = new BigDecimal(Double.parseDouble(getAppConfigFor("EGF","planning_budget_multiplication_factor")));
		amount=amount.add(reappAmount);
		amount=amount.multiply(multiplicationFactor);
		return amount;
	}

	private boolean isNull(Object ob)
	{
		if(ob == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ScriptService getScriptExecutionService() {
		return scriptExecutionService;
	}
	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}
	public SequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}
	public void setSequenceGenerator(SequenceGenerator sequenceGenerator) {
		this.sequenceGenerator = sequenceGenerator;
	}

	public BudgetDetailsHibernateDAO(Class persistentClass, Session session){
		super(persistentClass, session);
	}
	
	public BudgetDetailsHibernateDAO(){
		super(null,null);
	}
	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	public void setBudgetService(BudgetService budgetService) {
		this.budgetService = budgetService;
	}
		
	
}
