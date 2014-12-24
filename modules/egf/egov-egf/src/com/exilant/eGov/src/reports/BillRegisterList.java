package com.exilant.eGov.src.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.exilant.exility.dataservice.DatabaseConnectionException;
public class BillRegisterList
{
	private static final Logger LOGGER = Logger.getLogger(BillRegisterList.class);

	private final static String ERRCONN = "Unable to get a connection from Pool Please make sure that the connection pool is set up properly";
	private double dAmount=0.0;
	private double cAmount=0.0;
	private String totalPassed;
	private String totalBillAmount;
	private String totalBill;
	private String totalPending;
	private String totalRejected;
	/**
	 *
	 */

	public BillRegisterList(){}

	/**
	 * @return Returns the cAmount.
	 */

	public double getCAmount() {
		return cAmount;
	}
	/**
	 * @param amount The cAmount to set.
	 */
	public void setCAmount(double amount) {
		cAmount = amount;
	}
	/**
	 * @return Returns the dAmount.
	 */
	public double getDAmount() {
		return dAmount;
	}
	/**
	 * @param amount The dAmount to set.
	 */
	public void setDAmount(double amount) {
		dAmount = amount;
	}
	private String covertDate(String changeDate)
		{
			String dateChanged ="";
			String dd="";
			String mm="";
			String yy="";
			int dateLenght = changeDate.length( );
			if(dateLenght==9)
			{
				dd=changeDate.substring(0,1);
				mm=month(changeDate.substring(2,4));

				yy=changeDate.substring(5,9);
			}
			if(dateLenght==10)
			{
				dd=changeDate.substring(0,2);
				mm=month(changeDate.substring(3,5));
				yy=changeDate.substring(6,10);
			}
			dateChanged = dd+"-"+mm+"-"+yy;


			return dateChanged;
		}

	private String month(String changeMonth)
		{
			int m = Integer.parseInt(changeMonth);
			 String  monthChanged = "";
			 switch(m)
			{
				case 1: monthChanged ="Jan"; break;
				case 2: monthChanged = "Feb"; break;
				case 3: monthChanged = "Mar"; break;
				case 4: monthChanged = "Apr"; break;
				case 5: monthChanged = "May"; break;
				case 6: monthChanged = "Jun"; break;
				case 7: monthChanged = "Jul"; break;
				case 8: monthChanged = "Aug"; break;
				case 9: monthChanged = "Sep"; break;
				case 10: monthChanged = "Oct"; break;
				case 11: monthChanged = "Nov"; break;
				case 12: monthChanged = "Dec"; break;
				default:  monthChanged = "";
			 }

			return monthChanged;
		}
	public String getQueryString(BillRegisterReportBean reportBean) {
		String dateStart="";
		String dateEnd="";
		////LOGGER.debug("5***1");
		String dateAppend="";
		String pOAppend="";
		String billAppStatusAppend= "";
		String billStatusAppend= "";
		String lastAppend="  ORDER BY PO";
		String returnQueryString ="";
		String unionAppend=" UNION ";
		String intersectAppend=" INTERSECT ";
		String creditorAppend="";

		String returnQueryStringOne = "SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS " +" BILLDATE"+
		", egb.BILLNUMBER, egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+" PAYREQDATE"+
		",cbd.PASSEDAMOUNT, cbd.PAIDAMOUNT, vh.VOUCHERNUMBER, TO_CHAR(vh.VOUCHERDATE, 'dd-Mon-yyyy') AS "+ " VOUCHERDATE"+ " FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		" CONTRACTORBILLDETAIL cbd WHERE (TO_CHAR(egb.id) = cbd.BILLNUMBER(+))AND (cbd.VOUCHERHEADERID = vh.ID (+)) ";

		String returnQueryStringOneU = "SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS " +" BILLDATE"+
		", egb.BILLNUMBER, egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+" PAYREQDATE"+
		",cbd.PASSEDAMOUNT, cbd.PAIDAMOUNT, vh.VOUCHERNUMBER, TO_CHAR(vh.VOUCHERDATE, 'dd-Mon-yyyy') AS "+ " VOUCHERDATE"+ " FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		" CONTRACTORBILLDETAIL cbd WHERE TO_CHAR(egb.id) = cbd.BILLNUMBER AND cbd.VOUCHERHEADERID = vh.ID  ";

		String returnQueryStringTwo = " SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS " + " BILLDATE"+
		 ", egb.BILLNUMBER,egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+" PAYREQDATE"+
		 ",cbd.PASSEDAMOUNT, cbd.PAIDAMOUNT, vh.VOUCHERNUMBER,TO_CHAR(vh.VOUCHERDATE, 'dd-Mon-yyyy') AS "+ " VOUCHERDATE"+ "  FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		 "SUPPLIERBILLDETAIL cbd WHERE (TO_CHAR(egb.id) = cbd.BILLNUMBER(+))AND (cbd.VOUCHERHEADERID = vh.ID (+))";

		String returnQueryStringTwoU = " SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS " + " BILLDATE"+
		 ", egb.BILLNUMBER,egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+" PAYREQDATE"+
		 ",cbd.PASSEDAMOUNT, cbd.PAIDAMOUNT, vh.VOUCHERNUMBER,TO_CHAR(vh.VOUCHERDATE, 'dd-Mon-yyyy') AS "+ " VOUCHERDATE"+ "  FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		 "SUPPLIERBILLDETAIL cbd WHERE TO_CHAR(egb.id) = cbd.BILLNUMBER AND cbd.VOUCHERHEADERID = vh.ID ";

		//String billCreditorOne= "  AND cbd.CONTRACTORID='"+reportBean.getCreditor()+"'";
		//String billCreditorTwo= "  AND cbd.SUPPLIERID='"+reportBean.getCreditor()+"'";
		////LOGGER.debug("##"+reportBean.getBill_Po());
		//LOGGER.debug("##"+reportBean.getBill_Creditor());
		//LOGGER.debug("##"+reportBean.getBill_Status());
		//LOGGER.debug("##"+reportBean.getBill_AppStaus());
		//LOGGER.debug("##"+reportBean.getStartDate());
		//LOGGER.debug("##"+reportBean.getEndDate());
		if(reportBean.getBill_Po()==null || reportBean.getBill_Po().equals(""))
		{
			//LOGGER.debug("@@@@@@@@@@@@@2");
			reportBean.setBill_Po("");
		}
		try
		{
			dateStart=covertDate(reportBean.getStartDate());
			//LOGGER.debug("5***2");
		//	//LOGGER.debug("3***");
			dateEnd=covertDate(reportBean.getEndDate());
			//LOGGER.debug("5***3");
			//LOGGER.debug("startDate :"+dateStart);
			//LOGGER.debug("endDate :"+dateEnd);
			//LOGGER.debug("4***");

			dateAppend=" AND egb.BILLDATE BETWEEN '"+dateStart+"' AND '"+dateEnd+"'";
			//LOGGER.debug("5***4");
			//pOAppend=" AND egb.PO='"+reportBean.getBill_Po()+"'";
			//LOGGER.debug("5***5");
			billAppStatusAppend= " AND egb.BILLAPPROVALSTATUS='"+reportBean.getBill_AppStaus()+"'";
			//LOGGER.debug("5***5");
			billStatusAppend= " AND egb.BILLSTATUS='"+reportBean.getBill_Status()+"'";
			//LOGGER.debug("5***6");
			creditorAppend="AND  egb.PO IN(SELECT code FROM WORKSDETAIL  WHERE RELATIONID ="+reportBean.getBill_Creditor()+")";
			if(reportBean.getBill_Po()!=null && !reportBean.getBill_Po().equals(""))
			{
				pOAppend=" AND egb.PO='"+reportBean.getBill_Po()+"'";
				returnQueryStringOne=returnQueryStringOne+pOAppend;
				returnQueryStringTwo=returnQueryStringTwo+pOAppend;
				returnQueryStringOneU=returnQueryStringOneU+pOAppend;
				returnQueryStringTwoU=returnQueryStringTwoU+pOAppend;

			}
			//LOGGER.debug("5***61");
			if(reportBean.getBill_Status()!=null &&!reportBean.getBill_Status().equals(""))
			{
				returnQueryStringOne=returnQueryStringOne+billStatusAppend;
				returnQueryStringTwo=returnQueryStringTwo+billStatusAppend;
				returnQueryStringOneU=returnQueryStringOneU+billStatusAppend;
				returnQueryStringTwoU=returnQueryStringTwoU+billStatusAppend;

			}
			//LOGGER.debug("5***62");
			if(reportBean.getBill_AppStaus()!=null && !reportBean.getBill_AppStaus().equals(""))
			{
				returnQueryStringOne=returnQueryStringOne+billAppStatusAppend;
				returnQueryStringTwo=returnQueryStringTwo+billAppStatusAppend;
				returnQueryStringOneU=returnQueryStringOneU+billAppStatusAppend;
				returnQueryStringTwoU=returnQueryStringTwoU+billAppStatusAppend;
			}
			//LOGGER.debug("5***63");
			if(reportBean.getStartDate()!=null && !reportBean.getStartDate().equals(""))
			{
				returnQueryStringOne=returnQueryStringOne+dateAppend;
				returnQueryStringTwo=returnQueryStringTwo+dateAppend;
				returnQueryStringOneU=returnQueryStringOneU+dateAppend;
				returnQueryStringTwoU=returnQueryStringTwoU+dateAppend;
			}
			//LOGGER.debug("5***64");
			if(reportBean.getBill_Creditor()!=null && !reportBean.getBill_Creditor().equals(""))
			{
				returnQueryStringOne=returnQueryStringOne+creditorAppend;
				returnQueryStringTwo=returnQueryStringTwo+creditorAppend;
				returnQueryStringOneU=returnQueryStringOneU+creditorAppend;
				returnQueryStringTwoU=returnQueryStringTwoU+creditorAppend;
			}
			//LOGGER.debug("5***65");
		}

		catch(Exception error){LOGGER.debug("Error:"+error);}
		finally
		{

		}
		//String returnQueryString = "SELECT PO, TO_CHAR(BILLDATE, 'dd-Mon-yyyy')  AS "+" BILLDATE"+", BILLNUMBER, BILLAMOUNT, BILLAPPROVALSTATUS, TO_CHAR(PAYREQDATE, 'dd-Mon-yyyy')  AS "+" PAYREQDATE"+" FROM EG_BILLREGISTER  WHERE BILLDATE between '"+dateStart+"' AND '"+dateEnd+"'" +
	    //"ORDER BY PO";
		/*String returnQueryString = "SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS " +"BILLDATE"+
		", egb.BILLNUMBER, egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+"PAYREQDATE"+
		",cbd.PASSEDAMOUNT, cbd.PAIDAMOUNT, vh.VOUCHERNUMBER, vh.VOUCHERDATE FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		" CONTRACTORBILLDETAIL cbd WHERE TO_CHAR(egb.id) = TO_CHAR(cbd.BILLNUMBER) AND  cbd.VOUCHERHEADERID = vh.ID "+
		 " UNION "+
		 "SELECT DISTINCT egb.PO, TO_CHAR(egb.BILLDATE, 'dd-Mon-yyyy')  AS" + "BILLDATE"+
		 ", egb.BILLNUMBER,egb.BILLAMOUNT, egb.BILLAPPROVALSTATUS, TO_CHAR(egb.PAYREQDATE, 'dd-Mon-yyyy')  AS "+"PAYREQDATE"+
		 ",sbd.PASSEDAMOUNT, sbd.PAIDAMOUNT, vh.VOUCHERNUMBER, vh.VOUCHERDATE FROM EG_BILLREGISTER egb , VOUCHERHEADER vh,"+
		 "SUPPLIERBILLDETAIL sbd WHERE TO_CHAR(egb.id) = TO_CHAR(sbd.BILLNUMBER)AND sbd.VOUCHERHEADERID = vh.ID  ORDER BY PO";*/



		return returnQueryStringOne+intersectAppend+returnQueryStringTwo+unionAppend+returnQueryStringOneU+unionAppend+returnQueryStringTwoU+lastAppend;


	}
	public LinkedList getBillRegisterList(BillRegisterReportBean reportBean)
	{
		Statement statement=null;
		ResultSet rs =null;
		
		String dateStart="";
		String dateEnd="";
		double crTotal = 0;
	    double dbTotal = 0;
		double totalBillAmount=0.0;
		int totalBill=0;
		int totalPassed=0;
		int totalPending=0;
		int totalRejected=0;
	    LinkedList links = new LinkedList();
	//    java.util.Map m=new java.util.TreeMap();
		//LOGGER.debug("1***");
	
	//	//LOGGER.debug("2***");
		/*try
		{
			dateStart=covertDate(reportBean.getStartDate());
		//	//LOGGER.debug("3***");
			dateEnd=covertDate(reportBean.getEndDate());
			//LOGGER.debug("startDate :"+dateStart);
			//LOGGER.debug("endDate :"+dateEnd);
			//LOGGER.debug("4***");
		}

		catch(Exception error){}*/
		/*String queryString =	"SELECT TO_CHAR(voucherdate, 'dd-Mon-yyyy')  AS "+" voucherdate"+", vouchernumber , gd.glcode AS " +" glcode"+ ",  ca.name AS "+ "particulars" + ",vh.name ||' - '|| vh.TYPE AS " + "type" +", vh.description AS "+ "narration" +", status , debitamount  ," +
		"creditamount,vh.CGN  FROM voucherheader vh, generalledger gd, chartofaccounts ca WHERE vh.ID=gd.VOUCHERHEADERID" + " AND ca.GLCODE=gd.GLCODE AND voucherdate between '"+dateStart+"' AND '"+dateEnd+"'" +
	    "ORDER BY voucherdate, vh.TYPE,vouchernumber,status";*/
		//LOGGER.debug("5***");

		String queryString = getQueryString(reportBean);

		//LOGGER.debug("queryString :"+queryString);
		try
		{
			rs=statement.executeQuery(queryString);
		}
		catch(Exception e)
		{
			LOGGER.debug("Exception in execute query :"+e);
		}
	//	LOGGER.debug("6***");
		try
		{
			while(rs.next())
			{
				BillRegister billRegister = new BillRegister();
				billRegister.setPo(rs.getString("PO"));
				billRegister.setBillAmount(rs.getString("BILLAMOUNT"));
				billRegister.setBillApprovalStatus(rs.getString("BILLAPPROVALSTATUS"));
				billRegister.setBillDate(rs.getString("BILLDATE"));
				billRegister.setBillNumber(rs.getString("BILLNUMBER"));
				billRegister.setPaidAmount(rs.getString("PAIDAMOUNT"));
				billRegister.setPassedAmount(rs.getString("PASSEDAMOUNT"));
				billRegister.setPaymentDate(rs.getString("PAYREQDATE"));
				billRegister.setVoucherDate(rs.getString("VOUCHERDATE"));
				billRegister.setVoucherNumbaer(rs.getString("VOUCHERNUMBER"));
				links.add(billRegister);

		        //m.put(rs.getString("BILLNUMBER"),billRegister);

				/*if(!reportBean.getBill_Po().equals(""))
				{
					totalBillAmount=totalBillAmount+Double.parseDouble(rs.getString("BILLAMOUNT"));
					totalBill=totalBill+1;
					if(rs.getString("BILLAPPROVALSTATUS").equals("PASSED"))
					{
						totalPassed=totalPassed+1;
					}
				}
				BillRegister.setTotalBillAmount(""+totalBillAmount);
				BillRegister.setTotalBill(""+totalBill);
				BillRegister.setTotalPassed(""+totalPassed);*/
				totalBillAmount += rs.getDouble("BILLAMOUNT");
				totalBill += 1;
				if(rs.getString("BILLAPPROVALSTATUS").equals("PASSED"))
				{
					totalPassed=totalPassed+1;
				}
				if(rs.getString("BILLAPPROVALSTATUS").equals("PENDING"))
				{
					totalPending=totalPending+1;
				}
				if(rs.getString("BILLAPPROVALSTATUS").equals("REJECTED"))
				{
					totalRejected=totalRejected+1;
				}


			}
			/*BillRegisterList.setTotalBillAmount(""+totalBillAmount);
			BillRegisterList.setTotalBill(""+totalBill);
			BillRegisterList.setTotalPassed(""+totalPassed);*/
			/*//LOGGER.debug("dbTotal:"+dbTotal);
			//LOGGER.debug("crTotal:"+crTotal);
			this.setCAmount(crTotal);
			this.setDAmount(dbTotal);*/
			this.setTotalBillAmount(""+totalBillAmount);
			this.setTotalBill(""+totalBill);
			this.setTotalPassed(""+totalPassed);
			this.setTotalRejected(""+totalRejected);
			this.setTotalPending(""+totalPending);
			//LOGGER.debug("totalBillAmount:"+totalBillAmount);
			//LOGGER.debug("totalBill:"+totalBill);
			//LOGGER.debug("totalPassed:"+totalPassed);
			//LOGGER.debug("totalPending:"+totalPending);
			//LOGGER.debug("totalRejected:"+totalRejected);
		}catch(Exception e){LOGGER.debug("Exception in while loop:"+e);}

		
		//links=new LinkedList(m.values());
		return links;
	}
	/**
	 * @return Returns the totalBill.
	 */
	public String getTotalBill() {
		return totalBill;
	}
	/**
	 * @param totalBill The totalBill to set.
	 */
	public  void setTotalBill(String totalBill) {
		this.totalBill = totalBill;
	}
	/**
	 * @return Returns the totalBillAmount.
	 */
	public String getTotalBillAmount() {
		return totalBillAmount;
	}
	/**
	 * @param totalBillAmount The totalBillAmount to set.
	 */
	public  void setTotalBillAmount(String totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	/**
	 * @return Returns the totalPassed.
	 */
	public String getTotalPassed() {
		return totalPassed;
	}
	/**
	 * @param totalPassed The totalPassed to set.
	 */
	public  void setTotalPassed(String totalPassed) {
		this.totalPassed = totalPassed;
	}
	/**
	 * @return Returns the totalPending.
	 */
	public String getTotalPending() {
		return totalPending;
	}
	/**
	 * @param totalPending The totalPending to set.
	 */
	public void setTotalPending(String totalPending) {
		this.totalPending = totalPending;
	}
	/**
	 * @return Returns the totalRejected.
	 */
	public String getTotalRejected() {
		return totalRejected;
	}
	/**
	 * @param totalRejected The totalRejected to set.
	 */
	public void setTotalRejected(String totalRejected) {
		this.totalRejected = totalRejected;
	}
}
