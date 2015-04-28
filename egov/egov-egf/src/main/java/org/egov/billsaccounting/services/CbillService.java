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
