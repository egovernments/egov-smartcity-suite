/*
 * Created on 22-Dec-2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;

import org.egov.billsaccounting.model.OtherBillDetail;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;
import org.egov.model.bills.EgBillregister;

/**
 * @author Sapna
 * @version 1.0
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *///This fix is for Phoenix Migration.
public interface CbillService 
{
	
	/**
	 * 
	 * @param billReg
	 * @return
	 */
	public boolean addBillRegister(EgBillregister billReg);
	/**
	 * This method creates an status for the application
	 * @param application
	 * @return
	 */

	
	/**
    * @param fundId
    * @param fundSource
    * @param vhFromDate
    * @param vhToDate
    * @param fromBillDate
    * @param toBillDate
    * @param billVhNoFrom
    * @param billVhNoTo
    * @return list of OtherBillDetail(Contingent Bills) filtered by optional conditions
    */
	public List<OtherBillDetail> getCBillFilterBy(Integer fundId, Fundsource fundSource, Date vhFromDate, Date vhToDate, Date fromBillDate, Date toBillDate, String billVhNoFrom, String billVhNoTo,Department dept,String functionary);
	
	/** 
	 * @param id
	 * @return OtherBillDetail
	 */
	public OtherBillDetail getOtherBillDetailById(Integer id);
	
	/** 
	 * @param pymntVoucherheader
	 * @return List of OtherBillDetail
	 */
	public List<OtherBillDetail> getOtherBillDetailByPymntVoucherheader(CVoucherHeader pymntVoucherheader);
	/**
	 * 
	 * @param otherBillDetail  void
	 */
	public void createOtherBillDetail(OtherBillDetail otherBillDetail);

}
