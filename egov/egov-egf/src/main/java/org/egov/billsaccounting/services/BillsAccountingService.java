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
package org.egov.billsaccounting.services;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CVoucherHeader;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infstr.ValidationException;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.voucher.PreApprovedVoucher;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author Manikanta
 * 
 */

@Transactional(readOnly=true)
public class BillsAccountingService  {
	
	private final static Logger LOGGER=Logger.getLogger(BillsAccountingService.class);
	 
	private static final String MISSINGMSG= "is not defined in AppConfig values cannot proceed creating voucher";
	
	@Autowired
        private AppConfigValuesDAO appConfigValuesDAO;  
	
	/**
	 * API to create voucher in pre approved status
	 * @param billId
	 * @return
	 */
	@Transactional
	public long createPreApprovedVoucherFromBill(int billId, String voucherNumber, Date voucherDate)  throws EGOVRuntimeException,ValidationException
	{
		String voucherStatus=null;
		long vh=-1;
		try {
			List vStatusList=appConfigValuesDAO.getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");	
			
			if(!vStatusList.isEmpty()&&vStatusList.size()==1)
			{	AppConfigValues appVal=(AppConfigValues)vStatusList.get(0);
			voucherStatus=(String)appVal.getValue();
			}
			else
			{
				throw new EGOVRuntimeException("PREAPPROVEDVOUCHERSTATUS"+MISSINGMSG);
			}
			CreateVoucher cv= new CreateVoucher();
			vh=cv.createVoucherFromBill(billId,voucherStatus, voucherNumber, voucherDate);
		} 
		 catch (ValidationException e) {
			 LOGGER.error(e.getErrors());
			throw new ValidationException(e.getErrors());
			} 
		catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
			throw new  EGOVRuntimeException(e.getMessage());
			
		}
		return vh;
		
	}
	
	/**
	 * API to create voucher in pre approved status
	 * @param billId
	 * @return
	 */
	@Transactional
	public long createPreApprovedVoucherFromBillForPJV(int billId,List<PreApprovedVoucher> voucherdetailList,List<PreApprovedVoucher> subLedgerList)throws EGOVRuntimeException
	{
		String voucherStatus=null;
		long vh=-1;
		try {
			List vStatusList=appConfigValuesDAO.getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");	
			
			if(!vStatusList.isEmpty()&&vStatusList.size()==1)
			{	AppConfigValues appVal=(AppConfigValues)vStatusList.get(0);
			voucherStatus=(String)appVal.getValue();
			}
			else
			{
				throw new EGOVRuntimeException("PREAPPROVEDVOUCHERSTATUS"+MISSINGMSG);
			}
			CreateVoucher cv= new CreateVoucher();
			vh=cv.createVoucherFromBillForPJV(billId,voucherStatus,voucherdetailList,subLedgerList);
		} catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
			throw new  EGOVRuntimeException(e.getMessage());
			
		}
		return vh;
		
	}
	/**
	 * API to Change the status of preapproved voucher
	 * @param vouhcerheaderid
	 * @return
	 */
	@Transactional
	public void createVoucherfromPreApprovedVoucher(long vouhcerheaderid)throws EGOVRuntimeException
	{
		String voucherStatus=null;
		
			try {
				List vStatusList=	appConfigValuesDAO.getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");	
				if(!vStatusList.isEmpty()&&vStatusList.size()==1)
				{	
					AppConfigValues appVal=(AppConfigValues)vStatusList.get(0);
					voucherStatus=(String)appVal.getValue();
				}
				else
				{
					throw new EGOVRuntimeException("APPROVEDVOUCHERSTATUS"+MISSINGMSG);
				}
				
				CreateVoucher cv= new CreateVoucher();
				cv.createVoucherFromPreApprovedVoucher(vouhcerheaderid,voucherStatus);
				
			} catch (EGOVRuntimeException e) {
			
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		
		
	}
	/**
	 * Api to create voucher from bill with normal flow
	 * @param billId
	 * @return
	 */
	@Transactional
	public long createVoucherFromBill(int billId)throws EGOVRuntimeException
	{
		try {
			String voucherStatus=null;
			List vStatusList=	appConfigValuesDAO.getConfigValuesByModuleAndKey("EGF", "DEFAULTVOUCHERCREATIONSTATUS");	
			if(!vStatusList.isEmpty()&&vStatusList.size()==1)
			{	
				AppConfigValues appVal=(AppConfigValues)vStatusList.get(0);
				voucherStatus=(String)appVal.getValue();
			}
			else
			{
				throw new EGOVRuntimeException("DEFAULTVOUCHERCREATIONSTATUS"+MISSINGMSG);
			}
			CreateVoucher cv= new CreateVoucher();
			long vh=	cv.createVoucherFromBill(billId,voucherStatus, null, null);
			return vh;
		} catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		
	}
	@Transactional
	public void updatePJV(CVoucherHeader vh, List<PreApprovedVoucher> detailList,List<PreApprovedVoucher> subledgerlist)  throws EGOVRuntimeException
	{
		CreateVoucher cv= new CreateVoucher();
		cv.updatePJV(vh, detailList, subledgerlist);
	}
	
	/**
	 * To get the PJV number for the bill number
	 * @param billNumber
	 * @return
	 */
	@Transactional
	public CVoucherHeader getPJVNumberForBill(String billNumber) throws EGOVException
	{
		try
		{
			Session session  = HibernateUtil.getCurrentSession();
			Query query = session.createQuery("select br.egBillregistermis.voucherHeader from EgBillregister br where br.billnumber=:billNumber");
			query.setString("billNumber", billNumber);
			if(null==query.uniqueResult())
				throw new EGOVException("PJV is not created for this bill number ["+billNumber+"]");
			
			return (CVoucherHeader) query.uniqueResult();
		}catch(Exception e)
		{
			throw new EGOVException(e.getMessage());
		}
	}
}
