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
 * Created on Mar 4, 2005
 * @author pushpendra.singh
 */

package com.exilant.eGov.src.domain;


import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Transactional(readOnly = true)
public class ChartOfAccts {
    private String id = null;
    private String glCode = null;
    private String name = null;
    private String description = null;
    private String isActiveForPosting = "0";
    private String parentId = null;
    private String lastModified = "";
    private String modifiedBy = null;
    private String created = "";
    private String purposeid = null;
    private String operation = null;
    private String type = null;
    private String classname = "0";
    private String classification = null;
    private String functionreqd = "0";
    private String scheduleId = null;
    private Integer FIEscheduleId = null;
    private String FIEoperation = null;
    private String receiptscheduleid = null;
    private String receiptoperation = null;
    private String paymentscheduleid = null;
    private String paymentoperation = null;
    private String budgetCheckReqd = null;
    private boolean isId = false, isField = false;
    private static final Logger LOGGER = Logger.getLogger(ChartOfAccts.class);
   
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;
 @Autowired
    private AppConfigValueService appConfigValuesService;
    
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    private @Autowired EGovernCommon eGovernCommon;
    
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

    public void setGLCode(final String aGLCode) {
        glCode = aGLCode;
        isField = true;
    }

    public void setName(final String aName) {
        name = aName;
        isField = true;
    }

    public void setDescription(final String aDescription) {
        description = aDescription;
        isField = true;
    }

    public void setIsActiveForPosting(final String aIsActiveForPosting) {
        isActiveForPosting = aIsActiveForPosting;
        isField = true;
    }

    public void setParentId(final String aParentId) {
        parentId = aParentId;
        isField = true;
    }

    public void setLastModified(final String aLastModified) {
        lastModified = aLastModified;
        isField = true;
    }

    public void setModifiedBy(final String aModifiedBy) {
        modifiedBy = aModifiedBy;
        isField = true;
    }

    public void setCreated(final String aCreated) {
        created = aCreated;
        isField = true;
    }

    public void setOperation(final String aOperation) {
        operation = aOperation;
        isField = true;
    }

    public void setType(final String aType) {
        type = aType;
        isField = true;
    }

    public void setClass(final String aclass) {
        classname = aclass;
        isField = true;
    }

    public void setPurposeId(final String aPurposeId) {
        purposeid = aPurposeId;
        isField = true;
    }

    public void setFunctionReqd(final String aFunctionReqd) {
        functionreqd = aFunctionReqd;
        isField = true;
    }

    public void setClassification(final String aClassification) {
        classification = aClassification;
        isField = true;
    }

    public void setScheduleId(final String aScheduleId) {
        scheduleId = aScheduleId;
        isField = true;
    }

    public void setReceiptscheduleid(final String aReceiptscheduleid) {
        receiptscheduleid = aReceiptscheduleid;
        isField = true;
    }

    public void setReceiptoperation(final String aReceiptoperation) {
        receiptoperation = aReceiptoperation;
        isField = true;
    }

    public void setPaymentscheduleid(final String aPaymentscheduleid) {
        paymentscheduleid = aPaymentscheduleid;
        isField = true;
    }

    public void setPaymentoperation(final String aPaymentoperation) {
        paymentoperation = aPaymentoperation;
        isField = true;
    }

    public void setBudgetCheckReqd(final String aBudgetCheckReqd) {
        budgetCheckReqd = aBudgetCheckReqd;
        isField = true;
    }

    public Integer getFIEscheduleId() {
        return FIEscheduleId;
    }

    public void setFIEscheduleId(final int escheduleId) {
        FIEscheduleId = escheduleId;
        isField = true;
    }

    public String getFIEoperation() {
        return FIEoperation;

    }

    public void setFIEoperation(final String eoperation) {
        FIEoperation = eoperation;
        isField = true;
    }

    @Transactional
    public void insert(final Connection connection) throws SQLException,
    TaskFailedException {
        created = new SimpleDateFormat("dd/mm/yyyy").format(new Date());
        try {
            created = formatter.format(sdf.parse(created));
            masterDataCache.removeFromCache(
                    "egi-activeCoaCodes");
            masterDataCache.removeFromCache("egi-coaCodes");
            masterDataCache.removeFromCache(
                    "egi-chartOfAccounts");
            masterDataCache.removeFromCache(
                    "egi-coaPurposeId10");
            masterDataCache.removeFromCache(
                    "egi-accountCodes");
            masterDataCache.removeFromCache(
                    "egi-liabilityCOACodes");
            masterDataCache.removeFromCache(
                    "egi-coaCodesForLiability");
            setLastModified(created);
            setId(String.valueOf(PrimaryKeyGenerator
                    .getNextKey("ChartOfAccounts")));

            final String insertQuery = "INSERT INTO ChartOfAccounts (id, glCode, name, description, isActiveForPosting, "
                    + " parentId, lastModified, modifiedBy, "
                    + "created,  purposeid,functionreqd, operation,type,classification,class,budgetCheckReq,majorcode)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(insertQuery);

            persistenceService.getSession().createSQLQuery(insertQuery)
            .setInteger(0, Integer.parseInt(id))
            .setString(1, removeSingleQuotes(glCode))
            .setString(2, removeSingleQuotes(name))
            .setString(3, removeSingleQuotes(description))
            .setString(4, removeSingleQuotes(isActiveForPosting))
            .setString(5, removeSingleQuotes(parentId))
            .setString(6, removeSingleQuotes(lastModified))
            .setString(7, removeSingleQuotes(modifiedBy))
            .setString(8, removeSingleQuotes(created))
            .setString(9, removeSingleQuotes(purposeid))
            .setString(10, removeSingleQuotes(functionreqd))
            .setString(11, removeSingleQuotes(operation))
            .setString(12, removeSingleQuotes(type))
            .setString(13, removeSingleQuotes(classification))
            .setString(14, removeSingleQuotes(classname))
            .setString(15, removeSingleQuotes(budgetCheckReqd))
            .setString(16, removeSingleQuotes(getMajorCode(glCode))).executeUpdate();
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new Exception(e.getMessage()));
        }
    }

    private String removeSingleQuotes(String obj) {
        if (obj != null)
            obj = obj.replaceAll("'", "");
        return obj;

    }

    /**
     * glcode contains comma also
     *
     * @param glcode
     * @return
     * @throws TaskFailedException
     */
    private String getMajorCode(final String glcode) throws TaskFailedException {
        final List<AppConfigValues> appList = appConfigValuesService.getConfigValuesByModuleAndKey("EGF",
                "coa_majorcode_length");
        if (appList == null || appList.isEmpty())
            throw new TaskFailedException(
                    "coa_majorcode_length is not defined in appconfig");
        final int majorcodelength = Integer.valueOf(appList.get(0).getValue());
        final String glcode1 = glcode.substring(0, glcode.length());
        return glcode1.length() >= majorcodelength ? "'"
                + glcode1.substring(0, majorcodelength) + "'" : "''";
    }

    @Transactional
    public void update() throws SQLException,
    TaskFailedException {
        if (isId && isField)
            newUpdate();
    }

    public void newUpdate() throws TaskFailedException,
    SQLException {
        created = eGovernCommon.getCurrentDate();
        Query pstmt = null;
        try {
            created = formatter.format(sdf.parse(created));
        } catch (final ParseException parseExp) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(parseExp.getMessage(), parseExp);
        }
        setCreated(created);
        setLastModified(created);
        final StringBuilder query = new StringBuilder(500);
        query.append("update ChartOfAccounts set ");
        if (glCode != null)
            query.append("glCode=?,");
        if (name != null)
            query.append("name=?,");
        if (description != null)
            query.append("description=?,");
        if (isActiveForPosting != null)
            query.append("ISACTIVEFORPOSTING=?,");
        if (parentId != null)
            query.append("PARENTID=?,");
        if (lastModified != null)
            query.append("LASTMODIFIED=?,");
        if (modifiedBy != null)
            query.append("MODIFIEDBY=?,");
        if (created != null)
            query.append("CREATED=?,");
        if (purposeid != null)
            query.append("PURPOSEID=?,");
        if (operation != null)
            query.append("OPERATION=?,");
        if (FIEoperation != null)
            query.append("FIEOPERATION=?,");
        if (type != null)
            query.append("type=?,");
        if (classname != null)
            query.append("class=?,");
        if (classification != null)
            query.append("CLASSIFICATION=?,");
        if (functionreqd != null)
            query.append("FUNCTIONREQD=?,");
        if (scheduleId != null)
            query.append("SCHEDULEID=?,");
        if (FIEscheduleId != null)
            query.append("FIEscheduleId=?,");
        if (receiptscheduleid != null)
            query.append("RECEIPTSCHEDULEID=?,");
        if (receiptoperation != null)
            query.append("RECEIPTOPERATION=?,");
        if (paymentscheduleid != null)
            query.append("PAYMENTSCHEDULEID=?,");
        if (paymentoperation != null)
            query.append("PAYMENTOPERATION=?,");
        if (budgetCheckReqd != null)
            query.append("BUDGETCHECKREQ=?,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id=?");

        try {
            int i = 1;
            pstmt = persistenceService.getSession().createSQLQuery(query.toString());

            if (glCode != null)
                pstmt.setString(i++, glCode);
            if (name != null)
                pstmt.setString(i++, name);
            if (description != null)
                pstmt.setString(i++, description);
            if (isActiveForPosting != null)
                pstmt.setString(i++, isActiveForPosting);
            if (parentId != null)
                pstmt.setString(i++, parentId);
            if (lastModified != null)
                pstmt.setString(i++, lastModified);
            if (modifiedBy != null)
                pstmt.setString(i++, modifiedBy);
            if (created != null)
                pstmt.setString(i++, created);
            if (purposeid != null)
                pstmt.setString(i++, purposeid);
            if (operation != null)
                pstmt.setString(i++, operation);
            if (FIEoperation != null)
                pstmt.setString(i++, FIEoperation);
            if (type != null)
                pstmt.setString(i++, type);
            if (classname != null)
                pstmt.setString(i++, classname);
            if (classification != null)
                pstmt.setString(i++, classification);
            if (functionreqd != null)
                pstmt.setString(i++, functionreqd);
            if (scheduleId != null)
                pstmt.setString(i++, scheduleId);
            if (FIEscheduleId != null)
                pstmt.setInteger(i++, FIEscheduleId);
            if (receiptscheduleid != null)
                pstmt.setString(i++, receiptscheduleid);
            if (receiptoperation != null)
                pstmt.setString(i++, receiptoperation);
            if (paymentscheduleid != null)
                pstmt.setString(i++, paymentscheduleid);
            if (paymentoperation != null)
                pstmt.setString(i++, paymentoperation);
            if (budgetCheckReqd != null)
                pstmt.setString(i++, budgetCheckReqd);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data  " + e.getMessage(), new Exception(e.getMessage()));
        }
    }

}