/* *
	Created on June 20, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;

public class OpeningBalance
{

	
	Statement statement;
	ResultSet resultset;
	String fundId="", finYear="";
	double grandTotalDr=0.0,grandTotalCr=0.0;
	ArrayList al=new ArrayList();
	EGovernCommon egc=new EGovernCommon();
    
	private static  final Logger LOGGER = Logger.getLogger(OpeningBalance.class);
    
	//	This method is called by the OpeningBalance.jsp
	public ArrayList getOBReport(OpeningBalanceInputBean OPBean) throws TaskFailedException
	{
	
		//String asOnDate1=OPBean.getAsOnDate();
		//isCurDate(con,asOnDate1);
		try
		{
			String fuId=OPBean.getObFund_id();
			if(fuId != null)
				fundId=OPBean.getObFund_id();
			finYear=OPBean.getFinYear();
			LOGGER.info("finYear --> "+finYear+" fundid  "+fundId);
			getReport();
			formatReport();
		}
		catch(Exception exception)
		{
			LOGGER.debug("EXP="+exception.getMessage());
		}
		
		return al;

	}

	private void getReport() throws Exception
	{
		
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {

				String fundCondition="";
				double totalDr=0.0,totalCr=0.0;  
		        NumberFormat formatter = new DecimalFormat();
		        formatter = new DecimalFormat("###############.00");
				if(!fundId.equalsIgnoreCase(""))
				fundCondition=" and b.id="+fundId+"";
				String query="SELECT b.name AS \"fund\",c.glcode AS \"accountcode\",c.name AS \"accountname\",a.narration as \"narration\",SUM(a.openingdebitbalance) AS \"debit\","
				+" SUM(a.openingcreditbalance)AS \"credit\"  FROM TRANSACTIONSUMMARY a,FUND  b,CHARTOFACCOUNTS c"
				+" WHERE a.financialyearid="+finYear
				+" "+fundCondition+" AND a.fundid=b.id AND a.glcodeid=c.id AND (a.openingdebitbalance>0 OR a.openingcreditbalance>0) GROUP BY b.name, c.glcode,c.name, a.narration ORDER BY  b.name,c.glcode";
				LOGGER.debug("&&&query  "+query);
				try
				{
					String fund="";
					String checkFund="";
					String glcode="";
					String name="",narration="";
					Double debit, credit,balance;			
					OpeningBalanceBean ob=null;
					statement = con.createStatement();
			 		resultset = statement.executeQuery(query);
			 		while(resultset.next())
			 		{
			 			if(!checkFund.equalsIgnoreCase(resultset.getString("fund")) && !checkFund.equalsIgnoreCase(""))
			 			{
							OpeningBalanceBean opeBalDiff=new OpeningBalanceBean();
							opeBalDiff.setFund("&nbsp;");
							opeBalDiff.setAccCode("&nbsp;");
							opeBalDiff.setAccName("&nbsp;&nbsp;&nbsp; Difference&nbsp;&nbsp;");
							double diff=totalDr-totalCr;
							if(diff >0)
							{
								opeBalDiff.setDebit("&nbsp;");
								opeBalDiff.setCredit(numberToString(((Double)diff).toString()).toString());
							}
							else
							{
								opeBalDiff.setDebit(numberToString(((Double)diff).toString()).toString());
								opeBalDiff.setCredit("&nbsp;");
							}
							al.add(opeBalDiff);
							OpeningBalanceBean opeBal=new OpeningBalanceBean();
							opeBal.setFund("&nbsp;");
							opeBal.setAccCode("&nbsp;");
							opeBal.setAccName("<b>&nbsp;&nbsp;&nbsp; Total:&nbsp;&nbsp;</b>");
							if(diff >0)
							{
								totalCr=totalCr+diff;
								opeBal.setDebit("<b>"+numberToString(((Double)totalDr).toString()).toString()+"</b>");
								opeBal.setCredit("<b>"+numberToString(((Double)totalCr).toString()).toString()+"</b>");
							}
							else
							{
								totalDr=totalDr+(diff * -1);
								opeBal.setDebit("<b>"+numberToString(((Double)totalDr).toString()).toString()+"</b>");
								opeBal.setCredit("<b>"+numberToString(((Double)totalCr).toString()).toString()+"</b>");
							}
							al.add(opeBal);
							totalDr=0.0;totalCr=0.0;
						}
						//LOGGER.debug("totalDr  "+totalDr+"  totalCr  "+totalCr);
						fund=resultset.getString("fund");
						glcode=resultset.getString("accountcode");
						name=resultset.getString("accountname");
						narration=formatStringToFixedLength(resultset.getString("narration"),30);
						debit=resultset.getDouble("debit");
						credit=resultset.getDouble("credit");
						ob=new OpeningBalanceBean();
						ob.setFund(fund);
						ob.setAccCode(glcode);
						ob.setAccName(name);
						ob.setDescription(narration);
						
						if(debit!= null && credit!= null )
						{
							balance = debit - credit;
							if(balance>0)
							{
								 ob.setDebit(numberToString(((Double)balance).toString()).toString());
								 ob.setCredit("&nbsp;");
							}
							else
							{
								balance = credit - debit;
								ob.setDebit("&nbsp;");
								ob.setCredit(numberToString(((Double)balance).toString()).toString());
							}
						}
		               /* if(debit!= null && debit>0)
		                    ob.setDebit(numberToString(((Double)debit).toString()).toString());
		                else
		                    ob.setDebit("&nbsp;");*/
						totalDr=totalDr+debit;
						grandTotalDr=grandTotalDr+debit;
		               /* if(credit != null && credit>0)
		                    ob.setCredit(numberToString(((Double)credit).toString()).toString());
		                else
		                    ob.setCredit("&nbsp;");*/
						totalCr=totalCr+credit;
						grandTotalCr=grandTotalCr+credit;
						al.add(ob);
						checkFund=fund;
					}
					OpeningBalanceBean opeBalDiff=new OpeningBalanceBean();
					opeBalDiff.setFund("&nbsp;");
					opeBalDiff.setAccCode("&nbsp;");
					opeBalDiff.setAccName("&nbsp;&nbsp;&nbsp; Difference&nbsp;&nbsp;");
					opeBalDiff.setDescription("&nbsp;");
					double diff=totalDr-totalCr;
					if(diff >0)
					{
						opeBalDiff.setDebit("&nbsp;");
						opeBalDiff.setCredit(numberToString(((Double)diff).toString()).toString());
					}
					else
					{
						opeBalDiff.setDebit(numberToString(((Double)diff).toString()).toString());
						opeBalDiff.setCredit("&nbsp;");
					}
					al.add(opeBalDiff);
					OpeningBalanceBean opeBal=new OpeningBalanceBean();
					opeBal.setFund("&nbsp;");
					opeBal.setAccCode("&nbsp;");
					opeBal.setAccName("<b>&nbsp;&nbsp;&nbsp; Total:&nbsp;&nbsp;</b>");
					opeBal.setDescription("&nbsp;");
					if(diff >0)
					{
						totalCr=totalCr+diff;
						opeBal.setDebit("<b>"+numberToString(((Double)totalDr).toString()).toString()+"</b>");
						opeBal.setCredit("<b>"+numberToString(((Double)totalCr).toString()).toString()+"</b>");
					}
					else
					{
						totalDr=totalDr+(diff * -1);
						opeBal.setDebit("<b>"+numberToString(((Double)totalDr).toString()).toString()+"</b>");
						opeBal.setCredit("<b>"+numberToString(((Double)totalCr).toString()).toString()+"</b>");
					}
					al.add(opeBal);


				}
				catch(Exception e)
				{
					LOGGER.error("Error in getReport");
					LOGGER.debug("Exp="+e.getMessage());
					
				}
			
				HibernateUtil.release(statement, resultset);
			}
		});
		
	}

	private void formatReport()
	{
        NumberFormat formatter = new DecimalFormat();
       // formatter = new DecimalFormat("##,##,##,##,##,##,###.00");
        double diff=grandTotalDr-grandTotalCr;
		OpeningBalanceBean ob=new OpeningBalanceBean();
		ob.setFund("<hr>&nbsp;<hr>");
		ob.setAccCode("<hr>&nbsp;<hr>");
		ob.setAccName("<hr><b>&nbsp;&nbsp;&nbsp;Grand Total:</b><hr>");
		ob.setDescription("<hr>&nbsp;<hr>");
		if(diff >0)
		{
			grandTotalCr=grandTotalCr+diff;
			ob.setDebit("<hr>&nbsp;<b>"+numberToString(((Double)grandTotalDr).toString()).toString()+"</b><hr>");
			ob.setCredit("<hr>&nbsp;<b>"+numberToString(((Double)grandTotalCr).toString()).toString()+"</b><hr>");
		}
		else
		{
			grandTotalDr=grandTotalDr+(diff * -1);
			ob.setDebit("<hr>&nbsp;<b>"+numberToString(((Double)grandTotalDr).toString()).toString()+"</b><hr>");
			ob.setCredit("<hr>&nbsp;<b>"+numberToString(((Double)grandTotalCr).toString()).toString()+"</b><hr>");
		}
		al.add(ob);
	}
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException{
			
			EGovernCommon egc=new EGovernCommon();
			try{
				Statement st = conn.createStatement();
				st = conn.createStatement();
				String today=egc.getCurrentDate(conn);
				String[] dt2 = today.split("/");
				String[] dt1= VDate.split("/");

				int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
				if(ret==-1 ){
					throw new Exception();
				}
				
			}catch(Exception ex){
				LOGGER.debug("Exception "+ex);
				throw new TaskFailedException("Date Should be within the today's date");
			}
			
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
    
     /**
      * this function inserts html line break at the interval of fixedLength value in String str
     * @param str
     * @param fixedLength
     * @return
     */
    public String formatStringToFixedLength(String str,int fixedLength)
     {
    	LOGGER.info("insidde formatStringToFixedLength");
    	
     	str=str== null ? "&nbsp;" :str.trim().equalsIgnoreCase("")? "&nbsp;":str;
     	if(str.equalsIgnoreCase("&nbsp;") || str.length()<= fixedLength) return str;
		int sIndex=0;
		String formattedString="";
		while(sIndex<str.length())
		{
			if(sIndex+fixedLength>=str.length()) 
				formattedString=formattedString+str.substring(sIndex,str.length());
			else
				formattedString=formattedString+str.substring(sIndex,sIndex+fixedLength)+"<BR>";
			sIndex=sIndex+fixedLength;
		}
		return formattedString;
     }

}
