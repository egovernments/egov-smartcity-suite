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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.BudgetGroup;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly=true)
public class BudgetGroupHibernateDAO extends GenericHibernateDAO implements BudgetGroupDAO
{

	private static final Logger LOGGER = Logger.getLogger(BudgetGroupHibernateDAO.class);
	private Session session;
	private static final String EMPTY_STRING="";
	
	@Autowired
	CommonsService cmnService;
	public BudgetGroupHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	
	}
	public BudgetGroupHibernateDAO(){ 
		super(null,null);
	}
	@Transactional
	public BudgetGroup createBudgetGroup(BudgetGroup cbg) throws ValidationException
	{
        BudgetGroup budgetGroup = null;
        try {
       		session =HibernateUtil.getCurrentSession();
          	session.saveOrUpdate(cbg);        	
        	budgetGroup = (BudgetGroup)session.get(BudgetGroup.class,cbg.getId());
        	if(LOGGER.isInfoEnabled())     LOGGER.info("budgetGroup saved id"+budgetGroup.getName());  
        }catch(ValidationException v)
		{
			LOGGER.error("Error in createBudgetGroup==="+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
        catch (Exception e)
        {
         	LOGGER.error(e.getCause()+" Error in createBudgetGroup");
           	throw new ValidationException(EMPTY_STRING,"egovexception in creation of budgetGroup"+e);
        }
        
        return budgetGroup;
	}
	
	public List<BudgetGroup> getBudgetHeadByDateAndFunction(String functionCode,java.util.Date date) throws ValidationException
	{
		List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
		try
		{
			CFunction function=null;
			StringBuffer qryStr = new StringBuffer();
			CFinancialYear fiancialyear = cmnService.getFinancialYearByDate(date);
			
			if(fiancialyear==null)
				throw new ValidationException(EMPTY_STRING,"Date is not defined in the Financial year master");
			
			
			qryStr.append("from BudgetGroup bg where  bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.budget.financialYear=:financialYearId   ");
			
			if(functionCode!=null && !functionCode.equals(""))
			{
				function =cmnService.getFunctionByCode(functionCode);
				if(function==null || function.getId()==null)
					throw new ValidationException(EMPTY_STRING,"Function Code is not defined in the system");
				
				qryStr.append(" AND bd.function=:functionId ");
			}
			qryStr.append(" ) ");
			
			session = HibernateUtil.getCurrentSession();
			Query qry = session.createQuery(qryStr.toString()) ;
			qry.setLong("financialYearId", Long.valueOf(fiancialyear.getId()));
				
			if(functionCode!=null && !functionCode.equals(""))
			{
				qry.setLong("functionId", function.getId());
			}
			budgetHeadList = (ArrayList) qry.list();
			
			if(budgetHeadList.isEmpty() || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"No budget defined for the year "+fiancialyear.getFinYearRange());
		}catch(ValidationException v)
		{
			LOGGER.error("Exception in getBudgetHeadByDateAndFunction API()"+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetHeadByDateAndFunction API()======="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return budgetHeadList;
	}
	
	public BudgetGroup getBudgetHeadById(Long id)
	{
		session = HibernateUtil.getCurrentSession();
		Query qry = session.createQuery("from BudgetGroup bg where bg.id =:id");
		qry.setLong("id",id);  
		return (BudgetGroup)qry.uniqueResult();
	}
	
	public List<BudgetGroup> getBudgetGroupList()throws ValidationException
	{
		List<BudgetGroup> budgetHeadList =null;
		try
		{
			session = HibernateUtil.getCurrentSession();
			Query qry = session.createQuery(" from BudgetGroup where isActive=1 order by name") ;
			budgetHeadList = (ArrayList<BudgetGroup>) qry.list();
		}catch(ValidationException v)
		{
			LOGGER.error("Exception in getBudgetGroupList API()"+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetGroupList API()======="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return budgetHeadList;
	}
	/**
	 * Returns a list of BudgetGroup having entry in budget detail with the given function code. 
	 * @param function code
	 * @throws ValidationException
	 */
	public List<BudgetGroup> getBudgetHeadByFunction(String functionCode) throws ValidationException
	{

		List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
		try
		{
			CFunction function=null;
			StringBuffer qryStr = new StringBuffer();
			
			if(functionCode!=null && !functionCode.equals(""))
			{
				function =cmnService.getFunctionByCode(functionCode);
				if(function==null || function.getId()==null)
					throw new ValidationException(EMPTY_STRING,"Function Code is not defined in the system");
				
				qryStr.append("from BudgetGroup bg where  bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
			}
			session=HibernateUtil.getCurrentSession();
			Query qry = session.createQuery(qryStr.toString()) ;
				
			if(functionCode!=null && !functionCode.equals(""))
			{
				qry.setLong("functionId", function.getId());
			}
			budgetHeadList = (ArrayList) qry.list();
			
			if(budgetHeadList.isEmpty() || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"No budget heads mapped for the function code - "+functionCode);
		}catch(ValidationException v)
		{
			LOGGER.error("Exception in getBudgetHeadByFunction API()"+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetHeadByFunction API()======="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return budgetHeadList;
	
	}
	/**
	 * Returns a list of BudgetGroup having entry in budget detail with the given function code or given List of ChartOfAccounts or both. 
	 * @param function code
	 * @param chartOfAccountsList
	 * @throws ValidationException
	 */
	public List<BudgetGroup> getBudgetHeadByCOAandFunction(String functionCode,List<CChartOfAccounts> chartOfAccountsList) throws ValidationException
	{

		List<BudgetGroup> budgetHeadList = new ArrayList<BudgetGroup>();
		try
		{
			//If only function code is given. 
			if(functionCode!=null && !functionCode.equals("") && (chartOfAccountsList == null || chartOfAccountsList.size() == 0)){
				
				return getBudgetHeadByFunction(functionCode);
				
			//If only chartOfAccountsList is given.	
			}else if(chartOfAccountsList != null && chartOfAccountsList.size() > 0 && (functionCode == null || functionCode.equals(""))){
				
				List<Long> coaIds = new ArrayList<Long>(); 
				for(CChartOfAccounts coa :chartOfAccountsList){
					coaIds.add(coa.getId());
				}
				int size = coaIds.size();
				if(size>999)
				{
					int fromIndex = 0;
					int toIndex = 0; 
					int step = 1000;
					List<BudgetGroup> newbudgetHeadList ;
					while(size-step>=0)
					{
						newbudgetHeadList = new ArrayList<BudgetGroup>();
						toIndex += step;  
						 Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
						 budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
						 budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
						 newbudgetHeadList = budgetHeadsQuery.list();
						fromIndex = toIndex;
						size-=step;
						if(newbudgetHeadList!=null)
						{
							budgetHeadList.addAll(newbudgetHeadList);
						}
						

					}

					if(size>0)
					{
						newbudgetHeadList = new ArrayList<BudgetGroup>();
						fromIndex = toIndex;
						toIndex = fromIndex+size;
						Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
						 budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
						 budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
						newbudgetHeadList = budgetHeadsQuery.list();
						if(newbudgetHeadList!=null)
						{
							budgetHeadList.addAll(newbudgetHeadList);
						}
					}
					

				}else
				{
					Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 )");
					budgetHeadsQuery.setParameterList("IDS1",coaIds);
					budgetHeadsQuery.setParameterList("IDS2",coaIds);
					budgetHeadList = budgetHeadsQuery.list();
				}
				if(budgetHeadList.isEmpty() || budgetHeadList.size()==0)
					throw new ValidationException(EMPTY_STRING,"No budget heads mapped for the function code - "+functionCode);
				return budgetHeadList;
				
			//If function code and chartOfAccountsList is given.	
			}else if(chartOfAccountsList != null && chartOfAccountsList.size() > 0 && functionCode != null && !functionCode.equals("")){
				
				List<Long> coaIds = new ArrayList<Long>(); 
				CFunction function=null;
				
				if(functionCode!=null && !functionCode.equals(""))
				{
					function =cmnService.getFunctionByCode(functionCode);
					if(function==null || function.getId()==null)
						throw new ValidationException(EMPTY_STRING,"Function Code is not defined in the system");
					
				}
				
				for(CChartOfAccounts coa :chartOfAccountsList){
					coaIds.add(coa.getId());
				}
				int size = coaIds.size();
				if(size>999)
				{
					int fromIndex = 0;
					int toIndex = 0; 
					int step = 1000;
					List<BudgetGroup> newbudgetHeadList ;
					while(size-step>=0)
					{
						newbudgetHeadList = new ArrayList<BudgetGroup>();
						toIndex += step;  
						 Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
						 budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
						 budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
						 if(functionCode!=null && !functionCode.equals(""))
							{
							 budgetHeadsQuery.setLong("functionId", function.getId());
							}
						 newbudgetHeadList = budgetHeadsQuery.list();
						fromIndex = toIndex;
						size-=step;
						if(newbudgetHeadList!=null)
						{
							budgetHeadList.addAll(newbudgetHeadList);
						}
						

					}

					if(size>0)
					{
						newbudgetHeadList = new ArrayList<BudgetGroup>();
						fromIndex = toIndex;
						toIndex = fromIndex+size;
						Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name");
						budgetHeadsQuery.setParameterList("IDS1", coaIds.subList(fromIndex, toIndex));
						budgetHeadsQuery.setParameterList("IDS2", coaIds.subList(fromIndex, toIndex));
						if(functionCode!=null && !functionCode.equals(""))
						{
						 budgetHeadsQuery.setLong("functionId", function.getId());
						}
						newbudgetHeadList = budgetHeadsQuery.list();
						if(newbudgetHeadList!=null)
						{
							budgetHeadList.addAll(newbudgetHeadList);
						}
					}
					

				}else
				{
					Query budgetHeadsQuery = getCurrentSession().createQuery(" from BudgetGroup bg where  bg.maxCode.id in ( :IDS1 ) and bg.minCode.id in ( :IDS2 ) and bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) order by bg.name ");
					budgetHeadsQuery.setParameterList("IDS1",coaIds);
					budgetHeadsQuery.setParameterList("IDS2",coaIds);
					if(functionCode!=null && !functionCode.equals(""))
					{
					 budgetHeadsQuery.setLong("functionId", function.getId());
					}
					budgetHeadList = budgetHeadsQuery.list();
				}
				if(budgetHeadList.isEmpty() || budgetHeadList.size()==0)
					throw new ValidationException(EMPTY_STRING,"No budget heads mapped for the function code - "+functionCode);
				return budgetHeadList;
			//If both are not given.	
			}else{
				
				StringBuffer qryStr = new StringBuffer();
				qryStr.append("from BudgetGroup bg order by bg.name");
				session=HibernateUtil.getCurrentSession();
				Query qry = session.createQuery(qryStr.toString()) ;
					
				budgetHeadList = (ArrayList) qry.list();
			}
			
			
			
		}catch(ValidationException v)
		{
			LOGGER.error("Exception in getBudgetHeadByFunction API()"+v.getErrors());
			throw new ValidationException(v.getErrors());
		}
		catch(Exception e)
		{
			LOGGER.error("Exception in getBudgetHeadByFunction API()======="+e.getMessage());
			throw new ValidationException(EMPTY_STRING,e.getMessage());
		}
		return budgetHeadList;
	
	}


}
