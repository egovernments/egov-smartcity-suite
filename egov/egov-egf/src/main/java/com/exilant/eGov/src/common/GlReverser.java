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
 * Created on Jun 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import com.exilant.eGov.src.domain.ClosedPeriods;
import com.exilant.eGov.src.domain.GeneralLedger;
import com.exilant.eGov.src.domain.GeneralLedgerDetail;
import com.exilant.eGov.src.domain.VoucherHeader;
import com.exilant.eGov.src.domain.VoucherMIS;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DataExtractor;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class GlReverser {
    Map vcHeader;
    Map vcDetail;
    Map gLedger;
    Map gLedgerDetails;
    Map egfDetail;
    Map vmis;
    // private String fieldID=null;
    public String refcgvn = null;
    private String newCessCGN = null;
    private static final Logger LOGGER = Logger.getLogger(GlReverser.class);
    private static TaskFailedException taskExc;

    public GlReverser() {

    }

    public void reverse(final String cgNo, final String newVcno, final String effDate, final String cgvNo, final DataCollection dc)
            throws TaskFailedException,
    SQLException {
        // ClosedPeriods cp=new ClosedPeriods();
        if (ClosedPeriods.isClosedForPosting(effDate))
            throw new TaskFailedException("Period Is Closed");
        if (isReconciled(cgNo))
            throw new TaskFailedException("Reconciliation Done Cant Reverse");
        final EGovernCommon cm = new EGovernCommon();
        try {
            if (!cm.isUniqueVN(newVcno, effDate, dc))
                throw new TaskFailedException();
        } catch (final Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            loadData(cgNo);
            final String userId = dc.getValue("current_UserID");
            final String newVhId = postInVoucherHeader(cgNo, newVcno, effDate, cgvNo, userId);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Inserted to VH,VMIS,VR");
            // postInVoucherDetail(newVhId,con);
            // postInVoucherDetail(newVhId);
            postInGeneralLedger(newVhId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("inserted into vD newVhId=" + newVhId);
        } catch (final Exception e) {
            LOGGER.error("Exception in Reverse():" + e.toString());
            throw taskExc;
        }

    }

    // Reversing Voucher without dc
    public void reverse(final String cgNo, final String newVcno, final String effDate, final String cgvNo, final int userId)
            throws TaskFailedException,
    SQLException
    {
        if (ClosedPeriods.isClosedForPosting(effDate))
            throw new TaskFailedException("Period Is Closed");
        if (isReconciled(cgNo))
            throw new TaskFailedException("Reconciliation Done Cant Reverse");
        final EGovernCommon cm = new EGovernCommon();
        try {
            if (!cm.isUniqueVN(newVcno, effDate))
                throw new TaskFailedException("Error: Duplicate Voucher Number");
        } catch (final Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try
        {
            loadData(cgNo);
            final String newVhId = postInVoucherHeader(cgNo, newVcno, effDate, cgvNo, Integer.toString(userId));
            // postInVoucherDetail(newVhId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("inserted into vD newVhId=" + newVhId);
        } catch (final Exception e)
        {
            LOGGER.error("Exception in Reverse():" + e.toString());
            throw taskExc;
        }
    }

    private void loadData(final String cgNo) throws TaskFailedException {
        final String vhSql = "select ID as \"id\" ,CGN as \"cgn\",CGDATE as \"cgDate\",NAME AS \"name\","
                +
                "TYPE as \"type\",DESCRIPTION as \"description\",EFFECTIVEDATE as \"effectiveDate\", "
                +
                "VOUCHERNUMBER as \"voucherNumber\",VOUCHERDATE as \"voucherDate\", "
                +
                "DEPARTMENTID as \"departmentId\",FUNDID as \"fundId\" ,FUNDSOURCEID as \"fundSourceId\",FISCALPERIODID as \"fiscalPeriodId\" ,"
                +
                "STATUS as \"status\",ORIGINALVCID as \"originalVcId\", cgvn as \"cgvn\" from VOUCHERHEADER where CGN='" + cgNo
                + "'";
        final String glSql = "select ID as \"id\" , VOUCHERLINEID as \"voucherLineId\", EFFECTIVEDATE as  \"effectiveDate\" ,GLCODEID as \"glCodeId\", GLCODE as \"glCode\","
                +
                "DEBITAMOUNT as \"debitAmount\",CREDITAMOUNT as \"creditAmount\", DESCRIPTION as \"description\", VOUCHERHEADERID as \"voucherHeaderId\",FUNCTIONID as \"functionId\" from GENERALLEDGER where VOUCHERHEADERID=";
        final String vmisSql = "Select ID as \"id\" ,voucherheaderid as \"voucherheaderid\", divisionid as \"divisionId\", departmentid as \"departmentId\", segmentid as \"segmentId\", sub_segmentid as \"subSegmentId\", fundsourceid as \"fundsourceid\", schemeid as \"scheme\", subschemeid as \"subscheme\",functionaryId as \"functionary\"  from vouchermis where voucherheaderid=";

        final String extraSQL = " order by GLCODE ";
        final DataExtractor de = DataExtractor.getExtractor();
        // load voucher header record for the cgn given
        vcHeader = de.extractIntoMap(vhSql, "cgn", VoucherHeader.class);
        if (vcHeader == null || vcHeader.size() == 0)
            throw new TaskFailedException("Cant Reverse : Record Not Found For CGN Provided" + vhSql);
        final VoucherHeader vhObj = (VoucherHeader) vcHeader.get(cgNo);
        final String vcHID = String.valueOf(vhObj.getId());
        // load vouchermis
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Calling extraxt map for VoucherMIS  :" + vmisSql + vcHID);
        vmis = de.extractIntoMap(vmisSql + vcHID, "voucherheaderid", VoucherMIS.class);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Object for voucherMIS" + vmis);

        // load voucher detail records for that header
        // vcDetail=de.extractIntoMap(vdSql+vcHID+extraSQL,"id",VoucherDetail.class);
        // load General Ledger
        gLedger = de.extractIntoMap(glSql + vcHID + extraSQL, "voucherLineId", GeneralLedger.class);
        // egfDetail=de.extractIntoMap(egfSql+vcHID,"id",egfRecordStatus.class);
    }

    private String postInVoucherHeader(final String cgNo, final String newVcNo, final String effDate, final String cgvNo,
            final String uId) throws Exception {
        String today;
        // String oldVdId="";
        final VoucherHeader vhObj = (VoucherHeader) vcHeader.get(cgNo);
        // egfRecordStatus egfstatus = new egfRecordStatus();
        final EGovernCommon egCom = new EGovernCommon();
        if (vhObj.getStatus().equals("1"))
            throw new TaskFailedException("Already Reversed");
        else if (vhObj.getStatus().equals("2"))
            throw new TaskFailedException("It is a Reversed Entry");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("UPDATING VH");
        // update the old one with status as reversed
        vhObj.setId(String.valueOf(vhObj.getId()));
        // oldVdId=String.valueOf(vhObj.getId());
        vhObj.setStatus("1");
        vhObj.update();

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final EGovernCommon cm = new EGovernCommon();
        today = cm.getCurrentDateTime();
        final String dateformat = formatter.format(sdf.parse(today));
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("dateformat is  " + dateformat);

        // insert the old one with status as reversed for egf_record_status
        /*
         * egfstatus.setVoucherheaderId(String.valueOf(vhObj.getId())); if(LOGGER.isDebugEnabled()) LOGGER.debug("vhobj type "+
         * vhObj.getType()); egfstatus.setStatus("2"); egfstatus.setRecord_Type(vhObj.getName()+" "+vhObj.getType());
         * egfstatus.setEffectiveDate(dateformat); egfstatus.setUserId(uId); //egfstatus.update(con); egfstatus.insert();
         */
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("inserted in  EGF as status 2");

        // insert the old one with status as new
        final String newCgNo = cgNo.substring(0, 3) + egCom.getCGNumber();
        newCessCGN = newCgNo;
        vhObj.setCgn(newCgNo);
        vhObj.setVoucherNumber(newVcNo);
        vhObj.setVoucherDate(effDate);
        if (vhObj.getFiscalPeriodId() == null || vhObj.getFiscalPeriodId().length() == 0)
            vhObj.setFiscalPeriodId("null");
        final String oldVHID = String.valueOf(vhObj.getId());
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("vhObj.getVoucherDate()=========" + vhObj.getVoucherDate());
        vhObj.setFiscalPeriodId(cm.getFiscalPeriod(vhObj.getVoucherDate()));
        vhObj.setStatus("2");
        vhObj.setOriginalVcId(String.valueOf(vhObj.getId()));
        vhObj.setCgvn(cgvNo);
        if (refcgvn != null && !refcgvn.equalsIgnoreCase("") && !refcgvn.equalsIgnoreCase("CESS"))
        {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Setting refcgvn");
            vhObj.setRefCgn(refcgvn);
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("INSERTING VOUCHERHEADER");
        vhObj.setCreatedby(uId);
        vhObj.insert();

        /*
         * // insert the old one with status as cancelled for egf_record_status egfRecordStatus egfstatus1 = new
         * egfRecordStatus(); egfstatus1.setVoucherheaderId(String.valueOf(vhObj.getId()));
         * egfstatus1.setRecord_Type(vhObj.getName()+" "+vhObj.getType()); egfstatus1.setStatus("3");
         * egfstatus1.setEffectiveDate(formatter.format(sdf.parse(today))); egfstatus1.setUserId(uId); if(LOGGER.isDebugEnabled())
         * LOGGER.debug("INSERTING VOUCHER STATUS"); egfstatus1.insert();
         */

        // Post to VoucherMIS table
        // if(fieldID != null)
        if (vmis.get(oldVHID) != null)
        {
            final VoucherMIS vmObj = (VoucherMIS) vmis.get(oldVHID);
            // VoucherMIS vmObj = new VoucherMIS();
            vmObj.setDivisionId(vmObj.getDivisionId());
            vmObj.setVoucherheaderid(String.valueOf(vhObj.getId()));
            vmObj.setDepartmentId(vmObj.getDepartmentId());
            vmObj.setSegmentId(vmObj.getSegmentId());
            vmObj.setSubSegmentId(vmObj.getSubSegmentId());
            vmObj.setFundsourceid(vmObj.getFundsourceid());
            vmObj.setScheme(vmObj.getScheme());
            vmObj.setSubscheme(vmObj.getSubscheme());
            vmObj.setCreateTimeStamp(formatter.format(sdf.parse(today)));
            vmObj.setFunctionary(vmObj.getFunctionary());
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("INSERTING VOUCHERMIS");
            vmObj.insert();
        }

        return String.valueOf(vhObj.getId());
    }

    private void postInGeneralLedger(final String newVhId) throws TaskFailedException {
        try {
            final Iterator it = gLedger.keySet().iterator();
            int voucherlineid = 1;
            String oldGlId = "";
            // String glDetAmt="0";
            while (it.hasNext()) {
                final GeneralLedger glObj = (GeneralLedger) gLedger.get(it.next());
                if (Double.parseDouble(glObj.getDebitAmount()) > 0) {
                    glObj.setCreditAmount(glObj.getDebitAmount());
                    glObj.setDebitAmount("0");
                    // glDetAmt=glObj.getcreditAmount();
                } else if (Double.parseDouble(glObj.getCreditAmount()) > 0) {
                    glObj.setDebitAmount(glObj.getCreditAmount());
                    glObj.setCreditAmount("0");
                    // glDetAmt=glObj.getdebitAmount();
                }
                glObj.setVoucherLineId(String.valueOf(voucherlineid++));
                glObj.setVoucherHeaderId(newVhId);
                oldGlId = String.valueOf(glObj.getId());
                if (!(glObj.getFunctionId() == null || glObj.getFunctionId().equals("")))
                    glObj.setFunctionId(glObj.getFunctionId());
                else
                    glObj.setFunctionId(null);
                glObj.insert();
                postInGLDetail(String.valueOf(glObj.getId()), oldGlId);
            }
        } catch (final Exception e) {
            LOGGER.error("Exp here" + e.toString());
            throw taskExc;
        }
    }

    private void postInGLDetail(final String glID, final String oldGlId) throws TaskFailedException, SQLException {
        final String glDSql = "select ID as \"id\",GENERALLEDGERID as \"glId\" ,DETAILKEYID as \"detailKeyId\",  DETAILTYPEID as \"detailTypeId\" ,AMOUNT as \"detailAmt\" from GENERALLEDGERDETAIL where GENERALLEDGERID="
                + oldGlId;
        final DataExtractor de = DataExtractor.getExtractor();
        gLedgerDetails = de.extractIntoMap(glDSql, "id", GeneralLedgerDetail.class);
        final Iterator it = gLedgerDetails.keySet().iterator();
        while (it.hasNext()) {
            final GeneralLedgerDetail glDObj = (GeneralLedgerDetail) gLedgerDetails.get(it.next());
            glDObj.setGLId(glID);
            // glDObj.setDetailAmt(glDetAmt);// by mani added in the glDSql query for new subledger screen with multiple entities
            glDObj.insert();
        }
    }

    public void reverseSubLedger(final String cgNo, final String newVcno, final String effDate, final String cgvNo,
            final DataCollection dc)
            throws TaskFailedException, SQLException {
        // ClosedPeriods cp=new ClosedPeriods();
        if (ClosedPeriods.isClosedForPosting(effDate))
            throw new TaskFailedException("Period Is Closed");
        if (isReconciled(cgNo))
            throw new TaskFailedException("Reconciliation Done Cant Reverse");
        final EGovernCommon cm = new EGovernCommon();
        try {
            if (!cm.isUniqueVN(newVcno, effDate, dc))
                throw new TaskFailedException();
        } catch (final Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            // ClosedPeriods cp=new ClosedPeriods();
            if (ClosedPeriods.isClosedForPosting(effDate))
                throw new TaskFailedException("Period Is Closed");

            if (isReconciled(cgNo))
                throw new TaskFailedException("Reconciliation Done Cant Reverse");

            if (!cm.isUniqueVN(newVcno, effDate, dc))
                throw new TaskFailedException();
            loadData(cgNo);
            final String userId = dc.getValue("current_UserID");
            postInVoucherHeader(cgNo, newVcno, effDate, cgvNo, userId);
            dc.getValue("reverseType");

        } catch (final Exception e) {
            LOGGER.error("Exp reverse subledger:" + e.toString());
            throw taskExc;
        }

    }

    private boolean isReconciled(final String cgNo) throws SQLException {
        boolean valid = false;
        final String sql = "select isreconciled from BANKRECONCILIATION bc ,voucherheader vh where bc.voucherheaderid=vh.id and vh.cgn= ?";
        Query pst = null;
        List<Object[]> rset = null;
        try {
            pst = HibernateUtil.getCurrentSession().createSQLQuery(sql);
            pst.setString(1, cgNo);
            rset = pst.list();
            for (final Object[] element : rset)
                if (element[0].toString().equals("1"))
                    valid = true;

        } catch (final Exception e) {
            throw new SQLException();
        }
        return valid;
    }

    public String reversePT(final String cgNo, final String newVcno, final String effDate, final String cgvNo,
            final DataCollection dc, final String refcgvn1)
            throws TaskFailedException, SQLException
    {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("coming..");
        refcgvn = refcgvn1;
        reverse(cgNo, newVcno, effDate, cgvNo, dc);
        return newCessCGN;
    }
}
