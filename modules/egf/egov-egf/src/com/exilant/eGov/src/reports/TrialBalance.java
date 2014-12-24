/*
 * Created on June 7, 2006
 * @author Tilak
 */
package com.exilant.eGov.src.reports;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.jdbc.Work;

import com.exilant.eGov.src.common.EGovernCommon;
import com.exilant.exility.common.TaskFailedException;


public class TrialBalance
{

	
	Statement statement;
	ResultSet resultset;
	String endDate, startDate;
    String fundId;
    public String reqFundId[];
    public String reqFundName[];
    String fundcondition="";	
	ArrayList al=new ArrayList();
    ArrayList formatedArrlist=new ArrayList();   
    HashMap debitAmountHs=new HashMap();
    HashMap creditAmountHs=new HashMap();
    HashMap totDrAmtHs=new HashMap();
    HashMap totCrAmtHs=new HashMap();   
    double totalDr=0.0,totalCr=0.0,totalOpgBal=0.0,totalClosingBal=0.0;
    TrialBalanceBean tb;
    CommnFunctions cf=new CommnFunctions();
	java.util.Date dt=new java.util.Date();
	SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    NumberFormat numberformatter=new DecimalFormat("##############0.00");
	EGovernCommon egc=new EGovernCommon();
	private static  final Logger LOGGER = Logger.getLogger(TrialBalance.class);
	private static TaskFailedException taskExc;
	
	//	This method is called by the TrialBalance.jsp
	public ArrayList getTBReport(String asOnDate,String fId,String departmentId,String functionaryId,String functionCodeId,String fieldId)throws Exception
	{
		
		LOGGER.debug("getTBReport | Depaartment ID >>>>>>>>>>>>>>>>>>>>> := "+ departmentId);
		LOGGER.debug("getTBReport | Functionary ID >>>>>>>>>>>>>>>>>>>>> := "+ functionaryId);
		LOGGER.debug("getTBReport | Function Code ID >>>>>>>>>>>>>>>>>>>>> := "+ functionCodeId);
		LOGGER.debug("getTBReport | Field ID >>>>>>>>>>>>>>>>>>>>> := "+ fieldId);
		EGovernCommon.isCurDate(asOnDate);
		try
		{			        
            fundId=fId;            
            LOGGER.info("fundid: "+fundId);
            
            if(!fundId.equalsIgnoreCase(""))
            {
                 fundcondition=" and fundid="+fundId;
            }else{
            	fundcondition=" and fundid in (select id from fund where isactive='1' and isnotleaf!='1' )";
                LOGGER.info("fund cond query  "+fundcondition);
            }
			dt = sdf.parse(asOnDate);
			endDate = formatter.format(dt);
			LOGGER.info("EndDate --> "+endDate);
			startDate = EGovernCommon.getFinYearStartDate(endDate);
           
            cf.getFundList(fundId,startDate,endDate);
            reqFundId=cf.reqFundId;
            reqFundName=cf.reqFundName;  
                         
			getReport(departmentId,functionaryId,functionCodeId,fieldId);
			formatReport();
		}
		catch(Exception exception)
		{
			LOGGER.error("EXP in getTBReport"+exception.getMessage());
			throw exception;
		}
			
		return formatedArrlist;
		
	}
   
public String getDateTime() throws Exception
{
	SimpleDateFormat sdf1 =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	return 	formatter1.format(sdf1.parse(egc.getCurrentDateTime()));
}
	private void getReport(final String departmentId,final String functionaryId,final String functionCodeId,final String fieldId) throws Exception

	{
		HibernateUtil.getCurrentSession().doWork(new Work() {
			
			@Override
			public void execute(Connection con) throws SQLException {
				String voucherMisTable ="";
				String misClause = "";
				String misDeptCond="";
				String tsDeptCond="";
				String functionaryCond="";
				String tsfunctionaryCond="";
				String functionIdCond="";
				String tsFunctionIdCond="";
				String fieldIdCond="";
				String tsFieldIdCond="";
				if( (null != departmentId && !departmentId.trim().equals("")) || (null != functionaryId && !functionaryId.trim().equals("")) ||
						 (null != fieldId && !fieldId.trim().equals(""))){
					voucherMisTable=",vouchermis mis ";
					misClause=" and mis.voucherheaderid=vh.id ";
				}
				
				if(null != departmentId && !departmentId.trim().equals("")){
					misDeptCond = " and mis.DEPARTMENTID="+departmentId ;
					tsDeptCond=" and DEPARTMENTID="+departmentId;
				}
				if(null != functionaryId && !functionaryId.trim().equals("")){
					functionaryCond = " and mis.FUNCTIONARYID="+functionaryId ;
					tsfunctionaryCond=" and FUNCTIONARYID="+functionaryId;
				}
				if(null != functionCodeId && !functionCodeId.trim().equals("")){
					functionIdCond = " and gl.voucherheaderid in (select distinct(voucherheaderid) from generalledger where functionid ="+functionCodeId +")";
					tsFunctionIdCond=" and FUNCTIONID="+functionCodeId;
				}
				if(null != fieldId && !fieldId.trim().equals("")){
					fieldIdCond = " and mis.divisionId="+fieldId ;
					tsFieldIdCond=" and divisionId="+fieldId;
				}
				String defaultStatusExclude=null;
				List<AppConfigValues> listAppConfVal=new AppConfigValuesHibernateDAO(AppConfigValues.class, HibernateUtil.getCurrentSession()).
				getConfigValuesByModuleAndKey("finance","statusexcludeReport");
				if(null!= listAppConfVal)
				{
					defaultStatusExclude = ((AppConfigValues)listAppConfVal.get(0)).getValue();
				}
				else
				{
					throw new EGOVRuntimeException("Exlcude statusses not  are not defined for Reports");
				}
				String query=" SELECT gl.glcode AS \"glcode\" ,coa.name AS \"accountHead\" ,vh.fundid AS \"fundId\",(SUM(debitamount)+SUM((SELECT DECODE(SUM(OPENINGDEBITBALANCE),NULL,0,SUM(OPENINGDEBITBALANCE)) FROM transactionsummary WHERE"
							+" financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"
							+" AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) AND fundid=vh.fundid" + fundcondition +tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+"))/COUNT(*))-"
							+" (SUM(creditamount)+SUM((SELECT  DECODE(SUM(OPENINGCREDITBALANCE),NULL,0,SUM(OPENINGCREDITBALANCE)) FROM"
							+" transactionsummary WHERE financialyearid=(SELECT id FROM financialyear  WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"
							+" AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) AND fundid=vh.fundid"+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+"))/COUNT(*) ) as \"amount\" "
							+" FROM generalledger gl,chartofaccounts   coa,voucherheader vh "+  voucherMisTable+ " WHERE coa.glcode=gl.glcode AND gl.voucherheaderid=vh.id"+ misClause
							+" AND vh.status not in ("+defaultStatusExclude+") "
							+" AND  vh.voucherdate<='"+endDate+"' AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<='"+endDate+"' AND   endingdate>='"+endDate+"') "+fundcondition+" " + misDeptCond + functionaryCond +functionIdCond + fieldIdCond
							+" GROUP BY gl.glcode,coa.name,vh.fundid    HAVING (SUM(debitamount)>0 OR SUM(creditamount)>0)    And"
							+" (SUM(debitamount)+SUM((SELECT DECODE(SUM(OPENINGDEBITBALANCE),NULL,0,SUM(OPENINGDEBITBALANCE)) FROM"
							+" transactionsummary WHERE  financialyearid=(SELECT id FROM financialyear       WHERE startingdate <='"+endDate+"'"
							+" AND endingdate >='"+endDate+"') AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode) "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+"))/COUNT(*))-"
							+" (SUM(creditamount)+SUM((SELECT  DECODE(SUM(OPENINGCREDITBALANCE),NULL,0,SUM(OPENINGCREDITBALANCE)) FROM"
							+" transactionsummary WHERE financialyearid=(SELECT id FROM financialyear    WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"') "
							+" AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=gl.glcode)  "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+"))/COUNT(*) )<>0"
							+" union"
							+" SELECT coa.glcode AS \"glcode\" ,coa.name AS \"name\" , fu.id as \"fundId\", SUM((SELECT DECODE(SUM(OPENINGDEBITBALANCE),NULL,0,SUM(OPENINGDEBITBALANCE))"
							+" FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE  startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"
							+" AND glcodeid =(SELECT id FROM chartofaccounts WHERE  glcode=coa.glcode) AND fundid= (select id from fund where id=fu.id)"
							+" "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+")) - SUM((SELECT  DECODE(SUM(OPENINGCREDITBALANCE),NULL,0,SUM(OPENINGCREDITBALANCE)) as \"amount\" FROM transactionsummary WHERE"
							+" financialyearid=(SELECT id FROM financialyear       WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"') AND glcodeid =(SELECT id FROM chartofaccounts"
							+" WHERE glcode=coa.glcode)AND fundid= (select id from fund where id=fu.id)"+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+")) "
							+" FROM chartofaccounts  coa, fund fu  WHERE  fu.id IN(SELECT fundid from transactionsummary WHERE financialyearid = (SELECT id FROM financialyear WHERE startingdate<='"+endDate+"' " 
							+" AND endingdate>='"+endDate+"') "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+" AND glcodeid =(SELECT id   FROM chartofaccounts WHERE  glcode=coa.glcode) ) AND coa.id NOT IN(SELECT glcodeid FROM generalledger gl,voucherheader vh "+ voucherMisTable+" WHERE "
							+" vh.status not in ("+defaultStatusExclude+") " + misClause+misDeptCond + functionaryCond+functionIdCond+fieldIdCond
							+" AND vh.id=gl.voucherheaderid AND vh.fundid=fu.id AND vh.voucherdate<='"+endDate+"' AND vh.voucherdate>=(SELECT startingdate FROM financialyear WHERE  startingdate<='"+endDate+"' AND   endingdate>='"+endDate+"') "+fundcondition+")"
							+" GROUP BY coa.glcode,coa.name, fu.id"
							+" HAVING((SUM((SELECT DECODE(SUM(OPENINGDEBITBALANCE),NULL,0,SUM(OPENINGDEBITBALANCE)) FROM transactionsummary WHERE"
							+" financialyearid=(SELECT id FROM financialyear       WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"') AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode) "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+" )) >0 )"
							+" OR (SUM((SELECT  DECODE(SUM(OPENINGCREDITBALANCE),NULL,0,SUM(OPENINGCREDITBALANCE)) FROM transactionsummary WHERE financialyearid=(SELECT id FROM financialyear WHERE startingdate<='"+endDate+"' AND endingdate>='"+endDate+"')"
							+" AND glcodeid =(SELECT id FROM chartofaccounts WHERE glcode=coa.glcode)     "+fundcondition+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+"))>0 ))  ORDER BY \"glcode\"";
				LOGGER.debug("&&&query  "+query);
				try
				{                
		            HashMap subList = null;
		            HashMap drAmtSubList = null;
		            HashMap crAmtSubList = null;          
		            String glcode="";
					String name="";			
		            Double amount=new Double(0);            
		            String fuId="";        
		            BigDecimal totalDr[]=new BigDecimal[reqFundId.length+2]; 
		            BigDecimal totalCr[]=new BigDecimal[reqFundId.length+2]; 
		            
					statement = con.createStatement();
			 		resultset = statement.executeQuery(query);
			 		while(resultset.next())
			 		{
			 			glcode=resultset.getString("glcode");               
			 			name=resultset.getString("accountHead"); 
		                fuId=resultset.getString("fundid"); 
		                LOGGER.info("fuId::::"+fuId);
			 			amount=resultset.getDouble("amount");               
		                Double debAmt=new Double(0);
		                Double creAmt=new Double(0);                            
		                if(amount>0)
		                    debAmt=amount;
		                else if(amount<0)
		                    creAmt=amount;  
		                
		                LOGGER.info("amount::::"+amount);                
		                drAmtSubList=new HashMap();
		                crAmtSubList=new HashMap();
		               
		                if(!debitAmountHs.containsKey(glcode))
		                {                    
		                    tb=new TrialBalanceBean();
		                    tb.setAccCode(glcode);                   
		                    tb.setAccName(name);                    
		                    al.add(tb); 
		                    //BigDecimal fundTotal=new BigDecimal(0.00);
		                    for(int i=0;i<reqFundId.length;i++)
		                    {                      
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {                                                          
		                           drAmtSubList.put(reqFundId[i],debAmt);
		                           LOGGER.info(" debAmt::::"+debAmt);  
		                           debitAmountHs.put(glcode,drAmtSubList);                                                   
		                        }
		                        else
		                        {
		                            drAmtSubList.put(reqFundId[i],new Double(0));
		                            crAmtSubList.put(reqFundId[i],new Double(0)); 
		                            debitAmountHs.put(glcode,drAmtSubList);                          
		                        }                                                                   
		                    }                     
		                   
		                }
		                
		             
		                else
		                {
		                    LOGGER.info("inside else fuId:::"+fuId+" debAmt::::"+debAmt);      
		                    ((HashMap)debitAmountHs.get(glcode)).put(fuId,debAmt);                                        
		                }  
		                             
		                
		                if(!totDrAmtHs.containsKey(glcode))
		                {    
		                    for(int i=0;i<reqFundId.length;i++)
		                    {                          
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {    
		                            if(totalDr[i]==null)totalDr[i]=totalDr[i].ZERO;
		                            totalDr[i]=totalDr[i].add(BigDecimal.valueOf(Double.parseDouble((((HashMap)debitAmountHs.get(glcode)).get(reqFundId[i])).toString())));                
		                            totDrAmtHs.put(reqFundId[i],totalDr[i]);                                                       
		                        }                          
		                    }                      
		                }
		           else
		                {
		                    for(int i=0;i<reqFundId.length;i++)
		                    {     
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {                                               
		                            totalDr[i]=totalDr[i].add(BigDecimal.valueOf(Double.parseDouble((((HashMap)debitAmountHs.get(glcode)).get(fuId)).toString())));                       
		                            ((HashMap)totDrAmtHs).put(fuId,totalDr[i]);
		                        }                       
		                    }
		                }
		                                       
		                
		                if(!creditAmountHs.containsKey(glcode))
		                {                    
		                    for(int i=0;i<reqFundId.length;i++)
		                    {
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {
		                            crAmtSubList.put(reqFundId[i],creAmt);                        
		                        }
		                        else
		                        {                            
		                            drAmtSubList.put(reqFundId[i],new Double(0));
		                            crAmtSubList.put(reqFundId[i],new Double(0));                           
		                            debitAmountHs.put(glcode,drAmtSubList);
		                        }                          
		                    }
		                    creditAmountHs.put(glcode,crAmtSubList);  
		                    
		                }
		                else
		                {           
		                    ((HashMap)creditAmountHs.get(glcode)).put(fuId,creAmt);                                    
		                }
		                

		                if(!totCrAmtHs.containsKey(glcode))
		                {                    
		                    for(int i=0;i<reqFundId.length;i++)
		                    {                          
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {    
		                            if(totalCr[i]==null)totalCr[i]=totalCr[i].ZERO;
		                            totalCr[i]=totalCr[i].add(BigDecimal.valueOf(Double.parseDouble((((HashMap)creditAmountHs.get(glcode)).get(reqFundId[i])).toString())));                           
		                            totCrAmtHs.put(reqFundId[i],totalCr[i].negate());                             
		                        }
		                    }
		                }
		               else
		                {
		                    for(int i=0;i<reqFundId.length;i++)
		                    {         
		                        if(reqFundId[i].equalsIgnoreCase(fuId))
		                        {
		                            totalCr[i]=totalCr[i].add(BigDecimal.valueOf(Double.parseDouble((((HashMap)creditAmountHs.get(glcode)).get(fuId)).toString())));                       
		                            totCrAmtHs.put(fuId,totalCr[i].negate());  
		                        }
		                    }
		                }             
					}               
			 		HibernateUtil.release(statement, resultset);
				}
				catch(Exception e)
				{
					LOGGER.error("Error in getReport"+e.getMessage());
					
				}
				
			}
		});
	}
    
    private void formatReport() throws Exception
    {
        HashMap formatReport = null;
        TrialBalanceBean tb=null;
        Set set = null;
        Double debitAmount=0.0;
        Double creditAmount=0.0;          
        BigDecimal drGrandTotal=new BigDecimal("0.00");
        BigDecimal crGrandTotal=new BigDecimal("0.00");
        BigDecimal diffTotal=new BigDecimal("0.00");
        TreeSet keyList=null;  
        TreeSet keyListTotDr = null;  
        TreeSet keyListTotCr = null; 
        TreeSet keyListCredit = null;  
        Set setTotDr = null;
        Set setTotCr = null;
        Set setCredit = null;
        LOGGER.info(">>>>>  al.size()  "+al.size());
            
        for(int i=0;i<=al.size();i++)
        {            
           if(i!=0 && i==al.size())
            {        	   	
        	   	LOGGER.info(">>>>> i==al.size():  "+i);	
        	   	HashMap formatReport1 = new HashMap();
        	   	formatReport1.put("serialNo","<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
       	   		formatReport1.put("accCode","<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
              	formatReport1.put("accName","<hr noshade color=black size=1><b>Opening Balanace Diff:<hr noshade color=black size=1></b>");
              	
                formatReport = new HashMap();
                formatReport.put("serialNo","<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>");
                formatReport.put("accCode","<hr noshade color=black size=1><b>Total:<hr noshade color=black size=1></b>");
                formatReport.put("accName","<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>"); 
                setTotDr=(Set)((HashMap)totDrAmtHs).keySet();
                LOGGER.info(">>>>> setTotDeb:  "+setTotDr);
                keyListTotDr=new TreeSet(setTotDr);                   
                for(Iterator val = keyListTotDr.iterator();val.hasNext();)
                {
                    String fundId=val.next().toString();
                    LOGGER.info(">>>>> fundId:  "+fundId);
                    BigDecimal totDr=new BigDecimal("0.00");                     
                    totDr=(BigDecimal)(((HashMap)totDrAmtHs).get(fundId));
                    LOGGER.info(">>>>> totDr:  "+totDr);                  
                    drGrandTotal=drGrandTotal.add(totDr);  
                    setTotCr=(Set)((HashMap)totCrAmtHs).keySet();
                    LOGGER.info(">>>>> setTotCr:  "+setTotCr);
                    keyListTotCr=new TreeSet(setTotCr);    
	                for(Iterator val2 = keyListTotCr.iterator();val2.hasNext();)
	                {              	                       	
	                	BigDecimal totCr=new BigDecimal("0.00");
	                    String fundId1=val2.next().toString();
	                    if(fundId.equals(fundId1))
	                    {
	                    	totCr=(BigDecimal)(((HashMap)totCrAmtHs).get(fundId1));	
	                    	LOGGER.info(">>>>> totDr:  "+totDr);	                    	
	                    	crGrandTotal=crGrandTotal.add(totCr);         
	                    	BigDecimal diff=totDr.subtract(totCr);
	                    	LOGGER.info("diff--->"+diff);
	                    	if(diff.compareTo(new BigDecimal("0.00"))<0.00)
	                    	{
	                    		BigDecimal diff1=diff.multiply(new BigDecimal(-1));
	                    		formatReport1.put("debitAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((diff1).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");
	                    		formatReport.put("debitAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((totDr.subtract(diff)).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");
	                    		formatReport.put("creditAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((totCr).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");
	                    	}
	                    	else
	                    	{                    	   
	                    		formatReport1.put("creditAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((diff).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");
	                    		formatReport.put("debitAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((totDr).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");
	                    		formatReport.put("creditAmount"+fundId,"<hr noshade color=black size=1><b>"+numberToString((totCr.add(diff)).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"<hr noshade color=black size=1></b>");		                	   
		                   }  
	                    }
	                }
                 }
              
                LOGGER.info(">>>>> drGrandTotal:  "+drGrandTotal);        
                LOGGER.info(">>>>> crGrandTotal:  "+crGrandTotal);
                diffTotal=drGrandTotal.subtract(crGrandTotal);
                if(diffTotal.compareTo(new BigDecimal("0.00"))<0.00)
                {
                	BigDecimal diffTotal1=diffTotal.multiply(new BigDecimal(-1));
                	formatReport1.put("drTotal","<hr noshade color=black size=1><b>"+numberToString(diffTotal1.setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                	formatReport1.put("crTotal","<hr noshade color=black size=1><b>"+numberToString(new BigDecimal(0).toString())+"</b><hr noshade color=black size=1>");
                	formatReport.put("drTotal","<hr noshade color=black size=1><b>"+numberToString(drGrandTotal.subtract(diffTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                	formatReport.put("crTotal","<hr noshade color=black size=1><b>"+numberToString(crGrandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                }
                else
                {
                	formatReport1.put("crTotal","<hr noshade color=black size=1><b>"+numberToString(diffTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                	formatReport1.put("drTotal","<hr noshade color=black size=1><b>"+numberToString(new BigDecimal(0).toString())+"</b><hr noshade color=black size=1>");
                	formatReport.put("drTotal","<hr noshade color=black size=1><b>"+numberToString(drGrandTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                	formatReport.put("crTotal","<hr noshade color=black size=1><b>"+numberToString(crGrandTotal.add(diffTotal).setScale(2, BigDecimal.ROUND_HALF_UP).toString())+"</b><hr noshade color=black size=1>");
                }
                formatedArrlist.add(formatReport1);                                             
                formatedArrlist.add(formatReport);               
            }
          
            if(i<al.size())
            {
                LOGGER.info(">>>>>  i  "+i);
                tb=(TrialBalanceBean)al.get(i);
                formatReport = new HashMap();
                formatReport = new HashMap();
                formatReport.put("serialNo",i+1+"");
                formatReport.put("accCode",tb.getAccCode());
                formatReport.put("accName",tb.getAccName());            
                if(debitAmountHs.get(tb.getAccCode()) != null)
                {
                    set = null;
                    set=(Set)((HashMap)debitAmountHs.get(tb.getAccCode())).keySet();
                    keyList=new TreeSet(set);                   
                    BigDecimal glcodeDrTotal=new BigDecimal("0.00");
                    for(Iterator val = keyList.iterator();val.hasNext();)
                    {                        
                        String fundId=val.next().toString();
                        debitAmount=(Double)(((HashMap)debitAmountHs.get(tb.getAccCode())).get(fundId));
                        glcodeDrTotal=glcodeDrTotal.add(new BigDecimal(debitAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
                        formatReport.put("debitAmount"+fundId,numberToString(debitAmount.toString())); 
                        formatReport.put("drTotal","<b>"+numberToString(glcodeDrTotal.toString())+"</b>");                        
                        LOGGER.info(">>> debitAmount  >>"+debitAmount);                         
                    }
                    LOGGER.info(">>> glcodeDrTotal  >>"+glcodeDrTotal);  
                }
                else
                {
                    set=(Set)((HashMap)debitAmountHs).keySet();
                    keyList=new TreeSet(set);                    
                    
                    for(Iterator val = keyList.iterator();val.hasNext();)
                    {
                        String fundId=val.next().toString();
                        debitAmount=(Double)(((HashMap)debitAmountHs).get(fundId));                     
                        formatReport.put("debitAmount"+fundId,numberToString(debitAmount.toString()));                   
                        LOGGER.info(">>> debitAmount  >>"+debitAmount);                                              
                    }
                }
                
              if(creditAmountHs.get(tb.getAccCode()) != null)
                {
                    setCredit=(Set)((HashMap)creditAmountHs.get(tb.getAccCode())).keySet();
                    keyListCredit=new TreeSet(setCredit);
                    BigDecimal glcodeCrTotal=new BigDecimal("0.00");
                    for(Iterator val = keyListCredit.iterator();val.hasNext();)
                    {
                        String fundId=val.next().toString();
                        creditAmount=(Double)(((HashMap)creditAmountHs.get(tb.getAccCode())).get(fundId));
                        if(creditAmount!=0.0)
                        {
                            Double creditAmount1=creditAmount*-1;
                            formatReport.put("creditAmount"+fundId,numberToString(creditAmount1.toString()));
                        }
                        else
                        {
                            formatReport.put("creditAmount"+fundId,numberToString(creditAmount.toString()));
                        }
                        glcodeCrTotal=glcodeCrTotal.add(new BigDecimal(creditAmount*-1).setScale(2, BigDecimal.ROUND_HALF_UP));
                        formatReport.put("crTotal","<b>"+numberToString(glcodeCrTotal.toString())+"<b>");    
                        LOGGER.info(">>> creditAmount  >>"+creditAmount);                        
                    }
                    LOGGER.info(">>> glcodeCrTotal  >>"+glcodeCrTotal); 
                }
              else
              {
                  setCredit=null;
                  setCredit=(Set)((HashMap)creditAmountHs).keySet();
                  keyListCredit=new TreeSet(setCredit);                
                  
                  for(Iterator val = keyListCredit.iterator();val.hasNext();)
                  {
                      String fundId=val.next().toString();
                      creditAmount=(Double)(((HashMap)creditAmountHs).get(fundId));                     
                      if(creditAmount!=0.0)
                      {
                          //Double creditAmount1=creditAmount*-1;
                          formatReport.put("creditAmount"+fundId,numberToString(creditAmount.toString()));
                      }
                      else
                      {
                          formatReport.put("creditAmount"+fundId,numberToString(creditAmount.toString()));
                      }
                      LOGGER.info(">>> creditAmount  >>"+creditAmount);                                               
                  }
              }
    
                LOGGER.info(">>> formatReport  >>"+formatReport);               
                formatedArrlist.add(formatReport); 
            }           
        }            
    }
    	
	 public void isCurDate(Connection conn,String VDate) throws TaskFailedException{
			
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
				LOGGER.error("Exception in isCurDate"+ex.getMessage());
				throw new TaskFailedException("Date Should be within the today's date");
			}
			
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
        
        //This method is called by the TrialBalance.jsp for single fund and date range
        public ArrayList getTBReportForDateRange(String strtDate,String toDate,String fId,String departmentId,String functionaryId,String functionCodeId,String fieldId)throws TaskFailedException
    	{
    	
    		//isCurDate(con,toDate);
    		LOGGER.debug("getTBReportForDateRange | Depaartment ID >>>>>>>>>>>>>>>>>>>>> := "+ departmentId);
    		LOGGER.debug("getTBReportForDateRange | Functionary ID >>>>>>>>>>>>>>>>>>>>> := "+ functionaryId);
    		LOGGER.debug("getTBReportForDateRange | Function Code ID >>>>>>>>>>>>>>>>>>>>> := "+ functionCodeId);
    		LOGGER.debug("getTBReportForDateRange | Field ID >>>>>>>>>>>>>>>>>>>>> := "+ fieldId);
    		try
    		{
    			fundId=fId;
    			dt = sdf.parse(strtDate);
    			startDate = formatter.format(dt);
    			dt = sdf.parse(toDate);
    			endDate = formatter.format(dt);
    			LOGGER.info("EndDate --> "+endDate+" fundid  "+fundId);
    			LOGGER.info("StartDate --> "+startDate+" fundid  "+fundId);
    			getTBReport(departmentId,functionaryId,functionCodeId,fieldId);
    			formatTBReport();
    		}
    		catch(Exception e)
    		{
    			LOGGER.error("Exception in getTBReportForDateRange"+e.getMessage());
    			throw taskExc;
    		}
    		
    		return al;

    	}
        
        private void getTBReport(final String departmentId,final String functionaryId,final String functionCodeId,final String fieldId ) throws Exception
    	{	
        
        	HibernateUtil.getCurrentSession().doWork(new Work() {
				
				@Override
				public void execute(Connection con) throws SQLException {
					String voucherMisTable ="";
		    		String misClause = "";
		    		String misDeptCond="";
		    		String tsDeptCond="";
		    		String functionaryCond="";
		    		String tsfunctionaryCond="";
		    		String functionIdCond="";
		    		String tsFunctionIdCond="";
		    		String fieldIdCond="";
		    		String tsFieldIdCond="";
		    		if( (null != departmentId && !departmentId.trim().equals("")) || (null != functionaryId && !functionaryId.trim().equals(""))
		    				|| (null != fieldId && !fieldId.trim().equals("")) ){
		    			voucherMisTable=",vouchermis mis ";
		    			misClause=" and mis.voucherheaderid=vh.id ";
		    		}
		    		
		    		if(null != departmentId && !departmentId.trim().equals("")){
		    			misDeptCond = " and mis.DEPARTMENTID="+departmentId ;
		    			tsDeptCond=" and ts.DEPARTMENTID="+departmentId;
		    		}
		    		if(null != functionaryId && !functionaryId.trim().equals("")){
		    			functionaryCond = " and mis.FUNCTIONARYID="+functionaryId ;
		    			tsfunctionaryCond=" and ts.FUNCTIONARYID="+functionaryId;
		    		}  
		    		if(null != functionCodeId && !functionCodeId.trim().equals("")){
		    			functionIdCond = " and gl.voucherheaderid in (select distinct(voucherheaderid) from generalledger where functionid ="+functionCodeId+")" ;
		    			tsFunctionIdCond=" and ts.FUNCTIONID="+functionCodeId;
		    		}
		    		if(null != fieldId && !fieldId.trim().equals("")){
		    			fieldIdCond = " and mis.divisionId="+fieldId ;
		    			tsFieldIdCond=" and divisionId="+fieldId;
		    		}
		    		String defaultStatusExclude=null;
					List<AppConfigValues> listAppConfVal=new AppConfigValuesHibernateDAO(AppConfigValues.class, HibernateUtil.getCurrentSession()).
					getConfigValuesByModuleAndKey("finance","statusexcludeReport");
					if(null!= listAppConfVal)
					{
						defaultStatusExclude = ((AppConfigValues)listAppConfVal.get(0)).getValue();
					}
					else
					{
						throw new EGOVRuntimeException("Exlcude statusses not  are not defined for Reports");
					}
		    		String query="SELECT * from(SELECT coa.glcode AS \"accCode\" ,coa.name AS \"accName\",NVL((SELECT SUM(ts.openingcreditbalance) FROM transactionsummary ts WHERE ts.glcodeid=coa.id AND" 
		    		+" ts.fundid="+fundId+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+" AND ts.financialyearid=fy.id GROUP BY ts.glcodeid HAVING SUM(ts.openingcreditbalance)>0),0) OpgCreditBal,"
					 +" NVL((SELECT SUM(ts.openingdebitbalance) FROM transactionsummary ts WHERE ts.glcodeid=coa.id AND ts.fundid="+fundId+tsDeptCond+tsfunctionaryCond+tsFunctionIdCond+tsFieldIdCond+" AND ts.financialyearid=fy.id GROUP BY" 
					 +" ts.glcodeid HAVING SUM(ts.openingdebitbalance)>0),0) OpgDebitBal,"
					 +" NVL((SELECT SUM(gl.debitamount) FROM generalledger gl,voucherheader vh " + voucherMisTable +" WHERE gl.glcodeid=coa.id AND gl.VOUCHERHEADERID=vh.id "+ misClause + misDeptCond +functionaryCond+functionIdCond+fieldIdCond
					+" AND vh.voucherdate>= fy.startingdate AND vh.voucherdate<=TO_DATE(TO_DATE('"+startDate+"')-1) AND vh.status not in ("+defaultStatusExclude+") AND vh.fundid="+fundId+""
					+" GROUP BY gl.glcodeid HAVING SUM(gl.debitamount)>0),0) PrevDebit,"
					+" NVL((SELECT SUM(gl.creditamount) FROM generalledger gl, voucherheader vh "+ voucherMisTable+" WHERE gl.glcodeid=coa.id AND gl.VOUCHERHEADERID=vh.id " + misClause + misDeptCond+functionaryCond+functionIdCond+fieldIdCond
					+" AND vh.voucherdate>=fy.startingdate AND vh.voucherdate<=TO_DATE(TO_DATE('"+startDate+"')-1) AND vh.status not in ("+defaultStatusExclude+") AND vh.fundid="+fundId+""
					+" GROUP BY gl.glcodeid HAVING SUM(gl.creditamount)>0),0) PrevCredit,"
					+" NVL((SELECT SUM(gl.debitamount) FROM generalledger gl,voucherheader vh "+ voucherMisTable +" WHERE gl.glcodeid=coa.id AND gl.VOUCHERHEADERID=vh.id "+ misClause + misDeptCond+functionaryCond+functionIdCond+fieldIdCond
					 +" AND vh.voucherdate>=TO_DATE('"+startDate+"') AND vh.voucherdate<=TO_DATE('"+endDate+"') AND vh.statusnot in ("+defaultStatusExclude+") AND vh.fundid="+fundId+""
					+" GROUP BY gl.glcodeid HAVING SUM(gl.debitamount)>0),0) Debit,"
					+" NVL((SELECT SUM(gl.creditamount) FROM generalledger gl, voucherheader vh "+voucherMisTable + " WHERE gl.glcodeid=coa.id AND gl.VOUCHERHEADERID=vh.id " + misClause + misDeptCond+functionaryCond+functionIdCond+fieldIdCond
					+" AND vh.voucherdate>=TO_DATE('"+startDate+"') AND vh.voucherdate<=TO_DATE('"+endDate+"') AND vh.status not in ("+defaultStatusExclude+") AND vh.fundid="+fundId+""
					+" GROUP BY gl.glcodeid HAVING SUM(gl.creditamount)>0),0) Credit"
					+" FROM chartofaccounts coa, financialyear fy WHERE fy.isactiveforposting=1 AND fy.startingdate<=TO_DATE('"+startDate+"') AND fy.endingdate>=TO_DATE('"+endDate+"')"
					+" ORDER BY coa.glcode ASC) where DEBIT+CREDIT+PREVCREDIT+PREVDEBIT+OPGDEBITBAL+OPGCREDITBAL>0";
					
		    		LOGGER.debug("&&&query getTBReport "+query);
		    		try
		    		{
		    			String glcode="";
		    			String glName="";    					
		    			TrialBalanceBean tb=null;
		    			int i=1;
		    			statement = con.createStatement();
		    	 		resultset = statement.executeQuery(query);
		    	 		while(resultset.next())
		    	 		{
		    	 			tb=new TrialBalanceBean(); 
		        			double dr=0.0;
		        			double cr=0.0; 
		        			double openingBal=0.0;
		        			double closingBal=0.0;    	
		        			double opgCreditBal=0.0;
		        			double opgDebitBal=0.0;
		        			double prevDebit=0.0;
		        			double prevCredit=0.0;
		    	 			
		        			glcode=resultset.getString("accCode");
		    	 			glName=resultset.getString("accName");
		    	 			if(resultset.getString("Debit")!= null)
		    	 				dr=resultset.getDouble("Debit");
		    	 			if(resultset.getString("Credit")!= null)
		    	 				cr=resultset.getDouble("Credit");
		    	 			if(resultset.getString("OpgCreditBal")!= null)
		    	 				opgCreditBal=resultset.getDouble("OpgCreditBal");
		    	 			if(resultset.getString("OpgDebitBal")!= null)
		    	 				opgDebitBal=resultset.getDouble("OpgDebitBal");
		    	 			if(resultset.getString("PrevDebit")!= null)
		    	 				prevDebit=resultset.getDouble("PrevDebit");
		    	 			if(resultset.getString("PrevCredit")!= null)
		    	 				prevCredit=resultset.getDouble("PrevCredit");
		    	 			
		    	 			tb.setSerialNo(""+i);
		    	 			tb.setAccCode(glcode);
		    	 			tb.setAccName(glName);
		    	 			
		    	 			openingBal=opgCreditBal+prevCredit-(opgDebitBal+prevDebit);
		    	 			if(openingBal>0)
		    	 			{
		    	 				tb.setOpeningBal(""+numberToString(((Double)openingBal).toString())+" Cr");
		    	 				totalOpgBal=totalOpgBal+openingBal;
		    	 			}    	 			
		    	 			else if(openingBal<0)
		    	 			{
		    	 				totalOpgBal=totalOpgBal+openingBal;    	 				
		    	 				double openingBal1=openingBal*-1;
		    	 				tb.setOpeningBal(""+numberToString(((Double)openingBal1).toString())+" Dr");    	 				
		    	 			}
		    	 			else
		    	 				tb.setOpeningBal("&nbsp;");
		    	 			closingBal=openingBal+cr-dr;
		    	 			if(closingBal>0)
		    	 			{
		    	 				tb.setClosingBal(""+numberToString(((Double)closingBal).toString())+" Cr");
		    	 				
		    	 			}    	 			
		    	 			else if(closingBal<0)
		    	 			{
		    	 				double closingBal1=closingBal*-1;
		    	 				tb.setClosingBal(""+numberToString(((Double)closingBal1).toString())+" Dr");    	 				
		    	 			}
		    	 			else
		    	 				tb.setClosingBal("&nbsp;");    	 				 			
		    	 			if(dr>0)
		    	 			{    	 				
		    	 				tb.setDebit(""+numberToString(((Double)dr).toString()));    	 				
		    	 				totalDr=totalDr+dr;
		    				}
		    	 			else
		    	 				tb.setDebit("&nbsp;");
		    				if(cr>0)
		    				{					
		    					tb.setCredit(""+numberToString(((Double)cr).toString()));
		    	 				totalCr=totalCr+(cr);
		    				}
		    				else
		    	 				tb.setCredit("&nbsp;");
		    				
		    				totalClosingBal=totalOpgBal+totalCr-totalDr;
		    			
		    				al.add(tb);
		    				i++;
		    			}
		    	 		HibernateUtil.release(statement, resultset);
		    		}
		    		catch(Exception e)
		    		{
		    			LOGGER.error("Error in getTBReport"+e.getMessage());    			
		    			
		    		}
				}
			});
    	}
        
        private void formatTBReport()
    	{
    		TrialBalanceBean tb=new TrialBalanceBean();
    		tb.setSerialNo("<hr noshade color=black size=1><b>Sl No:<hr noshade color=black size=1></b>");
    		tb.setAccCode("<hr noshade color=black size=1><b>Total:<hr noshade color=black size=1></b>");
    		tb.setAccName("<hr noshade color=black size=1>&nbsp;<hr noshade color=black size=1>"); 
    		if(totalOpgBal>0)
    		{
    			tb.setOpeningBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalOpgBal).toString())+" Cr<hr noshade color=black size=1></b>");
    		}
    		else if(totalOpgBal<0)
    		{    			
    			totalOpgBal=totalOpgBal*-1;
    			tb.setOpeningBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalOpgBal).toString())+" Dr<hr noshade color=black size=1></b>");
    		}
    		if(totalClosingBal>0)
    		{
    			tb.setClosingBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalClosingBal).toString())+" Cr<hr noshade color=black size=1></b>");
    		}
    		else if(totalClosingBal<0)
    		{
    			totalClosingBal=totalClosingBal*-1;    		
    			tb.setClosingBal("<hr noshade color=black size=1><b>"+numberToString(((Double)totalClosingBal).toString())+" Dr<hr noshade color=black size=1></b>");
    		}
    		tb.setDebit("<hr noshade color=black size=1><b>"+numberToString(((Double)totalDr).toString())+"<hr noshade color=black size=1></b>");
    		tb.setCredit("<hr noshade color=black size=1><b>"+numberToString(((Double)totalCr).toString())+"<hr noshade color=black size=1></b>");    		
    		al.add(tb);    				
    	}
}
