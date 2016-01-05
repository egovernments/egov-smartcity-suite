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

    private static final Logger LOGGER = Logger.getLogger(BankEntriesDelegate.class);
    EGovernCommon ecm = new EGovernCommon();

    /*
     * create and pass payment voucher
     */
    public String createPaymentVoucher(final String chqNumber, final String chqAmount, final String narration,
            final String voucherDate, final String glCodeId,
            final int accId, final int userId, final int fundId, final int fundSourceId, final String departmentId,
            final String functionaryId, final Connection con)
                    throws TaskFailedException, Exception
    {
        /**
         * implement using create voucher api
         */

        return null;
    }

    /*
     * create and pass Receipt voucher
     */
    public String createReceiptVoucher(final String chqNumber, final String chqAmount, final String narration,
            final String voucherDate, final String glCodeId,
            final int accId, final int userId, final int fundId, final int fundSourceId, final String departmentId,
            final String functionaryId, final Connection con)
                    throws TaskFailedException, Exception
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
    public String getVoucherNumber(final String type, final Connection con, final String fundid, final int fieldid,
            final String vdate) throws Exception {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside getVoucherNumber-->BankEntriesDelegate");
        String vno = null;
        vno = ecm.maxVoucherNumber(type, fundid);
        return vno;
    }

    /**
     * This function will generate the cgvn number for the core product
     * @param voucherNum
     * @param fiscalPeriodId
     * @param con
     * @return cgvn number
     */
    public String getcgvn(final String voucherNum, final String fiscalPeriodId, final Connection con) throws Exception
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside getcgvn-->BankEntriesDelegate");
        String cgvn = null;

        final String vType = voucherNum.substring(0, Integer.parseInt(FinancialConstants.VOUCHERNO_TYPE_LENGTH));
        cgvn = ecm.getEg_Voucher(vType, fiscalPeriodId);
        for (int i = cgvn.length(); i < 5; i++)
            cgvn = "0" + cgvn;
        cgvn = vType + cgvn;
        return cgvn;
    }

    /**
     * This function will return the cgnumber for voucherheader
     * @param type
     * @return
     * @throws Exception
     */
    public String getCgNumber(final String type, final String fpId, final Connection con, final String vdate) throws Exception
    {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside getCgNumber-->BankEntriesDelegate");
        String cgn = null;
        cgn = type + ecm.getCGNumber();
        return cgn;
    }

}
