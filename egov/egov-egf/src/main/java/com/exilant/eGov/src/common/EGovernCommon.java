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
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author pushpendra.singh
 * <p>
 * This class contains the common methods used for E-Governments applciation
 */
@Transactional(readOnly = true)
@Service("eGovernCommon")
public class EGovernCommon extends AbstractTask {

    private static final Logger LOGGER = Logger.getLogger(EGovernCommon.class);
    private static final String EXILRPERROR = "exilRPError";
    private static TaskFailedException taskExc;
    private final SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MMM-yyyy");
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
                        final boolean gridHasCol, final String prefix) {
        datacol.addValue("voucherHeader_cgn", getCGNumber());
        if (datacol.getValue("hasSecondCGN").equalsIgnoreCase("true"))
            datacol.addValue("jv_cgn", getCGNumber());
        datacol.addValue("databaseDate", getCurrentDate());
    }

    public long getCGNumber() {
        return PrimaryKeyGenerator.getNextKey("voucherheader");
    }

    /**
     * This function returns the system date of the database server.
     *
     * @return
     * @throws TaskFailedException
     */
    public String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    /**
     * @param datacol
     * @return
     */
    public List getFormFields(final DataCollection datacol) {
        final Set formSet = datacol.values.keySet();
        final List formList = new ArrayList();
        final Iterator itr = formSet.iterator();
        while (itr.hasNext())
            formList.add(itr.next());
        return formList;
    }

    /**
     * This function is to handle the single quotes.
     *
     * @param strToFormat
     * @return
     */
    public String formatString(final String strToFormat) {
        if (strToFormat != null) {
            if (strToFormat.equalsIgnoreCase(""))
                return " ";
            final String valn1 = strToFormat.replaceAll("\n", " ");
            final String formtStr = valn1.replaceAll("\r", " ");
            return formtStr.replaceAll("'", "''");
        } else
            return " ";
    }

    @Deprecated
    public String getCurrentDateTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    /**
     * @param vouType           Eg - U/DBP/CGVN
     * @param fiscalPeriodIdStr
     * @return
     * @throws Exception
     */
    public String getEg_Voucher(final String vouType, final String fiscalPeriodIdStr) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" In EGovernCommon :getEg_Voucher method ");
        final CFiscalPeriod fiscalPeriod = (CFiscalPeriod) persistenceService.find("from CFiscalPeriod where id = ?1",
                Long.parseLong(fiscalPeriodIdStr));
        BigInteger cgvn = null;
        String sequenceName = "";
        // Sequence name will be SQ_U_DBP_CGVN_FP7 for vouType U/DBP/CGVN and fiscalPeriodIdStr 7
        try {
            sequenceName = VoucherHelper.sequenceNameFor(vouType, fiscalPeriod.getName());
            cgvn = (BigInteger) databaseSequenceProvider.getNextSequence(sequenceName);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("----- CGVN : " + cgvn);

        } catch (final PersistenceException e) {
            databaseSequenceCreator.createSequence(sequenceName);
            cgvn = (BigInteger) databaseSequenceProvider.getNextSequence(sequenceName);
            LOGGER.error("Error in generating CGVN" ,e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final Exception e) {
            LOGGER.error("Error in generating CGVN" ,e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        return cgvn.toString();
    }

    public String getEffectiveDateFilter(final String val) {
        String returnVal = "";
        if (val == null)
            returnVal = new StringBuilder(" and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<")
                    .append(" TO_DATE('").append(getCurrentDateTime()).append("','dd-Mon-yyyy HH24:MI:SS')").toString();
        else
            returnVal = new StringBuilder(" and TO_DATE(TO_CHAR(vh.effectivedate,'dd-Mon-yyyy HH24:MI:SS'),'dd-Mon-yyyy HH24:MI:SS')<")
                    .append(" TO_DATE('").append(val).append("','dd-Mon-yyyy HH24:MI:SS')").toString();
        return returnVal;
    }

    public String getCurDateTime() {
        return new SimpleDateFormat("dd-Mon-yyyy HH:mm:ss").format(new Date());
    }

    public String getBillNumber() throws TaskFailedException {
        throw new TaskFailedException("Method Not Supported Exception");
    }

    /**
     * This API returns the fiscialperiodid for the date passed
     *
     * @param vDate
     * @return
     * @throws TaskFailedException
     */
    public String getFiscalPeriod(final String vDate) throws TaskFailedException {
        BigInteger fiscalPeriod = null;
        final String sql = "select id from fiscalperiod where :vDate between startingdate and endingdate";
        try {
            final List<BigInteger> rset = persistenceService.getSession().createNativeQuery(sql)
                    .setParameter("vDate", vDate, StringType.INSTANCE)
                    .list();
            fiscalPeriod = rset != null ? rset.get(0) : BigInteger.ZERO;
        } catch (final Exception e) {
            LOGGER.error("Exception...", e);
            throw new TaskFailedException(e.getMessage());
        }
        return fiscalPeriod.toString();
    }

    /**
     * Function to check if the voucher number is Unique
     *
     * @param vcNum
     * @param vcDate
     * @param datacol
     * @return
     */
    public boolean isUniqueVN(String vcNum, final String vcDate, final DataCollection datacol) throws Exception {
        boolean isUnique = false;
        vcNum = vcNum.toUpperCase();
        String fyEndDate = "";
        try {
            StringBuilder query = new StringBuilder("SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\"")
                    .append(" FROM financialYear")
                    .append(" WHERE startingDate <= :voucherDate AND endingDate >= :voucherDate");
            List<Object[]> rs = persistenceService.getSession().createNativeQuery(query.toString())
                    .setParameter("voucherDate", vcDate, StringType.INSTANCE)
                    .list();
            for (final Object[] element : rs) {
                element[0].toString();
                fyEndDate = element[1].toString();
            }
            query = new StringBuilder("SELECT id FROM voucherHeader WHERE voucherNumber = :voucherNumber AND voucherDate >= :voucherDate AND voucherDate <= :fyEndDate and status != 4");
            rs = persistenceService.getSession().createNativeQuery(query.toString())
                    .setParameter("voucherDate", vcDate, StringType.INSTANCE)
                    .setParameter("fyEndDate", fyEndDate, StringType.INSTANCE)
                    .list();
            for (final Object[] element : rs)
                datacol.addMessage(EXILRPERROR, "duplicate voucher number");
            if (rs.isEmpty())
                isUnique = true;
        } catch (final Exception ex) {
            datacol.addMessage(EXILRPERROR, "DataBase Error(isUniqueVN) : " , ex.toString());
            throw new TaskFailedException();
        }
        return isUnique;
    }

    /**
     * @param vcNum
     * @param vcDate
     * @return
     * @throws TaskFailedException,Exception
     */
    public boolean isUniqueVN(String vcNum, final String vcDate) throws Exception {
        boolean isUnique = false;
        String fyStartDate = "";
        String fyEndDate = "";
        vcNum = vcNum.toUpperCase();
        try {
            StringBuilder query = new StringBuilder("SELECT to_char(startingDate, 'DD-Mon-YYYY') AS \"startingDate\", to_char(endingDate, 'DD-Mon-YYYY') AS \"endingDate\"")
                    .append(" FROM financialYear WHERE startingDate <= :voucherDate AND endingDate >= :voucherDate");
            List<Object[]> rs = persistenceService.getSession().createNativeQuery(query.toString())
                    .setParameter("voucherDate", vcDate, StringType.INSTANCE)
                    .list();
            if (!rs.isEmpty())
                for (final Object[] element : rs) {
                    fyStartDate = element[0].toString();
                    fyEndDate = element[1].toString();
                }
            query = new StringBuilder("SELECT id FROM voucherHeader WHERE voucherNumber = :voucherNumber AND voucherDate >= :fyStartDate AND voucherDate <= fyEndDate and status != 4");
            rs = persistenceService.getSession().createNativeQuery(query.toString())
                    .setParameter("voucherNumber", vcNum, StringType.INSTANCE)
                    .setParameter("fyStartDate", fyStartDate, StringType.INSTANCE)
                    .setParameter("fyEndDate", fyEndDate, StringType.INSTANCE)
                    .list();
            if (!rs.isEmpty()) {
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

    public BigDecimal getAccountBalance(final Date VoucherDate, final String bankAccountId) throws TaskFailedException {
        BigDecimal totalAvailable = BigDecimal.ZERO;
        BigDecimal opeAvailable = BigDecimal.ZERO;
        try {
            final SimpleDateFormat formatter = dtFormat;
            final String vcDate = formatter.format(VoucherDate);
            StringBuilder str = new StringBuilder("SELECT case when sum(openingDebitBalance) = null then 0 else sum(openingDebitBalance) end -")
                    .append(" case when sum(openingCreditBalance) = null then 0 else sum(openingCreditBalance) end AS \"openingBalance\"")
                    .append(" FROM transactionSummary")
                    .append(" WHERE financialYearId = (SELECT id FROM financialYear WHERE startingDate <= :vcDate")
                    .append(" AND endingDate >= :vcDate) AND glCodeId = (select glcodeid from bankaccount where id = :bankAccountId)");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            List<Object[]> resultset = persistenceService.getSession().createNativeQuery(str.toString())
                    .setParameter("vcDate", vcDate, StringType.INSTANCE)
                    .setParameter("bankAccountId", bankAccountId, StringType.INSTANCE)
                    .list();
            for (final Object[] element : resultset)
                opeAvailable = new BigDecimal(element[0].toString());
            if (resultset.isEmpty())
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);

            str = new StringBuilder("SELECT (case when sum(gl.debitAmount) = null then 0 else sum(gl.debitAmount) end) -")
                    .append(" (case when sum(gl.creditAmount) = null then 0 else sum(gl.creditAmount) end)")
                    .append(opeAvailable).append(" as \"totalAmount\"")
                    .append(" FROM generalLedger gl, voucherHeader vh")
                    .append(" WHERE vh.id = gl.voucherHeaderId AND gl.glCodeid = (select glcodeid from bankaccount where id=:bankAccountId)")
                    .append(" AND vh.voucherDate >= (SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') FROM financialYear WHERE startingDate <= :vcDate AND endingDate >= :vcDate)")
                    .append(" AND vh.voucherDate <= :vcDate and vh.status != 4");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str);
            resultset = persistenceService.getSession().createNativeQuery(str.toString())
                    .setParameter("bankAccountId", bankAccountId, StringType.INSTANCE)
                    .setParameter("vcDate", vcDate, StringType.INSTANCE)
                    .list();
            for (final Object[] element : resultset) {
                totalAvailable = new BigDecimal(element[0].toString());
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("total balance  " + totalAvailable);
            }
            if (resultset.isEmpty())
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Else resultset in getbalance...");

            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);

        } catch (final Exception e) {
            LOGGER.error("Exception in getAccountBalance", e);
            throw taskExc;
        }
        return totalAvailable;
    }

    public String assignValue(final String data, final String defaultValue) {
        if (StringUtils.isNotBlank(data))
            return "'".concat(trimChar(formatString(data), "'".charAt(0))).concat("'");
        else if (StringUtils.isNotBlank(defaultValue))
            return "'".concat(trimChar(defaultValue, "'".charAt(0))).concat("'");
        else
            return defaultValue;
    }

    public BigDecimal getAccountBalance(final String recDate, final String bankAccountId) throws TaskFailedException {

        BigDecimal opeAvailable = BigDecimal.ZERO;
        BigDecimal totalAvailable = BigDecimal.ZERO;
        try {
            StringBuilder str = new StringBuilder("SELECT case when sum(openingDebitBalance) is null then 0 else sum(openingDebitBalance) end -")
                    .append(" case when sum(openingCreditBalance) is null then 0 else sum(openingCreditBalance) end AS \"openingBalance\" ")
                    .append(" FROM transactionSummary")
                    .append(" WHERE financialYearId = (SELECT id FROM financialYear WHERE startingDate <= :recDate AND endingDate >= :recDate)")
                    .append(" AND glCodeId = (select glcodeid from bankaccount where id = :bankAccountId)");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("getAccountBalance(EGovernCommon.java): " + str);
            SimpleDateFormat dtSlashFormat = new SimpleDateFormat("dd/MMM/yyyy");
            Date reconDate = dtSlashFormat.parse(recDate);
            java.sql.Date sDate = new java.sql.Date(reconDate.getTime());
            List list = persistenceService.getSession().createNativeQuery(str.toString())
                    .setParameter("recDate", sDate, DateType.INSTANCE)
                    .setParameter("bankAccountId", Integer.valueOf(bankAccountId), IntegerType.INSTANCE)
                    .list();
            if (list.isEmpty()) {
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Else resultset in getAccountBalance...");
            } else
                opeAvailable = new BigDecimal(list.get(0).toString());

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("opening balance  " + opeAvailable);

            str = new StringBuilder("SELECT (case when sum(gl.debitAmount) is null then 0 else sum(gl.debitAmount) end - case when sum(gl.creditAmount) is null then 0")
                    .append(" else sum(gl.creditAmount) end ) + ")
                    .append(opeAvailable).append(" as \"totalAmount\"")
                    .append(" FROM  generalLedger gl, voucherHeader vh")
                    .append(" WHERE vh.id = gl.voucherHeaderId and gl.glCodeid = (select glcodeid from bankaccount where id = :bankAccountId)")
                    .append(" AND vh.voucherDate >= (SELECT startingDate FROM financialYear WHERE startingDate <= :reconDate AND endingDate >= :reconDate)")
                    .append(" AND vh.voucherDate <= :reconDate and vh.status != 4");

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Curr Yr Bal: " + str);
            list = persistenceService.getSession().createNativeQuery(str.toString())
                    .setParameter("bankAccountId", Integer.valueOf(bankAccountId), IntegerType.INSTANCE)
                    .setParameter("reconDate", reconDate, DateType.INSTANCE)
                    .list();
            if (!list.isEmpty())
                totalAvailable = new BigDecimal(list.get(0).toString());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance  " + totalAvailable);

            totalAvailable = totalAvailable.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("total balance before return " + totalAvailable);
            return totalAvailable;
        } catch (final Exception e) {
            LOGGER.error("Exception in getAccountBalance", e);
            throw taskExc;
        }
    }

    /**
     * this function trims ch in string
     *
     * @param str
     * @param ch
     * @return
     */
    public String trimChar(String str, final char ch) {
        Boolean b = true, e = true;
        str = str.trim();
        while (str.length() > 0 && (b || e)) {
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
     *
     * @param moduleType
     * @param description
     * @return statusId
     */
    public String getEGWStatusId(final String moduleType, final String description) throws TaskFailedException {
        String statusId = "0";
        try {
            final String sql = "select distinct id from egw_status where upper(moduletype) = :moduleType and upper(description) = :description";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("statement" + sql);
            List<Object[]> rs = persistenceService.getSession().createNativeQuery(sql)
                    .setParameter("moduleType", moduleType.toUpperCase())
                    .setParameter("description", description.toUpperCase())
                    .list();
            for (final Object[] element : rs)
                statusId = element[0].toString();

            if (LOGGER.isDebugEnabled())
                LOGGER.debug("$$$$$$$$$$$$$$$$$$$statusId===" + statusId);
            if (statusId == null || statusId.equals("0"))
                throw taskExc;

        } catch (final Exception e) {
            LOGGER.error("Exception in getEGWStatusId=====:", e);
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