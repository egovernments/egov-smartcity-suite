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

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.updateservice.PrimaryKeyGenerator;

@Transactional(readOnly = true)
public class ChartOfAccts {
    private static final Logger LOGGER = Logger.getLogger(ChartOfAccts.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale
            .getDefault());
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
            Locale.getDefault());
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

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private EGovernCommon eGovernCommon;

    @PersistenceContext
    private EntityManager entityManager;

    public String getId() {
        return id;
    }

    public void setId(final String aId) {
        id = aId;
        isId = true;
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
    public void insert(final Connection connection) {
        created = new SimpleDateFormat("dd/mm/yyyy").format(new Date());
        try {
            created = formatter.format(sdf.parse(created));

            setLastModified(created);
            setId(String.valueOf(PrimaryKeyGenerator
                    .getNextKey("ChartOfAccounts")));

            final StringBuilder insertQuery = new StringBuilder("INSERT INTO ChartOfAccounts (id, glCode, name, description,")
                    .append(" isActiveForPosting, parentId, lastModified, modifiedBy,")
                    .append(" created, purposeid, functionreqd, operation, type, classification, class, budgetCheckReq, majorcode)")
                    .append(" values (:id, :glCode, :name, :description, :isActiveForPosting, :parentId, :lastModified,")
                    .append(" :modifiedBy, :created, :purposeid, :functionreqd, :operation,")
                    .append(" :type, :classification, :class, :budgetCheckReq, :majorcode)");
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(insertQuery);

            entityManager.createNativeQuery(insertQuery.toString())
                    .setParameter("id", Integer.parseInt(id))
                    .setParameter("glCode", removeSingleQuotes(glCode))
                    .setParameter("name", removeSingleQuotes(name))
                    .setParameter("description", removeSingleQuotes(description))
                    .setParameter("isActiveForPosting", removeSingleQuotes(isActiveForPosting))
                    .setParameter("parentId", removeSingleQuotes(parentId))
                    .setParameter("lastModified", removeSingleQuotes(lastModified))
                    .setParameter("modifiedBy", removeSingleQuotes(modifiedBy))
                    .setParameter("created", removeSingleQuotes(created))
                    .setParameter("purposeid", removeSingleQuotes(purposeid))
                    .setParameter("functionreqd", removeSingleQuotes(functionreqd))
                    .setParameter("operation", removeSingleQuotes(operation))
                    .setParameter("type", removeSingleQuotes(type))
                    .setParameter("classification", removeSingleQuotes(classification))
                    .setParameter("class", removeSingleQuotes(classname))
                    .setParameter("budgetCheckReq", removeSingleQuotes(budgetCheckReqd))
                    .setParameter("majorcode", removeSingleQuotes(getMajorCode(glCode)))
                    .executeUpdate();
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data  ", new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data  ", new Exception(e.getMessage()));
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
        return glcode1.length() >= majorcodelength ? "'".concat(glcode1.substring(0, majorcodelength)).concat("'") : "''";
    }

    @Transactional
    public void update() throws SQLException, TaskFailedException {
        if (isId && isField)
            newUpdate();
    }

    public void newUpdate() {
        created = eGovernCommon.getCurrentDate();
        try {
            created = formatter.format(sdf.parse(created));
        } catch (final ParseException parseExp) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(parseExp.getMessage(), parseExp);
        }
        setCreated(created);
        setLastModified(created);
        final StringBuilder query = new StringBuilder("update ChartOfAccounts set");
        if (glCode != null)
            query.append(" glCode = :glCode,");
        if (name != null)
            query.append(" name = :name,");
        if (description != null)
            query.append(" description = :description,");
        if (isActiveForPosting != null)
            query.append(" ISACTIVEFORPOSTING = :isActiveForPosting,");
        if (parentId != null)
            query.append(" PARENTID = :parentId,");
        if (lastModified != null)
            query.append(" LASTMODIFIED = :lastModified,");
        if (modifiedBy != null)
            query.append(" MODIFIEDBY = :modifiedBy,");
        if (created != null)
            query.append(" CREATED = :created,");
        if (purposeid != null)
            query.append(" PURPOSEID = :purposeId,");
        if (operation != null)
            query.append(" OPERATION = :operation,");
        if (FIEoperation != null)
            query.append(" FIEOPERATION = :fieOperation,");
        if (type != null)
            query.append(" type = :type,");
        if (classname != null)
            query.append(" class = :class,");
        if (classification != null)
            query.append(" CLASSIFICATION = :classification,");
        if (functionreqd != null)
            query.append(" FUNCTIONREQD = :functionReqd,");
        if (scheduleId != null)
            query.append(" SCHEDULEID = :scheduleId,");
        if (FIEscheduleId != null)
            query.append(" FIEscheduleId = :fieScheduleId,");
        if (receiptscheduleid != null)
            query.append(" RECEIPTSCHEDULEID = :receiptScheduleId,");
        if (receiptoperation != null)
            query.append(" RECEIPTOPERATION = :receiptOperation,");
        if (paymentscheduleid != null)
            query.append(" PAYMENTSCHEDULEID = :paymentScheduleId,");
        if (paymentoperation != null)
            query.append(" PAYMENTOPERATION = :paymentOperation,");
        if (budgetCheckReqd != null)
            query.append(" BUDGETCHECKREQ = :budgetCheckReq,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id = :id");
        try {
            Query nativeQuery = entityManager.createNativeQuery(query.toString());

            if (glCode != null)
                nativeQuery.setParameter("glCode", glCode);
            if (name != null)
                nativeQuery.setParameter("name", name);
            if (description != null)
                nativeQuery.setParameter("description", description);
            if (isActiveForPosting != null)
                nativeQuery.setParameter("isActiveForPosting", isActiveForPosting);
            if (parentId != null)
                nativeQuery.setParameter("parentId", parentId);
            if (lastModified != null)
                nativeQuery.setParameter("lastModified", lastModified);
            if (modifiedBy != null)
                nativeQuery.setParameter("modifiedBy", modifiedBy);
            if (created != null)
                nativeQuery.setParameter("created", created);
            if (purposeid != null)
                nativeQuery.setParameter("purposeId", purposeid);
            if (operation != null)
                nativeQuery.setParameter("operation", operation);
            if (FIEoperation != null)
                nativeQuery.setParameter("fieOperation", FIEoperation);
            if (type != null)
                nativeQuery.setParameter("type", type);
            if (classname != null)
                nativeQuery.setParameter("class", classname);
            if (classification != null)
                nativeQuery.setParameter("classification", classification);
            if (functionreqd != null)
                nativeQuery.setParameter("functionReqd", functionreqd);
            if (scheduleId != null)
                nativeQuery.setParameter("scheduleId", scheduleId);
            if (FIEscheduleId != null)
                nativeQuery.setParameter("fieScheduleId", FIEscheduleId);
            if (receiptscheduleid != null)
                nativeQuery.setParameter("receiptScheduleId", receiptscheduleid);
            if (receiptoperation != null)
                nativeQuery.setParameter("receiptOperation", receiptoperation);
            if (paymentscheduleid != null)
                nativeQuery.setParameter("paymentScheduleId", paymentscheduleid);
            if (paymentoperation != null)
                nativeQuery.setParameter("paymentOperation", paymentoperation);
            if (budgetCheckReqd != null)
                nativeQuery.setParameter("budgetCheckReq", budgetCheckReqd);
            nativeQuery.setParameter("id", id);
            nativeQuery.executeUpdate();
        } catch (final HibernateException e) {
            LOGGER.error("Exception occured while getting the data  ", new HibernateException(e.getMessage()));
        } catch (final Exception e) {
            LOGGER.error("Exception occured while getting the data  ", new Exception(e.getMessage()));
        }
    }
}