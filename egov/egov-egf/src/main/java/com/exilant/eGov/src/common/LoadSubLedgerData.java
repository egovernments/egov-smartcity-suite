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
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.exilant.eGov.src.common;

import com.exilant.exility.common.AbstractTask;
import com.exilant.exility.common.DataCollection;
import com.exilant.exility.common.TaskFailedException;
import org.apache.log4j.Logger;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
@Transactional(readOnly = true)
public class LoadSubLedgerData extends AbstractTask {
 @Autowired
 @Qualifier("persistenceService")
 private PersistenceService persistenceService;

    private final static Logger LOGGER = Logger.getLogger(LoadSubLedgerData.class);
    private static TaskFailedException taskExc;

    @Override
    public void execute(final String taskName,
            final String gridName, final DataCollection dc,
            final Connection con, final boolean errorOnNoData,
            final boolean gridHasColumnHeading, final String prefix) throws TaskFailedException
    {
        //
        int noOfRec = 0;
        List<Object[]> rset = null;
        Query pst = null;

        final String cgn = dc.getValue("drillDownCgn");
        try {
            String relationType = "";
            String relationTypeID = "";
            String relationBillTable = "";
            String relationBillID = "";
            String chequeId = "";
            String sql = "select sph.type,sph.chequeid from subledgerpaymentheader sph,voucherheader  vh  where " +
                    " sph.voucherheaderid=vh.id and vh.cgn= ?";
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, cgn);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            rset = pst.list();
            for (final Object[] element : rset) {
                relationType = element[0].toString();
                chequeId = element[1].toString();
            }
            dc.addValue("pay_hide", relationType);
            if (chequeId == null || chequeId.equals("0"))
                dc.addValue("subLedgerPaymentHeader_typeOfPayment", "Cash");
            else
                dc.addValue("subLedgerPaymentHeader_typeOfPayment", "Cheque");
            relationTypeID = relationType + "id";
            relationBillTable = relationType + "billdetail";
            relationBillID = relationType + "billid";
            sql = "select sph.type as \"pay_type\","
                    + relationTypeID
                    + " as \"payToid\","
                    +
                    " paidby as \"paidByid\",bankaccountid as \"accId\",worksdetailid as \"worksDetailid\", "
                    +
                    "f.name as \"fund_name\",f.id as \"fund_id\",fsrc.name as \"fundSource_id\",fsrc.name as \"fundSource_name\" "
                    +
                    " from subledgerpaymentheader" +
                    " sph,voucherheader  vh ,fund f ,fundsource fsrc where " +
                    " sph.voucherheaderid=vh.id  and f.id=vh.fundid and fsrc.id=vh.fundsourceid" +
                    " and vh.cgn= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, cgn);
            rset = pst.list();
            for (final Object[] element : rset) {
                dc.addValue("pay_type", element[0].toString());
                dc.addValue("payToid", element[1].toString());
                dc.addValue("paidByid", element[2].toString());
                dc.addValue("accId", element[3].toString());
                dc.addValue("worksDetailid", element[4].toString());
                dc.addValue("fund_name", element[5].toString());
                dc.addValue("fund_id", element[6].toString());
                dc.addValue("fundSource_id", element[7].toString());
                dc.addValue("fundSource_name", element[8].toString());
            }
            // rset.close();
            // billcollector
            sql = "select a.name as \"paidBy\",b.glcode as \"billCollector_cashInHandDesc\" from billcollector a,chartofaccounts b where "
                    +
                    " a.cashinhand=b.id and a.id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, dc.getValue("paidByid"));
            rset = pst.list();
            for (final Object[] element : rset) {
                dc.addValue("paidBy", element[0].toString());
                dc.addValue("billCollector_cashInHandDesc", element[1].toString());
            }
            // rset.close();
            // supplier/contractor name
            sql = "select name  as \"payTo\" from relation where id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, dc.getValue("payToid"));
            rset = pst.list();
            for (final Object[] element : rset)
                dc.addValue("payTo", element[0].toString());
            // rset.close();
            // workorder
            sql = "select name  as \"worksDetail_id\" ,advanceamount as \"worksDetail_advanceAmount\" from worksDetail where id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, dc.getValue("worksDetailid"));
            rset = pst.list();
            for (final Object[] element : rset) {
                dc.addValue("worksDetail_id", element[0].toString());
                dc.addValue("worksDetail_advanceAmount", element[1].toString());
            }
            // rset.close();
            // bank name
            sql = "select a.name||' '||b.branchname as \"subLedgerPaymentHeader_bankId\" from bank a ,bankbranch b, bankaccount c  where"
                    +
                    " a.id=b.bankid and b.id=c.branchid and c.id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, dc.getValue("accId"));
            rset = pst.list();
            for (final Object[] element : rset)
                dc.addValue("subLedgerPaymentHeader_bankId", element[0].toString());
            // rset.close();
            // acount number
            sql = "select accountnumber as \"branchAccountId\" from bankaccount where id= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, dc.getValue("accId"));
            rset = pst.list();
            for (final Object[] element : rset)
                dc.addValue("branchAccountId", element[0].toString());
            // rset.close();
            sql = "select count(*)" +
                    " from " + relationBillTable + " a ," +
                    " voucherheader b ,subledgerpaymentheader sph  " +
                    " where b.id=a.voucherheaderid  and " +
                    " sph." + relationBillID + "=a.id and " +
                    " sph.voucherheaderid =(select id from voucherheader where cgn= ?)" +
                    " and passedamount>(a.paidamount+tdsamount+advadjamt)-sph.paidamount " +
                    " and a." + relationTypeID + "= ? and b.fundid=" +
                    " and a.worksdetailid= ?" + " order by a.billDate";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, cgn);
            pst.setString(1, dc.getValue("payToid"));
            pst.setString(2, dc.getValue("fund_id"));
            pst.setString(3, dc.getValue("worksDetailid"));
            rset = pst.list();
            for (final Object[] element : rset)
                noOfRec = Integer.parseInt(element[0].toString());
            // rset.close();
            if (noOfRec > 0) {
                final String[][] grid = new String[noOfRec + 1][13];
                sql = "select a.id as \"billNoId\",billNumber as\"billNo\",vouchernumber as \"d_voucherNo\" ," +
                        "to_char(billdate,'dd-Mon-yyyy') as \"billDate\",a.PassedAmount as \"passedAmount\"," +
                        " advadjamt as \"advance\",TDSamount as \"tds\",OtherRecoveries as \"otherRecoveries\"," +
                        " a.passedAmount-(advadjamt+tdsamount+otherrecoveries) as \"net\"," +
                        " a.PaidAmount-sph.paidamount as \"earlierPayment\" ," +
                        " sph.paidamount as \"slph_paidAmount\"," +
                        // ((a.passedAmount-(advadjamt+tdsamount+otherrecoveries))-a.paidamount)
                        " rownum as \"slNo\" ,'1' as \"billSelect\" from " + relationBillTable + " a ," +
                        " voucherheader b ,subledgerpaymentheader sph  " +
                        " where b.id=a.voucherheaderid  and " +
                        " sph." + relationBillID + "=a.id and " +
                        " sph.voucherheaderid =(select id from voucherheader where cgn= ?)" +
                        " and passedamount>(a.paidamount+tdsamount+advadjamt)-sph.paidamount " +
                        " and a." + relationTypeID + "= ? and b.fundid= ?" +
                        " and a.worksdetailid= ? order by a.billDate";
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug(sql);
                pst = persistenceService.getSession().createSQLQuery(sql);
                pst.setString(0, cgn);
                pst.setString(1, dc.getValue("payToid"));
                pst.setString(2, dc.getValue("fund_id"));
                pst.setString(3, dc.getValue("worksDetailid"));
                rset = pst.list();
                // grid[0][x] we filled control name
                for (final Object[] element : rset) {
                    dc.addValue("billNoId", element[0].toString());
                    dc.addValue("billNo", element[1].toString());
                    dc.addValue("d_voucherNo", element[2].toString());
                    dc.addValue("billDate", element[3].toString());
                    dc.addValue("passedAmount", element[4].toString());
                    dc.addValue("advance", element[5].toString());
                    dc.addValue("tds", element[6].toString());
                    dc.addValue("otherRecoveries", element[7].toString());
                    dc.addValue("net", element[8].toString());
                    dc.addValue("earlierPayment", element[9].toString());
                    dc.addValue("slph_paidAmount", element[10].toString());
                    dc.addValue("slNo", element[11].toString());
                    dc.addValue("billSelect", element[12].toString());
                }
                int idx = 1;// grid[from 1][x] we start filling data
                for (final Object[] element : rset) {
                    grid[idx][0] = element[0].toString();
                    grid[idx][1] = element[1].toString();
                    grid[idx][2] = element[2].toString();
                    grid[idx][3] = element[3].toString();
                    grid[idx][4] = element[4].toString();
                    grid[idx][5] = element[5].toString();
                    grid[idx][6] = element[6].toString();
                    grid[idx][7] = element[7].toString();
                    grid[idx][8] = element[8].toString();
                    grid[idx][9] = element[9].toString();
                    grid[idx][10] = element[10].toString();
                    grid[idx][11] = element[11].toString();
                    grid[idx][12] = element[12].toString();
                    idx++;
                }
                dc.addGrid(gridName, grid);
            }
            sql = "select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-Mon-yyyy') as \"voucherHeader_voucherDate\","
                    +
                    " chequenumber as \"chequeDetail_chequeNumber\" ,to_char(chequedate,'dd-Mon-yyyy')  as \"chequeDetail_chequeDate\",vh.description as \"narration\",vh.fundsourceid as \"fundsource_id\" from voucherheader vh,subledgerpaymentheader sph,chequedetail cq where"
                    +
                    " sph.voucherheaderid=vh.id  and cq.id=sph.chequeid"
                    +
                    " and chequeid >0 and chequeid is not null  and vh.cgn= ?"
                    +
                    " union "
                    +
                    " select cgn as \"voucherHeader_cgn\",vouchernumber as \"voucherHeader_voucherNumber\",to_char(voucherdate,'dd-Mon-yyyy') as \"voucherHeader_voucherDate\",'','',vh.description as \"narration\",vh.fundsourceid as \"fundsource_id\" from voucherheader vh,subledgerpaymentheader sph  where"
                    +
                    " sph.voucherheaderid=vh.id " +
                    " and (chequeid is  null or chequeid=0) and vh.cgn= ?";
            if (LOGGER.isDebugEnabled())
                LOGGER.debug(sql);
            pst = persistenceService.getSession().createSQLQuery(sql);
            pst.setString(0, cgn);
            pst.setString(1, cgn);
            rset = pst.list();
            for (final Object[] element : rset) {
                dc.addValue("voucherHeader_cgn", element[0].toString());
                dc.addValue("voucherHeader_voucherNumber", element[1].toString());
                dc.addValue("voucherHeader_voucherDate", element[2].toString());
                dc.addValue("chequeDetail_chequeNumber", element[3].toString());
                dc.addValue("chequeDetail_chequeDate", element[4].toString());
                dc.addValue("subLedgerPaymentHeader_narration", element[5].toString());
                dc.addValue("fundsource_id", element[6].toString());
            }
            // rset.close();
            // st.close();
        } catch (final Exception e) {
            LOGGER.error("Error in executing query");
            throw taskExc;
        }

    }
}