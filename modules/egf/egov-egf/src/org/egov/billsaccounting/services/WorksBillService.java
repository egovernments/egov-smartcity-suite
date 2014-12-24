/**
 * 
 */
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;


import org.egov.billsaccounting.model.Contractorbilldetail;
import org.egov.billsaccounting.model.EgwWorksDeductions;
import org.egov.billsaccounting.model.EgwWorksMis;
import org.egov.billsaccounting.model.Supplierbilldetail;
import org.egov.billsaccounting.model.Worksdetail;
import org.egov.commons.Fundsource;
import org.egov.commons.Relation;
import org.egov.lib.rjbac.dept.DepartmentImpl;
  
/**
 * @author Manikanta 
 *
 */
public interface WorksBillService
{
	public List<EgwWorksDeductions> getStatutoryDeductionsByWorksdetail(Worksdetail worksdetail);
	
	/**
    * @param fundId
    * @param fundSource
    * @param vhFromDate
    * @param vhToDate
    * @param relation
    * @param vhNoFrom,DepartmentImpl dept,String functionary
    * @param vhNoTo
    * @return list of Contractorbilldetail filtered by optional conditions
    */
	public List<Contractorbilldetail> getContractorBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,DepartmentImpl dept,String functionary);
	
	/**
    * @param fundId
    * @param fundSource
    * @param vhFromDate
    * @param vhToDate
    * @param relation
    * @param vhNoFrom
    * @param vhNoTo
    * @return list of Supplierbilldetail filtered by optional conditions
    */
	public List<Supplierbilldetail> getSupplierBillDetailFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Relation relation, String vhNoFrom, String vhNoTo,DepartmentImpl dept,String functionary);
	
	/** 
	 * @param id
	 * @return Contractorbilldetail
	 */
	public Contractorbilldetail getContractorbilldetailById(Integer id);
	
	/** 
	 * @param id
	 * @return Supplierbilldetail
	 */
	public Supplierbilldetail getSupplierbilldetailById(Integer id);
	
	/** 
	 * @param worksdetailId
	 * @return Worksdetail
	 */
	public Worksdetail getWorksDetailById(Integer worksdetailId);
	
	/** 
	 * updates Worksdetail object
	 * @param worksdetail 
	 */
	public void updateWorksdetail(Worksdetail worksdetail);
	
	public List getTdsForWorkOrder(Worksdetail worksdetailId);
	 public List getTotalBillValue(Worksdetail wd);
	 public List getTotalAdvAdj(Worksdetail wd);
	 public EgwWorksMis findByWorkOrderId(String workOrderId);
	 public void  createSupplierbilldetail(Supplierbilldetail sbd);
	 public void createContractorbilldetail( Contractorbilldetail cbd);
	 

	//public WorksBillForm getWorksBillById(int billId);
}


