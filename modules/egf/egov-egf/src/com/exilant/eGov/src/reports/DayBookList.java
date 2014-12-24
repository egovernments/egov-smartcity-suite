package com.exilant.eGov.src.reports;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.ReturningWork;

import com.exilant.GLEngine.DayBook;
import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;
import com.exilant.exility.dataservice.DatabaseConnectionException;
public class DayBookList 
{
	//private final static String datasource = "java:/ezgovOraclePool";
//	 private DecimalFormat moneyFormat = new DecimalFormat("#,###,###.00");
	private final static String errConnOpenString = "Unable to get a connection from Pool Please make sure that the connection pool is set up properly";
	private double dAmount=0.0;
	private double cAmount=0.0;
	private static TaskFailedException taskexp;
	protected static final Logger LOGGER = Logger.getLogger(DayBookList.class);
	public DayBookList(){}

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
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException{

			EGovernCommon egc=new EGovernCommon();
			try{
		//		Statement st = conn.createStatement();
			//	st = conn.createStatement();
				String today=egc.getCurrentDate(conn);
				String[] dt2 = today.split("/");
				String[] dt1= VDate.split("/");

				int ret = (Integer.parseInt(dt2[2])>Integer.parseInt(dt1[2])) ? 1 : (Integer.parseInt(dt2[2])<Integer.parseInt(dt1[2])) ? -1 : (Integer.parseInt(dt2[1])>Integer.parseInt(dt1[1])) ? 1 : (Integer.parseInt(dt2[1])<Integer.parseInt(dt1[1])) ? -1 : (Integer.parseInt(dt2[0])>Integer.parseInt(dt1[0])) ? 1 : (Integer.parseInt(dt2[0])<Integer.parseInt(dt1[0])) ? -1 : 0 ;
				if(ret==-1 ){
					throw new Exception();
				}

			}catch(Exception ex){
                LOGGER.error("Exception "+ex);
				throw new TaskFailedException("Date Should be within the today's date");
			}

		}
	public LinkedList getDayBookList(final DayBookReportBean reportBean)throws TaskFailedException
	{
		
		return HibernateUtil.getCurrentSession().doReturningWork(new ReturningWork<LinkedList>() {

			@Override
			public LinkedList execute(Connection conn) throws SQLException {
			

				Statement statement=null;
				ResultSet rs =null;
				
				String dateStart="";
				String dateEnd="";
				String isconfirmed = "";
				double crTotal = 0;
			    double dbTotal = 0;
				LinkedList links = new LinkedList();
				
		        
		        int fundId=Integer.parseInt(reportBean.getFundId());
		        LOGGER.debug("fundid: "+fundId);
		        String fundcondition="";
		        if(fundId > 0)
		        {
		             fundcondition=" and vh.fundid="+fundId;
		        }
		        
				String endDate1=(String)reportBean.getEndDate();
				EGovernCommon.isCurDate(endDate1);
				try	{
					statement=conn.createStatement();}
				catch(Exception e){LOGGER.error("Exception in creating statement:"+statement);}
				try
				{
					dateStart=covertDate(reportBean.getStartDate());
					dateEnd=covertDate(reportBean.getEndDate());
				}catch(Exception error){	LOGGER.error("Error Occured in getDayBookList"+error);}
				String queryString =	"SELECT voucherdate as vdate, TO_CHAR(voucherdate, 'dd-Mon-yyyy')  AS "+" voucherdate"+", vouchernumber , gd.glcode AS " +" glcode"+ ",  ca.name AS "+ "particulars" + ",vh.name ||' - '|| vh.TYPE AS " + "type" +", decode(vh.description,null,' ',vh.description) AS "+ "narration" +", " +
				"decode(status,0,decode(vh.isconfirmed,0,'Unconfirmed',1,'Confirmed'),1,'Reversed',2,'Reversal') as \"status\" , debitamount  ," +
				"creditamount,vh.CGN ,vh.isconfirmed as \"isconfirmed\" FROM voucherheader vh, generalledger gd, chartofaccounts ca WHERE vh.ID=gd.VOUCHERHEADERID" + " AND ca.GLCODE=gd.GLCODE AND voucherdate between '"+dateStart+"' AND '"+dateEnd+"'  and vh.status not in (4,5) "+
		        fundcondition +
			    "  ORDER BY vdate,vouchernumber";
		        LOGGER.debug("queryString :"+queryString);
				try
				{
					rs=statement.executeQuery(queryString);
				}
				catch(Exception e)
				{
					LOGGER.error("Error Occured in getDayBookList"+e);
				}
				try
				{
					int totalCount=0, isConfirmedCount=0;
					String vn2="";
						//added by raja
						String tempVD="",tempVN="",tempTY="",tempN="",tempST="",tempGL="", tempPS="",tempDA="",tempCA="";
						while(rs.next())
					{
						DayBook dBook = new DayBook();
								/*dBook.setVoucherdate(rs.getString("voucherdate"));
								 		dBook.setVoucher(rs.getString("vouchernumber"));
								 			dBook.setType(rs.getString("type"));
								 				tempN = rs.getString("narration");
								 				*/
						 	// added by raja
						tempVD = rs.getString("voucherdate");
						tempVN = rs.getString("vouchernumber");
						tempTY = rs.getString("type");
						tempN = rs.getString("narration");
						tempST = rs.getString("status");
						tempGL= rs.getString("glcode");
						tempPS= rs.getString("particulars");
						tempDA=rs.getString("debitamount");
						tempCA=rs.getString("creditamount");
						
						dBook.setStatus(tempST);			
		if(tempVD.equals(" "))
					dBook.setVoucherdate("&nbsp;");
			else
					dBook.setVoucherdate(tempVD);
		if(tempVN.equals(" "))
					dBook.setVoucher("&nbsp;");
			else
					dBook.setVoucher(tempVN);
		if(tempTY.equals(" "))
					dBook.setType("&nbsp;");
			else
					dBook.setType(tempTY);
		if(tempN.equals(" "))
					dBook.setNarration("&nbsp;");
			else
					dBook.setNarration(tempN);
		/*if(tempST.equals("0"))
				 	dBook.setStatus("Unconfirmed");
			else
					dBook.setStatus("Confirmed");	*/
		if(tempGL.equals(" "))
					dBook.setGlcode("&nbsp;");
			else
				   dBook.setGlcode(tempGL);
		 if(tempPS.equals(" "))
					dBook.setParticulars("&nbsp;");
			else
				   dBook.setParticulars(tempPS);
		if(tempDA.equals("0"))
		 		 	dBook.setDebitamount("&nbsp;");
		    else
				{
				dBook.setDebitamount(""+numberToString(new BigDecimal(Double.parseDouble(tempDA)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
				}
			 	if(tempCA.equals("0"))
						dBook.setCreditamount("&nbsp;");
				else
				{
					dBook.setCreditamount(""+numberToString(new BigDecimal(Double.parseDouble(tempCA)).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
				}
		        LOGGER.debug("aFTER The tempDA value is:"+ tempDA+"tempCA : "+tempCA);
			 			dbTotal += rs.getDouble("debitamount");
				 		crTotal += rs.getDouble("creditamount");
						dBook.setCgn(rs.getString("CGN"));
						isconfirmed= rs.getString("isconfirmed")==null?"":rs.getString("isconfirmed");
						if(!isconfirmed.equalsIgnoreCase(""))
						{
							String vn1=rs.getString("vouchernumber");
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
						reportBean.setTotalCount(Integer.toString(totalCount));
						reportBean.setIsConfirmedCount(Integer.toString(isConfirmedCount));
						links.add(dBook);

					}   //While loop
		          
		            LOGGER.debug("dbTotal:"+dbTotal);
		            LOGGER.debug("crTotal:"+crTotal); 
		            DayBook dBook1 = new DayBook();
		            dBook1.setStatus("<hr>&nbsp;<hr>");       
		            dBook1.setVoucherdate("<hr>&nbsp;<hr>");  
		            dBook1.setVoucher("<hr>&nbsp;<hr>");   
		            dBook1.setParticulars("<hr>&nbsp;<hr>");
		            dBook1.setType("<hr><b>Total</b><hr>");   
		            dBook1.setNarration("<hr>&nbsp;<hr>");           
		            dBook1.setGlcode("<hr>&nbsp;<hr>");    
		            dBook1.setDebitamount("<hr><b>"+numberToString(new BigDecimal(dbTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).toString()+"</b><hr>");    
		            dBook1.setCreditamount("<hr><b>"+numberToString(new BigDecimal(crTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString()).toString()+"</b><hr>");        
		            dBook1.setCgn("&nbsp;");
		            links.add(dBook1);
		            
					setCAmount(Math.round(crTotal));
					setDAmount(Math.round(dbTotal));
					HibernateUtil.release(statement, rs);
					
				}catch(Exception e){e.printStackTrace();
					}
				return links;
				
			}
			
		});
		
	}
    
     public static StringBuffer numberToString(final String strNumberToConvert)
        {
            String strNumber="",signBit="";
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