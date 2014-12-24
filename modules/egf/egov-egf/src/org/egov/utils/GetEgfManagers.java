/*
 * Created on 28-Dec-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.utils;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.BillsAccountingService;
import org.egov.billsaccounting.services.SalaryBillService;
import org.egov.billsaccounting.services.WorksBillService;
import org.egov.infstr.beanfactory.ApplicationContextBeanProvider;
import org.egov.masters.services.MastersService;


/**
 * @author Sapna
 * @version 1.0
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GetEgfManagers {
	private static final Logger LOGGER = Logger.getLogger(GetEgfManagers.class);
	private static WorksBillService worksBillService = null;
	private static SalaryBillService salaryBillService = null;
	private static MastersService mastersService = null;
	
	private static BillsAccountingService billsAccountingService = null;
	private static ApplicationContextBeanProvider beanProvider = new ApplicationContextBeanProvider();
	
	/**Gets the WorksBillManager*/
    public static WorksBillService getWorksBillService() 
    {
    	worksBillService = (WorksBillService) beanProvider.getBean("worksBillService", "classpath*:org/serviceconfig-Bean.xml");
        return worksBillService;
    }
	
    /**Gets the SalaryBillManager*/
    public static SalaryBillService getSalaryBillService() 
    {
    	salaryBillService = (SalaryBillService) beanProvider.getBean("salaryBillService", "classpath*:org/serviceconfig-Bean.xml");
        return salaryBillService;
    }
    
    /**Gets the MastersManager*/
    public static MastersService getMastersService()
    {
    	mastersService = (MastersService) beanProvider.getBean("mastersService", "classpath*:org/serviceconfig-Bean.xml");
        return mastersService;
    }

    
    public static BillsAccountingService getBillsAccountingService()
    {
    	billsAccountingService = (BillsAccountingService) beanProvider.getBean("billsAccountingService", "classpath*:org/serviceconfig-Bean.xml");
    	return billsAccountingService;
    }
}
