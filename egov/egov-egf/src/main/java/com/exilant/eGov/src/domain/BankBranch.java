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
 * Created on Jan 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

/**
 * @author pushpendra.singh
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class BankBranch {
    private String id = null;
    private String branchCode = null;
    private String branchName = null;
    private String branchAddress1 = null;
    private String branchAddress2 = null;
    private String branchCity = null;
    private String branchState = null;
    private String branchPin = null;
    private String branchMICR = null;
    private String branchPhone = null;
    private String branchFax = null;
    private String bankId = null;
    private String contactPerson = null;
    private String isActive = "1";
    private String created = "1-Jan-1900";
    private String modifiedBy = null;
    private String lastModified = "1-Jan-1900";
    private String narration = null;
    private TaskFailedException taskExc;
    private final static Logger LOGGER = Logger.getLogger(BankBranch.class);
    private boolean isId = false, isField = false;
    EGovernCommon cm = new EGovernCommon();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
            .getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
            Locale.getDefault());

    public void setId(final String aId) {
        id = aId;
        isId = true;
    }

    public String getId() {
        return id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(final String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(final String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress1() {
        return branchAddress1;
    }

    public void setBranchAddress1(final String branchAddress1) {
        this.branchAddress1 = branchAddress1;
    }

    public String getBranchAddress2() {
        return branchAddress2;
    }

    public void setBranchAddress2(final String branchAddress2) {
        this.branchAddress2 = branchAddress2;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(final String branchCity) {
        this.branchCity = branchCity;
    }

    public String getBranchState() {
        return branchState;
    }

    public void setBranchState(final String branchState) {
        this.branchState = branchState;
    }

    public String getBranchPin() {
        return branchPin;
    }

    public void setBranchPin(final String branchPin) {
        this.branchPin = branchPin;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(final String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchFax() {
        return branchFax;
    }

    public void setBranchFax(final String branchFax) {
        this.branchFax = branchFax;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(final String bankId) {
        this.bankId = bankId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(final String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(final String isActive) {
        this.isActive = isActive;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(final String created) {
        this.created = created;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(final String lastModified) {
        this.lastModified = lastModified;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(final String narration) {
        this.narration = narration;
    }

    public boolean isId() {
        return isId;
    }

    public void setId(final boolean isId) {
        this.isId = isId;
    }

    public boolean isField() {
        return isField;
    }

    public void setField(final boolean isField) {
        this.isField = isField;
    }

    public String getBranchMICR() {
        return branchMICR;
    }

    public void setBranchMICR(final String branchMICR) {
        this.branchMICR = branchMICR;
    }

    /**
     * This is Insert API
     *
     * @param connection
     * @throws TaskFailedException
     * @throws SQLException
     */
    @Transactional
    public void insert() throws TaskFailedException,
    SQLException {
        final EGovernCommon commommethods = new EGovernCommon();
        created = commommethods.getCurrentDate();
        Query pst = null;
        try {
            /*
             * created = cm.assignValue(formatter.format(sdf.parse( created )),created);
             */
            created = formatter.format(sdf.parse(created));
            setLastModified(created);
            setId(String.valueOf(PrimaryKeyGenerator.getNextKey("BankBranch")));
            setBranchCode(id);
            final String insertQuery = "INSERT INTO bankbranch (Id, BranchCode, BranchName, BranchAddress1, BranchAddress2, "
                    + "BranchCity, BranchState, BranchPin, BranchPhone, BranchFax, BankId, ContactPerson, "
                    + "IsActive, Created, ModifiedBy, LastModified, Narration, MICR) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            if (LOGGER.isDebugEnabled())
                LOGGER.debug(insertQuery);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
            pst.setString(0, id);
            pst.setString(1, branchCode);
            pst.setString(2, branchName);
            pst.setString(3, branchAddress1);
            pst.setString(4, branchAddress2);
            pst.setString(5, branchCity);
            pst.setString(6, branchState);
            pst.setString(7, branchPin);
            pst.setString(8, branchPhone);
            pst.setString(9, branchFax);
            pst.setString(10, bankId);
            pst.setString(11, contactPerson);
            pst.setString(12, isActive);
            pst.setString(13, created);
            pst.setString(14, modifiedBy);
            pst.setString(15, lastModified);
            pst.setString(16, narration);
            pst.setString(17, branchMICR);
            pst.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in insert" + e.getMessage());
            throw taskExc;
        }
    }

    /**
     * This is the update API
     *
     * @param connection
     * @throws TaskFailedException
     * @throws SQLException
     */
    public void update() throws TaskFailedException,
    SQLException {

        newUpdate();

        /*
         * if (isId && isField) { EGovernCommon commommethods = new EGovernCommon(); created =
         * commommethods.getCurrentDate(connection); Query pst = null; try { created = formatter.format(sdf.parse(created));
         * setCreated(created); setLastModified(created); updateQuery = updateQuery .substring(0, updateQuery.length() - 1);
         * updateQuery = updateQuery + " WHERE id = ?"; if(LOGGER.isDebugEnabled()) LOGGER.debug(updateQuery); pst =
         * HibernateUtil.getCurrentSession().createSQLQuery(updateQuery); pst.setString(1, id); pst.executeUpdate(); updateQuery =
         * "UPDATE bankbranch SET"; } catch (Exception e) { LOGGER.error("Exp in update: " + e.getMessage()); throw taskExc; }
         * finally { try { pst.close(); } catch (Exception e) { LOGGER.error("Inside finally block of update"); } } }
         */
    }

    /**
     * This API gives the details for all the branches
     *
     * @param con
     * @return
     * @throws TaskFailedException
     */
    public HashMap getBankBranch() throws TaskFailedException {
        final String query = "SELECT  CONCAT(CONCAT(bankBranch.bankId, '-'),bankBranch.ID) as \"bankBranchID\",concat(concat(bank.name , ' '),bankBranch.branchName) as \"bankBranchName\" FROM bank, bankBranch where"
                + " bank.isactive=true and bankBranch.isactive=true and bank.ID = bankBranch.bankId order by bank.name";
        if (LOGGER.isInfoEnabled())
            LOGGER.info("  query   " + query);
        final Map hm = new LinkedHashMap<String, String>();
        Query pst = null;
        List<Object[]> rs = null;

        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            rs = pst.list();
            for (final Object[] element : rs)
                hm.put(element[0].toString(), element[1].toString());

        } catch (final Exception e) {
            LOGGER.error("Error in getBankBranch ", e);
            throw taskExc;
        }
        return (HashMap) hm;
    }

    /**
     * This API will give the list of Bankaccounts for any Bank branch
     *
     * @param bankBranchId
     * @param con
     * @return
     * @throws Exception
     */
    public HashMap getAccNumber(final String bankBranchId)
            throws TaskFailedException {
        final String id[] = bankBranchId.split("-");
        final String branchId = id[1];
        Query pst = null;
        List<Object[]> rs = null;

        final String query = "SELECT  ID, accountNumber from bankAccount WHERE branchId= ? and isactive=true  ORDER BY ID";
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("  query   " + query);
        final Map hm = new HashMap();
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
            pst.setString(1, branchId);
            rs = pst.list();
            for (final Object[] element : rs)
                hm.put(element[0].toString(), element[1].toString());
        } catch (final Exception e) {
            LOGGER.error("Error in getAccNumber ", e);
            throw taskExc;
        }

        return (HashMap) hm;
    }

    public void newUpdate() throws TaskFailedException,
    SQLException {
        final EGovernCommon commommethods = new EGovernCommon();
        created = commommethods.getCurrentDate();
        try {
            created = formatter.format(sdf.parse(created));
        } catch (final ParseException parseExp) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(parseExp.getMessage(), parseExp);
        }
        setCreated(created);
        setLastModified(created);
        final StringBuilder query = new StringBuilder(500);
        query.append("update BankBranch set ");
        if (branchCode != null)
            query.append("branchCode=?");
        if (branchName != null)
            query.append(",branchName=?");
        if (branchAddress1 != null)
            query.append(",branchAddress1=?");
        if (branchAddress2 != null)
            query.append(",branchAddress2=?");
        if (branchCity != null)
            query.append(",branchCity=?");
        if (branchState != null)
            query.append(",branchState=?");
        if (branchPin != null)
            query.append(",branchPin=?");
        if (branchPhone != null)
            query.append(",branchPhone=?");
        if (branchFax != null)
            query.append(",branchFax=?");
        if (bankId != null)
            query.append(",bankId=?");
        if (contactPerson != null)
            query.append(",contactPerson=?");
        if (isActive != null)
            query.append(",isActive=?");
        if (created != null)
            query.append(",created=?");
        if (modifiedBy != null)
            query.append(",modifiedBy=?");
        if (lastModified != null)
            query.append(",lastModified=?");
        if (narration != null)
            query.append(",narration=?");
        if (branchMICR != null && !branchMICR.isEmpty())
            query.append(",MICR=?");
        query.append(" where id=?");
        Query pstmt = null;
        try {

            int i = 1;
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
            if (branchCode != null)
                pstmt.setString(i++, branchCode);
            if (branchName != null)
                pstmt.setString(i++, branchName);
            if (branchAddress1 != null)
                pstmt.setString(i++, branchAddress1);
            if (branchAddress2 != null)
                pstmt.setString(i++, branchAddress2);
            if (branchCity != null)
                pstmt.setString(i++, branchCity);
            if (branchState != null)
                pstmt.setString(i++, branchState);
            if (branchPin != null)
                pstmt.setString(i++, branchPin);
            if (branchPhone != null)
                pstmt.setString(i++, branchPhone);
            if (branchFax != null)
                pstmt.setString(i++, branchFax);
            if (bankId != null)
                pstmt.setString(i++, bankId);
            if (contactPerson != null)
                pstmt.setString(i++, contactPerson);
            if (isActive != null)
                pstmt.setString(i++, isActive);
            if (created != null)
                pstmt.setString(i++, created);
            if (modifiedBy != null)
                pstmt.setString(i++, modifiedBy);
            if (lastModified != null)
                pstmt.setString(i++, lastModified);
            if (narration != null)
                pstmt.setString(i++, narration);
            if (branchMICR != null && !branchMICR.isEmpty())
                pstmt.setString(i++, branchMICR);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: " + e.getMessage(), e);
            throw taskExc;
        }
    }
}
