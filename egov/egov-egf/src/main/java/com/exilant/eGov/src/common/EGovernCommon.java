/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
/*
 * Created on Jan 7, 2005
 *
 */
package com.exilant.eGov.src.common;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFiscalPeriod;
import org.egov.infra.persistence.utils.DatabaseSequenceCreator;
import org.egov.infra.persistence.utils.DatabaseSequenceProvider;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.services.PersistenceService;
import org.egov.utils.VoucherHelper;
import org.hibernate.Query;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author pushpendra.singh
 *
 * This class contains the common methods used for E-Governments applciation
 */
@Transactional(readOnly = true)
@Service("eGovernCommon")
public class EGovernCommon extends AbstractTask {
	 
	private final SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MMM-yyyy");
	
	private static final Logger LOGGER = Logger.getLogger(EGovernCommon.class);
	private static TaskFailedException taskExc;
	private static final String EXILRPERROR = "exilRPError";
	@Autowired
	@Qualifier("persistenceService")
	private PersistenceService persistenceService;
	
	@Autowired
	private DatabaseSequenceCreator databaseSequenceCreator;
	@Autowired
	private DatabaseSequenceProvider databaseSequenceProvider;
	

	@Override
	public void execute(final String taskName,
			final String gridName,
			final DataCollection datacol,
			final Connection con,
			final boolean errorData,
			final boolean gridHasCol, final String prefix) throws TaskFailedException
	{
		datacol.addValue("voucherHeader_cgn", getCGNumber());
		if (datacol.getValue("hasSecondCGN").equalsIgnoreCase("true"))
			datacol.addValue("jv_cgn", getCGNumber());
		datacol.addValue("databaseDate", getCurrentDate());
	}

	public long getCGNumber()
	{
		return PrimaryKeyGenerator.getNextKey("voucherheader");
	}

	/**
	 * This function returns the system date of the database server.
	 * @param connection
	 * @return
	 * @throws TaskFailedException
	 */
	public String getCurrentDate() throws TaskFailedException
	{
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	}

	/**
	 *
	 * @param datacol
	 * @return
	 */
	public List getFormFields(final DataCollection datacol)
	{
		final Set formSet = datacol.values.keySet();
		final List formList = new ArrayList();
		final Iterator itr = formSet.iterator();
		while (itr.hasNext())
			formList.add(itr.next());
		return formList;
	}

	/**
	 *
	 * @param field
	 * @param data
	 * @param connection
	 * @return
	 */



	/**
	 * This function is to handle the single quotes.
	 * @param strToFormat
	 * @return
	 */
	public String formatString(final String strToFormat) {
		if (strToFormat != null)
		{
			if (strToFormat.equalsIgnoreCase(""))
				return " ";
			final String valn1 = strToFormat.replaceAll("\n", " ");
			final String formtStr = valn1.replaceAll("\r", " ");
			return formtStr.replaceAll("'", "''");
		} else
			return " ";
	}

	 

	 
	@Deprecated
	public String getCurrentDateTime() throws TaskFailedException
	{
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	}












	/**
	 *
	 * @param vouType Eg - U/DBP/CGVN
	 * @param fiscialPeriod
	 * @param conn
	 * @return
	 * @throws TaskFailedException,Exception
	 */
	public String getEg_Voucher(final String vouType, final String fiscalPeriodIdStr) throws TaskFailedException, Exception
	{
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(" In EGovernCommon :getEg_Voucher method ");
		final CFiscalPeriod fiscalPeriod = (CFiscalPeriod) persistenceService.find("from CFiscalPeriod where id=?",
				Long.parseLong(fiscalPeriodIdStr));
		BigInteger cgvn = null;
		String sequenceName = "";
		// Sequence name will be SQ_U_DBP_CGVN_FP7 for vouType U/DBP/CGVN and fiscalPeriodIdStr 7
		try {
			sequenceName   = VoucherHelper.sequenceNameFor(vouType, fiscalPeriod.getName());
			cgvn = (BigInteger) databaseSequenceProvider.getNextSequence(sequenceName);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("----- CGVN : " + cgvn);

		} catch (final SQLGrammarException e)
		{
			databaseSequenceCreator.createSequence(sequenceName);
			cgvn = (BigInteger) databaseSequenceProvider.getNextSequence(sequenceName);
			LOGGER.error("Error in generating CGVN" + e);
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		} catch (final Exception e)
		{
			LOGGER.error("Error in generating CGVN" + e);
			throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
		}
		return cgvn.toString();

	}



	public String getEffectiveDateFilter(final String val) throws TaskFailedException, Exception {
		String returnVal = "";
		if (val == null) {
			returnVal = getCurrentDateTime();
			returnVal = " and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
					" TO_DATE('" + returnVal + "','dd-Mon-yyyy HH24:MI:SS')";

		} else
			returnVal = " and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<" +
					" TO_DATE('" + val + "','dd-Mon-yyyy HH24:MI:SS')";
		return returnVal;
	}

	public String getCurDateTime() throws TaskFailedException, Exception {
		return new SimpleDateFormat("dd-Mon-yyyy HH:mm:ss").format(new Date());
	}







	public String getBillNumber() throws TaskFailedException, Exception
	{
		throw new TaskFailedException("Method Not Supported Exception");
	}












	/**
	 * This API returns the fiscialperiodid for the date passed
	 * @param vDate
	 * @param con
	 * @return
	 * @throws TaskFailedException
	 */
	public String getFiscalPeriod(final String vDate) throws TaskFailedException {
		BigInteger fiscalPeriod = null;
		final String sql = "select id from fiscalperiod  where '" + vDate + "' between startingdate and endingdate";
		try {
			final Query pst = persistenceService.getSession().createSQLQuery(sql);
			final List<BigInteger> rset = pst.list();
			fiscalPeriod = rset != null ? rset.get(0) : BigInteger.ZERO;
		} catch (final Exception e) {
			LOGGER.error("Exception..." + e.getMessage());
			throw new TaskFailedException(e.getMessage());
		}
		return fiscalPeriod.toString();
	}
	
	 /**
     * Function to check if the voucher number is Unique
     * @param vcNum
     * @param vcDate
     * @param datacol
     * @param conn
     * @return
     */
    public boolean isUniqueVN(String vcNum, final String vcDate, final DataCollection datacol) throws TaskFailedException,
            Exception {
        boolean isUnique = false;
        vcNum = vcNum.toUpperCase();
        Query pst = null;
        List<Object[]> rs = null;
        String fyEndDate = "";
        try {
            final String query1 = "SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"
                    + vcDate + "' AND endingDate >= '" + vcDate + "'";
            pst = persistenceService.getSession().createSQLQuery(query1);
            rs = pst.list();
            for (final Object[] element : rs) {
                element[0].toString();
                fyEndDate = element[1].toString();
            }
            final String query2 = "SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='"
                    + vcDate
                    + "' AND voucherDate<='" + fyEndDate + "' and status!=4";
            pst = persistenceService.getSession().createSQLQuery(query2);
            rs = pst.list();
            for (final Object[] element : rs)
                datacol.addMessage(EXILRPERROR, "duplicate voucher number");
            if (rs == null || rs.size() == 0)
                isUnique = true;
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueVN) : " + ex.toString());
            throw new TaskFailedException();
        }
        return isUnique;
    }
	   
   /**
    * @param vcNum
    * @param vcDate
    * @param conn
    * @return
    * @throws TaskFailedException,Exception
    */
   public boolean isUniqueVN(String vcNum, final String vcDate) throws Exception, TaskFailedException {
       boolean isUnique = false;
       String fyStartDate = "", fyEndDate = "";
       vcNum = vcNum.toUpperCase();
       Query pst = null;                           
       List<Object[]> rs = null;
       try {
           final String query1 = "SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\" FROM financialYear WHERE startingDate <= '"
                   + vcDate + "' AND endingDate >= '" + vcDate + "'";
           pst = persistenceService.getSession().createSQLQuery(query1);
           rs = pst.list();
           if (rs != null && rs.size() > 0)
               for (final Object[] element : rs) {
                   fyStartDate = element[0].toString();
                   fyEndDate = element[1].toString();
               }
           final String query2 = "SELECT id FROM voucherHeader WHERE voucherNumber = '" + vcNum + "' AND voucherDate>='"
                   + fyStartDate
                   + "' AND voucherDate<='" + fyEndDate + "' and status!=4";
           pst = persistenceService.getSession().createSQLQuery(query2);
           rs = pst.list();
           if (rs != null && rs.size() > 0) {
               if (LOGGER.isDebugEnabled())
                   LOGGER.debug("Duplicate Voucher Number");
           } else
               isUnique = true;
       } catch (final Exception ex) {
           LOGGER.error("error in finding unique VoucherNumber");
           throw taskExc;
       } finally {
           try {
           } catch (final Exception e) {
               LOGGER.error("isUniqueVN....");
           }
       }
       return isUnique;
   }

      
   
   public BigDecimal getAccountBalance(final Date VoucherDate, final String bankAccountId) throws TaskFailedException
   {
       BigDecimal totalAvailable = BigDecimal.ZERO;
       BigDecimal opeAvailable = BigDecimal.ZERO;  
       Query pst = null;
       List<Object[]> resultset = null;
       List<Object[]> resultset1 = null;
       try {
           final SimpleDateFormat formatter = dtFormat;
           final String vcDate = formatter.format(VoucherDate);

           final String str = "SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end-  case when sum(openingCreditBalance) = null  then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                   +
                   "FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=?" +
                   "AND endingDate >= ?)  AND glCodeId =(select glcodeid from bankaccount where id=?)";
           if (LOGGER.isDebugEnabled())
               LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
           pst = persistenceService.getSession().createSQLQuery(str);
           pst.setString(0, vcDate);
           pst.setString(1, vcDate);
           pst.setString(2, bankAccountId);
           resultset = pst.list();
           for (final Object[] element : resultset)
               opeAvailable = new BigDecimal(element[0].toString());
           if (resultset == null || resultset.size() == 0)
               if (LOGGER.isDebugEnabled())
                   LOGGER.debug("Else resultset in getbalance");

           if (LOGGER.isDebugEnabled())
               LOGGER.debug("opening balance  " + opeAvailable);
           // resultset.close();

           final String str1 = "SELECT (case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end) - (case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end) + "
                   + opeAvailable
                   +
                   " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id=?) AND  "
                   +
                   " vh.voucherDate >=( SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4";

           if (LOGGER.isDebugEnabled())
               LOGGER.debug("Curr Yr Bal: " + str1);
           pst = persistenceService.getSession().createSQLQuery(str1);
           pst.setString(0, bankAccountId);
           pst.setString(1, vcDate);
           pst.setString(2, vcDate);
           pst.setString(3, vcDate);
           resultset1 = pst.list();
           for (final Object[] element : resultset1) {
               totalAvailable = new BigDecimal(element[0].toString());
               if (LOGGER.isDebugEnabled())
                   LOGGER.debug("total balance  " + totalAvailable);
           }
           if (resultset1 == null || resultset1.size() == 0)
               if (LOGGER.isDebugEnabled())
                   LOGGER.debug("Else resultset in getbalance...");

           totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
           if (LOGGER.isDebugEnabled())
               LOGGER.debug("total balance before return " + totalAvailable);

       } catch (final Exception e) {
           LOGGER.error(e.getMessage(), e);
           throw taskExc;
       }
       return totalAvailable;
   }
	public String assignValue(final String data, final String defaultValue)
	{
		if (StringUtils.isNotBlank(data))
			return "'" + trimChar(formatString(data), "'".charAt(0)) + "'";
		else if (StringUtils.isNotBlank(defaultValue))
			return "'" + trimChar(defaultValue, "'".charAt(0)) + "'";
		else
			return defaultValue;
	}
	
	
	public BigDecimal getAccountBalance(final String recDate, final String bankAccountId) throws TaskFailedException
    {

        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        Query pst = null;
        List<Object[]> resultset = null;
        List<Object[]> resultset1 = null;
        try {
            final String str = "SELECT case when sum(openingDebitBalance) is null then 0 else sum(openingDebitBalance) end - case when sum(openingCreditBalance) is null then 0 else sum(openingCreditBalance) end AS \"openingBalance\" "
                    +
                    " FROM transactionSummary WHERE financialYearId=( SELECT id FROM financialYear WHERE startingDate <=? " +
                    " AND endingDate >=? )  AND glCodeId =(select glcodeid from bankaccount where id=? )";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            pst = persistenceService.getSession().createSQLQuery(str);
            SimpleDateFormat dtSlashFormat = new SimpleDateFormat("dd/MMM/yyyy");
            Date reconDate=dtSlashFormat.parse(recDate);
            java.sql.Date sDate=new java.sql.Date(reconDate.getTime());
            pst.setDate(0, sDate);
            pst.setDate(1, sDate);
            pst.setInteger(2, Integer.valueOf(bankAccountId));
            List list = pst.list();  
            if (list == null || list.size() == 0)
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Else resultset in getAccountBalance...");

            if(list!=null || list.size() > 0)
            {
            	opeAvailable=new BigDecimal(list.get(0).toString());
            }
            
           /* for (final Object[] element : resultset)
            {
            	if(element[0]!=null)
                opeAvailable = new BigDecimal(element[0].toString());
            }*/
           
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);

            final String str1 = "SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount)  is null then 0 else sum(gl.creditAmount) end ) + "
                    + opeAvailable
                    +
                    " as \"totalAmount\" FROM   generalLedger gl, voucherHeader vh WHERE vh.id = gl.voucherHeaderId and gl.glCodeid = (select glcodeid from bankaccount where id=?) AND  "
                    +
                    " vh.voucherDate >=( SELECT startingDate FROM financialYear WHERE startingDate <= ? AND endingDate >= ?) AND vh.voucherDate <= ? and vh.status!=4";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str1);  
            pst = persistenceService.getSession().createSQLQuery(str1);
            pst.setInteger(0, Integer.valueOf(bankAccountId));
            pst.setDate(1, reconDate);
            pst.setDate(2, reconDate);
            pst.setDate(3, reconDate);
           List list2 = pst.list();
            if(list2!=null)
               totalAvailable = new BigDecimal(list2.get(0).toString());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance  " + totalAvailable);       
            
            if (resultset1 == null || resultset1.size() == 0)
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getAccountBalance...");

            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);
            return totalAvailable;
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }    
    }

	/**
	 * this function trims ch in string
	 * @param str
	 * @param ch
	 * @return
	 */
	public String trimChar(String str, final char ch)
	{
		Boolean b = true, e = true;
		str = str.trim();
		while (str.length() > 0 && (b || e))
		{
			if (str.charAt(0) == ch)
				str = str.substring(1, str.length());
			else
				b = false;
			if (str.charAt(str.length() - 1) == ch)
				str = str.substring(0, str.length() - 1);
			else
				e = false;
		}
		return str;
	}










	/**
	 * To get the EGW_STATUS id
	 * @param con
	 * @param moduleType
	 * @param description
	 * @return statusId
	 */
	public String getEGWStatusId(final String moduleType, final String description) throws TaskFailedException
	{
		String statusId = "0";
		Query pstmt = null;
		List<Object[]> rs = null;
		try
		{
			final String sql = " select distinct id from egw_status where upper(moduletype)= ? and upper(description)= ? ";
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("statement" + sql);
			pstmt = persistenceService.getSession().createSQLQuery(sql);
			pstmt.setString(0, moduleType.toUpperCase());
			pstmt.setString(1, description.toUpperCase());
			rs = pstmt.list();
			for (final Object[] element : rs)
				statusId = element[0].toString();

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("$$$$$$$$$$$$$$$$$$$statusId===" + statusId);
			if (statusId == null || statusId.equals("0"))
				throw taskExc;

		} catch (final Exception e)
		{
			LOGGER.error("Exception in getEGWStatusId=====:" + e.getMessage());
			throw taskExc;
		}
		return statusId;
	}



	public PersistenceService getPersistenceService() {
		return persistenceService;
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

}
