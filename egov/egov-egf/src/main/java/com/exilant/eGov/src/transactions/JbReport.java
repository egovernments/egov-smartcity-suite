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
 * Created on Jul 6, 2005
 * @author pushpendra.singh
 */
package com.exilant.eGov.src.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;


import com.exilant.GLEngine.GeneralLedgerBean;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class JbReport 
{
	Connection connection;
	PreparedStatement pst;
	ResultSet resultset;
	TaskFailedException taskExc;
	String startDate, endDate,fromFund,toFund,fromFundSource,toFundSource,revEntry;
	String fundSourceCond="";
    String addTableToQuery="";
	private String voucherNameCond;
	private String deptNameCondition;
    private static final Logger LOGGER = Logger.getLogger(JbReport.class);
	public JbReport(){}
	    
    public LinkedList getJbReport(GeneralLedgerBean reportBean)
    throws TaskFailedException
    {     
    	if(LOGGER.isInfoEnabled())     LOGGER.info("entered java file");
        LinkedList dataList = new LinkedList();
        try
        {
            connection = null;//This fix is for Phoenix Migration.EgovDatabaseManager.openConnection();
            //if(LOGGER.isDebugEnabled())     LOGGER.debug("connection"+connection);

        }
        catch(Exception exception)
        {
            throw new TaskFailedException();
        }
        NumberFormat formatter = new DecimalFormat();              
        formatter = new DecimalFormat("###############0.00");       
       // revEntry = reportBean.getRevEntry();
        reportBean.setUlbName(getULBName());
        
        String formstartDate="";
        String formendDate="";
       
            //if(LOGGER.isInfoEnabled())     LOGGER.info("within try");
            SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
            Date dt=new Date();       
//          
            
            String endDate1=(String)reportBean.getEndDate();
            isCurDate(connection,endDate1);
            try
            {
            endDate=(String)reportBean.getEndDate();
            dt = sdf.parse(endDate );
            formendDate = formatter1.format(dt);
            }
            catch(Exception e){
                if(LOGGER.isDebugEnabled())     LOGGER.debug("inside the try-startdate"+e);
            }
            try
            {   startDate=(String)reportBean.getStartDate();
                if(startDate.equalsIgnoreCase("null")){
                }else{
                dt = sdf.parse(startDate);
                formstartDate = formatter1.format(dt);
                }
            }

            catch(Exception e){
                if(LOGGER.isDebugEnabled())     LOGGER.debug("inside the try-startdate"+e);
            }            
                startDate = formstartDate;
           
            //if(LOGGER.isDebugEnabled())     LOGGER.debug("startDate22 "+startDate);
            endDate = formendDate;
            String startDateformat = startDate;
            String startDateformat1 = "";
            try{
            dt = formatter1.parse(startDateformat);
            startDateformat1 = sdf.format(dt);
            }
            catch(Exception e){
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Parse Exception"+e);
            }               
                       
        setDates(reportBean);        
      //  reportBean.setStartDate(startDate);
      // reportBean.setEndDate(endDate);     
        String fyId = getFYID(startDate);      
        if(fyId.equalsIgnoreCase(""))
        {
            //if(LOGGER.isDebugEnabled())     LOGGER.debug("Financial Year Not Valid");
            if(LOGGER.isInfoEnabled())     LOGGER.info("eGovFailure,"+"JbReport, Financial Year Not Valid");
            throw taskExc;
        }

        String fundId = reportBean.getFund_id();
        String fundSourceId = reportBean.getFundSource_id();
        if((fundId == null || fundId.equalsIgnoreCase("")))
        {
            try
            {
            	String query = "SELECT max(id) as \"max\"  FROM fund";
            	pst = connection.prepareStatement(query);
                resultset = pst.executeQuery();
                if(resultset.next())
                    toFund = resultset.getString("max");
                fromFund="1";
                resultset = null;
            }
            catch(SQLException ex)
            {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Error getting max fund: " + ex.toString());
                throw new TaskFailedException();
            }
        }
        else
        {
            toFund=fundId;
            fromFund=fundId;
        }

        if(LOGGER.isInfoEnabled())     LOGGER.info("fundSourceId:"+fundSourceId);
        if((fundSourceId == null || fundSourceId.trim().equalsIgnoreCase("")))
        {
            try
            {
            	String query1 = "SELECT max(id) as \"max\"  FROM fundsource";
            	pst = connection.prepareStatement(query1);
                resultset = pst.executeQuery();
                if(resultset.next())
                toFundSource = resultset.getString("max");
                fromFundSource="1";
                resultset = null;
            }
            catch(SQLException ex)
            {
                if(LOGGER.isDebugEnabled())     LOGGER.debug("Error getting max fundSource: " + ex.toString());
                throw new TaskFailedException();
            }
        }
        else
        {
        	if(LOGGER.isInfoEnabled())     LOGGER.info("inside esle addTableToQuery:"+addTableToQuery);
            toFundSource=fundSourceId;
            fromFundSource=fundSourceId;
            //addTableToQuery=", vouchermis vmis ";
            fundSourceCond=" and	vmis.fundsourceid=d.id ";   
            
        }

        if(reportBean.getVoucher_name()!=null && !reportBean.getVoucher_name().equals(""))
        {
        	voucherNameCond=" and a.Name='"+reportBean.getVoucher_name()+"' ";
        }
        else
        {
        	voucherNameCond=" ";
        }
        if(reportBean.getDept_name()!=null && !reportBean.getDept_name().equals(""))
        {
        	deptNameCondition=" and vmis.departmentid="+reportBean.getDept_name()+" ";
        }
        else
        {
        	deptNameCondition=" ";
        }

        String query = getQuery();
        if(LOGGER.isDebugEnabled())     LOGGER.debug("**************QUERY: " + query);
        try
        {
            pst.close();
            pst = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultset = pst.executeQuery();

            /**
             * When using ResultSet.TYPE_SCROLL_INSENSITIVE in createStatement
             * if no records are there, rs.next() will return true
             * but when trying to access (rs.getXXX()), it will throw an error
             **/
            //if(LOGGER.isDebugEnabled())     LOGGER.debug("RS: " + resultset.isBeforeFirst() + " " + resultset.isAfterLast());

            ArrayList data = new ArrayList();
            String voucherNo="", vcDate="", name="", cgn="",vhId = "", glcode="", accName="", desc="", curVou="",isconfirmed="", rowNumber="1",vname="";
            Double dr, cr;           
            int totalCount=0, isConfirmedCount=0;
            String vn2="";
                while(resultset.next())
                {
                    voucherNo = resultset.getString("vNumber");
                    cgn = resultset.getString("cgn");
                    vhId = resultset.getString("vhId");
                    vcDate = resultset.getString("vDate");
                    //name = resultset.getString("fund");
                    glcode = resultset.getString("glcode");
                    accName = resultset.getString("accountname");
                    desc = resultset.getString("desc");
                    dr = resultset.getDouble("debit");                   
                    cr = resultset.getDouble("credit");
                    isconfirmed= resultset.getString("isconfirmed")==null?"":resultset.getString("isconfirmed");
                    vname=resultset.getString("vname");
                //  if(LOGGER.isDebugEnabled())     LOGGER.debug("isconfirmed -->   "+isconfirmed);
                    //9 is the dummy value used in the query

                    if(!isconfirmed.equalsIgnoreCase(""))
                    {
                        String vn1=resultset.getString("vNumber");
                        if(!vn1.equalsIgnoreCase(vn2))
                        {
                            vn2=vn1;
                            totalCount=totalCount + 1;
                            if(isconfirmed.equalsIgnoreCase("0"))
                            {
                                isConfirmedCount=isConfirmedCount+1;
                            }
                        }
                    }
                    //if(LOGGER.isDebugEnabled())     LOGGER.debug("totalCount -->   "+totalCount);
                    //if(LOGGER.isDebugEnabled())     LOGGER.debug("isConfirmedCount -->   "+isConfirmedCount);
                    reportBean.setTotalCount(Integer.toString(totalCount));
                    reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));                    
                    if(!curVou.equalsIgnoreCase(voucherNo))
                    {
                        String arr[] = new String[10];
                        //arr[0] = rowNumber;
                        arr[0] = vcDate;
                        arr[1] = voucherNo;
                        arr[7] = cgn;
                        arr[9] = vhId;
                        arr[2] = arr[3] = arr[5] = arr[6] = "&nbsp;";
                        if(vname != null && vname!="")
                            arr[8] = vname;
                        else
                            arr[8]="&nbsp;";
                        if(desc != null)
                            arr[4] = "&nbsp;"+desc;
                        else
                            arr[4] = "&nbsp;";
                        data.add(arr);
                        //int i=Integer.parseInt(rowNumber)+1;
                        //Integer i1=new Integer(i);
                        //rowNumber=i1.toString();
                    }
                    String arr1[] = new String[10];
                    arr1[0] = "&nbsp;";
                    arr1[1] = "";
                    arr1[2] = glcode;
                    arr1[3] = accName;
                    arr1[7]=cgn;
                    arr1[9]=vhId;
                    arr1[8]="";
                    arr1[4] = "";
                    if(dr != null && dr>0)
                        arr1[5] = numberToString(dr.toString()).toString();
                    else 
                        arr1[5]="&nbsp;";
                    if(cr != null && cr>0)
                        arr1[6] = numberToString(cr.toString()).toString();
                    else
                        arr1[6]="&nbsp;";
                    
                    data.add(arr1);

            curVou=voucherNo;
            }

            //Adding data to 2 dimension array to pass to Bean
            String gridData[][] = new String[data.size()+1][10];            
            gridData[0][0] = "voucherdate";
            gridData[0][1] = "vouchernumber";            
            gridData[0][2] = "code";
            gridData[0][3] = "accName";
            gridData[0][4] = "narration";
            gridData[0][5] = "debit";
            gridData[0][6] = "credit";
            gridData[0][7] = "cgn";
            gridData[0][8] = "vname";
            gridData[0][9] = "vhId";
            for(int i=1; i<=data.size(); i++)
                gridData[i] = (String[])data.get(i-1);

            for(int i=1; i<=data.size(); i++)
            {
                GeneralLedgerBean jbReportBean = new GeneralLedgerBean();
                jbReportBean.setVoucherdate(gridData[i][0]);
                jbReportBean.setVouchernumber(gridData[i][1]);
                jbReportBean.setCode(gridData[i][2]);
                jbReportBean.setAccName(gridData[i][3]);
                jbReportBean.setNarration(gridData[i][4]);
                jbReportBean.setDebitamount(gridData[i][5]);
                jbReportBean.setCreditamount(gridData[i][6]);
                jbReportBean.setCGN(gridData[i][7]);
                jbReportBean.setVhId(gridData[i][9]);
                jbReportBean.setTotalCount(Integer.toString(totalCount));
                jbReportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
                jbReportBean.setVoucherName(gridData[i][8]);
                dataList.add(jbReportBean);
            }                
            return dataList;      
        } 
        catch(Exception ex)
        {
        	LOGGER.error("Exp="+ex.getMessage());
            if(LOGGER.isDebugEnabled())     LOGGER.debug("ERROR: " + ex.toString());
            //dc.addValue("status", "nodata");
            throw new TaskFailedException();
        }
              
    }  
    
	private String getQuery(){
		//if(revEntry.equalsIgnoreCase("on")){
			return "SELECT voucherdate as \"vDate1\",to_char(voucherdate,'dd-Mon-yyyy') as \"vDate\",vouchernumber as \"vNumber\",a.cgn as \"cgn\",c.name as \"fund\",glcode as \"glcode\", "+
			" b.accountname as \"accountname\",a.description as \"desc\",a.isconfirmed as \"isconfirmed\",debitamount as \"debit\",creditamount as \"credit\",a.name as \"vname\",1 as \"dbOrCr\",a.id as \"vhId\" "+
			" from voucherheader a,voucherdetail b,fund c,fundsource d , vouchermis vmis " +
			"  where a.id=b.voucherheaderid "+ fundSourceCond + voucherNameCond + deptNameCondition +
			" and a.fundid=c.id and c.id >= "+fromFund+" and c.id <= "+toFund+" "+
			"  and d.id >= "+fromFundSource+" and d.id <= "+toFundSource+" "+
			" and voucherdate>='"+startDate+"' "+
			" and voucherdate<='"+endDate+"' and debitamount>0   "+
            " and a.status not in (4,5) "+
			" and( cgn like 'JVG%' or  cgn like 'CJV%' or  cgn like 'SJV%' or cgn like 'SAL%' or cgn like 'OJV%')  AND vmis.voucherheaderid=a.id "+
			" union "+
			" SELECT voucherdate as \"vDate1\", to_char(voucherdate,'dd-Mon-yyyy') as \"vDate\",vouchernumber as \"vNumber\",a.cgn as \"cgn\",c.name as \"fund\", "+
			" glcode as \"glcode\",b.accountname as \"accountname\",a.description as \"desc\",a.isconfirmed as \"isconfirmed\",debitamount as \"debit\",creditamount as \"credit\",a.name as \"vname\", "+
			" 2 as \"dbOrCr\",a.id as \"vhId\" "+
			" from voucherheader a,voucherdetail b,fund c,fundsource d , vouchermis vmis " +
			" where b.voucherheaderid=a.id "+ fundSourceCond + voucherNameCond+ deptNameCondition +
			" and c.id >="+fromFund+" and c.id <= "+toFund+" "+
			" and d.id >="+fromFundSource+" and d.id <= "+toFundSource+" "+
			" and a.fundid=c.id  and voucherdate >='"+startDate+"'  "+
			" and voucherdate<='"+endDate+"' and creditamount>0  "+
            " and a.status not in (4,5) " +
			" and( cgn like 'JVG%' or  cgn like 'CJV%' or  cgn like 'SJV%' or cgn like 'SAL%' or cgn like 'OJV%')  AND vmis.voucherheaderid=a.id "+
			" ORDER BY \"vDate1\",\"vNumber\",\"dbOrCr\" ";
		/*}else{
		return "SELECT voucherdate as \"vDate1\", to_char(voucherdate,'dd-Mon-yyyy') as \"vDate\",vouchernumber as \"vNumber\",a.cgn as \"cgn\",c.name as \"fund\",glcode as \"glcode\", "+
			" b.accountname as \"accountname\",b.narration as \"desc\",a.isconfirmed as \"isconfirmed\",debitamount as \"debit\",creditamount as \"credit\" ,1 as \"dbOrCr\" "+
			" from voucherheader a,voucherdetail b,fund c,fundsource d "+ addTableToQuery + 
			" where a.id=b.voucherheaderid and a.status = 0 "+  fundSourceCond +
			" and a.fundid=c.id and c.id >= "+fromFund+" and c.id <= "+toFund+" "+
			" and  d.id >= "+fromFundSource+" and d.id <= "+toFundSource+" "+
			" and voucherdate>='"+startDate+"' "+
			" and voucherdate<='"+endDate+"' and debitamount>0  "+
            " AND a.status<>4 "+
			" and( cgn like 'JVG%' or  cgn like 'CJV%' or  cgn like 'SJV%' or cgn like 'SAL%' or cgn like 'OJV%') "+
			" union "+
			" SELECT voucherdate as \"vDate1\", to_char(voucherdate,'dd-Mon-yyyy') as \"vDate\",vouchernumber as \"vNumber\",a.cgn as \"cgn\",c.name as \"fund\", "+
			" glcode as \"glcode\",b.accountname as \"accountname\",b.narration as \"desc\",a.isconfirmed as \"isconfirmed\",debitamount as \"debit\",creditamount as \"credit\" "+
			" , 2 as \"dbOrCr\" "+
			" from voucherheader a,voucherdetail b,fund c,fundsource d" + addTableToQuery +
			" where b.voucherheaderid=a.id and a.status = 0 "+  fundSourceCond +
			" and c.id >="+fromFund+" and c.id <= "+toFund+" "+
			" and d.id >="+fromFundSource+" and d.id <= "+toFundSource+" "+
			" and a.fundid=c.id  and voucherdate >='"+startDate+"'  "+
			" and voucherdate<='"+endDate+"' and creditamount>0 "+
            " AND a.status<>4 "+
			" and( cgn like 'JVG%' or  cgn like 'CJV%' or  cgn like 'SJV%' or cgn like 'SAL%' or cgn like 'OJV%') "+
			" ORDER BY \"vDate1\",\"vNumber\",\"dbOrCr\" ";
		}*/ 
	}

	private String getFYID(String sDate)
    throws TaskFailedException
    {
		String fyId="";		 
        try{
        	 String query = "SELECT id FROM financialYear " +
				"WHERE startingDate<= ? AND endingDate>= ?";
             pst=connection.prepareStatement(query);
             pst.setString(1, sDate);
             pst.setString(2, sDate);

			//for accross the financial year
			resultset = pst.executeQuery();
            if(LOGGER.isInfoEnabled())     LOGGER.info("SELECT id FROM financialYear " +
                    "WHERE startingDate<='"+sDate+"' AND endingDate>='"+sDate+"'");
			if(resultset.next()) fyId = resultset.getString("id");
			resultset = null;
		}
        catch(SQLException  ex)
        {
            throw new TaskFailedException();
        }
		
		return fyId;
	}
        
	public String getULBName()
    {
		String ulbName="";
		try{
			Statement st = connection.createStatement();
			ResultSet rset = st.executeQuery("select name from companydetail");
			rset.next();
			ulbName = rset.getString(1);
			rset.close();
			st.close();
		}catch(Exception sqlex){
			LOGGER.error("Inside getULBName"+sqlex.getMessage());
			return null;
		}
		return ulbName;
	}

    public void isCurDate(Connection conn,String VDate) throws TaskFailedException
    {

            EGovernCommon egc=new EGovernCommon();
            try{
                String today=egc.getCurrentDate(conn);
                String[] dt2 = today.split("/");
                String[] dt1= VDate.split("/");

                int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
                if(ret==-1 ){
                    throw new Exception();
                }

            }catch(Exception ex){
                LOGGER.error("Exception in isCurDate():"+ex,ex);
                throw new TaskFailedException("Date Should be within the today's date");
            }

        }
    
	private void setDates(GeneralLedgerBean reportBean) throws TaskFailedException{
			PreparedStatement pst;
			ResultSet rs = null;
			ResultSet rs1 = null;
			String formstartDate="";
			String formendDate="";
			try
	   		{
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("inside try catch");
	   			SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
				Date dt=new Date();
				startDate=(String)reportBean.getStartDate() ;
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("startDate"+startDate);
				dt = sdf.parse(startDate);
				//if(LOGGER.isDebugEnabled())     LOGGER.debug("after parse");
				formstartDate = formatter1.format(dt);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("formstartDate"+formstartDate);
				endDate=(String)reportBean.getEndDate();
				dt = sdf.parse(endDate );
				formendDate = formatter1.format(dt);
				if(LOGGER.isDebugEnabled())     LOGGER.debug("within try transactionDate"+startDate);


	   		}
	   		catch(Exception e){
	   			LOGGER.error("Inside setDate"+e.getMessage(),e);
	   		}
	   		startDate = formstartDate;
			endDate = formendDate;
			if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate == null || endDate.equalsIgnoreCase("")))
			{
			   try{
					String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" " +
									"FROM financialYear WHERE startingDate <= SYSDATE AND endingDate >= SYSDATE";
					pst = connection.prepareStatement(query);
					rs = pst.executeQuery();
					if(rs.next()) startDate = rs.getString("startingDate");
					rs.close();
					String query1 = "SELECT TO_CHAR(sysdate, 'dd-Mon-yyyy') AS \"endingDate\" FROM dual";
					pst = connection.prepareStatement(query1);
					rs1 = pst.executeQuery();
					if(rs1.next()) endDate = rs1.getString("endingDate");
					rs1.close();
					pst.close();
				}
				catch(SQLException  ex)
				{if(LOGGER.isInfoEnabled())     LOGGER.info("eGovFailure"+ "," +"setDates");throw new TaskFailedException();}
			}
			if((startDate == null || startDate.equalsIgnoreCase("")) && (endDate != null || !endDate.equalsIgnoreCase("")))
			{
				try{
					String query = "SELECT TO_CHAR(startingDate, 'dd-Mon-yyyy') AS \"startingDate\" FROM financialYear WHERE startingDate <= ? AND endingDate >= ?";
					pst = connection.prepareStatement(query);
					pst.setString(1, endDate);
					pst.setString(2, endDate);
					rs = pst.executeQuery();
					if(rs.next()) startDate = rs.getString("startingDate");
					rs.close();
					pst.close();
					}
					catch(SQLException  ex)
					{
                        if(LOGGER.isInfoEnabled())     LOGGER.info("eGovFailure"+ "," +"setDates1");
						throw new TaskFailedException();
					}
			}

			if((endDate == null || endDate.equalsIgnoreCase("")) && (startDate != null || !startDate.equalsIgnoreCase("")))
			{
				try{
					String query = "SELECT TO_CHAR(endingDate, 'dd-Mon-yyyy') AS \"endingDate\" " +
					"FROM financialYear WHERE startingDate <= ? AND endingDate >= ?";
					pst = connection.prepareStatement(query);
					pst.setString(1, startDate);
					pst.setString(2, startDate);
					rs = pst.executeQuery();
					if(rs.next()) endDate = rs.getString("endingDate");
					rs.close();
					pst.close();
					pst = null;
				}catch(SQLException  ex){if(LOGGER.isInfoEnabled())     LOGGER.info("eGovFailure"+ "," +"setDates2");throw new TaskFailedException();}
			}
			//preYrSDate = startDate.split("-")[0] + "-" + startDate.split("-")[1] + "-" + (Integer.parseInt(startDate.split("-")[2])-1)+"";
			//preYrEDate = endDate.split("-")[0] + "-" + endDate.split("-")[1] + "-" + (Integer.parseInt(endDate.split("-")[2])-1)+"";
			//if(LOGGER.isDebugEnabled())     LOGGER.debug("******Dates: "+ startDate + ", " + endDate + ", " + preYrSDate + ", " + preYrEDate);
		}

    public static StringBuffer numberToString(final String strNumberToConvert)
    {
        String strConvertedNumber="",strNumber="",signBit="";
        if(strNumberToConvert.startsWith("-"))
        {
            strNumber=""+strNumberToConvert.substring(1,strNumberToConvert.length());
            signBit="-";
        }
        else strNumber=""+strNumberToConvert;
        DecimalFormat dft = new DecimalFormat("##############0.00");
        String strtemp=""+dft.format(Double.parseDouble(strNumber));            
        StringBuffer strbNumber=new StringBuffer(strtemp);                       
        int intLen=strbNumber.length();
           
        for(int i=intLen-6;i>0;i=i-2)                      
        {            
            strbNumber.insert(i,',');               
        }                
       if(signBit.equals("-"))strbNumber=strbNumber.insert(0,"-");
        return strbNumber;
    }

}
