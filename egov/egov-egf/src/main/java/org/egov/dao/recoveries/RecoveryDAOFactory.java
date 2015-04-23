package org.egov.dao.recoveries;

public abstract class RecoveryDAOFactory {
	private static final RecoveryDAOFactory HIBERNATE = new RecoveryHibernateDAOFactory();	
	
	public static RecoveryDAOFactory getDAOFactory()
	{
		return HIBERNATE;
	}
	
	public abstract TdsHibernateDAO getTdsDAO();
	public abstract EgDeductionDetailsHibernateDAO getEgDeductionDetailsDAO();
}
