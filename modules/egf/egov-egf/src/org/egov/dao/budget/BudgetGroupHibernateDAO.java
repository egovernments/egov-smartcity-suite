/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.budget.BudgetGroup;
import org.egov.utils.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetGroupHibernateDAO extends GenericHibernateDAO implements BudgetGroupDAO
{

	private static final Logger LOGGER = Logger.getLogger(BudgetGroupHibernateDAO.class);
	private Session session;
	private static final String EMPTY_STRING="";
	private CommonsService commonsService;
	public BudgetGroupHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	
	}
	public BudgetGroupHibernateDAO(){ 
		super(null,null);
	}
	public BudgetGroup createBudgetGroup(BudgetGroup cbg) throws ValidationException
	{
        BudgetGroup budgetGroup = null;
        try {
       		session = HibernateUtil.getCurrentSession();
          	session.saveOrUpdate(cbg);        	
        	budgetGroup = (BudgetGroup)session.get(BudgetGroup.class,cbg.getId());
        	LOGGER.info("budgetGroup saved id"+budgetGroup.getName());  
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
			
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MMM-yyyy",Constants.LOCALE);
			
					CFinancialYear financialyearObj=commonsService.getFinancialYearByDate(date);
					String fiancialyearid = financialyearObj.getId().toString();
			
			if(fiancialyearid==null || fiancialyearid.equals(""))
				throw new ValidationException(EMPTY_STRING,"Date is not defined in the Financial year master");
			
			CFinancialYear financialyear = commonsService.findFinancialYearById(Long.valueOf(fiancialyearid));
			
			qryStr.append("from BudgetGroup bg where  bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.budget.financialYear=:financialYearId   ");
			
			if(functionCode!=null && !functionCode.equals(""))
			{
				function =commonsService.getFunctionByCode(functionCode);
				if(function==null || function.getId()==null)
					throw new ValidationException(EMPTY_STRING,"Function Code is not defined in the system");
				
				qryStr.append(" AND bd.function=:functionId ");
			}
			qryStr.append(" ) ");
			
			session=HibernateUtil.getCurrentSession();
			Query qry = session.createQuery(qryStr.toString()) ;
			qry.setLong("financialYearId", Long.valueOf(fiancialyearid));
				
			if(functionCode!=null && !functionCode.equals(""))
			{
				qry.setLong("functionId", function.getId());
			}
			budgetHeadList = (ArrayList) qry.list();
			
			if(budgetHeadList.isEmpty() || budgetHeadList.size()==0)
				throw new ValidationException(EMPTY_STRING,"No budget defined for the year "+financialyear.getFinYearRange());
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
		session=HibernateUtil.getCurrentSession();
		Query qry = session.createQuery("from BudgetGroup bg where bg.id =:id");
		qry.setLong("id",id);  
		return (BudgetGroup)qry.uniqueResult();
	}
	
	public List<BudgetGroup> getBudgetGroupList()throws ValidationException
	{
		List<BudgetGroup> budgetHeadList =null;
		try
		{
			session=HibernateUtil.getCurrentSession();
			Query qry = session.createQuery(" from BudgetGroup where isActive=1") ;
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
				function =commonsService.getFunctionByCode(functionCode);
				if(function==null || function.getId()==null)
					throw new ValidationException(EMPTY_STRING,"Function Code is not defined in the system");
				
				qryStr.append("from BudgetGroup bg where  bg in ( select bd.budgetGroup from BudgetDetail bd  where bd.function=:functionId ) ");
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
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
}
