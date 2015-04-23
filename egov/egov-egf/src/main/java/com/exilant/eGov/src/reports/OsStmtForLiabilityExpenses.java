package com.exilant.eGov.src.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.NumberToWord;


/**
 * @purpose : To get the outstanding for liability expenses for
 *          contractors/suppliers
 * @author Ilayaraja P
 * @version 1.0
 */
public class OsStmtForLiabilityExpenses {

	private static final Logger LOGGER = Logger
			.getLogger(OsStmtForLiabilityExpenses.class);

	public HashMap<String, Object> getReport(String asOnDate, String type,
			String relationId, String param_fromDate, String param_toDate,
			String param_fund) {
		if(LOGGER.isDebugEnabled())     LOGGER.debug("Inside OsStmtForLiabilityExpenses getReport method");
		PreparedStatement pstmt = null;
		Connection con = null;
		// Statement stmt = null;
		String query = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		List<Object> osStmtList = new ArrayList<Object>();
		CommnFunctions cf = new CommnFunctions();
		Double total = 0.0;
		String amtInWords = "";
		String fromDate = "";
		String toDate = "";
		try {
			// java.util.Date asOn = sdf.parse(asOnDate);
			// String date = formatter.format(asOn);
			java.util.Date date = null;
			String dateCondition = " ";
			if(LOGGER.isInfoEnabled())     LOGGER.info("fromdate:" + param_fromDate);
			if(LOGGER.isInfoEnabled())     LOGGER.info("todate:" + param_toDate);
			if (param_fromDate != null && !param_fromDate.trim().equals("")) {
				date = sdf.parse(param_fromDate);
				if(LOGGER.isInfoEnabled())     LOGGER.info("from date:" + date);
				fromDate = formatter.format(date);
				dateCondition = dateCondition + " and v.VOUCHERDATE >=?";
			}
			if (param_toDate != null && !param_toDate.trim().equals("")) {
				date = null;
				date = sdf.parse(param_toDate);
				if(LOGGER.isInfoEnabled())     LOGGER.info("to date:" + date);
				toDate = formatter.format(date);
				dateCondition = dateCondition + " and v.VOUCHERDATE <= ?";
			}
			String fundCondition = " ";
			if (param_fund != null && !param_fund.trim().equals("")) {
				fundCondition = fundCondition + " and v.fundid = ? ";
			}
			String whereCondition = "";
			con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();

			if (!relationId.equals(""))
				whereCondition = " and r.id=?";

			if (type.equalsIgnoreCase("0")) // both (contractor/supplier)
			{

				query = "SELECT  c.BILLNUMBER||' / '||v.vouchernumber as \"Bill Number\",c.contractorid,r.NAME AS \"NAME\",(c.PASSEDAMOUNT-(c.PAIDAMOUNT+c.otherrecoveries)) AS \"Bill Amount\",v.VOUCHERDATE AS \"Bill DATE\", "
						+ " gl.GLCODE AS \"Code of Account\",v.ID AS \"VHID\",'Contractor' AS \"Nature of Payable\",wd.NAME AS \"Work Order\",f.NAME AS \"FUND\", v.cgn"
						+ " FROM contractorbilldetail c,relation r,voucherheader v,generalledger gl,worksdetail wd,fund f"
						+ " WHERE c.contractorid=r.ID AND v.ID=c.voucherheaderid AND v.status=0 "
						+ dateCondition
						+ fundCondition
						+ " AND gl.voucherheaderid=v.ID AND gl.DEBITAMOUNT>0 "
						+ " AND wd.ID=c.WORKSDETAILID AND f.ID=wd.fundid "
						+ " GROUP BY c.ID,c.contractorid,r.NAME,c.PASSEDAMOUNT,c.PAIDAMOUNT,c.otherrecoveries,c.tdsamount,c.advadjamt,v.VOUCHERDATE,gl.GLCODE,v.ID,wd.NAME,f.NAME,c.BILLNUMBER,v.vouchernumber, v.cgn "
						+
						// " HAVING (c.PASSEDAMOUNT-(c.PAIDAMOUNT+c.otherrecoveries))>0 "
						// +
						"  HAVING (c.passedamount > (c.paidamount + c.tdsamount + c.advadjamt + c.otherrecoveries) ) "
						+ " UNION ALL "
						+ " SELECT s.BILLNUMBER||' / '||v.vouchernumber as \"Bill Number\",s.supplierid,r.NAME AS \"NAME\",(s.PASSEDAMOUNT-(s.PAIDAMOUNT+s.otherrecoveries)) AS \"Bill Amount\",v.VOUCHERDATE AS \"Bill DATE\", "
						+ " gl.GLCODE AS \"Code of Account\",v.ID AS \"VHID\",'Supplier' AS \"Nature of Payable\",wd.NAME AS \"Work Order\",f.NAME AS \"FUND\", v.cgn "
						+ " FROM supplierbilldetail s,relation r,voucherheader v,generalledger gl,worksdetail wd,fund f "
						+ " WHERE s.supplierid=r.ID AND v.ID=s.voucherheaderid AND v.status=0 "
						+ dateCondition
						+ fundCondition
						+ " AND gl.voucherheaderid=v.ID AND gl.DEBITAMOUNT>0 "
						+ " AND wd.ID=s.WORKSDETAILID AND f.ID=wd.fundid "
						+ " GROUP BY s.ID,s.supplierid,r.NAME,s.PASSEDAMOUNT,s.PAIDAMOUNT,s.otherrecoveries,s.tdsamount,s.advadjamt,v.VOUCHERDATE,gl.GLCODE,v.ID,wd.NAME,f.NAME,s.BILLNUMBER,v.vouchernumber, v.cgn "
						+
						// " HAVING (s.PASSEDAMOUNT-(s.PAIDAMOUNT+s.otherrecoveries))>0 "
						// +
						"  HAVING (s.passedamount > (s.paidamount + s.tdsamount + s.advadjamt + s.otherrecoveries)) "
						+ " ORDER BY \"NAME\",\"Bill Number\",\"Bill DATE\",\"VHID\" ";
				pstmt = con.prepareStatement(query);
				int i = 1;
				if (param_fromDate != null && !param_fromDate.trim().equals("")) {
					pstmt.setString(i++, fromDate);
				}
				if (param_toDate != null && !param_toDate.trim().equals("")) {
					pstmt.setString(i++, toDate);
				}
				if (param_fund != null && !param_fund.trim().equals("")) {
					pstmt.setString(i++, param_fund);
				}
				if (param_fromDate != null && !param_fromDate.trim().equals("")) {
					pstmt.setString(i++, fromDate);
				}
				if (param_toDate != null && !param_toDate.trim().equals("")) {
					pstmt.setString(i++, toDate);
				}
				if (param_fund != null && !param_fund.trim().equals("")) {
					pstmt.setString(i++, param_fund);
				}
			} else if (type.equalsIgnoreCase("1")) // supplier
			{
				query = "SELECT s.BILLNUMBER||' / '||v.vouchernumber as \"Bill Number\",s.supplierid,r.NAME AS \"NAME\",(s.PASSEDAMOUNT-(s.PAIDAMOUNT+s.otherrecoveries)) AS \"Bill Amount\",v.VOUCHERDATE AS \"Bill DATE\", "
						+ " gl.GLCODE AS \"Code of Account\",v.ID AS \"VHID\",'Supplier' AS \"Nature of Payable\",wd.NAME AS \"Work Order\",f.NAME AS \"FUND\", v.cgn"
						+ " FROM supplierbilldetail s,relation r,voucherheader v,generalledger gl,worksdetail wd,fund f"
						+ " WHERE s.supplierid=r.ID AND v.ID=s.voucherheaderid AND v.status=0 and r.relationtypeid=1 "
						+ whereCondition
						+ " "
						+ dateCondition
						+ fundCondition
						+ " AND gl.voucherheaderid=v.ID AND gl.DEBITAMOUNT>0"
						+ " AND wd.ID=s.WORKSDETAILID AND f.ID=wd.fundid "
						+ " GROUP BY s.ID,s.supplierid,r.NAME,s.PASSEDAMOUNT,s.PAIDAMOUNT,s.otherrecoveries,s.tdsamount,s.advadjamt,v.VOUCHERDATE,gl.GLCODE,v.ID,wd.NAME,f.NAME,s.BILLNUMBER,v.vouchernumber, v.cgn"
						+
						// " HAVING (s.PASSEDAMOUNT-(s.PAIDAMOUNT+s.otherrecoveries))>0 "
						// +
						"  HAVING (s.passedamount > (s.paidamount + s.tdsamount + s.advadjamt + s.otherrecoveries) )"
						+ " ORDER BY \"NAME\", \"Bill Number\", \"Bill DATE\",\"VHID\"";
				pstmt = con.prepareStatement(query);
				int i = 1;
				if (!relationId.equals("")){
					pstmt.setString(i++, relationId);
				}
				if (param_fromDate != null && !param_fromDate.trim().equals("")) {
					pstmt.setString(i++, fromDate);
				}
				if (param_toDate != null && !param_toDate.trim().equals("")) {
					pstmt.setString(i++, toDate);
				}
				if (param_fund != null && !param_fund.trim().equals("")) {
					pstmt.setString(i++, param_fund);
				}
			} else if (type.equalsIgnoreCase("2")) // contractor
			{
				query = " SELECT c.BILLNUMBER||' / '||v.vouchernumber as \"Bill Number\",c.contractorid,r.NAME AS \"NAME\",(c.PASSEDAMOUNT-(c.PAIDAMOUNT+c.otherrecoveries)) AS \"Bill Amount\",v.VOUCHERDATE AS \"Bill DATE\", "
						+ " gl.GLCODE AS \"Code of Account\",v.ID AS \"VHID\",'Contractor' AS \"Nature of Payable\",wd.NAME AS \"Work Order\", f.NAME AS \"FUND\", v.cgn "
						+ " FROM contractorbilldetail c,relation r,voucherheader v,generalledger gl,worksdetail wd,fund f"
						+ " WHERE c.contractorid=r.ID AND v.ID=c.voucherheaderid AND v.status=0 and r.relationtypeid=2 "
						+ whereCondition
						+ " "
						+ dateCondition
						+ fundCondition
						+ " AND gl.voucherheaderid=v.ID AND gl.DEBITAMOUNT>0"
						+ " AND wd.ID=c.WORKSDETAILID AND f.ID=wd.fundid"
						+ " GROUP BY c.ID,c.contractorid,r.NAME,c.PASSEDAMOUNT,c.PAIDAMOUNT,c.otherrecoveries,c.tdsamount,c.advadjamt,v.VOUCHERDATE,gl.GLCODE,v.ID,wd.NAME,f.NAME,c.BILLNUMBER,v.vouchernumber, v.cgn"
						+
						// " HAVING (c.PASSEDAMOUNT-(c.PAIDAMOUNT+c.otherrecoveries))>0"
						// +
						" HAVING (c.passedamount > (c.paidamount + c.tdsamount + c.advadjamt + c.otherrecoveries) )"
						+ " ORDER BY \"NAME\", \"Bill Number\",\"Bill DATE\",\"VHID\" ";

				pstmt = con.prepareStatement(query);
				int i = 1;
				if (!relationId.equals("")){
					pstmt.setString(i++, relationId);
				}
				if (param_fromDate != null && !param_fromDate.trim().equals("")) {
					pstmt.setString(i++, fromDate);
				}
				if (param_toDate != null && !param_toDate.trim().equals("")) {
					pstmt.setString(i++, toDate);
				}
				if (param_fund != null && !param_fund.trim().equals("")) {
					pstmt.setString(i++, param_fund);
				}
			}
			if(LOGGER.isInfoEnabled())     LOGGER.info("Query for O/S report=" + query);

			// stmt = con.createStatement();
			ResultSet rs = pstmt.executeQuery();

			HashMap<String, String> osMap = null;
			Integer srlNo = 1;
			String tempConSupName = "", tempVHID = "", codeOfAccount = "", cgn = "";
			String csName = "", natureOfPay = "", billDate = "", billAmt = "", fund = "", billNumber = "";
			Double tempTotal = 0.0;
			while (rs.next()) {
				// if same voucher header id, concatenate the account code
				if (tempVHID.equals(rs.getString("VHID"))
						|| tempVHID.equals("")) {
					if (!codeOfAccount.equals(""))
						codeOfAccount = codeOfAccount + ", "
								+ rs.getString("Code of Account");
					else
						codeOfAccount = rs.getString("Code of Account");

					natureOfPay = (rs.getString("Nature of Payable") == null ? ""
							: rs.getString("Nature of Payable"))
							+ "/ "
							+ (rs.getString("Work Order") == null ? "" : rs
									.getString("Work Order"));
					billDate = formatter.format(rs.getDate("Bill DATE"));
					billAmt = cf.numberToString(
							rs.getBigDecimal("Bill Amount").toString())
							.toString();
					billNumber = rs.getString("Bill Number");
					fund = rs.getString("FUND");
					cgn = rs.getString("cgn");
					tempTotal = rs.getDouble("Bill Amount");
					csName = rs.getString("NAME");
				} else {
					osMap = new HashMap<String, String>();
					osMap.put("srlNo", srlNo.toString());

					// if same supplier/contractor name, print once
					if (!csName.equals(tempConSupName))
						osMap.put("csName", csName);
					else
						osMap.put("csName", "&nbsp;");

					osMap.put("natureOfPayable", natureOfPay);
					osMap.put("codeOfAccount", codeOfAccount);
					osMap.put("billDate", billDate);
					osMap.put("billNumber", billNumber);
					osMap.put("cgn", cgn);
					osMap.put("billAmount", billAmt);
					osMap.put("fund", fund);
					osMap.put("remarks", "&nbsp;");

					tempConSupName = csName;

					total = total + tempTotal;
					codeOfAccount = rs.getString("Code of Account");
					natureOfPay = (rs.getString("Nature of Payable") == null ? ""
							: rs.getString("Nature of Payable"))
							+ "/ "
							+ (rs.getString("Work Order") == null ? "" : rs
									.getString("Work Order"));
					billDate = formatter.format(rs.getDate("Bill DATE"));
					billNumber = rs.getString("Bill Number");
					billAmt = cf.numberToString(
							rs.getBigDecimal("Bill Amount").toString())
							.toString();
					fund = rs.getString("FUND");
					cgn = rs.getString("cgn");
					tempTotal = rs.getDouble("Bill Amount");
					csName = rs.getString("NAME");

					osStmtList.add(osMap);
					srlNo++;
				}
				tempVHID = rs.getString("VHID");
			} // end of while loop

			if (tempTotal != 0) // printing the last record
			{
				osMap = new HashMap<String, String>();
				osMap.put("srlNo", srlNo.toString());
				if (!csName.equals(tempConSupName))
					osMap.put("csName", csName);
				else
					osMap.put("csName", "&nbsp;");
				osMap.put("natureOfPayable", natureOfPay);
				osMap.put("codeOfAccount", codeOfAccount);
				osMap.put("billDate", billDate);
				osMap.put("billNumber", billNumber);
				osMap.put("billAmount", billAmt);
				osMap.put("cgn", cgn);
				osMap.put("fund", fund);
				osMap.put("remarks", "&nbsp;");
				osStmtList.add(osMap);
				total = total + tempTotal;

				NumberFormat nf = NumberFormat.getInstance();
				nf.setGroupingUsed(false);
				nf.setMinimumFractionDigits(2);
				nf.setMaximumFractionDigits(2);
				amtInWords = NumberToWord.amountInWords(Double.parseDouble(nf.format(total)));

				osMap = new HashMap<String, String>();
				osMap
						.put("srlNo",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("csName",
								"<hr noshade color=black size=1><b>Total : </b><hr noshade color=black size=1>");
				osMap
						.put("natureOfPayable",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("codeOfAccount",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("billNumber",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("billDate",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap.put("billAmount", "<hr noshade color=black size=1>"
						+ cf.numberToString(total.toString()).toString()
						+ "<hr noshade color=black size=1>");
				osMap
						.put("fund",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("remarks",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap.put("cgn", "&nbsp;");
				osStmtList.add(osMap);

				osMap = new HashMap<String, String>();
				osMap.put("srlNo", "&nbsp;");
				osMap.put("csName", "<b>Amount(in words) : Rupees</b>");
				osMap.put("natureOfPayable", "&nbsp;");
				osMap.put("codeOfAccount", "&nbsp;");
				osMap.put("billDate", "&nbsp;");
				osMap.put("billNumber", "&nbsp;");
				osMap.put("billAmount", "<b>" + amtInWords + "</b>");
				osMap.put("fund", "&nbsp;");
				osMap.put("remarks", "&nbsp;");
				osMap.put("cgn", "&nbsp;");
				osStmtList.add(osMap);

				osMap = new HashMap<String, String>();
				osMap
						.put("srlNo",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put(
								"csName",
								"<hr noshade color=black size=1><b>Prepared By :</b><hr noshade color=black size=1>");
				osMap
						.put("natureOfPayable",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("codeOfAccount",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("billNumber",
								"<hr noshade color=black size=1><b>&nbsp;</b><hr noshade color=black size=1>");
				osMap
						.put("billDate",
								"<hr noshade color=black size=1><b>&nbsp;</b><hr noshade color=black size=1>");
				osMap
						.put(
								"billAmount",
								"<hr noshade color=black size=1><b>Checked By : </b><hr noshade color=black size=1>");
				osMap
						.put("fund",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap
						.put("remarks",
								"<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
				osMap.put("cgn", "&nbsp;");
				osStmtList.add(osMap);
			}
		} catch (Exception e) {
			LOGGER
					.error("Exception while getting the O/S stmt for liability expenses report="
							+ e.getMessage());

		} finally {
			try {
				pstmt.close();
				//This fix is for Phoenix Migration.EgovDatabaseManager.releaseConnection(con, null);
			} catch (Exception e) {
				LOGGER.error("Exp in finally...");
			}
		}
		returnMap.put("osStmtList", osStmtList);
		return returnMap;
	}
}
