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
 * Created on Feb 14, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

@Transactional(readOnly = true)
public class GeneralLedger {

    private static final Logger LOGGER = Logger.getLogger(GeneralLedger.class);
    private static TaskFailedException taskExc;

    @PersistenceContext
    private EntityManager entityManager;

    private String id = null;
    private String voucherLineId = "0";
    private String effectiveDate = "1-Jan-1900";
    private String glCodeId = null;
    private String glCode = null;
    private String debitAmount = "0";
    private String creditAmount = "0";
    private String[] accountDetail = null;
    private String description = null;
    private String voucherHeaderId = null;
    private String created = "1-Jan-1900";
    private String functionId = null;

    public void setAccountDetailSize(final int length) {
        if (accountDetail != null)
            return;
        accountDetail = new String[length];
        for (int i = 0; i < length; i++)
            accountDetail[i] = "0";
    }

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    public void setId(final String aId) {
        id = aId;
    }

    @Transactional
    public void insert() throws TaskFailedException {
        final EGovernCommon commommethods = new EGovernCommon();
        try {
            effectiveDate = String.valueOf(commommethods
                    .getCurrentDate());
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date dt = sdf.parse(effectiveDate);
            effectiveDate = formatter.format(dt);

            description = commommethods.formatString(description);
            setId(String.valueOf(PrimaryKeyGenerator
                    .getNextKey("GeneralLedger")));

            if (functionId == null || functionId.equals(""))
                functionId = null;
            StringBuilder insertQuery = new StringBuilder(
                    "INSERT INTO generalledger (id, voucherLineID, effectiveDate, glCodeID, glCode, debitAmount, creditAmount,")
                            .append(" description, voucherHeaderId, functionId) VALUES ( :id, :voucherLineId, :effectiveDate, :glcodeId, :glcode, :debitAmount, :creditAmount,")
                            .append(" :description, :voucherHeaderId, :functionId)");

            if (LOGGER.isInfoEnabled())
                LOGGER.info(insertQuery);
            entityManager.createNativeQuery(insertQuery.toString())
                    .setParameter("id", BigInteger.valueOf(Long.valueOf(id)))
                    .setParameter("voucherLineId",
                            voucherLineId == null ? BigInteger.ZERO : BigInteger.valueOf(Long.valueOf(voucherLineId)))
                    .setParameter("effectiveDate", dt, TemporalType.TIMESTAMP)
                    .setParameter("glcodeId",
                            glCodeId.equalsIgnoreCase("null") ? null : BigInteger.valueOf(Long.valueOf(glCodeId)))
                    .setParameter("glcode", glCode)
                    .setParameter("debitAmount", debitAmount.equalsIgnoreCase("null") ? null : Double.parseDouble(debitAmount))
                    .setParameter("creditAmount", creditAmount.equalsIgnoreCase("null") ? null : Double.parseDouble(creditAmount))
                    .setParameter("description", description)
                    .setParameter("voucherHeaderId",
                            voucherHeaderId.equalsIgnoreCase("null") ? null : BigInteger.valueOf(Long.valueOf(voucherHeaderId)))
                    .setParameter("functionId", functionId == null ? null : BigInteger.valueOf(Long.valueOf(functionId)))
                    .executeUpdate();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        } finally {

        }
    }

    /**
     * Fucntion for update generalledger
     *
     * @throws TaskFailedException
     */
    @Transactional
    public void update() throws TaskFailedException {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            created = formatter.format(sdf.parse(created));
            newUpdate();
        } catch (final Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw taskExc;
        }
    }

    public void newUpdate() throws TaskFailedException {
        final StringBuilder query = new StringBuilder("update generalledger set");
        if (voucherLineId != null)
            query.append(" VOUCHERLINEID = :voucherLineId,");
        if (effectiveDate != null)
            query.append(" EFFECTIVEDATE = :effectiveDate,");
        if (glCodeId != null)
            query.append(" GLCODEID = :glCodeId,");
        if (glCode != null)
            query.append(" GLCODE = :glCode,");
        if (debitAmount != null)
            query.append(" DEBITAMOUNT = :debitAmount,");
        if (creditAmount != null)
            query.append(" CREDITAMOUNT = :creditAmount,");
        if (description != null)
            query.append(" DESCRIPTION = :description,");
        if (voucherHeaderId != null)
            query.append(" VOUCHERHEADERID = :voucherHeaderId,");
        if (functionId != null)
            query.append(" FUNCTIONID = :functionId,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id = :id");
        try {
            Query nativeQuery = entityManager.createNativeQuery(query.toString());
            if (voucherLineId != null)
                nativeQuery.setParameter("voucherLineId", voucherLineId);
            if (effectiveDate != null)
                nativeQuery.setParameter("effectiveDate", effectiveDate);
            if (glCodeId != null)
                nativeQuery.setParameter("glCodeId", glCodeId);
            if (glCode != null)
                nativeQuery.setParameter("glCode", glCode);
            if (debitAmount != null)
                nativeQuery.setParameter("debitAmount", debitAmount);
            if (creditAmount != null)
                nativeQuery.setParameter("creditAmount", creditAmount);
            if (description != null)
                nativeQuery.setParameter("description", description);
            if (voucherHeaderId != null)
                nativeQuery.setParameter("voucherHeaderId", voucherHeaderId);
            if (functionId != null)
                nativeQuery.setParameter("functionId", functionId);
            nativeQuery.setParameter("id", id)
                    .executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: ", e);
            throw taskExc;
        }
    }

    /**
     * Function to get all the recoveries not in fund
     *
     * @param accountDetailType
     * @param accountDetailKey
     * @param fund
     * @param date
     * @param status
     * @return HashMap with account code as the key and the total pending recovery amount for that account code.
     * @throws SQLException
     * @throws TaskFailedException
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, BigDecimal> getRecoveryForSubLedgerNotInFund(final Integer accountDetailType,
            final Integer accountDetailKey, final Integer fund, final Date date, final int status)
            throws SQLException, TaskFailedException {
        final HashMap<String, BigDecimal> hmA = new HashMap<>();
        final HashMap<String, BigDecimal> hmB = new HashMap<>();
        HashMap<String, BigDecimal> hmFinal = new HashMap<>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        final String vDate = formatter.format(date);
        try {

            // Query1 - to get the sum of credit amount glcode wise
            StringBuilder selQuery = new StringBuilder("SELECT GL.GLCODE as ACCOUNTCODE, SUM(GLD.AMOUNT) AS CREDITAMOUNT")
                    .append(" FROM VOUCHERHEADER VH, GENERALLEDGER GL, GENERALLEDGERDETAIL GLD")
                    .append(" WHERE VH.FUNDID NOT IN (:fundId) AND GLD.DETAILTYPEID = :accountDetailType")
                    .append(" AND DETAILKEYID = :accountDetailKey AND VH.STATUS = :status AND GL.CREDITAMOUNT > 0")
                    .append(" AND VH.ID = GL.VOUCHERHEADERID AND GL.ID = GLD.GENERALLEDGERID AND VH.VOUCHERDATE <= :voucherDate")
                    .append(" GROUP BY GL.GLCODE");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query (CreditAmount)--> " + selQuery);
            List<Object[]> rs = entityManager.createNativeQuery(selQuery.toString())
                    .setParameter("fundId", fund)
                    .setParameter("accountDetailType", accountDetailType)
                    .setParameter("accountDetailKey", accountDetailKey)
                    .setParameter("staus", status)
                    .setParameter("voucherDate", vDate)
                    .getResultList();
            for (final Object[] element : rs)
                hmA.put(element[0].toString(), new BigDecimal(element[1].toString()));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("map size -------> " + hmA.size());

            // Query2 - to get the sum of debit amount glcode wise
            selQuery = new StringBuilder("SELECT GL.GLCODE AS GLCODE, SUM(GLD.AMOUNT) AS DEBITAMOUNT")
                    .append(" FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD")
                    .append(" WHERE VH.FUNDID NOT IN (:fundId) AND GLD.DETAILTYPEID = :accountDetailType")
                    .append(" AND DETAILKEYID = :accountDetailKey AND VH.STATUS = :status AND GL.DEBITAMOUNT > 0")
                    .append(" AND VH.ID = GL.VOUCHERHEADERID AND GL.ID = GLD.GENERALLEDGERID AND VH.VOUCHERDATE <= :voucherDate")
                    .append(" GROUP BY GL.GLCODE");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query (DebitAmount)--> " + selQuery);
            rs = entityManager.createNativeQuery(selQuery.toString())
                    .setParameter("fundId", fund)
                    .setParameter("accountDetailType", accountDetailType)
                    .setParameter("accountDetailKey", accountDetailKey)
                    .setParameter("staus", status)
                    .setParameter("voucherDate", vDate)
                    .getResultList();
            for (final Object[] elementB : rs)
                hmB.put(elementB[0].toString(), new BigDecimal(elementB[1].toString()));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("map size -------> " + hmB.size());
            if (hmA.isEmpty())
                return hmB;
            else if (hmB.isEmpty()) {
                final Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
                for (final Map.Entry<String, BigDecimal> meA : setA)
                    hmFinal.put(meA.getKey(), meA.getValue().multiply(
                            new BigDecimal(-1)));
                return hmFinal;
            }

            hmFinal = hmB;
            final Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
            for (final Map.Entry<String, BigDecimal> meA : setA)
                if (hmFinal.containsKey(meA.getKey())) {
                    final BigDecimal iC = hmFinal.get(meA.getKey()).subtract(
                            meA.getValue());
                    hmFinal.put(meA.getKey(), iC);
                } else
                    hmFinal.put(meA.getKey(), meA.getValue().multiply(
                            new BigDecimal(-1)));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("hmCopy------>" + hmFinal);
        } catch (final Exception e) {
            LOGGER.error("Exception in getRecoveryForSubLedgerNotInFund():", e);
            throw taskExc;
        } finally {
        }
        return hmFinal;
    }

    /**
     * Function to get all the recoveries for a particular fund
     *
     * @param accountDetailType
     * @param accountDetailKey
     * @param fund
     * @param date
     * @param status
     * @return HashMap with account code as the key and the total pending recovery amount for that account code.
     * @throws SQLException
     * @throws TaskFailedException
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, BigDecimal> getRecoveryForSubLedger(final Integer accountDetailType,
            final Integer accountDetailKey, final Integer fund, final Date date, final int status)
            throws TaskFailedException {
        final HashMap<String, BigDecimal> hmA = new HashMap<>();
        final HashMap<String, BigDecimal> hmB = new HashMap<>();
        HashMap<String, BigDecimal> hmFinal = new HashMap<>();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        final String vDate = formatter.format(date);
        try {

            // Query1 - to get the sum of credit amount glcode wise
            StringBuilder selQuery = new StringBuilder("SELECT GL.GLCODE as ACCOUNTCODE, SUM(GLD.AMOUNT) as CREDITAMOUNT")
                    .append(" FROM VOUCHERHEADER VH, GENERALLEDGER GL, GENERALLEDGERDETAIL GLD")
                    .append(" WHERE VH.FUNDID = :fundId AND GLD.DETAILTYPEID = :accountDetailType")
                    .append(" AND DETAILKEYID = :accountDetailKey AND VH.STATUS = :status AND GL.CREDITAMOUNT > 0")
                    .append(" AND VH.ID = GL.VOUCHERHEADERID AND GL.ID = GLD.GENERALLEDGERID AND VH.VOUCHERDATE <= :voucherDate")
                    .append(" GROUP BY GL.GLCODE");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query (CreditAmount)--> " + selQuery);
            List<Object[]> rs = entityManager.createNativeQuery(selQuery.toString())
                    .setParameter("fundId", fund)
                    .setParameter("accountDetailType", accountDetailType)
                    .setParameter("accountDetailKey", accountDetailKey)
                    .setParameter("status", status)
                    .setParameter("voucherDate", vDate)
                    .getResultList();
            for (final Object[] element : rs)
                hmA.put(element[0].toString(), new BigDecimal(element[1].toString()));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("map size -------> " + hmA.size());

            // Query2 - to get the sum of debit amount glcode wise
            selQuery = new StringBuilder("SELECT GL.GLCODE as GLCODE, SUM(GLD.AMOUNT) as DEBITAMOUNT")
                    .append(" FROM VOUCHERHEADER VH,GENERALLEDGER GL,GENERALLEDGERDETAIL GLD")
                    .append(" WHERE VH.FUNDID = :fundId AND GLD.DETAILTYPEID = :accountDetailType")
                    .append(" AND DETAILKEYID = :accountDetailKey AND VH.STATUS = :status AND GL.DEBITAMOUNT > 0")
                    .append(" AND VH.ID = GL.VOUCHERHEADERID AND GL.ID = GLD.GENERALLEDGERID AND VH.VOUCHERDATE <= :voucherDate")
                    .append(" GROUP BY GL.GLCODE");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("query (DebitAmount)--> " + selQuery);
            rs = entityManager.createNativeQuery(selQuery.toString())
                    .setParameter("fundId", fund)
                    .setParameter("accountDetailType", accountDetailType)
                    .setParameter("accountDetailKey", accountDetailKey)
                    .setParameter("status", status)
                    .setParameter("voucherDate", vDate)
                    .getResultList();
            for (final Object[] element : rs)
                hmB.put(element[0].toString(), new BigDecimal(element[1].toString()));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("map size -------> " + hmB.size());

            if (hmA.size() == 0)
                return hmB;
            else if (hmB.size() == 0) {
                final Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
                for (final Map.Entry<String, BigDecimal> meA : setA)
                    hmFinal.put(meA.getKey(), meA.getValue().multiply(
                            new BigDecimal(-1)));
                return hmFinal;
            }

            hmFinal = hmB;
            final Set<Map.Entry<String, BigDecimal>> setA = hmA.entrySet();
            for (final Map.Entry<String, BigDecimal> meA : setA)
                if (hmFinal.containsKey(meA.getKey())) {
                    final BigDecimal iC = hmFinal.get(meA.getKey()).subtract(
                            meA.getValue());
                    hmFinal.put(meA.getKey(), iC);
                } else
                    hmFinal.put(meA.getKey(), meA.getValue().multiply(
                            new BigDecimal(-1)));
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("hmCopy------>" + hmFinal);
        } catch (final Exception e) {
            LOGGER.error("Exception in getRecoveryForSubLedger():", e);
            throw taskExc;
        } finally {
        }
        return hmFinal;
    }

    public String getVoucherLineId() {
        return voucherLineId;
    }

    public void setVoucherLineId(final String voucherLineId) {
        this.voucherLineId = voucherLineId;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(final String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getGlCodeId() {
        return glCodeId;
    }

    public void setGlCodeId(final String glCodeId) {
        this.glCodeId = glCodeId;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(final String glCode) {
        this.glCode = glCode;
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(final String debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(final String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String[] getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(final String[] accountDetail) {
        this.accountDetail = accountDetail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getVoucherHeaderId() {
        return voucherHeaderId;
    }

    public void setVoucherHeaderId(final String voucherHeaderId) {
        this.voucherHeaderId = voucherHeaderId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(final String functionId) {
        this.functionId = functionId;
    }
}