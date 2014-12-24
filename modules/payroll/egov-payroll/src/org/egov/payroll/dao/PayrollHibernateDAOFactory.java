package org.egov.payroll.dao;


import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.ExceptionMstr;
import org.egov.payroll.model.IncrementDetails;
import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.PayScaleDetails;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.model.providentfund.PFDetails;
import org.egov.payroll.model.providentfund.PFHeader;
import org.hibernate.Session;


/**
 * Returns Hibernate-specific instances of DAOs.
 * <p>
 * One of the responsiblities of the factory is to inject a Hibernate Session
 * into the DAOs. You can customize the getCurrentSession() method if you
 * are not using the default strategy, which simply delegates to
 * <tt>HibernateUtil.getCurrentSession()</tt>, and also starts a transaction
 * lazily, if none exists already for the current thread or current EJB.
 * <p>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * an inner class to implement the interface in a generic way. This allows clean
 * refactoring later on, should the interface implement business data access
 * methods at some later time. Then, we would externalize the implementation into
 * its own first-level class. We can't use anonymous inner classes for this trick
 * because they can't extend or implement an interface and they can't include
 * constructors.
 *
 * @author christian.bauer@jboss.com
 */
public class PayrollHibernateDAOFactory extends PayrollDAOFactory
{
    protected Session getCurrentSession()
    {
        HibernateUtil.beginTransaction();
        return HibernateUtil.getCurrentSession();
    }

    public  SalaryCodesDAO getSalaryCodesDAO()
    {
		return new SalaryCodesHibernateDAO(SalaryCodes.class, HibernateUtil.getCurrentSession());
	}
    
    public  BulkRuleMstrDAO getPayGenRuleUpdation()
    {
		return new BulkRuleMstrHibernateDAO(PayGenUpdationRule.class, HibernateUtil.getCurrentSession());
	}
    
 
    public AdvanceDAO getSalAdvancesDAO()
	{
    	return new AdvanceHibernateDAO(Advance.class, getCurrentSession());
	}

    public ExceptionDAO getExceptionDAO()
	{
	    	return new ExceptionHibernateDAO(EmpException.class, getCurrentSession());
	}

	public EarningsDAO getEarningsDAO()
	{
	    	return new EarningsHibernateDAO(Earnings.class, getCurrentSession());
	}
	public SalaryCategoryMasterDAO getSalaryCategoryMasterDAO()
	{
	    	return new SalaryCategoryMasterHibernateDAO(SalaryCategoryMaster.class, getCurrentSession());
	}
	public  DeductionsDAO getDeductionsDAO()
	{
			return new DeductionsHibernateDAO(Deductions.class, getCurrentSession());
	}
	public EmpPayrollDAO getEmpPayrollDAO()
	{
	    	return new EmpPayrollHibernateDAO(EmpPayroll.class, getCurrentSession());
	}
	
	public ExceptionMstrDAO getExceptionMstrDAO()
	{
	    	return new ExceptionMstrHibernateDAO(ExceptionMstr.class, getCurrentSession());
	}
	public PayScaleDetailsDAO getPayScaleDetailsDAO()
	{
	    return new PayScaleDetailsHibernateDAO(PayScaleDetails.class, getCurrentSession());
	}
	public PayScaleHeaderDAO getPayScaleHeaderDAO()
	{
	    return new PayScaleHeaderHibernateDAO(PayScaleHeader.class, getCurrentSession());
	}
	public PayStructureDAO getPayStructureDAO()
	{
	    return new PayStructureHibernateDAO(PayStructure.class, getCurrentSession());
	}

	public BatchFailureDetailsDAO getBatchFailureDetailsDAO() {
		return new BatchFailureDetailsHibernateDAO(BatchFailureDetails.class, getCurrentSession());
	}
	public IncrementDetailsDAO getIncrementDetailsDAO() {
		return new IncrementDetailsHibernateDAO(IncrementDetails.class, getCurrentSession());
	}

	public BatchGenDetailsDAO getBatchGenDetailsDAO() {
		return new BatchGenDetailsHibernateDAO(BatchGenDetails.class, getCurrentSession());
	}

	public PayTypeMasterDAO getPayTypeMasterDAO() {
		return new PayTypeMasterHibernateDAO(PayTypeMaster.class, getCurrentSession());
	}
	
	public PayheadRuleDAO getPayheadRuleDAO(){
		return new PayheadRuleHibernateDAO(PayheadRule.class, HibernateUtil.getCurrentSession());
	}
	
	public PFHibernateDAO getPFHeaderDAO(){
		return new PFHibernateDAO(PFHeader.class, HibernateUtil.getCurrentSession());
	}
	
	public PFHibernateDAO getPFDetailsDAO(){
		return new PFHibernateDAO(PFDetails.class, HibernateUtil.getCurrentSession());
	}
}
