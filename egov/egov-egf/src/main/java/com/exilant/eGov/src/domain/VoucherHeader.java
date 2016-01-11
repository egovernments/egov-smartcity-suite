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
 * Created on Jan 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.domain;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;
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
public class VoucherHeader {
    private static final Logger LOGGER = Logger.getLogger(VoucherHeader.class);
    private String id = null;
    /* CG number - unique identifier for the project */
    private String cgn = null;
    /* The system date when the transaction was created */
    private String cgDate = "1-Jan-1900";
    private String name = null;
    private String type = null;
    private String description = null;
    private String effectiveDate = "1-Jan-1900";
    private String voucherNumber = null;
    private String voucherDate = "1-Jan-1900";
    private String departmentId = null;
    private String functionId = null;
    private String fundSourceId = null;
    private String fundId = null;
    private String fiscalPeriodId = null;
    private String status = null;
    private String originalVcId = null;
    private String isConfirmed = null;
    private String createdby = null;
    private String refCgno = null;
    private String cgvn = null;
    private String lastModifiedBy = null;
    private String lastModifiedDate = "1-Jan-1900";
    private String moduleId = null;
    private boolean isId = false, isField = false;
    private static TaskFailedException taskExc;
    EGovernCommon commonmethods = new EGovernCommon();

    public VoucherHeader() {
    }

    public void setId(final String aId) {
        id = aId;
        isId = true;
    }

    public void setCgn(final String aCgn) {
        cgn = aCgn;
        isField = true;
    }

    public void setRefCgn(final String aRefCgn) {
        refCgno = aRefCgn;
        isField = true;
    }

    public void setCgDate(final String aCgDate) {
        cgDate = aCgDate;
        isField = true;
    }

    public void setName(final String aName) {
        name = aName;
        isField = true;
    }

    public void setType(final String aType) {
        type = aType;
        isField = true;
    }

    public void setDescription(final String aDescription) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("voucherheader Description Before:" + aDescription);
        description = aDescription != null ? commonmethods
                .formatString(aDescription) : "";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("voucherheader Description After:" + description);
                if (description.length() == 0)
                    description = "";
                isField = true;
    }

    public void setEffectiveDate(final String aEffectiveDate) {
        effectiveDate = aEffectiveDate;
        isField = true;
    }

    public void setVoucherNumber(final String aVoucherNumber) {
        voucherNumber = aVoucherNumber;

        isField = true;
    }

    public void setVoucherDate(final String aVoucherDate) {
        voucherDate = aVoucherDate;
        isField = true;
    }

    public void setDepartmentId(final String aDepartmentId) {
        departmentId = aDepartmentId;
        if (departmentId.length() == 0)
            departmentId = null;
        isField = true;
    }

    public void setFunctionId(final String aFunctionId) {
        functionId = aFunctionId;
        if (functionId.length() == 0)
            functionId = null;
        isField = true;
    }

    public void setCreatedby(final String aCreatedBy) {
        createdby = aCreatedBy;
        isField = true;
    }

    public void setFundSourceId(final String aFundSourceId) {
        fundSourceId = aFundSourceId;
        if (aFundSourceId != null) {
            if (fundSourceId.length() == 0
                    || fundSourceId.trim().equalsIgnoreCase(""))
                fundSourceId = "null";
            isField = true;
        }
    }

    public void setLastModifiedBy(final String aLastModifiedBy) {
        lastModifiedBy = aLastModifiedBy;
        isField = true;
    }

    public void setLastModifiedDate(final String aLastModifiedDate) {
        lastModifiedDate = aLastModifiedDate;
        isField = true;
    }

    public void setFundId(final String aFundId) {
        fundId = aFundId;

        isField = true;
    }

    public void setFiscalPeriodId(final String afiscalPeriodId) {
        fiscalPeriodId = afiscalPeriodId;
        isField = true;
    }

    public void setStatus(final String aStatus) {
        status = aStatus;
        isField = true;
    }

    public void setOriginalVcId(final String aOriginalVcId) {
        originalVcId = aOriginalVcId;
        isField = true;
    }

    public void setIsConfirmed(final String aIsConfirmed) {
        isConfirmed = aIsConfirmed;
        isField = true;
    }

    public void setCgvn(final String aCgvn) {
        cgvn = aCgvn;
        isField = true;
    }

    public void setModuleId(final String aModuleid) {
        moduleId = aModuleid;
        isField = true;
    }

    public int getId() {
        return Integer.valueOf(id).intValue();
    }

    public String getCgn() {
        return cgn;
    }

    public String getRefCgn() {
        return refCgno;
    }

    public String getCgDate() {
        return cgDate;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public String getFundId() {
        return fundId;
    }

    public String getFundSourceId() {
        return fundSourceId;
    }

    public String getFiscalPeriodId() {
        return fiscalPeriodId;
    }

    public String getStatus() {
        return status;
    }

    public String getOriginalVcId() {
        return originalVcId;
    }

    public String getCreatedby() {
        return createdby;
    }

    public String getCgvn() {
        return cgvn;
    }

    public String getlastModifiedBy() {
        return lastModifiedBy;
    }

    public String getlastModifiedDate() {
        return lastModifiedDate;
    }

    public String getModuleId() {
        return moduleId;
    }

    @Transactional
    public void insert() throws SQLException,
    TaskFailedException {
        final String code = EGovConfig.getProperty("egf_config.xml",
                "confirmoncreate", "", "JournalVoucher");// get from the config
        // file
        // If its N we need to confirm on create.
        if (!getStatus().equals("2")) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("status   " + getStatus());
            if (code.equalsIgnoreCase("N"))
                isConfirmed = "1";
            else
                isConfirmed = "0";
        }

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh departmentId   " + departmentId);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh functionId   " + functionId);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh fundId   " + fundId);

        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh fundSourceId   " + fundSourceId);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh fiscalPeriodId   " + fiscalPeriodId);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh originalVcId  " + originalVcId);
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Inside vh moduleId  " + moduleId);

        if (departmentId == null || departmentId.length() == 0)
            departmentId = null;

        if (functionId == null || functionId.length() == 0)
            functionId = null;

        if (fundId == null || fundId.length() == 0)
            fundId = null;

        // if(fundSourceId != null && fundSourceId.length()==0)
        if (fundSourceId == null || fundSourceId.length() == 0)
            fundSourceId = null;

        if (fiscalPeriodId == null || fiscalPeriodId.length() == 0)
            fiscalPeriodId = null;

        if (originalVcId == null || originalVcId.length() == 0)
            originalVcId = null;

        if (moduleId == null || moduleId.length() == 0)
            moduleId = null;

        effectiveDate = commonmethods.getCurrentDateTime();

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm:ss");
            effectiveDate = formatter.format(sdf.parse(effectiveDate));
            setCgDate(effectiveDate);
        } catch (final Exception e) {
            LOGGER.error("Exp in insert" + e.getMessage(), e);
            throw taskExc;
        }

        setId(String.valueOf(PrimaryKeyGenerator.getNextKey("VoucherHeader")));

        description = commonmethods.formatString(description);
        name = commonmethods.formatString(name);
        Query pst = null;
        try {
            lastModifiedDate = commonmethods.getCurrentDateTime();
            Date dt = new Date();
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm:ss");
            dt = sdf.parse(lastModifiedDate);
            lastModifiedDate = formatter.format(dt);

            final String insertQuery = "INSERT INTO voucherheader (Id, CGN, CGDate, Name, Type, Description, "
                    + "EffectiveDate,VoucherNumber,VoucherDate,DepartmentId, FundId,fiscalPeriodId,status,originalVcId,fundSourceid, isConfirmed,createdby, FunctionId,refcgno,cgvn,moduleid,LASTMODIFIEDDATE) "
                    + "VALUES ( ?, ?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?, to_date(?,'dd-Mon-yyyy HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,to_date(?,'dd-Mon-yyyy HH24:MI:SS'))";

            if (LOGGER.isInfoEnabled())
                LOGGER.info(insertQuery);
            pst = HibernateUtil.getCurrentSession().createSQLQuery(insertQuery);
            pst.setString(0, id);
            pst.setString(1, cgn);
            pst.setString(2, cgDate);
            pst.setString(3, name);
            pst.setString(4, type);
            pst.setString(5, description);
            pst.setString(6, effectiveDate);
            pst.setString(7, voucherNumber.toUpperCase());
            pst.setString(8, voucherDate);
            pst.setString(9, departmentId);
            pst.setString(10, fundId);
            pst.setString(11, fiscalPeriodId);
            pst.setString(12, status);
            pst.setString(13, originalVcId);
            pst.setString(14, fundSourceId);
            pst.setString(15, isConfirmed);
            pst.setString(16, createdby);
            pst.setString(17, functionId);
            pst.setString(18, refCgno);
            pst.setString(19, cgvn);
            pst.setString(20, moduleId);
            pst.setString(21, lastModifiedDate);
            pst.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exception in insert " + e.getMessage(), e);
            throw taskExc;
        }

    }

    @Transactional
    public void update() throws SQLException,
    TaskFailedException {
        lastModifiedDate = commonmethods.getCurrentDateTime();

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy");
            lastModifiedDate = formatter.format(sdf.parse(lastModifiedDate));
        } catch (final Exception e) {
            LOGGER.error("Exception in update " + e.getMessage(), e);
            throw taskExc;
        }

        if (isId && isField)
            /*
             * description = commonmethods.formatString(description); name = commonmethods.formatString(name);
             */
            newUpdate();
    }

    public void newUpdate() throws TaskFailedException,
    SQLException {
        new EGovernCommon();
        Query pstmt = null;
        if (description != null)
            description = commonmethods.formatString(description);
        /* name = commonmethods.formatString(name); */
        setLastModifiedDate(lastModifiedDate);
        final StringBuilder query = new StringBuilder(500);
        query.append("update VoucherHeader set ");
        if (cgn != null)
            query.append("cgn=?,");
        if (cgDate != null && !cgDate.equalsIgnoreCase("1-Jan-1900"))
            query.append("cgDate=?,");
        if (name != null)
            query.append("name=?,");
        if (type != null)
            query.append("type=?,");
        if (description != null)
            query.append("description=?,");
        if (effectiveDate != null && !effectiveDate.equalsIgnoreCase("1-Jan-1900"))
            query.append("effectiveDate=?,");
        if (voucherNumber != null)
            query.append("voucherNumber=?,");
        if (voucherDate != null && !voucherDate.equalsIgnoreCase("1-Jan-1900"))
            query.append("voucherDate=?,");
        if (departmentId != null)
            query.append("departmentId=?,");
        if (functionId != null)
            query.append("functionId=?,");
        if (fundSourceId != null)
            query.append("fundSourceId=?,");
        if (fundId != null)
            query.append("fundId=?,");
        if (fiscalPeriodId != null)
            query.append("fiscalPeriodId=?,");
        if (status != null)
            query.append("status=?,");
        if (originalVcId != null)
            query.append("originalVcId=?,");
        if (isConfirmed != null)
            query.append("isConfirmed=?,");
        if (createdby != null)
            query.append("createdby=?,");
        if (refCgno != null)
            query.append("refCgno=?,");
        if (cgvn != null)
            query.append("cgvn=?,");
        if (lastModifiedBy != null)
            query.append("lastModifiedBy=?,");
        if (lastModifiedDate != null && !lastModifiedDate.equalsIgnoreCase("1-Jan-1900"))
            query.append("lastModifiedDate=?,");
        if (moduleId != null)
            query.append("moduleId=?,");
        final int lastIndexOfComma = query.lastIndexOf(",");
        query.deleteCharAt(lastIndexOfComma);
        query.append(" where id=?");
        try {
            int i = 1;
            pstmt = HibernateUtil.getCurrentSession().createSQLQuery(query.toString());
            if (cgn != null)
                pstmt.setString(i++, cgn);
            if (cgDate != null && !cgDate.equalsIgnoreCase("1-Jan-1900"))
                pstmt.setString(i++, cgDate);
            if (name != null)
                pstmt.setString(i++, name);
            if (type != null)
                pstmt.setString(i++, type);
            if (description != null)
                pstmt.setString(i++, description);
            if (effectiveDate != null && !effectiveDate.equalsIgnoreCase("1-Jan-1900"))
                pstmt.setString(i++, effectiveDate);
            if (voucherNumber != null)
                pstmt.setString(i++, voucherNumber);
            if (voucherDate != null && !voucherDate.equalsIgnoreCase("1-Jan-1900"))
                pstmt.setString(i++, voucherDate);
            if (departmentId != null)
                pstmt.setString(i++, departmentId);
            if (functionId != null)
                pstmt.setString(i++, functionId);
            if (fundSourceId != null)
                pstmt.setString(i++, fundSourceId);
            if (fundId != null)
                pstmt.setString(i++, fundId);
            if (fiscalPeriodId != null)
                pstmt.setString(i++, fiscalPeriodId);
            if (status != null)
                pstmt.setString(i++, status);
            if (originalVcId != null)
                pstmt.setString(i++, originalVcId);
            if (isConfirmed != null)
                pstmt.setString(i++, isConfirmed);
            if (createdby != null)
                pstmt.setString(i++, createdby);
            if (refCgno != null)
                pstmt.setString(i++, refCgno);
            if (cgvn != null)
                pstmt.setString(i++, cgvn);
            if (lastModifiedBy != null)
                pstmt.setString(i++, lastModifiedBy);
            if (lastModifiedDate != null && !lastModifiedDate.equalsIgnoreCase("1-Jan-1900"))
                pstmt.setString(i++, lastModifiedDate);
            if (moduleId != null)
                pstmt.setString(i++, moduleId);
            pstmt.setString(i++, id);

            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOGGER.error("Exp in update: " + e.getMessage(), e);
            throw taskExc;
        } finally {
            try {
            } catch (final Exception e) {
                LOGGER.error("Inside finally block of update" + e.getMessage());
            }
        }
    }

    public void cancelVouchers(final String cgvn) throws Exception {

        final String query = "select id from voucherheader where cgvn= ?";
        final Query pst = HibernateUtil.getCurrentSession().createSQLQuery(query);
        pst.setString(0, cgvn);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(query);
        final List<Object[]> resultset = pst.list();
        String vid = "";
        for (final Object[] element : resultset)
            vid = element[0].toString();
        if (resultset == null || resultset.size() == 0)
            throw new Exception("Voucher does not exist.");

        Query ps = null;
        List<Object[]> rs = null;
        final VoucherHeader vh = new VoucherHeader();
        final String getRefVoucher = "SELECT a.id,a.vouchernumber,a.cgn FROM voucherheader a,voucherheader b "
                + "WHERE a.CGN=b.REFCGNO AND b.id=?";

        if (LOGGER.isInfoEnabled())
            LOGGER.info("getRefVoucher   " + getRefVoucher);
        ps = HibernateUtil.getCurrentSession().createSQLQuery(getRefVoucher);
        vh.setId(vid);
        new SimpleDateFormat("dd/MM/yyyy");
        new SimpleDateFormat("dd-MMM-yyyy");
        final EGovernCommon cm = new EGovernCommon();
        cm.getCurrentDate();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Update the egf_record_status table of original voucher");
        /*
         * egfstatus.setEffectiveDate(formatter.format(sdf.parse(today))); egfstatus.setStatus("4");
         * egfstatus.setVoucherheaderId(vid); egfstatus.update();
         */
        if (LOGGER.isInfoEnabled())
            LOGGER.info("Update the original voucher");
        vh.setStatus("" + 4);
        vh.update();

        // Check if there is any related vouchers
        ps.setString(0, vid);
        rs = ps.list();
        // if(LOGGER.isInfoEnabled()) LOGGER.info("if any related vouchers exist then we need to  that also.");
        for (final Object[] element : rs) {
            // egfRecordStatus egfstatusRef = new egfRecordStatus();
            final String refVhid = element[0].toString();
            vh.setId(refVhid);
            /*
             * egfstatusRef.setEffectiveDate(formatter.format(sdf.parse(today))); egfstatusRef.setStatus("4");
             * egfstatusRef.setVoucherheaderId(refVhid); egfstatusRef.update();
             */
            vh.setStatus("" + 4);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("before voucher update");
            vh.update();
        }
    }

}
