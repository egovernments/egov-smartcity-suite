/*
 * BankEntriesDelegate.java  Created on Sep 1, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.egov.utils.FinancialConstants;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BankEntriesDelegate {

	private static  final Logger LOGGER = Logger.getLogger(BankEntriesDelegate.class);
	EGovernCommon ecm = new EGovernCommon();
	

	/*
		create and pass payment voucher
	*/
	public String createPaymentVoucher(String chqNumber,String chqAmount,String narration,String voucherDate,String glCodeId,
			int accId,int userId,int fundId,int fundSourceId,String departmentId,String functionaryId,Connection con) throws TaskFailedException,Exception
	{
		/**
		 * implement using create voucher api
		 */
		
		
		return null;
	}
	/*
			create and pass Receipt voucher
	*/
	public String  createReceiptVoucher(String chqNumber,String chqAmount,String narration,String voucherDate,String glCodeId,
			int accId,int userId,int fundId,int fundSourceId,String departmentId,String functionaryId,Connection con) throws TaskFailedException,Exception
	{
		/**
		 * implement using create voucher api
		 */
			return null;
	}

		/**
		 * This function will generate the voucher number for the core product
		 * @param type
		 * @param con
		 * @param fundid
		 * @return voucher number
		 */
		public String getVoucherNumber(String type,Connection con,String fundid,int fieldid,String vdate)throws Exception{
			if(LOGGER.isInfoEnabled())     LOGGER.info("Inside getVoucherNumber-->BankEntriesDelegate");
			String vno=null;
			vno=ecm.maxVoucherNumber(type,con,fundid);
			return vno;
		}

		/**
		 * This function will generate  the cgvn number for the core product
		 * @param voucherNum
		 * @param fiscalPeriodId
		 * @param con
		 * @return cgvn number
		 */
		public String getcgvn(String voucherNum,String fiscalPeriodId,Connection con)throws Exception
		{
			if(LOGGER.isInfoEnabled())     LOGGER.info("Inside getcgvn-->BankEntriesDelegate");
			String cgvn=null;

			String vType=voucherNum.substring(0,Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
			cgvn=ecm.getEg_Voucher(vType,fiscalPeriodId,con);
			for(int i=cgvn.length();i<5;i++)
			{
				cgvn="0"+cgvn;
			}
			cgvn=vType+cgvn;
			return cgvn;
		}
		/**
		 * This function will return the cgnumber for voucherheader
		 * @param type
		 * @return
		 * @throws Exception
		 */
		public String getCgNumber(String type,String fpId,Connection con,String vdate)throws Exception
		{
			if(LOGGER.isInfoEnabled())     LOGGER.info("Inside getCgNumber-->BankEntriesDelegate");
			String cgn=null;
			cgn=type+ecm.getCGNumber();
			return cgn;
		}

}
