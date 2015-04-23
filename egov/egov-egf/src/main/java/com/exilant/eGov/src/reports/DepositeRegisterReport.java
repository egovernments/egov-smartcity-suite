package com.exilant.eGov.src.reports;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infstr.utils.EGovConfig;


import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class DepositeRegisterReport 
{
	Connection con;
    Statement statement;
    ResultSet rs;
    ResultSet resultset;
    ResultSet resultset1;
    ResultSet rs1;
    String startDate;
    String endDate;
    String fundName;
    String vhNumber;
    String fieldId;
    String functionaryId;
   
    double chequetotal;
    double cashtotal;
   
    TaskFailedException taskExc;
    ArrayList list;
    Date dt;
    EGovernCommon egc;
    int j;
    private static final Logger LOGGER = Logger.getLogger(DepositeRegisterReport.class); 
	String dateCondition=""; 
     String FundCondition="";
     String FunctionaryCondition="";
     String FieldCondition="";
     String VhCondition="";
     String wherecondition="";
     String tableCondition="";
     String rcptTableCondition="";
     String rcptWhereCondition="";
     public DepositeRegisterReport()
     {
        list = new ArrayList();
        dt = new Date();
        egc = new EGovernCommon();
        j = 0;
     }

     public ArrayList getDepositRegisterReport(String StartDate, String EndDate, String FundName,String field,String functionary)
     																throws TaskFailedException
     {
        String formstartDate = "";
        String formendDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        taskExc = new TaskFailedException();   
        
        try
        {
            con = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
        }
        catch(Exception exception)
        {
            LOGGER.error("Could Not Get Connection");
            throw taskExc;
        }
        try
        {
            startDate = StartDate;
            endDate = EndDate;
            fundName = FundName;
            fieldId=field;
            functionaryId=functionary;
            if(LOGGER.isInfoEnabled())     LOGGER.info("StartDate-> " + startDate + " EndDate  " + endDate + " FundName->" + fundName+ "FieldName->"+fieldId+ "VhNumber->"+vhNumber);
            try
            {
                dt = sdf.parse(startDate);
                formstartDate = formatter1.format(dt);
            }
            catch(Exception exception1) {            throw taskExc; }
            try
            {
               // dt = ;
                formendDate = formatter1.format(sdf.parse(endDate));
            }
            catch(Exception exception2) {             throw taskExc;}
            startDate = formstartDate;
            endDate = formendDate;
            if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate == null || endDate.equalsIgnoreCase("")))
                try
                {
                    statement = con.createStatement();
                    String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= SYSDATE AND endingDate >= SYSDATE";
                    rs = statement.executeQuery(query);
                    if(rs.next())
                        startDate = rs.getString("startingDate");
                    if(LOGGER.isDebugEnabled())     LOGGER.debug("in first condition :" + endDate);
                    rs.close();
                    String query1 = "SELECT TO_CHAR(sysdate, 'dd-Mon-yyyy') AS \"endingDate\" FROM dual";
                    rs = statement.executeQuery(query1);
                    if(rs.next())
                        endDate = rs.getString("endingDate");
                    if(LOGGER.isDebugEnabled())     LOGGER.debug("in first condition 1 :" + endDate);
                    rs.close();
                    statement.close();
                }
                catch(SQLException ex)
                {
                    throw taskExc;
                }
            if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate != null && !endDate.equalsIgnoreCase("")))
                try
                {
                    statement = con.createStatement();
                    String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= '" + endDate + "' AND endingDate >= '" + endDate + "'";
                    rs = statement.executeQuery(query);
                    if(rs.next())
                        startDate = rs.getString("startingDate");
                    if(LOGGER.isDebugEnabled())     LOGGER.debug("in second   condition :" + startDate+" endDate "+endDate);
                    
                    rs.close();
                    statement.close();
                }
                catch(SQLException ex)
                {
                    throw taskExc;
                }
            if((endDate == null || endDate.equalsIgnoreCase("")) && (startDate != null && !startDate.equalsIgnoreCase("")))
            {
                endDate = startDate;
                if(LOGGER.isDebugEnabled())     LOGGER.debug("in third   condition :" + endDate);
            }
           
            if(LOGGER.isInfoEnabled())     LOGGER.info("functionaryId: "+functionaryId);
            if(LOGGER.isInfoEnabled())     LOGGER.info("fieldId: "+fieldId);
            int fundId1=Integer.parseInt(fundName);
            if(LOGGER.isInfoEnabled())     LOGGER.info("fundName: "+fundName);
          // long  vhNumber1=Long.parseLong(vhNumber);
            //if(LOGGER.isInfoEnabled())     LOGGER.info("vhNumber: "+vhNumber1);

            if(startDate!=null &&  !startDate.equalsIgnoreCase("") && startDate.length()!=0)
            {
            	   dateCondition=" AND VH.VOUCHERDATE  >=  '"+startDate+"' AND VH.VOUCHERDATE  <='"+endDate+"'";
            }
            
            if(fundId1!=0)
            {
           	   //FundCondition="AND VMIS.SEGMENTID='"+  fundId1  +"'";
            	FundCondition="AND VH.FUNDID='"+  fundId1  +"'";
            }
            if(fieldId!=null)
            {
            	   //FieldCondition= "AND VMIS.DIVISIONID in (select id_bndry from eg_boundary where parent ='"+fieldId+"')"; 
            	FieldCondition= "AND VMIS.DIVISIONID ='"+fieldId+"' ";
            	tableCondition = " ,VOUCHERMIS VMIS";
            	wherecondition = " AND VH.ID = VMIS.VOUCHERHEADERID";
            	rcptTableCondition = ",EG_BOUNDARY div";
            	rcptWhereCondition = " AND vmis.DIVISIONID=div.ID_BNDRY ";
            }

            getReport();
           formatReport();
        }
        catch(Exception exception)
        {
        	LOGGER.error("EXP in getDepositRegisterReport: "+exception.getMessage());
            throw taskExc;
        }

        return list;
    }

     private void getReport()throws Exception
	 {
		String majorcodes[] = StringUtils.splitByWholeSeparator(EGovConfig.getProperty("egf_config.xml","majorCodes","","DepositRegReport"),",");
		String glcodes="";
		for(int i=0;i<majorcodes.length;i++)
		{
			if(!glcodes.equals(""))
				glcodes =glcodes +" OR ";
			glcodes = glcodes .concat(" GL.GLCODE LIKE '"+majorcodes[i]+"%' ");
		}
		if(!glcodes.equals(""))
			glcodes = " AND ( "+glcodes+" )";
		
    	 String query="SELECT  distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate ,ETH.PAYEE_NAME AS \"PAYEENAME\", " +
    	 		" rh.MODEOFCOLLECTION AS \"Mode1\",rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO as \"reciptnumber\", " +
 				" CASHAMOUNT as  cashamount,cd.AMOUNT AS  chqamt,'' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount " +
 				" from EGF_RECEIPT_MIS ERM,EGCL_TRANSACTION_HEADER ETH ,EG_BOUNDARY div, " +
 				" VOUCHERHEADER vh1  LEFT JOIN BANKRECONCILIATION Br ON (Br.VOUCHERHEADERID =vh1.ID),VOUCHERHEADER vh, " +
 				" RECEIPTHEADER rh,CHEQUEDETAIL cd,VOUCHERMIS vmis,GENERALLEDGER GL WHERE RH.VOUCHERHEADERID=VH.ID " +
 				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +
 				" AND VH.TYPE IN ('Receipts','Receipt') and vh.status=0 " +
 				" and Br.CHEQUENUMBER=cd.CHEQUENUMBER AND vmis.DIVISIONID=div.ID_BNDRY " +
 				" and Br.CHEQUEDATE=cd.CHEQUEDATE and rh.VOUCHERHEADERID =cd.VOUCHERHEADERID AND cd.isdeposited=1 and " +
 				" vh1.vouchernumber=cd.PAYINSLIPNUMBER AND vh1.VOUCHERDATE=cd.PAYINSLIPDATE AND vmis.VOUCHERHEADERID= vh.ID " +
 				" AND VH.ID=ERM.VOUCHERHEADERID AND ETH.ID_TRANS=ERM.ID_TRANS_HEADER AND VH.ID=GL.VOUCHERHEADERID " +
 				" "+glcodes+" AND GL.CREDITAMOUNT>0 " +
 				" UNION ALL " +
 				" select distinct TO_CHAR(VH.VOUCHERDATE , 'dd-Mon-yyyy') VOUCHERDATE,ETH.PAYEE_NAME AS \"PAYEENAME\", " +
				" rh.MODEOFCOLLECTION AS \"Mode1\",rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO as \"reciptnumber\", " +
				" RH.CASHAMOUNT AS CASHAMOUNT, CASE WHEN cd.AMOUNT IS NULL THEN 0 ELSE cd.AMOUNT END aS chqamt, " +
				" '' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount " +
				" FROM EGF_RECEIPT_MIS ERM,EGCL_TRANSACTION_HEADER ETH ,EG_BOUNDARY div,RECEIPTHEADER RH,VOUCHERHEADER VH ,CHEQUEDETAIL CD, " +
				" VOUCHERMIS vmis,GENERALLEDGER GL WHERE RH.VOUCHERHEADERID=VH.ID " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +
				" and vmis.DIVISIONID=div.ID_BNDRY AND VH.TYPE IN ('Receipts','Receipt') " +
				" and vh.status=0 AND rh.VOUCHERHEADERID=cd.VOUCHERHEADERID AND rh.CHEQUEID=cd.ID AND cd.isdeposited=0 and " +
				" vmis.VOUCHERHEADERID= vh.ID AND VH.ID=ERM.VOUCHERHEADERID AND ETH.ID_TRANS=ERM.ID_TRANS_HEADER " +
				" AND VH.ID=GL.VOUCHERHEADERID "+glcodes+" AND GL.CREDITAMOUNT>0 " +
				" UNION ALL " +
				" SELECT distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate,ETH.PAYEE_NAME AS \"PAYEENAME\", " +
				" rh.MODEOFCOLLECTION AS \"Mode1\",rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO as \"reciptnumber\", " +
				" CASHAMOUNT AS   cashamount,0 AS chqamt,'' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount " +
				" FROM EG_BOUNDARY div,RECEIPTHEADER RH, VOUCHERHEADER VH, " +
				" VOUCHERMIS vmis,EGF_RECEIPT_MIS ERM,EGCL_TRANSACTION_HEADER ETH,GENERALLEDGER GL WHERE " +
				" RH.VOUCHERHEADERID=VH.ID AND(rh.MODEOFCOLLECTION='cash' OR rh.MODEOFCOLLECTION='Cash') " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +
				" and  vmis.DIVISIONID=div.ID_BNDRY AND VH.TYPE IN ('Receipts','Receipt') and vh.status=0 and vmis.VOUCHERHEADERID= vh.ID " +
				" AND VH.ID=ERM.VOUCHERHEADERID AND ETH.ID_TRANS=ERM.ID_TRANS_HEADER AND VH.ID=GL.VOUCHERHEADERID " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0 " +
				" UNION ALL " +
				" SELECT distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate, ETH.PAYEE_NAME AS \"PAYEENAME\"," +
				" rh.MODEOFCOLLECTION AS \"Mode1\",rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO as \"reciptnumber\", " +
				" 0 as cashamount, cd.AMOUNT AS  chqamt,'' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount " +
				" FROM CHEQUEDETAIL CD ,EG_BOUNDARY div,RECEIPTHEADER RH,VOUCHERHEADER vh1  LEFT JOIN BANKRECONCILIATION Br ON(Br.VOUCHERHEADERID =vh1.ID), VOUCHERHEADER VH, " +
				" VOUCHERMIS vmis,EGF_RECEIPT_MIS ERM,EGCL_TRANSACTION_HEADER ETH,GENERALLEDGER GL WHERE   " +
				" rh.VOUCHERHEADERID=cd.VOUCHERHEADERID  AND cd.isdeposited=1 and vh1.vouchernumber=cd.PAYINSLIPNUMBER AND   " +
				" vh1.VOUCHERDATE=cd.PAYINSLIPDATE and Br.CHEQUENUMBER=cd.CHEQUENUMBER and Br.CHEQUEDATE=cd.CHEQUEDATE AND   " +
				" RH.VOUCHERHEADERID=VH.ID AND (rh.MODEOFCOLLECTION='cheque' OR rh.MODEOFCOLLECTION='Cheque')    " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ "  " +
				" AND  vmis.DIVISIONID=div.ID_BNDRY AND VH.TYPE IN ('Receipts','Receipt') and vh.status=0 and vmis.VOUCHERHEADERID=vh.ID   " +
				" AND VH.ID=ERM.VOUCHERHEADERID AND ETH.ID_TRANS=ERM.ID_TRANS_HEADER AND VH.ID=GL.VOUCHERHEADERID   " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0  " +
				" UNION ALL     " + // CASH RECEIPT
				" SELECT distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate,'' AS \"PAYEENAME\",rh.MODEOFCOLLECTION AS \"Mode1\",  " +
				" rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO AS \"reciptnumber\",CASHAMOUNT AS cashamount, 0 AS chqamt,  " +
				" '' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount  " +
				" FROM RECEIPTHEADER RH,VOUCHERHEADER VH,GENERALLEDGER GL  " +rcptTableCondition+tableCondition+
				" WHERE RH.VOUCHERHEADERID=VH.ID  " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +rcptWhereCondition+wherecondition+
				" AND (rh.MODEOFCOLLECTION='Cash' OR rh.MODEOFCOLLECTION='cash')   " +
				" AND VH.TYPE IN ('Receipts','Receipt') AND vh.status=0 AND   " +
				" NOT EXISTS (SELECT voucherheaderid FROM EGF_RECEIPT_MIS WHERE voucherheaderid=vh.ID) AND VH.ID=GL.VOUCHERHEADERID   " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0  " +
				" UNION ALL      " + // CHEQUE RECEIPT
				" SELECT distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate,'' AS \"PAYEENAME\",rh.MODEOFCOLLECTION AS \"Mode1\",  " +
				" rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO as \"reciptnumber\",0 AS cashamount, cd.AMOUNT AS chqamt,  " +
				" '' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount   " +
				" FROM chequedetail cd,RECEIPTHEADER RH,VOUCHERHEADER VH " +rcptTableCondition+tableCondition+" ,VOUCHERHEADER vh1 "+  
				" LEFT JOIN BANKRECONCILIATION Br ON(Br.VOUCHERHEADERID =vh1.ID),GENERALLEDGER GL WHERE  " +
				" rh.VOUCHERHEADERID=cd.VOUCHERHEADERID AND cd.isdeposited=1 AND vh1.vouchernumber=cd.PAYINSLIPNUMBER AND   " +
				" vh1.VOUCHERDATE=cd.PAYINSLIPDATE AND Br.CHEQUENUMBER=cd.CHEQUENUMBER AND Br.CHEQUEDATE=cd.CHEQUEDATE AND   " +
				" RH.VOUCHERHEADERID=VH.ID  " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +rcptWhereCondition+wherecondition+
				" AND (rh.MODEOFCOLLECTION='Cheque' OR rh.MODEOFCOLLECTION='cheque')  " +
				" AND VH.TYPE IN ('Receipts','Receipt') AND vh.status=0  " +
				" AND NOT EXISTS (SELECT voucherheaderid FROM EGF_RECEIPT_MIS WHERE voucherheaderid=vh.ID)    " +
				" AND VH.ID=GL.VOUCHERHEADERID "+glcodes+" AND GL.CREDITAMOUNT>0  " +
				" UNION ALL    " +  // BANK RECEIPT
				" SELECT distinct TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') voucherdate,'' AS \"PAYEENAME\",rh.MODEOFCOLLECTION AS \"Mode1\",  " +
				" rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO AS \"reciptnumber\",CASHAMOUNT AS cashamount, 0 AS chqamt,  " +
				" '' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount  " +
				" FROM RECEIPTHEADER RH, VOUCHERHEADER VH,GENERALLEDGER GL " +rcptTableCondition+tableCondition+
				" WHERE  " +
				" RH.VOUCHERHEADERID=VH.ID  " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " + rcptWhereCondition+wherecondition+
				" AND rh.MODEOFCOLLECTION='bank' AND   " +
				" VH.TYPE IN ('Receipts','Receipt') AND vh.status=0   " +
				" AND NOT EXISTS (SELECT voucherheaderid FROM EGF_RECEIPT_MIS WHERE voucherheaderid=vh.ID) AND VH.ID=GL.VOUCHERHEADERID  " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0  " +
				" UNION ALL    " +  // RECEIPTS
				" SELECT distinct TO_CHAR(VH.VOUCHERDATE , 'dd-Mon-yyyy')  VOUCHERDATE,'' AS \"PAYEENAME\",rh.MODEOFCOLLECTION AS \"Mode1\",  " +
				" rh.MANUALRECEIPTNUMBER as \"manualreciptnumber\",rh.RECEIPTNO AS \"reciptnumber\",RH.CASHAMOUNT AS CASHAMOUNT,  " +
				" CASE WHEN cd.AMOUNT IS NULL THEN 0 ELSE cd.AMOUNT END AS chqamt,	'' as vouchernumberanddate,'' as income,'' as year,0 as incomeadjustmentamount  " +
				" FROM RECEIPTHEADER RH,VOUCHERHEADER VH ,CHEQUEDETAIL CD,GENERALLEDGER GL " +rcptTableCondition+tableCondition+
				" WHERE RH.VOUCHERHEADERID=VH.ID  " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ "  " +rcptWhereCondition+wherecondition+
				" AND VH.TYPE IN ('Receipts','Receipt') AND vh.status=0   " +
				" AND	rh.VOUCHERHEADERID=cd.VOUCHERHEADERID AND rh.CHEQUEID=cd.ID AND cd.isdeposited=0    " +
				" AND NOT EXISTS (SELECT voucherheaderid FROM EGF_RECEIPT_MIS WHERE voucherheaderid=vh.ID) AND VH.ID=GL.VOUCHERHEADERID   " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0  " +
				" UNION ALL   " + // JOURNAL VOUCHER CREDIT
				" SELECT distinct TO_CHAR(VH.VOUCHERDATE , 'dd-Mon-yyyy') as VOUCHERDATE,'' AS \"PAYEENAME\",'' AS \"Mode1\",'' as \"manualreciptnumber\",vh.vouchernumber AS \"reciptnumber\", " + 
				" gl.creditamount as  cashamount,0 AS  chqamt,'' as vouchernumberanddate,   " +
				" '' as income,'' as year,0 as incomeadjustmentamount  " +
				" FROM VOUCHERHEADER VH,GENERALLEDGER GL,FINANCIALYEAR FY,CHARTOFACCOUNTS COA,FISCALPERIOD FP,VOUCHERMIS vmis "+tableCondition+"   " +
				" WHERE VH.ID=GL.VOUCHERHEADERID "+wherecondition+" " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ "  " +
				" "+glcodes+" AND GL.CREDITAMOUNT>0 AND vmis.VOUCHERHEADERID=vh.ID " +
				" AND COA.GLCODE=GL.GLCODE AND VH.FISCALPERIODID=FP.ID AND FP.FINANCIALYEARID=FY.ID AND VH.TYPE='Journal Voucher'  " +
				" UNION ALL   " + // JOURNAL VOUCHER DEBIT
				" SELECT distinct '' as VOUCHERDATE,'' AS \"PAYEENAME\",'' AS \"Mode1\",'' as \"manualreciptnumber\",'' AS \"reciptnumber\", " + 
				" 0 as  cashamount,0 AS  chqamt,VOUCHERNUMBER||'/ '||TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') vouchernumberanddate,   " +
				" COA.GLCODE||'-'||COA.NAME as income,fy.FINANCIALYEAR as year,GL.DEBITAMOUNT as incomeadjustmentamount  " +
				" FROM VOUCHERHEADER VH,GENERALLEDGER GL,FINANCIALYEAR FY,CHARTOFACCOUNTS COA,FISCALPERIOD FP "+tableCondition+"   " +
				" WHERE VH.ID=GL.VOUCHERHEADERID "+wherecondition+" " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ "  " +
				" "+glcodes+" AND GL.DEBITAMOUNT>0  " +
				" AND COA.GLCODE=GL.GLCODE AND VH.FISCALPERIODID=FP.ID AND FP.FINANCIALYEARID=FY.ID AND VH.TYPE='Journal Voucher'  " +
				" UNION ALL    " +  // PAYMENT
				" SELECT distinct '' as VOUCHERDATE,'' AS \"PAYEENAME\",'' AS \"Mode1\",'' as \"manualreciptnumber\",'' AS \"reciptnumber\",  " +
				" 0 as  cashamount,0 AS  chqamt,VOUCHERNUMBER||'/ '||TO_CHAR(vh.voucherdate, 'dd-Mon-yyyy') vouchernumberanddate,  " +
				" COA.GLCODE||'-'||COA.NAME as income,fy.FINANCIALYEAR as year,GL.DEBITAMOUNT as incomeadjustmentamount   " +
				" FROM VOUCHERHEADER VH,GENERALLEDGER GL,FINANCIALYEAR FY,CHARTOFACCOUNTS COA,FISCALPERIOD FP "+tableCondition+"   " +
				" WHERE VH.ID=GL.VOUCHERHEADERID "+wherecondition+" " +
				"  "+dateCondition+" "+FieldCondition+" "+FundCondition+ " " +
				" "+glcodes+"  " +
				" AND GL.DEBITAMOUNT>0  " +
				" AND COA.GLCODE=GL.GLCODE AND VH.FISCALPERIODID=FP.ID AND FP.FINANCIALYEARID=FY.ID AND VH.TYPE='Payment'  " +
				" ORDER BY VOUCHERDATE,\"reciptnumber\" ";
	 	if(LOGGER.isDebugEnabled())     LOGGER.debug("&&&query  " + query);
        try
        {
           String vDate="";
           String partyName="";
           String modeOfDeposite="";
           String receiptNumber="";
           String amount="";
           String dateandVoucher="";
           String income="";
           String fYear="";
           String chqAmount="";
           int sno=0;
          
            DepositeRegisterReportBean dBean = null;
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            
            while(rs.next())
            {
            	sno++;
            	dBean = new DepositeRegisterReportBean();
            	if(rs.getString("VOUCHERDATE")!= null)
            		vDate=rs.getString("VOUCHERDATE");
            	else
            		vDate="&nbsp;";
            	
            	if(rs.getString("PAYEENAME")!= null)
            		partyName=rs.getString("PAYEENAME");
            	else
            		partyName="&nbsp;";
             	
            	if(rs.getString("Mode1")!= null)
            		modeOfDeposite=rs.getString("Mode1");
            	else
            		modeOfDeposite="&nbsp;";
             	
            	if(rs.getString("reciptnumber")!= null)
            		receiptNumber=rs.getString("reciptnumber");
            	else
            		receiptNumber="&nbsp;";

            	dateandVoucher=(rs.getString("vouchernumberanddate")==null)?"&nbsp;":rs.getString("vouchernumberanddate");
        		amount=(rs.getString("cashamount")==null?(rs.getString("chqamt")==null?"0":rs.getString("chqamt")):rs.getString("cashamount"));
        		chqAmount=(rs.getString("incomeadjustmentamount")==null?"0":rs.getString("incomeadjustmentamount"));
        		dBean.setAmount(numberToString(amount).toString());
        		dBean.setChequeAmount(numberToString(chqAmount).toString());

        		if(rs.getString("income")!= null)
            		income=rs.getString("income");
	         	else
	         		income="&nbsp;";
            	
            	if(rs.getString("year")!= null)
            		fYear=rs.getString("year");
	         	else
	         		fYear="&nbsp;";
             	
             	cashtotal += Double.parseDouble(amount);
            	chequetotal += Double.parseDouble(chqAmount);
    			dBean.setSno(Integer.toString(sno));
    			dBean.setIncome(income);
    			dBean.setYear(fYear);
    			
    			//dBean.setBdeposite(balanceDeposite);        			
    			dBean.setBdeposite((cashtotal - chequetotal)+"");
    			dBean.setPayeeName(partyName);
				dBean.setReciptnumber(receiptNumber);
				dBean.setVoucherDate(vDate);
				dBean.setVoucherNumber(dateandVoucher);						
				dBean.setMode(modeOfDeposite);	
				list.add(dBean);
            }
        }
        catch(Exception e)
        {
            LOGGER.error("Error in getReport"+e.getMessage());
            throw taskExc;
        }
    }

     private void formatReport()
     {
     	DepositeRegisterReportBean tb = new DepositeRegisterReportBean();
     	tb.setIncome("<hr> &nbsp; <hr>");
     	tb.setYear("<hr> &nbsp; <hr>");
 		tb.setAmount("<hr><b>" + (new BigDecimal(cashtotal)).setScale(2, 4) + "</b><hr>");
 		tb.setChequeAmount("<hr><b>" + (new BigDecimal(chequetotal)).setScale(2, 4) + "</b><hr>");
 		tb.setBdeposite("<hr>&nbsp;<hr>");       			
 		tb.setPayeeName("<hr> &nbsp; <hr>");
 		tb.setReciptnumber("<hr><b> Total :<b>  <hr>");
 		tb.setVoucherDate("<hr> &nbsp; <hr>");
 		tb.setVoucherNumber("<hr> &nbsp; <hr>");						
 		tb.setMode("<hr> &nbsp; <hr>");	
        list.add(tb);
     }
    public static StringBuffer numberToString(String strNumberToConvert)
    {
        String strNumber = "";
        String signBit = "";
        if(strNumberToConvert.startsWith("-"))
        {
            strNumber = (new StringBuilder()).append(strNumberToConvert.substring(1, strNumberToConvert.length())).toString();
            signBit = "-";
        } else
        {
            strNumber = (new StringBuilder()).append(strNumberToConvert).toString();
        }
        DecimalFormat dft = new DecimalFormat("##############0.00");
        String strtemp = (new StringBuilder()).append(dft.format(Double.parseDouble(strNumber))).toString();
        StringBuffer strbNumber = new StringBuffer(strtemp);
        int intLen = strbNumber.length();
        for(int i = intLen - 6; i > 0; i -= 2)
            strbNumber.insert(i, ',');

        if(signBit.equals("-"))
            strbNumber = strbNumber.insert(0, "-");
        return strbNumber;
    }
 

}
