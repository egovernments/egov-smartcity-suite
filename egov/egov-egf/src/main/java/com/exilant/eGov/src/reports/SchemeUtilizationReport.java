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
 * Created on July 25, 2008
 * @author Iliyaraja.S
 */
package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.utils.EGovConfig;

import com.exilant.exility.common.TaskFailedException;

@SuppressWarnings("deprecation")
public class SchemeUtilizationReport {

    private static final Logger LOGGER = Logger.getLogger(SchemeUtilizationReport.class);
    private static TaskFailedException taskExc;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    String filterGlcode = "";
    List<LinkedHashMap<String, String>> part1 = new ArrayList<>();
    List<LinkedHashMap<String, String>> part2 = new ArrayList<>();
    List<LinkedHashMap<String, String>> part3 = new ArrayList<>();
    List<LinkedHashMap<String, String>> partTotalReceipts = new ArrayList<>();
    List<LinkedHashMap<String, String>> part4 = new ArrayList<>();
    List<LinkedHashMap<String, String>> part5 = new ArrayList<>();
    List<LinkedHashMap<String, String>> partTotalClosingBal = new ArrayList<>();
    CallableStatement cstmt = null;
    CallableStatement cstmt2 = null;
    String dynamicSubSchemName[];

    @SuppressWarnings("null")
    public ArrayList<LinkedHashMap<String, String>> getSchemeUtilizationReport(final int pschemeid, final String startDate,
            final String endDate)
            throws Exception {

        Connection con = null;
        try {
            con = null;// This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();

            LinkedHashMap<String, String> data = null;

            final int majorCodeLength = Integer.parseInt(EGovConfig.getProperty("egf_config.xml", "majorcodevalue", "",
                    "AccountCode"));

            filterGlcode = EGovConfig.getProperty("egf_config.xml", "filterGlcodeForScheme", "", "SchemeUtilization");
            if (LOGGER.isInfoEnabled())
                LOGGER.info("SchemeUtilizationReport filterGlcode--->" + filterGlcode);
            if (LOGGER.isInfoEnabled())
                LOGGER.info("pschemeid=" + pschemeid + ",startDate=" + formatter.format(sdf.parse(startDate)) + ",endDate="
                        + formatter.format(sdf.parse(endDate)) + ",majorcodelength=" + majorCodeLength);

            /* For Opening balance Scheme and Sub scheme wise procedure start */

            if (LOGGER.isInfoEnabled())
                LOGGER.info("For Opening balance Scheme and Sub scheme wise procedure  start");

            final CallableStatement cstmt = con.prepareCall("{? = call EGF_REPORT.OPBALSCHEMEREPORT(?,?,?,?)}");
            cstmt.setFetchSize(1000);
            // This fix is for Phoenix Migration.
            // cstmt.registerOutParameter(1,OracleTypes.CURSOR);

            cstmt.setInt(1, pschemeid);
            cstmt.setString(2, formatter.format(sdf.parse(startDate)));
            // cstmt.setString(4,formatter.format(sdf.parse(endDate)));

            cstmt.setInt(4, majorCodeLength);
            cstmt.setString(5, filterGlcode);

            if (LOGGER.isInfoEnabled())
                LOGGER.info("BEFORE EXECUTE PROCEDURE for Opening Balance");
            cstmt.executeQuery();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("AFTER EXECUTE PROCEDURE for Opening Balance");

            final ResultSet rs = (ResultSet) cstmt.getObject(1);
            final ResultSetMetaData rsmd = rs.getMetaData();
            final int columnCount = rsmd.getColumnCount();

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Total result set column count-->" + columnCount);

            // Here number of static column is "1",that's why we have to subtract "1" from total column count,now we will get the
            // dynamic Column count
            final int dynamicColumn = columnCount - 1;
            String columnLabel = "", amt = "";

            if (rs.next()) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside while-for Opening balance Row");
                data = new LinkedHashMap<>();
                data.put("particulars", "<b>Opening Balance</b>");

                for (int j = 0; j < dynamicColumn; j++) {
                    // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicColumn amount--->"+rs.getString(j+1));
                    // Here dynamic Column is starting from "1",that's why we have to start dynamic Column count from "1" onwards.
                    amt = rs.getString(j + 1);
                    if (amt == null)
                        amt = "0.00";
                    columnLabel = rsmd.getColumnName(j + 1);
                    data.put(columnLabel, formatAmtTwoDecimal(amt));
                }// for
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("scheme total--->" + rs.getString("SCHAMT"));
                if (rs.getString("SCHAMT") != null)
                    data.put("schemetotal", formatAmtTwoDecimal(rs.getString("SCHAMT")));
                else
                    data.put("schemetotal", "0.00");

                part1.add(data);
            } else    // if no records in opening balance
            {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside else--No records for Opening balance ");
                data = new LinkedHashMap<>();
                data.put("particulars", "<b>Opening Balance</b>");

                for (int j = 0; j < dynamicColumn; j++) {
                    columnLabel = rsmd.getColumnName(j + 1);
                    data.put(columnLabel, "0.00");
                }// for

                // data.put("subschemetotal",formatAmtTwoDecimal("0.00"));
                data.put("schemetotal", formatAmtTwoDecimal("0.00"));
                part1.add(data);
            }

            if (LOGGER.isInfoEnabled())
                LOGGER.info("For Opening balance Scheme and Sub scheme wise procedure  end");

            /* For Opening balance Scheme and Sub scheme wise procedure end */

            /* For Scheme and Sub scheme wise procedure start */
            if (LOGGER.isInfoEnabled())
                LOGGER.info("For Scheme and Sub scheme wise procedure start");

            final CallableStatement cstmt2 = con.prepareCall("{? = call EGF_REPORT.SCHEMEREPORT(?,?,?,?,?)}");
            cstmt2.setFetchSize(1000);
            // This fix is for Phoenix Migration.
            // cstmt2.registerOutParameter(1,OracleTypes.CURSOR);

            cstmt2.setInt(2, pschemeid);
            cstmt2.setString(3, formatter.format(sdf.parse(startDate)));
            cstmt2.setString(4, formatter.format(sdf.parse(endDate)));

            cstmt2.setInt(5, majorCodeLength);
            cstmt2.setString(6, filterGlcode);

            if (LOGGER.isInfoEnabled())
                LOGGER.info("BEFORE EXECUTE PROCEDURE for Scheme and Sub scheme");
            cstmt2.executeQuery();
            if (LOGGER.isInfoEnabled())
                LOGGER.info("AFTER EXECUTE PROCEDURE for Scheme and Sub scheme");

            final ResultSet rs2 = (ResultSet) cstmt2.getObject(1);
            final ResultSetMetaData rsmd2 = rs2.getMetaData();
            final int columnCount2 = rsmd2.getColumnCount();

            if (LOGGER.isInfoEnabled())
                LOGGER.info("Total result set column count-->" + columnCount2);

            // Here number of static column is "4",that's why we have to subtract "4" from total column count,now we will get the
            // dynamic Column count.
            final int dynamicColumn2 = columnCount2 - 4;

            String addCapitalReceiptsSubHead = "";
            String addRevenueReceiptsSubHead = "";
            String totalReceiptsSubHead = "";

            String lessCapitalExpSubHead = "";
            String lessRevenueExpSubHead = "";
            final String closingBalanceSubHead = "";

            dynamicSubSchemName = new String[dynamicColumn2];
            for (int j = 0; j < dynamicColumn2; j++) {

                // Here dynamic Column is starting from "3",that's why we have to start dynamic Column count from "3" onwards.
                dynamicSubSchemName[j] = rsmd2.getColumnName(j + 3);
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("ARRAY VALUE dynamicSubSchemName[j]--->" + dynamicSubSchemName[j]);

            }

            while (rs2.next()) {

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside while-for Scheme and sub scheme row");

                // double totalSubScheme2 =0;
                String columnLabel2 = "", amt2 = "";

                if (rs2.getString("CTYPE").equalsIgnoreCase("L")) {
                    if (!addCapitalReceiptsSubHead.equals("CRECEIPTS")) {
                        data = new LinkedHashMap<>();

                        data.put("particulars", "<b>Add: Capital Receipts</b>");

                        for (int j = 0; j < dynamicColumn2; j++) {
                            columnLabel2 = rsmd2.getColumnName(j + 3);
                            data.put(columnLabel2, "&nbsp;");
                        }
                        // data.put("subschemetotal","&nbsp;");
                        data.put("schemetotal", "&nbsp;");

                        part2.add(data);
                        addCapitalReceiptsSubHead = "CRECEIPTS";
                    }

                    data = new LinkedHashMap<>();

                    data.put("particulars", rs2.getString("MAJORCODE") + "-" + rs2.getString("COANAME"));

                    for (int j = 0; j < dynamicColumn2; j++) {
                        // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
                        // Here dynamic Column is starting from "3",that's why we have to start dynamic Column count from "3"
                        // onwards.
                        amt2 = rs2.getString(j + 3);

                        if (amt2 == null)
                            amt2 = "0.00";

                        columnLabel2 = rsmd2.getColumnName(j + 3);
                        data.put(columnLabel2, formatAmtTwoDecimal(amt2));
                        // totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);

                    }// for
                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("scheme total--->" + rs2.getString("SCHAMT"));

                    if (rs2.getString("SCHAMT") != null)
                        data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
                    else
                        data.put("schemetotal", "0.00");

                    part2.add(data);
                } // for "Liability code"
                else if (rs2.getString("CTYPE").equalsIgnoreCase("I")) {
                    if (!addRevenueReceiptsSubHead.equals("RENENUERECEIPTS")) {
                        data = new LinkedHashMap<>();
                        data.put("particulars", "<b>Add: Revenue Receipts</b>");
                        for (int j = 0; j < dynamicColumn2; j++) {
                            columnLabel2 = rsmd2.getColumnName(j + 3);
                            data.put(columnLabel2, "&nbsp;");
                        }
                        data.put("schemetotal", "&nbsp;");
                        part3.add(data);
                        addRevenueReceiptsSubHead = "RENENUERECEIPTS";
                    }
                    data = new LinkedHashMap<>();
                    data.put("particulars", rs2.getString("MAJORCODE") + "-" + rs2.getString("COANAME"));
                    for (int j = 0; j < dynamicColumn2; j++) {
                        // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
                        amt2 = rs2.getString(j + 3);

                        if (amt2 == null)
                            amt2 = "0.00";

                        columnLabel2 = rsmd2.getColumnName(j + 3);
                        data.put(columnLabel2, formatAmtTwoDecimal(amt2));
                        // totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);

                    }// for

                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("scheme total--->" + rs2.getString("SCHAMT"));

                    if (rs2.getString("SCHAMT") != null)
                        data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
                    else
                        data.put("schemetotal", "0.00");

                    part3.add(data);

                } // for "Income code"
                else if (rs2.getString("CTYPE").equalsIgnoreCase("A")) {
                    if (!lessCapitalExpSubHead.equals("CEXPENDITURE")) {
                        data = new LinkedHashMap<>();

                        data.put("particulars", "<b>Less: Capital Expenditure</b>");

                        for (int j = 0; j < dynamicColumn2; j++) {
                            columnLabel2 = rsmd2.getColumnName(j + 3);
                            data.put(columnLabel2, "&nbsp;");
                        }
                        // data.put("subschemetotal","&nbsp;");
                        data.put("schemetotal", "&nbsp;");

                        part4.add(data);
                        lessCapitalExpSubHead = "CEXPENDITURE";
                    }

                    data = new LinkedHashMap<>();
                    data.put("particulars", rs2.getString("MAJORCODE") + "-" + rs2.getString("COANAME"));

                    for (int j = 0; j < dynamicColumn2; j++) {
                        // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
                        amt2 = rs2.getString(j + 3);

                        if (amt2 == null)
                            amt2 = "0.00";

                        columnLabel2 = rsmd2.getColumnName(j + 3);
                        data.put(columnLabel2, formatAmtTwoDecimal(amt2));
                        // totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);

                    }// for

                    // if(LOGGER.isInfoEnabled()) LOGGER.info("totalSubScheme2--->"+totalSubScheme2);
                    // data.put("subschemetotal", formatAmtTwoDecimal(totalSubScheme2+""));

                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("scheme total--->" + rs2.getString("SCHAMT"));

                    if (rs2.getString("SCHAMT") != null)
                        data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
                    else
                        data.put("schemetotal", "0.00");

                    part4.add(data);

                } // for "Asset code"
                else if (rs2.getString("CTYPE").equalsIgnoreCase("E")) {
                    if (!lessRevenueExpSubHead.equals("RENENUEEXPENDITURE")) {
                        data = new LinkedHashMap<>();

                        data.put("particulars", "<b>Less: Revenue Expenditure</b>");

                        for (int j = 0; j < dynamicColumn2; j++) {
                            columnLabel2 = rsmd2.getColumnName(j + 3);
                            data.put(columnLabel2, "&nbsp;");
                        }
                        // data.put("subschemetotal","&nbsp;");
                        data.put("schemetotal", "&nbsp;");

                        part5.add(data);
                        lessRevenueExpSubHead = "RENENUEEXPENDITURE";
                    }

                    data = new LinkedHashMap<>();
                    data.put("particulars", rs2.getString("MAJORCODE") + "-" + rs2.getString("COANAME"));

                    for (int j = 0; j < dynamicColumn2; j++) {
                        // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicColumn amount--->"+rs2.getString(j+3));
                        amt2 = rs2.getString(j + 3);

                        if (amt2 == null)
                            amt2 = "0.00";

                        columnLabel2 = rsmd2.getColumnName(j + 3);
                        data.put(columnLabel2, formatAmtTwoDecimal(amt2));
                        // totalSubScheme2= totalSubScheme2 + Double.parseDouble(amt2);

                    }// for

                    // if(LOGGER.isInfoEnabled()) LOGGER.info("totalSubScheme2--->"+totalSubScheme2);
                    // data.put("subschemetotal", formatAmtTwoDecimal(totalSubScheme2+""));

                    if (LOGGER.isInfoEnabled())
                        LOGGER.info("scheme total--->" + rs2.getString("SCHAMT"));

                    if (rs2.getString("SCHAMT") != null)
                        data.put("schemetotal", formatAmtTwoDecimal(rs2.getString("SCHAMT")));
                    else
                        data.put("schemetotal", "0.00");

                    part5.add(data);

                } // for "Expense code"

            }// while

            if (LOGGER.isInfoEnabled())
                LOGGER.info("For Scheme and Sub scheme wise procedure end");

            /* For Scheme and Sub scheme wise procedure End */

            // If it is no record for that particular ctype,we have to show empty row start

            if (addCapitalReceiptsSubHead.equals("")) {

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside If it is No Capital Receipts---------->");

                data = new LinkedHashMap<>();
                data.put("particulars", "<b>Add: Capital Receipts</b>");

                for (final String element : dynamicSubSchemName)
                    // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
                    data.put(element, formatAmtTwoDecimal("0.00"));
                // data.put("subschemetotal","0.00");
                data.put("schemetotal", formatAmtTwoDecimal("0.00"));

                part2.add(data);

            }
            if (addRevenueReceiptsSubHead.equals("")) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside If it is No Revenue Receipts----------->");

                data = new LinkedHashMap<>();
                data.put("particulars", "<b>Add: Revenue Receipts</b>");

                for (final String element : dynamicSubSchemName)
                    // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
                    data.put(element, formatAmtTwoDecimal("0.00"));
                // data.put("subschemetotal","0.00");
                data.put("schemetotal", formatAmtTwoDecimal("0.00"));

                part3.add(data);

            }
            if (lessCapitalExpSubHead.equals("")) {

                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside If it is No Capital Expenditure---------->");
                data = new LinkedHashMap<>();

                data.put("particulars", "<b>Less: Capital Expenditure</b>");

                for (final String element : dynamicSubSchemName)
                    // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
                    data.put(element, formatAmtTwoDecimal("0.00"));
                // data.put("subschemetotal","0.00");
                data.put("schemetotal", formatAmtTwoDecimal("0.00"));

                part4.add(data);

            }
            if (lessRevenueExpSubHead.equals("")) {
                if (LOGGER.isInfoEnabled())
                    LOGGER.info("Inside If it is No Revenue Expenditure---------->");
                data = new LinkedHashMap<>();

                data.put("particulars", "<b>Less: Revenue Expenditure</b>");

                for (final String element : dynamicSubSchemName)
                    // if(LOGGER.isInfoEnabled()) LOGGER.info("dynamicSubSchemName[j]--->"+dynamicSubSchemName[j]);
                    data.put(element, formatAmtTwoDecimal("0.00"));
                // data.put("subschemetotal","0.00");
                data.put("schemetotal", formatAmtTwoDecimal("0.00"));

                part5.add(data);
            }
            // If it is no record for that particular ctype,we have to show empty row end

            // cumulative for Total Receipts including Opening Balance start

            final LinkedHashMap<String, Object> totalOpeningBal = new LinkedHashMap<>();

            final Iterator<LinkedHashMap<String, String>> itr = part1.iterator();
            while (itr.hasNext()) {
                final LinkedHashMap<String, String> hm = itr.next();
                final Set<String> keySet = hm.keySet();
                final Iterator<String> itr1 = keySet.iterator();
                while (itr1.hasNext()) {

                    final String str = itr1.next();
                    for (String element : dynamicSubSchemName)
                        if (totalOpeningBal.containsKey(element)) {
                            BigDecimal temp = new BigDecimal("0.00");
                            if (!hm.get(element).equals("&nbsp;") && str.equals(element))
                                temp = new BigDecimal(totalOpeningBal.get(element).toString())
                                        .add(new BigDecimal(hm.get(element).toString()));
                            else
                                temp = new BigDecimal(totalOpeningBal.get(element).toString());
                            totalOpeningBal.put(element, temp);
                        } else if (str.equals(element) && !hm.get(element).equals("&nbsp;"))
                            totalOpeningBal.put(element, hm.get(element).toString());

                    // for scheme total
                    if (totalOpeningBal.containsKey("schemetotal")) {

                        BigDecimal temp = new BigDecimal("0.00");
                        if (!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString()).add(new BigDecimal(hm
                                    .get("schemetotal").toString()));
                        else
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString());
                        totalOpeningBal.put("schemetotal", temp);
                    } else if (str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
                        totalOpeningBal.put("schemetotal", hm.get("schemetotal").toString());

                    //
                }// end while 2
            }// end while 1

            final Iterator<LinkedHashMap<String, String>> itr2 = part2.iterator();
            while (itr2.hasNext()) {
                final LinkedHashMap<String, String> hm = itr2.next();
                final Set<String> keySet = hm.keySet();
                final Iterator<String> itr1 = keySet.iterator();
                while (itr1.hasNext()) {
                    final String str = itr1.next();
                    for (String element : dynamicSubSchemName)
                        if (totalOpeningBal.containsKey(element)) {
                            BigDecimal temp = new BigDecimal("0.00");
                            if (!hm.get(element).equals("&nbsp;") && str.equals(element))
                                temp = new BigDecimal(totalOpeningBal.get(element).toString())
                                        .add(new BigDecimal(hm.get(element).toString()));
                            else
                                temp = new BigDecimal(totalOpeningBal.get(element).toString());
                            totalOpeningBal.put(element, temp);
                        } else if (str.equals(element) && !hm.get(element).equals("&nbsp;"))
                            totalOpeningBal.put(element, hm.get(element).toString());

                    // for scheme total
                    if (totalOpeningBal.containsKey("schemetotal")) {
                        BigDecimal temp = new BigDecimal("0.00");
                        if (!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString()).add(new BigDecimal(hm
                                    .get("schemetotal").toString()));
                        else
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString());
                        totalOpeningBal.put("schemetotal", temp);
                    } else if (str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
                        totalOpeningBal.put("schemetotal", hm.get("schemetotal").toString());

                    //
                }// end while 2
            }// end while 1

            final Iterator<LinkedHashMap<String, String>> itr3 = part3.iterator();
            while (itr3.hasNext()) {
                final LinkedHashMap<String, String> hm = itr3.next();
                final Set<String> keySet = hm.keySet();
                final Iterator<String> itr1 = keySet.iterator();
                while (itr1.hasNext()) {
                    final String str = itr1.next();
                    for (String element : dynamicSubSchemName)
                        if (totalOpeningBal.containsKey(element)) {
                            BigDecimal temp = new BigDecimal("0.00");
                            if (!hm.get(element).equals("&nbsp;") && str.equals(element))
                                temp = new BigDecimal(totalOpeningBal.get(element).toString())
                                        .add(new BigDecimal(hm.get(element).toString()));
                            else
                                temp = new BigDecimal(totalOpeningBal.get(element).toString());
                            totalOpeningBal.put(element, temp);
                        } else if (str.equals(element) && !hm.get(element).equals("&nbsp;"))
                            totalOpeningBal.put(element, hm.get(element).toString());

                    // for scheme total
                    if (totalOpeningBal.containsKey("schemetotal")) {
                        BigDecimal temp = new BigDecimal("0.00");
                        if (!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString()).add(new BigDecimal(hm
                                    .get("schemetotal").toString()));
                        else
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString());
                        totalOpeningBal.put("schemetotal", temp);
                    } else if (str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
                        totalOpeningBal.put("schemetotal", hm.get("schemetotal").toString());

                    //
                }// end while 2
            }// end while 1

            if (!totalReceiptsSubHead.equals("TotalReceipts")) {
                data = new LinkedHashMap<>();

                data.put("particulars", "<b>Total Receipts including Opening Balance</b>");

                for (final String element : dynamicSubSchemName)
                    data.put(element, "<b>" + totalOpeningBal.get(element).toString() + "</b>");
                // data.put("subschemetotal","<b>"+(totalOpeningBal.get("subschemetotal").toString())+"</b>");
                data.put("schemetotal", "<b>" + totalOpeningBal.get("schemetotal").toString() + "</b>");

                partTotalReceipts.add(data);
                totalReceiptsSubHead = "TotalReceipts";
            }

            // cumulative for Total Receipts including Opening Balance end

            // cumulative for Total Closing Balance start

            final Iterator<LinkedHashMap<String, String>> itr4 = part4.iterator();
            while (itr4.hasNext()) {
                final LinkedHashMap<String, String> hm = itr4.next();
                final Set<String> keySet = hm.keySet();
                final Iterator<String> itr1 = keySet.iterator();
                while (itr1.hasNext()) {
                    final String str = itr1.next();
                    for (String element : dynamicSubSchemName)
                        if (totalOpeningBal.containsKey(element)) {
                            BigDecimal temp = new BigDecimal("0.00");
                            if (!hm.get(element).equals("&nbsp;") && str.equals(element))
                                temp = new BigDecimal(totalOpeningBal.get(element).toString())
                                        .subtract(new BigDecimal(hm.get(element).toString()));
                            else
                                temp = new BigDecimal(totalOpeningBal.get(element).toString());
                            totalOpeningBal.put(element, temp);
                        } else if (str.equals(element) && !hm.get(element).equals("&nbsp;"))
                            totalOpeningBal.put(element, hm.get(element).toString());

                    // for scheme total
                    if (totalOpeningBal.containsKey("schemetotal")) {
                        BigDecimal temp = new BigDecimal("0.00");
                        if (!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString()).subtract(new BigDecimal(hm
                                    .get("schemetotal").toString()));
                        else
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString());
                        totalOpeningBal.put("schemetotal", temp);
                    } else if (str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
                        totalOpeningBal.put("schemetotal", hm.get("schemetotal").toString());

                    //
                }// end while 2
            }// end while 1

            final Iterator<LinkedHashMap<String, String>> itr5 = part5.iterator();
            while (itr5.hasNext()) {
                final LinkedHashMap<String, String> hm = itr5.next();
                final Set<String> keySet = hm.keySet();
                final Iterator<String> itr1 = keySet.iterator();
                while (itr1.hasNext()) {
                    final String str = itr1.next();
                    for (String element : dynamicSubSchemName)
                        if (totalOpeningBal.containsKey(element)) {
                            BigDecimal temp = new BigDecimal(0);
                            if (!hm.get(element).equals("&nbsp;") && str.equals(element))
                                temp = new BigDecimal(totalOpeningBal.get(element).toString())
                                        .subtract(new BigDecimal(hm.get(element).toString()));
                            else
                                temp = new BigDecimal(totalOpeningBal.get(element).toString());
                            totalOpeningBal.put(element, temp);
                        } else if (str.equals(element) && !hm.get(element).equals("&nbsp;"))
                            totalOpeningBal.put(element, hm.get(element).toString());

                    // for scheme total
                    if (totalOpeningBal.containsKey("schemetotal")) {
                        BigDecimal temp = new BigDecimal(0);
                        if (!hm.get("schemetotal").equals("&nbsp;") && str.equals("schemetotal"))
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString()).subtract(new BigDecimal(hm
                                    .get("schemetotal").toString()));
                        else
                            temp = new BigDecimal(totalOpeningBal.get("schemetotal").toString());
                        totalOpeningBal.put("schemetotal", temp);
                    } else if (str.equals("schemetotal") && !hm.get("schemetotal").equals("&nbsp;"))
                        totalOpeningBal.put("schemetotal", hm.get("schemetotal").toString());

                    //
                }// end while 2
            }// end while 1

            if (!closingBalanceSubHead.equals("ClosingBalance ")) {
                data = new LinkedHashMap<>();

                data.put("particulars", "<b>Closing Balance</b>");

                for (final String element : dynamicSubSchemName)
                    data.put(element, "<b>" + totalOpeningBal.get(element).toString() + "</b>");
                // data.put("subschemetotal","<b>"+(totalOpeningBal.get("subschemetotal").toString())+"</b>");
                data.put("schemetotal", "<b>" + totalOpeningBal.get("schemetotal").toString() + "</b>");

                partTotalClosingBal.add(data);
                totalReceiptsSubHead = "ClosingBalance";
            }

            // cumulative for Total Closing Balance end

        } catch (final Exception e) {
            LOGGER.error("Exp in Scheme Report==", e);
            throw taskExc;
        }

        // ArrayList finalReportList=(ArrayList)part1;

        final ArrayList<LinkedHashMap<String, String>> finalReportList = new ArrayList<>();

        final Iterator<LinkedHashMap<String, String>> itrOpBal = part1.iterator();
        while (itrOpBal.hasNext()) {
            final LinkedHashMap<String, String> hm = itrOpBal.next();
            finalReportList.add(hm);
        }

        final Iterator<LinkedHashMap<String, String>> itr1 = part2.iterator();
        while (itr1.hasNext()) {
            final LinkedHashMap<String, String> hm = itr1.next();
            finalReportList.add(hm);
        }
        final Iterator<LinkedHashMap<String, String>> itr2 = part3.iterator();
        while (itr2.hasNext()) {
            final LinkedHashMap<String, String> hm = itr2.next();
            finalReportList.add(hm);
        }

        final Iterator<LinkedHashMap<String, String>> itrTotalR = partTotalReceipts.iterator();
        while (itrTotalR.hasNext()) {
            final LinkedHashMap<String, String> hm = itrTotalR.next();
            finalReportList.add(hm);
        }

        final Iterator<LinkedHashMap<String, String>> itr3 = part4.iterator();
        while (itr3.hasNext()) {
            final LinkedHashMap<String, String> hm = itr3.next();
            finalReportList.add(hm);
        }

        final Iterator<LinkedHashMap<String, String>> itr4 = part5.iterator();
        while (itr4.hasNext()) {
            final LinkedHashMap<String, String> hm = itr4.next();
            finalReportList.add(hm);
        }

        final Iterator<LinkedHashMap<String, String>> itrClosingTotal = partTotalClosingBal.iterator();
        while (itrClosingTotal.hasNext()) {
            final LinkedHashMap<String, String> hm = itrClosingTotal.next();
            finalReportList.add(hm);
        }

        return finalReportList;
    }

    public String formatAmtTwoDecimal(final String amt) {
        // BigDecimal ammt= new BigDecimal(0.000);
        NumberFormat formatter;
        formatter = new DecimalFormat("##############0.00");

        return formatter.format(new BigDecimal(amt));
    }

} // CLASS
