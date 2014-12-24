package org.egov.billsaccounting.services;

import java.util.List;



import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CVoucherHeader;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.voucher.PreApprovedVoucher;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Manikanta
 * 
 */


public class BillsAccountingServiceImpl implements BillsAccountingService{
	private final static Logger LOGGER=Logger.getLogger(BillsAccountingServiceImpl.class); 
	private static final String MISSINGMSG= "is not defined in AppConfig values cannot proceed creating voucher";
	
	
	/**
	 * API to create voucher in pre approved status
	 * @param billId
	 * @return
	 */
	public long createPreApprovedVoucherFromBill(int billId)throws EGOVRuntimeException,ValidationException
	{
		String voucherStatus=null;
		long vh=-1;
		try {
			GenericHibernateDaoFactory genericHibDao=new GenericHibernateDaoFactory();
			LOGGER.debug("genericHibDao not null"+genericHibDao);
			List vStatusList=genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");	
			
			if(!vStatusList.isEmpty()&&vStatusList.size()==1)
			{	AppConfigValues appVal=(AppConfigValues)vStatusList.get(0);
			voucherStatus=(String)appVal.getValue();
			}
			else
			{
				throw new EGOVRuntimeException("PREAPPROVEDVOUCHERSTATUS"+MISSINGMSG);
			}
			CreateVoucher cv= new CreateVoucher();
			vh=cv.createVoucherFromBill(billId,voucherStatus);
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
	public long createPreApprovedVoucherFromBillForPJV(int billId,List<PreApprovedVoucher> voucherdetailList,List<PreApprovedVoucher> subLedgerList)throws EGOVRuntimeException
	{
		String voucherStatus=null;
		long vh=-1;
		try {
			GenericHibernateDaoFactory genericHibDao=new GenericHibernateDaoFactory();
			LOGGER.debug("genericHibDao not null"+genericHibDao);
			List vStatusList=genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "PREAPPROVEDVOUCHERSTATUS");	
			
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
	public void createVoucherfromPreApprovedVoucher(long vouhcerheaderid)throws EGOVRuntimeException
	{
		String voucherStatus=null;
		
			try {
				GenericHibernateDaoFactory	genericHibDao=new GenericHibernateDaoFactory();
				List vStatusList=	genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "APPROVEDVOUCHERSTATUS");	
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
	public long createVoucherFromBill(int billId)throws EGOVRuntimeException
	{
		try {
			GenericHibernateDaoFactory	genericHibDao=new GenericHibernateDaoFactory();
			String voucherStatus=null;
			List vStatusList=	genericHibDao.getAppConfigValuesDAO().getConfigValuesByModuleAndKey("EGF", "DEFAULTVOUCHERCREATIONSTATUS");	
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
			long vh=	cv.createVoucherFromBill(billId,voucherStatus);
			return vh;
		} catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage());
		}
		
	}
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
	public CVoucherHeader getPJVNumberForBill(String billNumber) throws EGOVException
	{
		try
		{
			Session session =HibernateUtil.getCurrentSession();
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
