package org.egov.payroll.dao;



/**
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * Either use the <tt>DEFAULT</tt> to get the same concrete RNDDAOFactory
 * throughout your application, or a concrete factory by name, e.g.
 * <tt>RNDDAOFactory.HIBERNATE</tt> is a concrete <tt>RNDHibernateDAOFactory</tt>.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it.
 * If you add a new persistence mechanism, add an additional concrete factory
 * for it to the enumeration of factories.
 * <p>
 * It probably wouldn't be a bad idea to move the <tt>DEFAULT</tt> setting
 * into external configuration.
 *
 * @author christian.bauer@jboss.com
 */
public abstract class PayrollDAOFactory {

    private static final PayrollDAOFactory HIBERNATE = new PayrollHibernateDAOFactory ();


    public static PayrollDAOFactory getDAOFactory()
    {
    	return HIBERNATE;
    }
    // Add your DAO interfaces here
    public abstract SalaryCategoryMasterDAO getSalaryCategoryMasterDAO();	
    public abstract SalaryCodesDAO getSalaryCodesDAO();	
	public abstract AdvanceDAO getSalAdvancesDAO();
	public abstract ExceptionDAO getExceptionDAO();
	public abstract EarningsDAO getEarningsDAO();
	public abstract DeductionsDAO getDeductionsDAO();
	public abstract EmpPayrollDAO getEmpPayrollDAO();
	public abstract ExceptionMstrDAO getExceptionMstrDAO();	
	public abstract PayScaleHeaderDAO getPayScaleHeaderDAO();
	public abstract PayScaleDetailsDAO getPayScaleDetailsDAO();
	public abstract PayStructureDAO getPayStructureDAO();
	public abstract BatchFailureDetailsDAO getBatchFailureDetailsDAO();
	public abstract IncrementDetailsDAO getIncrementDetailsDAO();
	public abstract BatchGenDetailsDAO getBatchGenDetailsDAO();
	public abstract PayTypeMasterDAO getPayTypeMasterDAO();
	
	public abstract BulkRuleMstrDAO getPayGenRuleUpdation();
	public abstract PayheadRuleDAO getPayheadRuleDAO();
	public abstract PFDAO getPFHeaderDAO();
	public abstract PFDAO getPFDetailsDAO();
	
}
